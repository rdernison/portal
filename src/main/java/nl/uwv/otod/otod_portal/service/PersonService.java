package nl.uwv.otod.otod_portal.service;

import java.util.List;

import nl.uwv.otod.otod_portal.model.Person;

public interface PersonService {

	void addContact(Person person);
	
	List<Person> getDevPeopleByRequestId(long requestId);
	List<Person> getTestPeopleByRequestId(long requestId);
	
	void save(Person person);
}
