package nl.uwv.otod.otod_portal.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Privilege {

	@Id
	@GeneratedValue
	private long id;
	
	private String name;
	
	@ManyToMany
	@JoinTable(
			  name = "role_privilege", 
			  joinColumns = @JoinColumn(name = "privilege_id"), 
			  inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;
}
