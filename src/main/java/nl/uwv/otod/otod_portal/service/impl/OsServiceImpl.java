package nl.uwv.otod.otod_portal.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.Os;
import nl.uwv.otod.otod_portal.persistence.OsDao;
import nl.uwv.otod.otod_portal.service.OsService;

@Service
public class OsServiceImpl implements OsService {

	private static final Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private OsDao osDao;
	
	@Override
	public Iterable<Os> getAll() {
		LOGGER.info("Getting all oses" );
		Iterable<Os> oses = osDao.findAll();
		LOGGER.info("Got some? {}", oses.iterator().hasNext());
		return oses;
	}

	@Override
	public void save(Os os) {
//		LOGGER.info("Storing os: {}" , os.getName());
		osDao.save(os);
	}

	@Override
	public List<Os> getByName(String name) {
		return osDao.findByName(name);
	}

	@Override
	public Optional<Os> getById(Long id) {
		return osDao.findById(id);
	}

	@Override
	public List<Os> getEnabled() {
		var oses = osDao.findAllEnabled();/*
		var enabled= new ArrayList<Os>();
		
		for (var os : oses) {
			if ("Windows 2016".equals(os.getName())
					
					|| "Windows 2019".equals(os.getName())
					|| "AIX 7.2".equals(os.getName())
					|| "Redhat 7".equals(os.getName())
					// redhat 8 toevoegen
					// alleen Windows
					) {
				enabled.add(os);
			}
		}
		
		// DUS ALLEEN
		// Windows
		// Redhat 7
		// Redhat 8
		// AIX 7.2
		return enabled;*/
		return oses;
	}

	@Override
	public List<Os> getEnabledByname(String name) {
		return osDao.findEnabledByName(name);
	}

}
