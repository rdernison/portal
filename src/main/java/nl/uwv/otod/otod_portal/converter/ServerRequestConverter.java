package nl.uwv.otod.otod_portal.converter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.DiskRequest;
import nl.uwv.otod.otod_portal.model.Middleware;
import nl.uwv.otod.otod_portal.model.MiddlewareRequest;
import nl.uwv.otod.otod_portal.model.Os;
import nl.uwv.otod.otod_portal.model.ServerRequest;

@Log4j2
public class ServerRequestConverter {

	public List<ServerRequest> convertJsonToServerRequests(String json) throws JsonMappingException, JsonProcessingException {
		List<ServerRequest> serverRequests;
		if (json == null) {
			serverRequests = new ArrayList<>();
		} else {
			var om = new ObjectMapper();
			log.info("Parsing json: {}", json);
			var listOfHashMaps = om.readValue(json, List.class);
			
			serverRequests = convertLinkedHashMapToArrayList(listOfHashMaps);
		}
		return serverRequests;
	}
	
	private List<ServerRequest> convertLinkedHashMapToArrayList(List<LinkedHashMap> linkedHashMaps) throws JsonMappingException, JsonProcessingException {
		var serverRequests = new ArrayList<ServerRequest>();
		for (var hashMap : linkedHashMaps) {
			var serverRequest = convertHashMapToServerRequest((LinkedHashMap)hashMap);
			serverRequests.add(serverRequest);
		}
		return serverRequests;
	}

	// 		var json = "[{\"id\":0,\"ram\":16,\"cpus\":4,\"os\":null,\"middleware\":null,\"transientMiddleware\":null,\"disks\":[{\"id\":0,\"mountPoint\":\"/\",\"size\":100,\"volumeGroup\":null,\"volumeName\":null,\"server\":null,\"environment\":null},{\"id\":0,\"mountPoint\":\"/apps\",\"size\":100,\"volumeGroup\":null,\"volumeName\":null,\"server\":null,\"environment\":null},{\"id\":0,\"mountPoint\":\"/data\",\"size\":100,\"volumeGroup\":null,\"volumeName\":null,\"server\":null,\"environment\":null}],\"transientDisks\":null,\"environment\":null,\"request\":null}]";

	private ServerRequest convertHashMapToServerRequest(LinkedHashMap hashMap) throws JsonMappingException, JsonProcessingException {
		var serverRequest = new ServerRequest();
		var osMap = (LinkedHashMap)hashMap.get("os");
		var os = convertLinkedHashMapToOs(osMap);
		var ram = (Integer)hashMap.get("ram");
		var cpus = (Integer)hashMap.get("cpus");
		var disksList = (List<LinkedHashMap>)hashMap.get("disks");
		var disks = parseDisks(disksList);
		var middleWareList = (List<LinkedHashMap>)hashMap.get("middlewareRequests");
		var middleWareRequests = convertLinkedHashMapToMiddlewareRequests(middleWareList);
		log.info("Found {} middleware requests", middleWareRequests.size());
		serverRequest.setOs(os);
		serverRequest.setRam(ram);
		serverRequest.setCpus(cpus);
		serverRequest.setDisks(disks);
		serverRequest.setMiddlewareRequests(middleWareRequests);
		return serverRequest;
	}
	
	private Os convertLinkedHashMapToOs(LinkedHashMap osMap) {
		var id = (Integer)osMap.get("id");
		var name = (String)osMap.get("name");
		var os = new Os();
		os.setId(id);
		os.setName(name);
		return os;
	}

	private List<MiddlewareRequest> convertLinkedHashMapToMiddlewareRequests(List<LinkedHashMap> middleWareList) {
		var middlewareRequests = new ArrayList<MiddlewareRequest>();
		log.info("Converting linked hashmap to middleware list");
		if (middleWareList != null) {
			for (var middlewareMap : middleWareList) {
				log.info("found middleware map");
				var middlewareItem = convertMapToMiddlewareRequest(middlewareMap);
				middlewareRequests.add(middlewareItem);
			}
		}
		
		return middlewareRequests;
	}

	private MiddlewareRequest convertMapToMiddlewareRequest(LinkedHashMap middlewareMap) {
		var middlewareRequest = new MiddlewareRequest();
		log.info("Converting linked hash map to middleware request");
		var middlewareInnerMap = (LinkedHashMap)middlewareMap.get("middleware");
		var id = (Integer)middlewareInnerMap.get("id");
		var name = (String)middlewareInnerMap.get("name");
		log.info("Id = {}, name = {}", id, name);
		var middleware = new Middleware();
		middleware.setId(id);
		middleware.setName(name);
		/* TODO check
		middlewareRequest.setMiddleware(middleware);
		
		var environment = (String)middlewareMap.get("environment");
		middlewareRequest.setEnvironment(environment);*/
		return middlewareRequest;
	}

	private List<DiskRequest> parseDisks(List<LinkedHashMap> disksList) throws JsonMappingException, JsonProcessingException {
		var diskRequests = new ArrayList<DiskRequest>();
		
		for (var diskMap : disksList) {
			var diskRequest = convertLinkedHashMapToDiskRequest(diskMap);
			diskRequests.add(diskRequest);
		}
		
		return diskRequests;

	}
	
	private DiskRequest convertLinkedHashMapToDiskRequest(LinkedHashMap hashMap) {
		// mountPoint
		// size
		// volumeGroup
		// volumeName
		var diskRequest = new DiskRequest();
		var mountPoint = (String)hashMap.get("mountPoint");
		var size = (Integer)hashMap.get("size");
		var volumeGroup = (String)hashMap.get("volumeGroup");
		var volumeName = (String)hashMap.get("volumeName");
		diskRequest.setMountPoint(mountPoint);
		diskRequest.setSize(size);
		diskRequest.setVolumeGroup(volumeGroup);
		diskRequest.setVolumeName(volumeName);
		return diskRequest;
	}

	public String convertServerRequestsToJson(List<ServerRequest> serverRequests) {
		ObjectMapper om = new ObjectMapper();
		StringWriter sw = new StringWriter();
		try {
			om.writeValue(sw, serverRequests);
		} catch (IOException e) {
			log.error(e.toString());
			e.printStackTrace();
		}
		
		return sw.toString();
	}
}
