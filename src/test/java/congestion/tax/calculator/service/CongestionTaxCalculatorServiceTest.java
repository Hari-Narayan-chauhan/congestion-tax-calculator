package congestion.tax.calculator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import congestion.tax.calculator.entity.CityEntity;
import congestion.tax.calculator.entity.VehicleEntity;
import congestion.tax.calculator.exception.CityException;
import congestion.tax.calculator.exception.VehicleException;
import congestion.tax.calculator.modal.TaxCalculatorRequest;
import congestion.tax.calculator.modal.TaxCalculatorResponse;
import congestion.tax.calculator.modal.Vehicle;
import congestion.tax.calculator.repository.CityRepository;
import congestion.tax.calculator.repository.VehicleRepository;
import congestion.tax.calculator.service.bean.CongestionTaxCalculatorServiceImpl;

@ExtendWith(MockitoExtension.class)
class CongestionTaxCalculatorServiceTest {

	@InjectMocks
	private CongestionTaxCalculatorServiceImpl service;

	@Mock
	private CityRepository cityRepository;

	@Mock
	private VehicleRepository vehicleRepository;

	@Test
	void getTaxSuccessTest() throws ParseException, CityException, VehicleException {
		TaxCalculatorRequest request = buildRequest("Car", "Gothenburg");

		Mockito.when(cityRepository.findByName(anyString()))
				.thenReturn(Optional.of(CityEntity.builder().id(1L).name("GothenBurg").build()));
		Mockito.when(vehicleRepository.findByName(anyString()))
				.thenReturn(Optional.of(VehicleEntity.builder().id(1L).name("Car").build()));
		TaxCalculatorResponse response = service.getTax(request.getVehicle(), request.getCheckinTime(),
				request.getCity());
		assertNotNull(response);

	}

	@Test
	void getTaxCalculatorCityNotFoundTest() throws CityException, VehicleException, ParseException {
		TaxCalculatorRequest request = buildRequest("Car", "Norway");

		CityException thrown = assertThrows(CityException.class,
				() -> service.getTax(request.getVehicle(), request.getCheckinTime(), request.getCity()),
				"Expected taxController.getTaxCalculator(request) to throw CityException, and it did");
		assertEquals("City not found", thrown.getMessage());

	}

	@Test
	void getTaxCalculatorVehicleNotFoundTest() throws CityException, VehicleException, ParseException {
		TaxCalculatorRequest request = buildRequest("car", "Gothenburg");
		Mockito.when(cityRepository.findByName(anyString()))
				.thenReturn(Optional.of(CityEntity.builder().id(1L).name("GothenBurg").build()));
		
		VehicleException thrown = assertThrows(VehicleException.class,
				() -> service.getTax(request.getVehicle(), request.getCheckinTime(), request.getCity()),
				"Expected taxController.getTaxCalculator(request) to throw CityException, and it did");
		assertEquals("Vehicle not found", thrown.getMessage());

	}

	private TaxCalculatorRequest buildRequest(String type, String city) throws ParseException {
		Vehicle vehicle = new Vehicle();
		vehicle.setType(type);
		List<Date> dateList = new ArrayList<>();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateTime = formatter.parse("2013-02-07 06:23:27");
		dateList.add(dateTime);

		TaxCalculatorRequest request = new TaxCalculatorRequest();
		request.setCity(city);
		request.setVehicle(vehicle);
		request.setCheckinTime(dateList);
		return request;

	}

}
