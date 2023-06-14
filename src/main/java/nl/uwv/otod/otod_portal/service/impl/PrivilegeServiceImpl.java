package nl.uwv.otod.otod_portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.Privilege;
import nl.uwv.otod.otod_portal.model.Role;
import nl.uwv.otod.otod_portal.persistence.PrivilegeDao;
import nl.uwv.otod.otod_portal.service.PrivilegeService;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

	@Autowired
	private PrivilegeDao privilegeDao;
	
	public List<Privilege> getByRole(Role role) {
		return privilegeDao.findByRole(role.getId());
	}

	@Override
	public void save(Privilege privilege) {
		privilegeDao.save(privilege);
		
	}
 }
