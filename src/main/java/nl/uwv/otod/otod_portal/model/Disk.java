package nl.uwv.otod.otod_portal.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Disk {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
//	@Size(min = 4)
	private String name;
	private int size;

//	@Size(min = 6, max = 16, message = "Computernaam moet tussen de 6 en 16 karakters bevatten")
	private String computerName;
	private int diskId;
	private String fileSystemName;
//	@Size(min  = 1, max = 16, message = "Mount point moet tussen de 1 en 16 karakters bevatten")
	private String mountPoint;
//	@Size(min = 4, max = 32, message = "Poolnaam moet tussen de 4 en 32 karakters bevatten")
	private String poolName;
	private String volumeName;
	private String volumeGroup;

	private /*DiskStatus*/Integer status;
	
	private String objId;
	
	private LocalDate removalDate;
//	@Size(min = 10, max = 10, message = "Datum moet 10 karakters bevatten")
	private String removalDateStr;
	
	private String statusName;
	
	@ManyToOne
	@JoinColumn(name = "server_id")
	private Server server;
	
//	@Column(name = "server_id", insertable = false, updatable = false)
//	private long serverId; 
	
	private DiskStatus diskStatus;
	
	@ManyToOne
	private FileSystem fileSystem;
}
