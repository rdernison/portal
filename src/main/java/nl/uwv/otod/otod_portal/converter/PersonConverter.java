package nl.uwv.otod.otod_portal.converter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.Person;
import nl.uwv.otod.otod_portal.model.ServerRequest;

@Log4j2
public class PersonConverter {
	public List<Person> convertJsonToPeople(String json) throws JsonMappingException, JsonProcessingException {
		List<Person> people;
		if (json == null) {
			people = new ArrayList<>();
		} else {
			var om = new ObjectMapper();
			var listOfHashMaps = om.readValue(json, List.class);
			
			people = convertLinkedHashMapToArrayList(listOfHashMaps);
		}
		return people;
	}
	
	private List<Person> convertLinkedHashMapToArrayList(List<LinkedHashMap> linkedHashMaps) throws JsonMappingException, JsonProcessingException {
		var people = new ArrayList<Person>();
		for (var hashMap : linkedHashMaps) {
			var person = convertHashMapToPerson((LinkedHashMap)hashMap);
			people.add(person);
		}
		return people;
	}

	private Person convertHashMapToPerson(LinkedHashMap hashMap) throws JsonMappingException, JsonProcessingException {
		var person = new Person();
		var userId = (String)hashMap.get("userId");
		var groupName = (String)hashMap.get("groupName");
		var role = (String)hashMap.get("role");
		person.setUserId(userId);
		person.setGroupName(groupName);
		person.setRole(role);
		return person;
	}
	
}
