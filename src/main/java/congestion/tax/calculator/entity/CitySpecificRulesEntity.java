package congestion.tax.calculator.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "city_specific_rules_entity")
public class CitySpecificRulesEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "number_of_tax_free_days_after_holiday", nullable = false, columnDefinition = "int default 0")
	private Integer noOfTaxFreeDaysAfterPublicHolidays;

	@Column(name = "number_of_tax_free_days_before_holiday", nullable = false, columnDefinition = "int default 0")
	private Integer noOfTaxFreeDaysBeforePublicHolidays;

	@Column(name = "max_tax_per_day", nullable = false, columnDefinition = "int default 60")
	private BigDecimal maxTaxPerDay;

	@Column(name = "single_charge_interval", nullable = false, columnDefinition = "int default 0")
	private Integer singleChargeIntervalInMin;

	@OneToOne
	@JoinColumn(name = "city_entity_id")
	private CityEntity cityEntity;

}
