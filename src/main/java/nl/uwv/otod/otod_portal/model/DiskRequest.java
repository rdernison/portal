package nl.uwv.otod.otod_portal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class DiskRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
//	@Size(min = 1, max = 16, message = "Mount point moet tussen de 1 en 16 karakters bevatten")
	private String mountPoint;
//	@Size(min = 1, max = 3, message = "Diskgrootte moet tussen de 1 en 3 karakters bevatten")
	@Column(columnDefinition = "int default 0")
	private int size;
	private String volumeGroup;
	private String volumeName;
	/*
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "server_request_id")
	private ServerRequest server;*/

//	@Size(min = 3, max = 16, message = "Omgeving moet tussen de 3 en 16 karakters bevatten")
	private String environment;

}
