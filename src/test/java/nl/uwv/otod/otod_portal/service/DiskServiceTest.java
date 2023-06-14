package nl.uwv.otod.otod_portal.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nl.uwv.otod.otod_portal.model.Disk;
import nl.uwv.otod.otod_portal.model.Server;
import nl.uwv.otod.otod_portal.persistence.DiskDao;
import nl.uwv.otod.otod_portal.service.impl.DiskServiceImpl;

@ExtendWith(SpringExtension.class)
public class DiskServiceTest {

	@Mock
	private DiskDao diskDao;
	
	@InjectMocks
	private DiskServiceImpl diskService;
	
	private Server server;
	
	@BeforeEach
	public void setUp() {
		var disk = new Disk();
		disk.setComputerName("vt010140006066");
		when(diskDao.findById(1L)).thenReturn(Optional.of(disk));
		var disks = new ArrayList<Disk>();
		disks.add(disk);
		when(diskDao.findAll()).thenReturn(disks);
		when(diskDao.findByComputerNameAndFileSystem("vt010140006066", "vfat")).thenReturn(disks);
		server = new Server();
		when(diskDao.findByServer(server)).thenReturn(disks);
		when(diskDao.findByFileSystem("vfat")).thenReturn(disks);
	}
	
	@Test
	public void testGetAll() {
		var allDisks = diskService.getAll();
		assertTrue(allDisks.iterator().hasNext());
	}

	@Test
	public void testGetByComputerNameAndFileSystem() {
		var allDisks = diskService.getByComputerNameAndFileSystem("vt010140006066", "vfat");
		assertTrue(allDisks.iterator().hasNext());
	}

	@Test
	public void testGetByFileSystem() {
		var allDisks = diskService.getByFileSystem("vfat");
		assertTrue(allDisks.iterator().hasNext());
	}

	@Test
	public void testGetByServer() {
		var allDisks = diskService.getByServer(server);
		assertTrue(allDisks.iterator().hasNext());
	}
}
