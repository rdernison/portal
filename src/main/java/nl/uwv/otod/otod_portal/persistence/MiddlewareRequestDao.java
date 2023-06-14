package nl.uwv.otod.otod_portal.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.MiddlewareRequest;

@Repository
public interface MiddlewareRequestDao extends JpaRepository<MiddlewareRequest, Long> {

}
