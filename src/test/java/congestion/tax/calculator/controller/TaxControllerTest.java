package congestion.tax.calculator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import congestion.tax.calculator.exception.CityException;
import congestion.tax.calculator.exception.VehicleException;
import congestion.tax.calculator.modal.TaxCalculatorRequest;
import congestion.tax.calculator.modal.TaxCalculatorResponse;
import congestion.tax.calculator.modal.Vehicle;
import congestion.tax.calculator.service.CongestionTaxCalculatorService;
import congestion.tax.calculator.util.Constants;

@ExtendWith(MockitoExtension.class)
class TaxControllerTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype());

	@InjectMocks
	private TaxController taxController;

	@Mock
	private CongestionTaxCalculatorService service;

	private MockMvc mockMvc;
	private static HttpHeaders httpHeaders;
	private static ObjectMapper objectMapper = new ObjectMapper();

	@BeforeAll
	static void init() throws IOException {
		httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
	}

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(taxController).build();
	}

	@Test
	void testTaxCalculatorRequest() throws Exception {

		TaxCalculatorRequest request = buildRequest("Car", "Gothenburg");
		TaxCalculatorResponse response = TaxCalculatorResponse.builder().taxAmount(new BigDecimal(60))
				.chargesHistoryByDate(new HashMap<>()).build();
		Mockito.when(service.getTax(any(), anyList(), anyString())).thenReturn(response);

		mockMvc.perform(MockMvcRequestBuilders.post(Constants.TAX_API + Constants.TAX_CALCULATOR)
				.content(objectMapper.writeValueAsString(request)).headers(httpHeaders)).andDo(print())
				.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(contentType))
				.andExpect(status().isOk()).andExpect(jsonPath("$.taxAmount").exists())
				.andExpect(jsonPath("$.chargesHistoryByDate").exists());
	}

	@Test
	void getTaxCalculatorTest() throws CityException, VehicleException {
		TaxCalculatorRequest request = buildRequest("Car", "Gothenburg");
		TaxCalculatorResponse response = TaxCalculatorResponse.builder().taxAmount(new BigDecimal(60)).build();
		Mockito.when(service.getTax(request.getVehicle(), request.getCheckinTime(), request.getCity()))
				.thenReturn(response);
		ResponseEntity<TaxCalculatorResponse> taxCalculatorResponse = taxController.getTaxCalculator(request);
		assertNotNull(taxCalculatorResponse);
		assertEquals(HttpStatus.OK, taxCalculatorResponse.getStatusCode());
		assertEquals(new BigDecimal(60), taxCalculatorResponse.getBody().getTaxAmount());
	}

	private TaxCalculatorRequest buildRequest(String type, String city) {
		Vehicle vehicle = new Vehicle();
		vehicle.setType(type);
		List<Date> dateList = new ArrayList<>();
		dateList.add(new Date());

		TaxCalculatorRequest request = new TaxCalculatorRequest();
		request.setCity(city);
		request.setVehicle(vehicle);
		request.setCheckinTime(dateList);
		return request;

	}

}
