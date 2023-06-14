package nl.uwv.otod.otod_portal.service;

import java.util.List;
import java.util.Optional;

import nl.uwv.otod.otod_portal.model.CostCenter;

public interface CostCenterService {
	void save(CostCenter costCenter);
	List<CostCenter> getAll();
	Optional<CostCenter> getById(Long id);
	Iterable<CostCenter> getByName(String name);
	
	List<CostCenter> getByPkunOrS();
}
