package nl.uwv.otod.otod_portal.model;

public enum SizingDocumentRowStatus {

	NEW("New"), IN_PROGRESS("In-Progress"), DELIVERED("Delivered"), CANCELED("canceled"), DECOMMISSIONED("Decom");
	
	private String name;
	
	private SizingDocumentRowStatus(String name) {
		this.name = name;
	}
	
	
	public String getName() {
		return name;
	}
	
}
