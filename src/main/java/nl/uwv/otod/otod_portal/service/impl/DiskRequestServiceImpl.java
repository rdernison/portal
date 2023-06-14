package nl.uwv.otod.otod_portal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.DiskRequest;
import nl.uwv.otod.otod_portal.persistence.DiskRequestDao;
import nl.uwv.otod.otod_portal.service.DiskRequestService;

@Service
public class DiskRequestServiceImpl implements DiskRequestService {

	@Autowired
	private DiskRequestDao diskRequestDao;
	
	@Override
	public void save(DiskRequest diskRequest) {
		diskRequestDao.save(diskRequest);

	}

}
