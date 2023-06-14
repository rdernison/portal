package nl.uwv.otod.otod_portal.service;

import java.util.List;
import java.util.Optional;

import nl.uwv.otod.otod_portal.model.Middleware;

public interface MiddlewareService {

	Iterable<Middleware> getAll();
	void save(Middleware middleware);
	Optional<Middleware> get(long id);
	
	List<Middleware> getEnabled();
}
