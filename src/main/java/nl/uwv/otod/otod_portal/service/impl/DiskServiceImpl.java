package nl.uwv.otod.otod_portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.Disk;
import nl.uwv.otod.otod_portal.model.Server;
import nl.uwv.otod.otod_portal.persistence.DiskDao;
import nl.uwv.otod.otod_portal.service.DiskService;

@Service
public class DiskServiceImpl implements DiskService {

	@Autowired
	private DiskDao diskDao;

	@Override
	public Iterable<Disk> getAll() {
		return diskDao.findAll();
	}
	
	@Override
	public List<Disk> getByServer(Server server) {
		return diskDao.findByServer(server);
	}

	@Override
	public void save(Disk disk) {
		diskDao.save(disk);		
	}

	@Override
	public List<Disk> getByFileSystem(String fileSystem) {
		return diskDao.findByFileSystem(fileSystem);
	}

	@Override
	public List<Disk> getByComputerNameAndFileSystem(String computerName, String fileSystem) {
		return diskDao.findByComputerNameAndFileSystem(computerName, fileSystem);
	}

	
}
