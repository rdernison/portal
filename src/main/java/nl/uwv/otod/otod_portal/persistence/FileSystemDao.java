package nl.uwv.otod.otod_portal.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.FileSystem;

@Repository
public interface FileSystemDao extends CrudRepository<FileSystem, Long> {
	
	@Query("select f from FileSystem f where lower(name)=lower(:name)")
	Optional<FileSystem> findByName(String name);
}
