package nl.uwv.otod.otod_portal.service;

import java.util.List;
import java.util.Optional;

import nl.uwv.otod.otod_portal.model.SizingRow;

public interface SizingRowService {
	Iterable<SizingRow> getAll();
	Optional<SizingRow> getById(Long id);
	void save(SizingRow row);	
	List<SizingRow> getByStatusApplicationNameDivisionOs(String status, String applicationName, String division, String os);
}
