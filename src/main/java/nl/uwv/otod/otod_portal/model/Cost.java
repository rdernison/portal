package nl.uwv.otod.otod_portal.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
public class Cost {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private CostType type;
	private double price;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp creationTime;
}
