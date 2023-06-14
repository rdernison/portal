package nl.uwv.otod.otod_portal.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.Calculation;
import nl.uwv.otod.otod_portal.persistence.CalculationDao;
import nl.uwv.otod.otod_portal.service.CalculationService;

@Service
public class CalculationServiceImpl implements CalculationService {

	@Autowired
	private CalculationDao calculationDao;

	@Override
	public Optional<Calculation> getById(Long id) {
		return calculationDao.findById(id);
	}

	@Override
	public Iterable<Calculation> getAll() {
		return calculationDao.findAll();
	}

	@Override
	public Calculation save(Calculation calculation) {
		return calculationDao.save(calculation);
	}
	
	
}
