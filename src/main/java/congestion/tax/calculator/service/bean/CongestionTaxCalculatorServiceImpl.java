package congestion.tax.calculator.service.bean;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import congestion.tax.calculator.entity.CityEntity;
import congestion.tax.calculator.entity.TarrifEntity;
import congestion.tax.calculator.entity.VehicleEntity;
import congestion.tax.calculator.exception.CityException;
import congestion.tax.calculator.exception.VehicleException;
import congestion.tax.calculator.modal.TaxCalculatorResponse;
import congestion.tax.calculator.modal.Vehicle;
import congestion.tax.calculator.repository.CityRepository;
import congestion.tax.calculator.repository.VehicleRepository;
import congestion.tax.calculator.service.CongestionTaxCalculatorService;
import congestion.tax.calculator.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Service
@NoArgsConstructor
@AllArgsConstructor
public class CongestionTaxCalculatorServiceImpl implements CongestionTaxCalculatorService {

	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private VehicleRepository vehicleRepository;

	@Override
	public TaxCalculatorResponse getTax(Vehicle vehicle, List<Date> dates, String city) throws CityException, VehicleException {
		
		//verify city
		if(cityRepository.findByName(city).isEmpty()) {
			throw new CityException("City not found", HttpStatus.NOT_FOUND);
		}
		//verify vehicle
		if(vehicleRepository.findByName(vehicle.getType()).isEmpty()) {
			throw new VehicleException("Vehicle not found", HttpStatus.NOT_FOUND);
		}

		Map<String, BigDecimal> chargesHistoryPerday = new HashMap<>();
		// Get city data by city name
		CityEntity cityEntity = cityRepository.findByName(city).get();

		// check toll free vehicle
		if (isTollFreeVehicle(cityEntity.getTaxExemptedVehicle(), vehicle))
			return TaxCalculatorResponse.builder().taxAmount(new BigDecimal(0)).build();
		if (dates == null || dates.isEmpty())
			return TaxCalculatorResponse.builder().taxAmount(new BigDecimal(0)).build();

		// sort the date by asc
		Collections.sort(dates, Comparator.comparingLong(Date::getTime));

		// remove weekends, public holidays, days before or after a public holiday
		dates.removeIf(date -> isTollFreeDate(date, cityEntity));

		// Calculate single charge rule
		Map<String, List<BigDecimal>> singleChargeRules = getSingleChargeRules(dates, cityEntity);
		// calculate total charge
		BigDecimal totalFee = calculateTotalTaxBySingleChargeRule(chargesHistoryPerday, cityEntity, singleChargeRules);

		//final response
		return TaxCalculatorResponse.builder().taxAmount(totalFee).chargesHistoryByDate(chargesHistoryPerday).build();

	}

	/**
	 * Calculate total tax with single rule for the specific city
	 * @param chargerHistoryPerDay
	 * @param cityEntity
	 * @param chargesPerDay
	 * @return
	 */
	private BigDecimal calculateTotalTaxBySingleChargeRule(Map<String, BigDecimal> chargerHistoryPerDay, CityEntity cityEntity,
			Map<String, List<BigDecimal>> chargesPerDay) {
		BigDecimal totalFee = new BigDecimal(0);
		
		//iterate through charges per day
		for (Map.Entry<String, List<BigDecimal>> entry : chargesPerDay.entrySet()) {
			BigDecimal totalChargePerDay = entry.getValue().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
			if (cityEntity.getCitySpecificRulesEntity() != null && cityEntity.getCitySpecificRulesEntity().getMaxTaxPerDay() != null
					&& totalChargePerDay.compareTo(cityEntity.getCitySpecificRulesEntity().getMaxTaxPerDay()) > 0) {
				totalChargePerDay = cityEntity.getCitySpecificRulesEntity().getMaxTaxPerDay();
			}

			chargerHistoryPerDay.put(entry.getKey(), totalChargePerDay);
			totalFee = totalFee.add(totalChargePerDay);
		}
		return totalFee;
	}

	/**
	 * calculate single charge rule for the specific city
	 * @param dates
	 * @param cityEntity
	 * @return
	 */
	private Map<String, List<BigDecimal>> getSingleChargeRules(List<Date> dates, CityEntity cityEntity) {
		List<Date> visitedTimes = new ArrayList<>();
		Map<String, List<BigDecimal>> result = new HashMap<>();

		for (int start = 0; start < dates.size(); start++) {
			if (visitedTimes.contains(dates.get(start)))
				continue;
			// toll fee for the city, date and the amount given
			BigDecimal charge = getTollFeeByTariffAndDate(dates.get(start), cityEntity.getTariffEntities());

			charge = compareWithOtherDates(dates, cityEntity, visitedTimes, start, charge);
			getChargesByDate(dates, result, start, charge);
		}
		return result;

	}

	/**
	 * compare dates and calculate the charge
	 * @param dates
	 * @param cityEntity
	 * @param visitedSlots
	 * @param start
	 * @param charge
	 * @return
	 */
	private BigDecimal compareWithOtherDates(List<Date> dates, CityEntity cityEntity, List<Date> visitedTimes, int start, BigDecimal charge) {
		for (int end = start + 1; end < dates.size(); end++) {
			long duration = dates.get(end).getTime() - dates.get(start).getTime();
			long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
			if (diffInMinutes <= cityEntity.getCitySpecificRulesEntity().getSingleChargeIntervalInMin()) {
				visitedTimes.add(dates.get(end));
				BigDecimal temp = getTollFeeByTariffAndDate(dates.get(end), cityEntity.getTariffEntities());
				if (temp.compareTo(charge) > 0)
					charge = temp;
			} else
				break;
		}
		return charge;
	}

	private void getChargesByDate(List<Date> dates, Map<String, List<BigDecimal>> result, int start, BigDecimal charge) {
		String dateString = DateUtils.formatDate(dates.get(start));
		List<BigDecimal> chargeLists;
		
		if (result.containsKey(dateString)) {
			chargeLists = result.get(dateString);
		} else {
			chargeLists = new ArrayList<>();
		}
		chargeLists.add(charge);
		result.put(dateString, chargeLists);
	}

	/**
	 * Get Toll fee by tarrif and date
	 * 
	 * @param date
	 * @param tariffs
	 * @return
	 */
	private BigDecimal getTollFeeByTariffAndDate(Date date, Set<TarrifEntity> tariffs) {
		BigDecimal totalFee = new BigDecimal(0);
		if (tariffs == null || tariffs.isEmpty())
			return totalFee;

		Optional<TarrifEntity> tarrifEntity = tariffs.stream()
				.filter(t -> (!date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime().isBefore(t.getFromTime())
						&& date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime().isBefore(t.getToTime())))
				.findFirst();
		if(tarrifEntity.isPresent()) {
			totalFee = tarrifEntity.get().getCharge();
		}

		return totalFee;
	}

	/**
	 * 
	 * @param date
	 * @param cityEntity
	 * @return
	 */
	private boolean isTollFreeDate(Date date, CityEntity cityEntity) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		int month = calendar.get(Calendar.DAY_OF_MONTH);

		return ((day == Calendar.SATURDAY || day == Calendar.SUNDAY) // Weekend
				|| (Boolean.TRUE.equals(DateUtils.isHolidayMonth(cityEntity.getHolidayMonthsEntity(), month))) // Monthly
				|| (Boolean.TRUE.equals(DateUtils.isBeforeOrAftertOrInPublicHoliday(date, cityEntity)))); // public holiday, before or after
	}

	private boolean isTollFreeVehicle(Set<VehicleEntity> taxExemptedVehicle, Vehicle vehicle) {
		if (taxExemptedVehicle == null || taxExemptedVehicle.isEmpty())
			return false;

		return taxExemptedVehicle.stream().filter(t -> Objects.equals(t.getName(), vehicle.getType())).count() > 0;

	}

}
