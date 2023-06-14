package nl.uwv.otod.otod_portal.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import nl.uwv.otod.otod_portal.model.Role;

@Repository
public interface RoleDao extends CrudRepository<Role, Long>{
	@Query("select r from Role r where name=:name")
	Iterable<Role> findByName(@PathVariable("name") String name);

	// 	@Query("select p from Privilege p join p.roles r where r.id=:roleId")

	@Query("select r from Role r JOIN r.users u WHERE u.id=:userId")
	Iterable<Role> findByUser(@PathVariable("userId") long userId);
}
