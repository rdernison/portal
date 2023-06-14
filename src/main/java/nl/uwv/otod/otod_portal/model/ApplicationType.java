package nl.uwv.otod.otod_portal.model;

public enum ApplicationType {
	GEEN("- Geen -"),MAATWERK("Maatwerk"), PAKKET("Pakket"), MIX("Mix");
	
	private String name;
	
	private ApplicationType(String name) {
		this.name  = name;
	}
	
	public String getName() {
		return name;
	}
}
