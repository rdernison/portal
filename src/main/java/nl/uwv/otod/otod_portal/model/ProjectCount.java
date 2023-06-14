package nl.uwv.otod.otod_portal.model;

public class ProjectCount {
	private long projectId;
	private String projectName;

	private int numberOfServers;
	private double numberOfPhysicalCores;
	private int systemDisk;
	private int virtualCpus;
	private int ram;
	private int extraDiskSpace;

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getNumberOfServers() {
		return numberOfServers;
	}

	public void setNumberOfServers(int numberOfServers) {
		this.numberOfServers = numberOfServers;
	}

	public double getNumberOfPhysicalCores() {
		return numberOfPhysicalCores;
	}

	public void setNumberOfPhysicalCores(double numberOfPhysicalCores) {
		this.numberOfPhysicalCores = numberOfPhysicalCores;
	}

	public int getSystemDisk() {
		return systemDisk;
	}

	public void setSystemDisk(int systemDisk) {
		this.systemDisk = systemDisk;
	}

	public int getVirtualCpus() {
		return virtualCpus;
	}

	public void setVirtualCpus(int virtualCpus) {
		this.virtualCpus = virtualCpus;
	}

	public int getRam() {
		return ram;
	}

	public void setRam(int ram) {
		this.ram = ram;
	}

	public int getExtraDiskSpace() {
		return extraDiskSpace;
	}

	public void setExtraDiskSpace(int extraDiskSpace) {
		this.extraDiskSpace = extraDiskSpace;
	}

}
