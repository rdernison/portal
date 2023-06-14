package nl.uwv.otod.otod_portal.service.impl;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.UsedIpAddress;
import nl.uwv.otod.otod_portal.persistence.UsedIpAddressDao;
import nl.uwv.otod.otod_portal.service.UsedIpAddressService;

@Service
public class UsedIpAddressServiceImpl implements UsedIpAddressService {

	private static Logger logger = LogManager.getLogger();
	
	@Autowired
	private UsedIpAddressDao usedIpAddressDao;
	
	@Override
	public Iterable<UsedIpAddress> getAll() {
		return usedIpAddressDao.findAll();
	}

	@Override
	public void save(UsedIpAddress address) {
		logger.info("Saving used ip address");
		logger.info("address = {}", address.getAddress());
		logger.info("id = {}", address.getId());
		usedIpAddressDao.save(address);
	}

	@Override
	public Optional<UsedIpAddress> getById(long id) {
		return usedIpAddressDao.findById(id);
	}

}
