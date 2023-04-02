package congestion.tax.calculator.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import congestion.tax.calculator.entity.VehicleEntity;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {

	  Optional<VehicleEntity> findByName(String name);
}
