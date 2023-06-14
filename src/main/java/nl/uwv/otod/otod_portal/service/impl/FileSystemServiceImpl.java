package nl.uwv.otod.otod_portal.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.FileSystem;
import nl.uwv.otod.otod_portal.persistence.FileSystemDao;
import nl.uwv.otod.otod_portal.service.FileSystemService;

@Service
public class FileSystemServiceImpl implements FileSystemService {

	@Autowired
	private FileSystemDao fileSystemDao;
	
	@Override
	public Iterable<FileSystem> getAll() {
		return fileSystemDao.findAll();
	}

	@Override
	public Optional<FileSystem> getById(Long id) {
		return fileSystemDao.findById(id);
	}

	@Override
	public Optional<FileSystem> getByName(String name) {
		return fileSystemDao.findByName(name);
	}

	@Override
	public void save(FileSystem fileSystem) {
		fileSystemDao.save(fileSystem);
	}

}
