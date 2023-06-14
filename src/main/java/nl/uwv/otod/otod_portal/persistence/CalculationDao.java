package nl.uwv.otod.otod_portal.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.Calculation;

@Repository
public interface CalculationDao extends CrudRepository<Calculation, Long> {

}
