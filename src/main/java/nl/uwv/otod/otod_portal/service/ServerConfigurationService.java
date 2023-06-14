package nl.uwv.otod.otod_portal.service;

import java.util.Optional;

import nl.uwv.otod.otod_portal.model.ServerConfiguration;

public interface ServerConfigurationService {

	Iterable<ServerConfiguration> getAll();
	Optional<ServerConfiguration> get(Long id);
}
