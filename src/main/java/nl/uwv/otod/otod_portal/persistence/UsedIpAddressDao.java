package nl.uwv.otod.otod_portal.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.UsedIpAddress;

@Repository
public interface UsedIpAddressDao extends CrudRepository<UsedIpAddress, Long> {

}
