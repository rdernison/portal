package nl.uwv.otod.otod_portal.conversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import nl.uwv.otod.otod_portal.converter.ServerRequestConverter;
import nl.uwv.otod.otod_portal.model.DiskRequest;
import nl.uwv.otod.otod_portal.model.Middleware;
import nl.uwv.otod.otod_portal.model.MiddlewareRequest;
import nl.uwv.otod.otod_portal.model.ServerRequest;

public class ServerRequestConverterTest {

	private ServerRequestConverter converter = new ServerRequestConverter();
	
	@Test
	public void testServerRequestListToString() {
		var serverRequests = makeServerRequests();
		var str = converter.convertServerRequestsToJson(serverRequests);
		assertEquals("[{\"id\":0,\"ram\":16,\"cpus\":4,\"os\":null,\"middleware\":[{\"id\":0,\"middleware\":{\"id\":0,\"name\":\"Apache Tomcat 8\"},\"environment\":null}],\"disks\":[{\"id\":0,\"mountPoint\":\"/\",\"size\":100,\"volumeGroup\":null,\"volumeName\":null,\"server\":null,\"environment\":null},{\"id\":0,\"mountPoint\":\"/apps\",\"size\":100,\"volumeGroup\":null,\"volumeName\":null,\"server\":null,\"environment\":null},{\"id\":0,\"mountPoint\":\"/data\",\"size\":100,\"volumeGroup\":null,\"volumeName\":null,\"server\":null,\"environment\":null}],\"environment\":null,\"request\":null}]", str);
	}
	
	private List<ServerRequest> makeServerRequests() {
		var serverRequests = new ArrayList<ServerRequest>();
		var serverRequest1 = makeServerRequest(4, 16, new int[] {100,100,100}, new String[] {"/", "/apps", "/data"}, new String[] {"Apache Tomcat 8"});
		serverRequests.add(serverRequest1);
		return serverRequests;
	}

	private ServerRequest makeServerRequest(int procs, int mem, int[] diskSizes, String[] mountPoints, String[] middlewareNames) {
		var serverRequest = new ServerRequest();
		serverRequest.setCpus(procs);
		serverRequest.setRam(mem);
		var diskRequests = makeDiskRequests(diskSizes, mountPoints);
		serverRequest.setDisks(diskRequests);
		var middlewareRequests = makeMiddlewareRequests(middlewareNames);
		serverRequest.setMiddlewareRequests(middlewareRequests);
		
		return serverRequest;
	}

	private List<DiskRequest> makeDiskRequests(int[] diskSizes, String[] mountPoints) {
		var diskRequests = new ArrayList<DiskRequest>();
		for (int i = 0; i < diskSizes.length;i++) {
			var size = diskSizes[i];
			var mountPoint = mountPoints[i];
			var diskRequest = new DiskRequest();
			diskRequest.setMountPoint(mountPoint);
			diskRequest.setSize(size);
			diskRequests.add(diskRequest);
		}
		return diskRequests;
	}

	private List<MiddlewareRequest> makeMiddlewareRequests(String[] middlewareNames) {
		var middlewareRequests = new ArrayList<MiddlewareRequest>();
		
		for (String middlewareName : middlewareNames) {
			var mwr = new MiddlewareRequest();
			
			var mw = new Middleware();
			mw.setName(middlewareName);
			/* TODO check
			mwr.setMiddleware(mw);
			*/ 
			middlewareRequests.add(mwr);
		}
		
		return middlewareRequests;
	}

	@Test
	public void testConvertStringToRequestList() throws JsonMappingException, JsonProcessingException {
		var json = "[{\"id\":0,\"ram\":16,\"cpus\":4,\"os\":null,\"middleware\":null,\"transientMiddleware\":null,\"disks\":[{\"id\":0,\"mountPoint\":\"/\",\"size\":100,\"volumeGroup\":null,\"volumeName\":null,\"server\":null,\"environment\":null},{\"id\":0,\"mountPoint\":\"/apps\",\"size\":100,\"volumeGroup\":null,\"volumeName\":null,\"server\":null,\"environment\":null},{\"id\":0,\"mountPoint\":\"/data\",\"size\":100,\"volumeGroup\":null,\"volumeName\":null,\"server\":null,\"environment\":null}],\"transientDisks\":null,\"environment\":null,\"request\":null}]";
		var serverRequests = converter.convertJsonToServerRequests(json);
		
		assertNotNull(serverRequests);
	}
}

