package nl.uwv.otod.otod_portal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MiddlewareRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String environment;
	
	@ManyToOne
	private Middleware middleware;
}
