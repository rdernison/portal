package nl.uwv.otod.otod_portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.Person;
import nl.uwv.otod.otod_portal.persistence.PersonDao;
import nl.uwv.otod.otod_portal.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private PersonDao personDao;

	@Override
	public void addContact(Person person) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Person> getDevPeopleByRequestId(long requestId) {
		return personDao.getPeopleByDevEnvironment(requestId);
	}

	@Override
	public List<Person> getTestPeopleByRequestId(long requestId) {
		return personDao.getPeopleByTestEnvironment(requestId);
	}

	@Override
	public void save(Person person) {
		personDao.save(person);
		
	}
	
	
}
