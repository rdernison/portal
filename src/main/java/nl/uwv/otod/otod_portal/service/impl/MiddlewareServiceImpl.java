package nl.uwv.otod.otod_portal.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.Middleware;
import nl.uwv.otod.otod_portal.persistence.MiddlewareDao;
import nl.uwv.otod.otod_portal.service.MiddlewareService;

@Service
public class MiddlewareServiceImpl implements MiddlewareService {

	@Autowired
	private MiddlewareDao middlewareDao;
	
	@Override
	public Iterable<Middleware> getAll() {
		return middlewareDao.findAll();
	}

	@Override
	public void save(Middleware middleware) {
		middlewareDao.save(middleware);
	}

	@Override
	public Optional<Middleware> get(long id) {
		return middlewareDao.findById(id);
	}

	@Override
	public List<Middleware> getEnabled() {
		return middlewareDao.findEnabled();
	}

}
