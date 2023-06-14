package nl.uwv.otod.otod_portal.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import nl.uwv.otod.otod_portal.model.Disk;
import nl.uwv.otod.otod_portal.model.Server;
import nl.uwv.otod.otod_portal.service.impl.DiskServiceImpl;
import nl.uwv.otod.otod_portal.service.impl.OsServiceImpl;
import nl.uwv.otod.otod_portal.service.impl.ServerServiceImpl;

//@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {DiskController.class, DiskServiceImpl.class})
@WebMvcTest
public class DiskControllerTest {

//	@Mock
//	private DiskServiceImpl diskService;
//	
//	@InjectMocks
//	private DiskController diskController;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private DiskServiceImpl diskService;
	
	@MockBean
	private OsServiceImpl osService;

	@MockBean
	private ServerServiceImpl serverService;
	/*
	 *
vmt010140176013	8635	NTFS	M	VMWare Additional Disk Pool	500			Decommissioned
vmt010140176013	10352	NTFS	M	VMWare Additional Disk Pool	1 000			Decommissioned
vmt010140176013	8252	NTFS	L	VMWare Additional Disk Pool	1 000			Decommissioned
vmt010140176013	8655	NTFS	d	VMWare Additional Disk Pool	1 500			Decommissioned
vmt010140176013	8656	NTFS	F	VMWare Additional Disk Pool	1 500			Decommissioned
vmt010140176013	8657	NTFS	F	VMWare Additional Disk Pool	1 500			Decommissioned
vmt010140176013	1933	NTFS	K	VMWare Additional Disk Pool	50			Decommissioned
	 * 
	 * 
	 */
	private Disk disk1;
	@BeforeEach
	public void setUp() {
		var allDisks = new ArrayList<Disk>();
		var server = new Server();
		server.setName("vmt010140176013");
		disk1 = makeDisk(server, "NTFS", "VMWare AdditionalDiskPool", "M");
		allDisks.add(disk1);
		var diskIterable = allDisks;
		when(diskService.getAll()).thenReturn(diskIterable);
		when(diskService.getByComputerNameAndFileSystem("vmt010140176013", "ntfs")).thenReturn(allDisks);
	}
	
	private Disk makeDisk(Server server, String fileSystem, String pool, String mountPoint) {
		var disk = new Disk();
		disk.setServer(server);
		disk.setFileSystemName(fileSystem);
		disk.setPoolName(pool);
		disk.setMountPoint(mountPoint);
		return disk;
		
	}
	
	@Test
	@WithMockUser("rde062")
	public void testFindAll() throws Exception {
		mockMvc.perform(get("/disk"))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("VMWare AdditionalDiskPool")));//diskController.allDisks(model);
	}
	
	
	/* TODO make this work
	@Test
	@WithMockUser("rde062")
	public void testFindServers() throws Exception {
		mockMvc.perform(get("/disk/find").requestAttr("disk", disk1))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("VMWare AdditionalDiskPool")));//diskController.allDisks(model);
	}*/
}
