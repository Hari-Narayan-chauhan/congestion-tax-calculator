package congestion.tax.calculator.service;

import java.util.Date;
import java.util.List;

import congestion.tax.calculator.exception.CityException;
import congestion.tax.calculator.exception.VehicleException;
import congestion.tax.calculator.modal.TaxCalculatorResponse;
import congestion.tax.calculator.modal.Vehicle;

public interface CongestionTaxCalculatorService {
	
	TaxCalculatorResponse getTax(Vehicle vehicle, List<Date> date, String city) throws CityException, VehicleException;

}
