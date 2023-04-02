package congestion.tax.calculator.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import congestion.tax.calculator.exception.CityException;
import congestion.tax.calculator.exception.VehicleException;
import congestion.tax.calculator.modal.TaxCalculatorRequest;
import congestion.tax.calculator.modal.TaxCalculatorResponse;
import congestion.tax.calculator.service.CongestionTaxCalculatorService;
import congestion.tax.calculator.util.Constants;
import io.swagger.annotations.ApiOperation;

@ApiOperation(value = "Congestion Tax Calculator")
@RestController
@RequestMapping(Constants.TAX_API)
public class TaxController {

	@Autowired
	CongestionTaxCalculatorService congestionTaxCalculatorService;

	@ApiOperation(value = "Endpoint to calculate the tax")
	@PostMapping(value = Constants.TAX_CALCULATOR, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TaxCalculatorResponse> getTaxCalculator(@RequestBody TaxCalculatorRequest request)
			throws CityException, VehicleException {

		try {
			return ResponseEntity.ok(congestionTaxCalculatorService.getTax(request.getVehicle(),
					request.getCheckinTime(), request.getCity()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(TaxCalculatorResponse.builder().taxAmount(new BigDecimal(0)).build());
		}

	}

}
