package nl.uwv.otod.otod_portal.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
//	@NotNull
//	@Size(min = 2, max=30)
	private String name;
	private String description;
	private String objId;
	private String budgetOwner;
	private String spoc;
	private String costPlace;
//	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	private boolean showEndDate;
	private String status;
//	@NotNull
//	@Size(min = 4, max = 30)
	private String projectId;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate statusDate;
//	@OneToMany
//	private List<Server> servers;
	
	private transient String startDateStr;
	private transient String statusDateStr;
	
	@Override
	public String toString() {
		return "{" + 
				"\"id\": " + id + "," + 
	"\"name\": \"" + name + "\""
 				+ "}";
	}

}
