package nl.uwv.otod.otod_portal.service;

import java.util.Optional;

import nl.uwv.otod.otod_portal.model.UsedIpAddress;

public interface UsedIpAddressService {
	Iterable<UsedIpAddress> getAll();
	
	void save(UsedIpAddress address);
	
	Optional<UsedIpAddress> getById(long id);
}
