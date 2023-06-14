package nl.uwv.otod.otod_portal.service;

import java.util.Optional;

import nl.uwv.otod.otod_portal.model.Calculation;

public interface CalculationService {

	Optional<Calculation> getById(Long id);
	
	Iterable<Calculation> getAll();
	
	Calculation save(Calculation calculation);
}
