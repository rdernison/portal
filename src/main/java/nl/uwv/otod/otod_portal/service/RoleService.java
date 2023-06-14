package nl.uwv.otod.otod_portal.service;

import java.util.Optional;

import nl.uwv.otod.otod_portal.model.Role;
import nl.uwv.otod.otod_portal.model.User;

public interface RoleService {

	Optional<Role> getById(long id);
	Iterable<Role> getAll();
	void save(Role role);
	Iterable<Role> getByName(String name);
	Iterable<Role> getByUser(User user);
}
