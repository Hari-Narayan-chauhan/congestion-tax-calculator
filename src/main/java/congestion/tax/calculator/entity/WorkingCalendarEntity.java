package congestion.tax.calculator.entity;

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
@Table(name = "working_calendar")
public class WorkingCalendarEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "is_monday", nullable = false, columnDefinition = "BIT", length = 1)
	private boolean isMonday;
	@Column(name = "is_tuesday", nullable = false, columnDefinition = "BIT", length = 1)
	private boolean isTuesday;
	@Column(name = "is_wednesday", nullable = false, columnDefinition = "BIT", length = 1)
	private boolean isWednesday;
	@Column(name = "is_thursday", nullable = false, columnDefinition = "BIT", length = 1)
	private boolean isThursday;
	@Column(name = "is_friday", nullable = false, columnDefinition = "BIT", length = 1)
	private boolean isFriday;
	@Column(name = "is_saturday", nullable = false, columnDefinition = "BIT", length = 1)
	private boolean isSaturday;
	@Column(name = "is_sunday", nullable = false, columnDefinition = "BIT", length = 1)
	private boolean isSunday;

	@OneToOne
	@JoinColumn(name = "city_entity_id")
	private CityEntity cityEntity;

}
