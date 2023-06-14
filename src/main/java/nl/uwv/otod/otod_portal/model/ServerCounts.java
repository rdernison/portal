package nl.uwv.otod.otod_portal.model;

public class ServerCounts {
	private int numberOfProjects;
	private int numberOfServers;
	private double physicalCores;
	private int virtualCpus;
	private int ram;
	private int systemDisk;
	private int extraDiskSpace;

	public int getNumberOfProjects() {
		return numberOfProjects;
	}

	public void setNumberOfProjects(int numberOfProjects) {
		this.numberOfProjects = numberOfProjects;
	}

	public int getNumberOfServers() {
		return numberOfServers;
	}

	public void setNumberOfServers(int numberOfServers) {
		this.numberOfServers = numberOfServers;
	}

	public double getPhysicalCores() {
		return physicalCores;
	}

	public void setPhysicalCores(double physicalCores) {
		this.physicalCores = physicalCores;
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

	public int getSystemDisk() {
		return systemDisk;
	}

	public void setSystemDisk(int systemDisk) {
		this.systemDisk = systemDisk;
	}

	public int getExtraDiskSpace() {
		return extraDiskSpace;
	}

	public void setExtraDiskSpace(int extraDiskSpace) {
		this.extraDiskSpace = extraDiskSpace;
	}

}
