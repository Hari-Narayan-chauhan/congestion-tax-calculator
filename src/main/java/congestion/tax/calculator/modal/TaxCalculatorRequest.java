package congestion.tax.calculator.modal;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxCalculatorRequest {

	@ApiModelProperty(example = "Car", allowEmptyValue = false, required = true)
	private Vehicle vehicle;
	@ApiModelProperty(example = "[ \"2013-02-08 06:27:00\",  \"2013-02-08 06:20:27\",\"2013-02-08 14:35:00\"]", allowEmptyValue = false, required = true)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private List<Date> checkinTime;
	@ApiModelProperty(example = "Gothenburg", allowEmptyValue = false, required = true)
	private String city;

}
