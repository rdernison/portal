package nl.uwv.otod.otod_portal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.Os;

@Service
public interface OsService {

	Iterable<Os> getAll();
	
	void save(Os os);
	
	List<Os> getByName(String name);
	
	Optional<Os> getById(Long id);
	
	List<Os> getEnabled();
	
	List<Os> getEnabledByname(String name);
}
