package nl.uwv.otod.otod_portal.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.CostCenter;
import nl.uwv.otod.otod_portal.persistence.CostCenterDao;
import nl.uwv.otod.otod_portal.service.CostCenterService;

@Log4j2
@Service
public class CostCenterServiceImpl implements CostCenterService {

	@Autowired
	private CostCenterDao costCenterDao;
	
	@Override
	public List<CostCenter> getAll() {
		log.info("Getting all cost centers");
		var allCostCenters =costCenterDao.findAll();
		log.info("Found {} cost centers", allCostCenters.size());
		return allCostCenters;
	}

	@Override
	public Optional<CostCenter> getById(Long id) {
		return costCenterDao.findById(id);
	}

	@Override
	public Iterable<CostCenter> getByName(String name) {
		return costCenterDao.findByName(name);
	}

	@Override
	public void save(CostCenter costCenter) {
		costCenterDao.save(costCenter);	
	}

	@Override
	public List<CostCenter> getByPkunOrS() {
		return costCenterDao.findByPkunOrS();
	}

}
