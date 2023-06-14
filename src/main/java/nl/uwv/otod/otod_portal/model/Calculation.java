package nl.uwv.otod.otod_portal.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
//@Builder
public class Calculation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "os_id")
	private Os os;
	private int ram;
	private int systemDisk;
	private int numberOfProcessors;
	private int extraHarddiskSpace;
	private double costPerServerPerMonth;
	private String osName;
	@OneToMany
	private List<CalculationLine> lines;
	
}
