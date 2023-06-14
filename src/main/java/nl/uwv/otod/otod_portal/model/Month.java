package nl.uwv.otod.otod_portal.model;

public enum Month {
	JANUARI("januari", 1),
	FEBRUARI("februari", 2),
	MAART("maart", 3),
	APRIL("april", 4),
	MEI("mei", 5),
	JUNI("juni", 6),
	JULI("juli", 7),
	AUGUSTUS("augustus", 8),
	SEPTEMBER("september", 9),
	OKTOBER("oktober", 10),
	NOVEMBER("november", 11),
	DECEMBER("december", 12);
	
	private String name;
	private int index;
	
	private Month(String name, int index) {
		this.name = name;
		this.index = index;
	}
	
	public String getName() {
		return name;
	}
	
	public int getIndex() {
		return index;
	}
}
