package nl.uwv.otod.otod_portal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.Request;
import nl.uwv.otod.otod_portal.model.ServerRequest;

@Repository
public interface ServerRequestDao extends JpaRepository<ServerRequest, Long>{
	@Query(value="SELECT * FROM ServerRequest WHERE request_id=:requestid AND environment=:environment", nativeQuery=true)
	public List<ServerRequest> findByRequestAndEnvironment(long requestid, String environment);
}
