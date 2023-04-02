package congestion.tax.calculator.modal;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxCalculatorResponse {
	private BigDecimal taxAmount;
    private Map<String, BigDecimal> chargesHistoryByDate;


}
