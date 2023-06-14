package nl.uwv.otod.otod_portal.service;

import java.util.Optional;

import nl.uwv.otod.otod_portal.model.FileSystem;

public interface FileSystemService {

	Iterable<FileSystem> getAll();
	
	Optional<FileSystem> getById(Long id);
	
	Optional<FileSystem> getByName(String name);
	
	void save(FileSystem fileSystem);
	
}
