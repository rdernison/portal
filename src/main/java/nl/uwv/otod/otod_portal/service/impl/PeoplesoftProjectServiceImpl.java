package nl.uwv.otod.otod_portal.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.PeoplesoftProject;
import nl.uwv.otod.otod_portal.persistence.PeoplesoftProjectDao;
import nl.uwv.otod.otod_portal.service.PeoplesoftProjectService;

@Service
public class PeoplesoftProjectServiceImpl implements PeoplesoftProjectService {

	@Autowired
	private PeoplesoftProjectDao dao;
	
	@Override
	public Iterable<PeoplesoftProject> getByMasterCode(String masterCode) {
		return dao.findByMasterCode(masterCode);
	}

	@Override
	public Optional<PeoplesoftProject> getByCode(String code) {
		return dao.findByCode(code);
	}

	@Override
	public Optional<PeoplesoftProject> getById(Long id) {
		return dao.findById(id);
	}

	@Override
	public Iterable<PeoplesoftProject> getAll() {
		return dao.findAll();
	}

	@Override
	public void save(PeoplesoftProject project) {
		dao.save(project);
	}
}
