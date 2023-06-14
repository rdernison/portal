package nl.uwv.otod.otod_portal.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.DiskRequest;

@Repository
public interface DiskRequestDao extends JpaRepository<DiskRequest, Long> {

}
