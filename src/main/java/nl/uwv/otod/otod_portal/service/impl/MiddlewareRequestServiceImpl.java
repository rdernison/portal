package nl.uwv.otod.otod_portal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.MiddlewareRequest;
import nl.uwv.otod.otod_portal.persistence.MiddlewareRequestDao;
import nl.uwv.otod.otod_portal.service.MiddlewareRequestService;

@Service
public class MiddlewareRequestServiceImpl implements MiddlewareRequestService {

	@Autowired
	private MiddlewareRequestDao middlewareRequestDao; 
	@Override
	public void save(MiddlewareRequest middlewareRequest) {
		middlewareRequestDao.save(middlewareRequest);
	}

}
