package nl.uwv.otod.otod_portal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ServerRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private int ram;
	private int cpus;
	
	@ManyToOne
	@JoinColumn(name = "os_id")
	private Os os;
	
	@OneToMany/*(orphanRemoval = true, cascade = CascadeType.ALL)*/
	@JoinColumn(name = "server_request_id")
	private List<MiddlewareRequest> middlewareRequests = new ArrayList<>();

//    @Valid
    @OneToMany/*(orphanRemoval = true, cascade = CascadeType.ALL)*/
    @JoinColumn(name = "server_request_id")
    private List<DiskRequest> disks = new ArrayList<>();
    
    
    private String environment;

	
	@OneToOne(mappedBy = "serverRequest")
	private Server server;
}
