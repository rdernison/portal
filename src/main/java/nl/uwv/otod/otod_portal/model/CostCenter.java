package nl.uwv.otod.otod_portal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Entity
@Getter
@Setter
@Builder
@Log4j2
public class CostCenter {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private int level;
	
	private String description;
	
	private String code;
	
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private CostCenter parent;
	
	public CostCenter() {}
	
	public CostCenter(long id, int level, String description, String code, CostCenter parent) {
		this.id = id;
		this.level = level;
		this.description = description;
		this.code = code;
		this.parent = parent;
	}
}
