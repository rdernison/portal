package nl.uwv.otod.otod_portal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nl.uwv.otod.otod_portal.model.CostCenter;
import nl.uwv.otod.otod_portal.persistence.CostCenterDao;
import nl.uwv.otod.otod_portal.service.impl.CostCenterServiceImpl;

@ExtendWith(SpringExtension.class)
public class CostCenterServiceTest {

	@InjectMocks
	private CostCenterServiceImpl costCenterService;
	
	@Mock
	private CostCenterDao costCenterDao;
	
	private List<CostCenter> allCostCenters;
	
	@BeforeEach
	public void setUp() {
		var level1 = CostCenter.builder()
				.code("GALKM0")
				.description("Mallegatsplein 10 Alkmaar")
				.build();
		when(costCenterDao.findById(1L)).thenReturn(Optional.of(level1));
		var pkun18 = CostCenter.builder()
				.code("PKUN18")
				.description("P-kstn E-werken ext gefinanc")
				.build();
		when(costCenterDao.findById(2L)).thenReturn(Optional.of(pkun18));
		allCostCenters = new ArrayList<CostCenter>();
		allCostCenters.add(level1);
		allCostCenters.add(pkun18);
		var pkun18s = new ArrayList<CostCenter>();
		pkun18s.add(pkun18);
		when(costCenterDao.findAll()).thenReturn(allCostCenters);
		when(costCenterDao.findByPkunOrS()).thenReturn(pkun18s);
	}
	
	@Test
	public void testGetAllCostCenters() {
		var cc = costCenterService.getAll();
		assertEquals(cc.size(), allCostCenters.size());
	}
	
	
	@Test
	public void testGetCostCentersByPkunOrS() {
		var ccs = costCenterService.getByPkunOrS();
		assertEquals(1, ccs.size());
	}
	
	@AfterEach
	public void tearDown() {
		
	}
}
