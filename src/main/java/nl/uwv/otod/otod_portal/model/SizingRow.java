package nl.uwv.otod.otod_portal.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SizingRow {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; //
	private String employee; //
	private Date arrivalDate; //
	@Deprecated
	private boolean delivered;
	private String wave; //
	private String applicationName; //
	private String function; //
	private String division; //
	private String serverName; //
	private String otodProjectName; //
	private String currentPlatform; //
	private String environment; // omgeving
	private String uwvIpAddress; //
	private int vCpu; //
	private int memory; //
	private String storage; //
	private String role;
	private String discussionAction;
	private String proceed;
	private String sprint;
	private String datacenter; //
	private String dxcEnvironment; // environment
	private String dxcUwvManaged;
	private String serviceTier;
	private String serverType;
	private String os;
	private char oracleDb;
	private char oracleMw;
	private char desirableMw;
	private String tShiftSize;
	private String dataDisk1;
	private String dataDisk2;
	private String dataDisk3;
	private String dataDisk4;
	private String dataDisk5;
	private String dataDisk6;
	private String dataDisk7;
	private String dataDisk8;
	private String dataDisk9;
	private String requestFile;
	private int requestId;
	private String dxcServerName;
	private String passwordSharedWithUwv;
	private String ip;
	private String postActionAdOu;
	private String postActionFlexera;
	private String postActionBackup;
	private String postActionBackupAgent;
	
	private String remark;
	
	private /*SizingDocumentRowStatus*/String status;
	
	private Date otodDeliveryDate; //
	
	
	public String toString() {
		return employee + ";" 
				+ arrivalDate + ";" 
				+ status + ";" 
				+ otodDeliveryDate + ";"
				+ wave + ";" 
				+ applicationName + ";" 
				+ division + ";" 
				+ serverName + ";"
				+ otodProjectName + ";"
				+ currentPlatform + ";"
				+ environment;
	}
}
