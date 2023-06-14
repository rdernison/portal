package nl.uwv.otod.otod_portal.builder;

import nl.uwv.otod.otod_portal.model.Disk;

public class DiskBuilder {

//	private DiskBuilder() {}
	
	private String name;
	private String computerName;
	private int size;
	private String fileSystem;
	private String mountPoint;
	
	public DiskBuilder setName(String name) {
		this.name = name;
		return this;
	}
	
	public DiskBuilder setSize(int size) {
		this.setSize(size);
		return this;
	}
	
	public DiskBuilder setComputername(String computerName) {
		this.computerName = computerName;
		return this;
	}
	
	public DiskBuilder setFileSystem(String fileSystem) {
		this.fileSystem = fileSystem;
		return this;
	}
	
	public DiskBuilder setMointPoint(String mountPoint) {
		this.mountPoint = mountPoint;
		return this;
	}
	
	
	
	public Disk build() {
		Disk disk = new Disk();
		disk.setName(name);
		disk.setSize(size);
		disk.setComputerName(computerName);
		disk.setFileSystemName(fileSystem);
		disk.setMountPoint(mountPoint);
		return disk;
	}
	
}
