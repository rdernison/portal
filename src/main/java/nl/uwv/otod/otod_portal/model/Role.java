package nl.uwv.otod.otod_portal.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.Setter;

/*
public enum Role {

	ADMINISTRATOR, USER;
	
	
}
*/

@Entity
@Getter
@Setter
public class Role {
	@Id
	@GeneratedValue
	private long id;
	private String name;
	
	@ManyToMany
	@JoinTable(
			  name = "role_privilege", 
			  joinColumns = @JoinColumn(name = "role_id"), 
			  inverseJoinColumns = @JoinColumn(name = "privilege_id"))
	private List<Privilege> privileges = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(
			  name = "user_role", 
			  joinColumns = @JoinColumn(name = "role_id"), 
			  inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> users = new ArrayList<>();

}