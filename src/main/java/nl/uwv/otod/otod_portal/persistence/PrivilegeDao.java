package nl.uwv.otod.otod_portal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.Privilege;

@Repository
public interface PrivilegeDao extends CrudRepository<Privilege, Long>{
// select b from sample$B b join b.collectionOfA a
//	where a.id = :ds$aDs
	@Query("select p from Privilege p join p.roles r where r.id=:roleId")
	public List<Privilege> findByRole(@Param("roleId") long roleId);
}
