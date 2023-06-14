package nl.uwv.otod.otod_portal.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.SizingRow;
import nl.uwv.otod.otod_portal.persistence.SizingRowDao;
import nl.uwv.otod.otod_portal.service.SizingRowService;

@Service
public class SizingRowServiceImpl implements SizingRowService {
	@Autowired
	private SizingRowDao sizingRowDao;

	@Override
	public Iterable<SizingRow> getAll() {
		return sizingRowDao.findAll();
	}

	@Override
	public Optional<SizingRow> getById(Long id) {
		return sizingRowDao.findById(id);
	}

	@Override
	public void save(SizingRow row) {
		sizingRowDao.save(row);
		
	}

	@Override
	public List<SizingRow> getByStatusApplicationNameDivisionOs(String status, String applicationName, String division,
			String os) {
		return sizingRowDao.findByStatusApplicationNameDivisionOs(status, applicationName, division, os);
	}

}
