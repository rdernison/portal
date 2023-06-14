package nl.uwv.otod.otod_portal.model;

public enum DiskStatus {
	OPERATIONAL(1, "Operational"), DECOMMISSIONED(2, "Decommissioned");
	private final Integer id;
	private final String text;
	
	DiskStatus(Integer id, String text) {
		this.id = id;
		this.text = text;
	}
	
	public Integer getid() {
		return id;
	}
	
	public String getText() {
		return text;
	}
}
