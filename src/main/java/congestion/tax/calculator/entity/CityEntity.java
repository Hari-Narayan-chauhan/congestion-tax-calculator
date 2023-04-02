package congestion.tax.calculator.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
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
@Table(name = "city_entity")
public class CityEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@OneToOne(fetch = FetchType.EAGER,mappedBy = "cityEntity", cascade = CascadeType.MERGE)
	@PrimaryKeyJoinColumn
	private HolidayMonthsEntity holidayMonthsEntity;

	@OneToOne(fetch = FetchType.EAGER,mappedBy = "cityEntity", cascade = CascadeType.MERGE)
	@PrimaryKeyJoinColumn
	private WorkingCalendarEntity workingCalendarEntity;

	@OneToMany(fetch = FetchType.EAGER,mappedBy = "cityEntity")
	private Set<HolidayCalendarEntity> holidayCalendarEntities;

	@ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinTable(name ="city_entity_vehicle_join_table", joinColumns = @JoinColumn(name  = "city_entity_id"), inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
	private Set<VehicleEntity> taxExemptedVehicle;

	@OneToOne(fetch = FetchType.EAGER,mappedBy = "cityEntity", cascade = CascadeType.MERGE)
	@PrimaryKeyJoinColumn
	private CitySpecificRulesEntity citySpecificRulesEntity;

	@OneToMany(fetch = FetchType.EAGER,mappedBy = "cityEntity")
	private Set<TarrifEntity> tariffEntities;

}
