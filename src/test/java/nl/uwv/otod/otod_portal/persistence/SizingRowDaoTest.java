package nl.uwv.otod.otod_portal.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.SizingRow;

@DataJpaTest
@ActiveProfiles("test")
@ComponentScan("nl.uwv.otod.otod_portal")
@Log4j2

// TODO make test work
public class SizingRowDaoTest /*extends BaseTest*/ {

	@Autowired
	private SizingRowDao dao;
	
	@BeforeEach
	public void setUp() {
		log.info("Setting up test");
		System.out.println("B");
		var sizingRow1 = initSizingRow1();
		var sizingRow2 = initSizingRow2();
		var sizingRow3 = initSizingRow3();
		dao.save(sizingRow1);
		dao.save(sizingRow2);
		dao.save(sizingRow3);
	}
	
	private SizingRow initSizingRow1() {
		var row = new SizingRow();
		row.setDivision("OToD");
		row.setApplicationName("Portal");
		row.setOs("Linux");
		row.setStatus("New");
		return row;
	}

	private SizingRow initSizingRow2() {
		var row = new SizingRow();
		row.setDivision("C-Ict");
		row.setApplicationName("XL Deploy");
		row.setOs("Windows");
		row.setStatus("In progress");
		return row;
	}

	private SizingRow initSizingRow3() {
		var row = new SizingRow();
		row.setDivision("TSC");
		row.setApplicationName("DATPROF Privacy");
		row.setOs("AIX");
		row.setStatus("Delivered");
		return row;
	}
	
	@Test
	public void testFindByDivision() {
		log.info("Finding rows by division");
		System.out.println("C");
		var rowsByOtod = dao.findByStatusApplicationNameDivisionOs("", "", "otod", "");
		var rowsByCIct = dao.findByStatusApplicationNameDivisionOs("", "", "c-ict", "");
		var rowsByTsc = dao.findByStatusApplicationNameDivisionOs("", "", "tsc", "");
		assertEquals(1, rowsByOtod.size());
		assertEquals(1, rowsByCIct.size());
		assertEquals(1, rowsByTsc.size());
	}
	
	@Test
	public void testFindByStatus() {
		var byEmpty = dao.findByStatusApplicationNameDivisionOs("New", "", "", "");
		var byInProgress = dao.findByStatusApplicationNameDivisionOs("In progress", "", "", "");
		var byDelivered = dao.findByStatusApplicationNameDivisionOs("Delivered", "", "", "");
		assertEquals(1, byEmpty.size());
		assertEquals(1, byInProgress.size());
		assertEquals(1, byDelivered.size());
	}

	@Test
	public void testFindByApplicationName() {
		var byDatprof = dao.findByStatusApplicationNameDivisionOs("", "datprof", "", "");
		var byPortal = dao.findByStatusApplicationNameDivisionOs("", "portal", "", "");
		var byXl = dao.findByStatusApplicationNameDivisionOs("", "xl", "", "");
		assertEquals(1, byDatprof.size());
		assertEquals(1, byPortal.size());
		assertEquals(1, byXl.size());
	}

	@Test
	public void testFindByOs() {
		var byWindows = dao.findByStatusApplicationNameDivisionOs("", "", "", "Windows");
		var byLinux = dao.findByStatusApplicationNameDivisionOs("", "", "", "Linux");
		var byAix = dao.findByStatusApplicationNameDivisionOs("", "", "", "AIX");
		assertEquals(1, byWindows.size());
		assertEquals(1, byLinux.size());
		assertEquals(1, byAix.size());
	}

	@AfterEach
	public void tearDown() {
		log.info("Tearing down");
		System.out.println("D");
		var allRows = dao.findAll();
		for (var row : allRows) {
			dao.delete(row);
		}
	}
}
