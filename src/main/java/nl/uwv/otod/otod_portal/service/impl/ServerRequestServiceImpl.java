package nl.uwv.otod.otod_portal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.ServerRequest;
import nl.uwv.otod.otod_portal.persistence.ServerRequestDao;
import nl.uwv.otod.otod_portal.service.ServerRequestService;

@Service
public class ServerRequestServiceImpl implements ServerRequestService {

	@Autowired
	private ServerRequestDao serverRequestDao;
	
	@Override
	public void save(ServerRequest serverRequest) {
		serverRequestDao.save(serverRequest);
	}

}
