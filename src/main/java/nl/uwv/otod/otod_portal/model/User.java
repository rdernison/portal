package nl.uwv.otod.otod_portal.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "portal_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
	@Id
	@GeneratedValue
	private long id;

//	@Size(min = 6)
	private String username;
	private String fullName;
//	@Size(min = 13)
	private String emailAddress;
	private String phoneNumber;
	private byte[] salt;
	private String password;
	private byte[] hashedPassword;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			  name = "user_role", 
			  joinColumns = @JoinColumn(name = "user_id"), 
			  inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
	
	private transient String oldPassword;
	private transient String password1;
	private transient String password2;
	
	@Builder.Default
	private boolean accountNonLocked = true;

	@Builder.Default
	private boolean accountNonExpired = true;

	@Builder.Default
	private boolean credentialsNonExpired = true;

	@UpdateTimestamp
	private Timestamp lastModifiedDate;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp createdDate;
	
	private String resetPasswordToken;
}
