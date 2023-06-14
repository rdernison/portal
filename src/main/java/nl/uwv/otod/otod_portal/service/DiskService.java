package nl.uwv.otod.otod_portal.service;

import java.util.List;

import nl.uwv.otod.otod_portal.model.Disk;
import nl.uwv.otod.otod_portal.model.Server;

public interface DiskService {
	Iterable<Disk> getAll();
	List<Disk> getByServer(Server server);
	void save(Disk disk);
	List<Disk> getByFileSystem(String fileSystem);
	List<Disk> getByComputerNameAndFileSystem(String computerName, String fileSystem);
}
