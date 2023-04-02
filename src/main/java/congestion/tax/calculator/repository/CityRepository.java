package congestion.tax.calculator.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import congestion.tax.calculator.entity.CityEntity;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {

	  Optional<CityEntity> findByName(String name);
}
