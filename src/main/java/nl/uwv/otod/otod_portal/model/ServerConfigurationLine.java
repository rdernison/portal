package nl.uwv.otod.otod_portal.model;

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
public class ServerConfigurationLine {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	@JoinColumn(name = "server_configuration_id")
	private ServerConfiguration serverConfiguration;
	
	@ManyToOne
	@JoinColumn(name = "middleware_id")
	private Middleware middleware;
}
