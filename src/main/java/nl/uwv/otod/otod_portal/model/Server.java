package nl.uwv.otod.otod_portal.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

// status: enum
// os: lijstje

@Entity
@Getter
@Setter
public class Server {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
//	@Size()
	private String name;
	private String objId;
	private String ipAddress;

	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;
	@Transient
	private String projectName;
	private int virtualCpus;
	private double physicalCores;
	private int ram;
	private String monitoring;
	private String pool;
	private int systemDisk;
	@ManyToOne
	@JoinColumn(name = "os_id")
	private Os os;
	
	@Column(name = "os")
	private String osName;
	private String username;
	private String rootPassword;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private LocalDate productionDate;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private LocalDate removalDate;
	private String status;
	private String adminPassword;
	private transient String productionDateStr;
	private transient String removalDateStr;

	@Column(name = "project_id", insertable = false, updatable = false)
	private Long projectId;

	@Column(name = "os_id", insertable = false, updatable = false)
	private Long osId;
	
//	private transient String osName;
	
	private String username2;
	private String password2;
	
	private String remark;
	
	@Transient
	private Iterable<Disk> disks;
	
	@OneToOne
	@JoinColumn(name = "server_request_id")
	private ServerRequest serverRequest;
}
