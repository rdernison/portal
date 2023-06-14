package nl.uwv.otod.otod_portal.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.Request;

@Log4j2
public class RequestReaderTest {

	private RequestReader reader = new RequestReader();
	
	@Test
	public void testReadRequests() throws IOException {
		var requests = reader.readRequestsFromPipedLines();
		assertTrue(requests.iterator().hasNext());
		
		validateRequests(requests);
	}

	private void validateRequests(List<Request> requests) {
		var request1 = requests.get(0);
		var request2 = requests.get(1);
		var request3 = requests.get(2);
		validateRequest1(request1);
		validateRequest2(request2);
		validateRequest3(request3);
	}

	private void validateRequest1(Request request1) {
		assertEquals("Proof Of Value (POV) Robotic Process Automation (RPA) (41106)", request1.getTitle());
		var requestDate = LocalDate.of(2020, 10, 1);
		assertEquals(requestDate, request1.getRequestDate());
		var desirableDeliveryDate = LocalDate.of(2020, 10, 2);
		assertEquals(desirableDeliveryDate, request1.getDesirableDeliveryDate());
		assertEquals("Eijden, Daniel van (D.N.)", request1.getNameServiceRequester());
		assertEquals("Peter van der Heijden", request1.getNameBudgetOwner());
		
	}

	private void validateRequest2(Request request2) {
		assertEquals("Sofie Doorontwikkeling (41080)", request2.getTitle());
		var requestDate = LocalDate.of(2020, 9, 21);
		assertEquals(requestDate, request2.getRequestDate());
		assertEquals("Gronloh, Paula (P.A.)", request2.getNameServiceRequester());
		assertEquals("SMZ InnovatieLab - Erik Vlijm (controller lab)", request2.getNameBudgetOwner());
	}

	private void validateRequest3(Request request3) {
		assertEquals("Sofie Doorontwikkeling (41079)", request3.getTitle());
		var requestDate = LocalDate.of(2020, 9, 21);
		assertEquals(requestDate, request3.getRequestDate());
		assertEquals("Gronloh, Paula (P.A.)", request3.getNameServiceRequester());
		assertEquals("SMZ InnovatieLab - Erik Vlijm (controller lab)", request3.getNameBudgetOwner());
		
	}
	
	
	@Test
	public void testReadRequestsFromPipedLines() throws IOException {
		var requests = reader.readRequestsFromPipedLines();
		log.info("Found {} requests", requests.size());
		assertNotEquals(0, requests.size());
	}
}
