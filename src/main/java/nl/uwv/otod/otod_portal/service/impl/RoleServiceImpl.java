package nl.uwv.otod.otod_portal.service.impl;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.Role;
import nl.uwv.otod.otod_portal.model.User;
import nl.uwv.otod.otod_portal.persistence.RoleDao;
import nl.uwv.otod.otod_portal.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	private static Logger logger = LogManager.getLogger();
	
	@Autowired
	private RoleDao roleDao;

	@Override
	public Optional<Role> getById(long id) {
		return roleDao.findById(id);
	}

	@Override
	public Iterable<Role> getAll() {
		return roleDao.findAll();
	}

	@Override
	public void save(Role role) {
		roleDao.save(role);
	}

	@Override
	public Iterable<Role> getByName(String name) {
		return roleDao.findByName(name);
	}

	@Override
	public Iterable<Role> getByUser(User user) {
		logger.info("Getting roles by user: {} {}", user.getId(), user.getUsername());
		return roleDao.findByUser(user.getId());
	}
}
