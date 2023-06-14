package nl.uwv.otod.otod_portal.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.ServerConfiguration;
import nl.uwv.otod.otod_portal.persistence.ServerConfigurationDao;
import nl.uwv.otod.otod_portal.service.ServerConfigurationService;

@Service
public class ServerConfigurationServiceImpl implements ServerConfigurationService {

	@Autowired
	private ServerConfigurationDao serverConfigurationDao;
	
	public Iterable<ServerConfiguration> getAll() {
		return serverConfigurationDao.findAll();
	}

	@Override
	public Optional<ServerConfiguration> get(Long id) {
		return serverConfigurationDao.findById(id);
	}
}
