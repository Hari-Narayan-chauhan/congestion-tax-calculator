package congestion.tax.calculator.entity;

import java.math.BigDecimal;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "tarrif")
public class TarrifEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_time", nullable = false)
    private LocalTime fromTime;

    @Column(name = "to_time", nullable = false)
    private LocalTime toTime;

    @Column(name = "charge", nullable = false)
    private BigDecimal charge;

    @ManyToOne
    @JoinColumn(name="city_id", nullable=false)
    private CityEntity cityEntity;


}
