package nl.uwv.otod.otod_portal.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
//import javax.validation.Valid;
//import javax.validation.constraints.Email;
//import javax.validation.constraints.Size;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Request {
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	@Id
	@GeneratedValue
	private long id;

//	@Size(min = 4, max = 256, message = "Titel moet tussen de 4 en 256 karakters bevatten")
	private String title; // deprecated ?
	private String employee;
//	@Size(min = 8, message = "Projectnaam moet mininmaal 8 karakters bevatten")
	private String projectName; // +
	@DateTimeFormat(pattern = DATE_FORMAT)
	private LocalDate requestDate;
	@DateTimeFormat(pattern = DATE_FORMAT)
	private LocalDate desirableDeliveryDate;
	@DateTimeFormat(pattern = DATE_FORMAT)
	private LocalDate deliveryDate;
	
//	@Size(min = 10, message = "Naam service-aanvrager moet minimaal 10 karakaters bevatten")
	private String nameServiceRequester;
//	private String nameBudgetHolder;
	private String signedContract;
	private boolean modified;
	@DateTimeFormat(pattern = DATE_FORMAT)
	private LocalDate modificationDate;

//	@Email
//	@Size(min = 10, message = "Emailadres serviceaanvrager moet minimaal 10 karakters bevatten")
	private String emailAddressServiceRequester;
	private String nameBudgetOwner;
//	@Email
	private String emailAddressBudgetOwner;
//	@Size(min = 8)
	private String directorate;
//	@Size(min = 8)
	private String costCenter;
	private String nameMvoDelegate;
//	@Email
	private String emailAddressDelegate;
	private String otodProjectName;
//	@Size(min = 6, max = 32, message = "Contactpersoon moet 6 tot 32 karakters bevatten")
	private String contact;
	private String department;
//	@Email
	private String emailAddressServiceProvider;
	private String desirableDeliveryDateStr;
	private String deliveryDateStr;
	private LocalDate date;
	private String takenBy;
	private String interfs;
//	@Size(min = 3, message = "Applicatienaam moet minimaal 3 karakters bevatten")
	private String applicationName;
	private String applicationType;
	@Column(length = 1024)
	private String applicationDescription;
	@Column(length = 1024)
	private String additionalRemarks;
	@Column(length = 1024)
	private String additionalRemarks1;
	@Column(length = 1024)
	private String additionalRemarks2;
	private boolean saveToEdit;
	
//    @Valid
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id")
    private List<ServerRequest> devServers = new ArrayList<>();
//    @Valid
	@OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id")
	private List<ServerRequest> testServers = new ArrayList<>();
	
	
	// ALTER TABLE mytable ALTER COLUMN mycolumn TYPE varchar(40);
	@Column(length = 5000)
	private String devServerRequestsJson;
	@Column(length = 5000)
	private String testServerRequestsJson;
	
	private String devUsersJson;
	private String testUsersJson;
	
    @ManyToOne
	private User user;
	private String comment;
	
//    @Valid
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id")
    private List<Person> devUsers = new ArrayList<>();//initDevUsers();
	
//    @Valid
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id")
    private List<Person> testUsers = new ArrayList<>();//initTestUsers();
    
    private boolean licensesAccepted;
}
