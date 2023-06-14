package nl.uwv.otod.otod_portal.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.Disk;
import nl.uwv.otod.otod_portal.model.Server;

@Repository
public interface DiskDao extends CrudRepository<Disk, Long> {

	@Query("SELECT d FROM Disk d WHERE d.server=:server")
	public List<Disk> findByServer(@Param("server") Server server);

	@Query("SELECT d FROM Disk d WHERE lower(d.fileSystemName) like lower(concat('%',:fileSystem,'%'))")
	public List<Disk> findByFileSystem(@Param("fileSystem") String fileSystem);
	
	@Query("SELECT d FROM Disk d WHERE lower(d.computerName) like lower(concat('%',:computerName,'%')) AND lower(d.fileSystem) like lower(concat('%',:fileSystem,'%'))")
	public List<Disk> findByComputerNameAndFileSystem(@Param("computerName") String computerName, @Param("fileSystem") String fileSystem);
}
