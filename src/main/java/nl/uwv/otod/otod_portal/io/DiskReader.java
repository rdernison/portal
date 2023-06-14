package nl.uwv.otod.otod_portal.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import nl.uwv.otod.otod_portal.model.Disk;

@Component
public class DiskReader {

	
	public List<Disk> readAllDisks() throws IOException {
		var allDisks = new ArrayList<Disk>();
		try (var in = getClass().getResourceAsStream("/disks.csv");
				BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			var line = "";
			while ((line = br.readLine()) != null) {
				var disk = convertLine(line);
				allDisks.add(disk);
			}
		}
		return allDisks;
	}
	
	@Deprecated
	public List<Disk> readDisksFromFile(String filename) throws IOException {
		List<Disk> allDisks = new ArrayList<>();
		try (InputStream in = getClass().getResourceAsStream("/disks.csv");
				BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				Disk disk = convertLine(line);
				allDisks.add(disk);
			}
		}
		return allDisks;
	}
	
	// Computernaam	Schijf ID	Filesysteem	Mount pad	Pool naam	Schijfgrootte (MB)	Volume naam	Volumegroep	Status

	private Disk convertLine(String line) {
		var disk = new Disk();
		var cols = line.split("\t");
		disk.setComputerName(cols[0]);
		disk.setDiskId(Integer.parseInt(cols[1]));
		var fileSystemName = cols[2];
		disk.setFileSystemName(fileSystemName);
		disk.setMountPoint(cols[3]);
		disk.setPoolName(cols[4]);
		var col5 = cols[5];
		if (col5.contains(" ")) {
			col5 = col5.substring(0, col5.indexOf(" ")) + col5.substring(col5.indexOf(" ") + 1);
		}
		disk.setSize(Integer.parseInt(col5));
		disk.setVolumeName(cols[6]);
		disk.setVolumeGroup(cols[7]);
		disk.setStatus(cols[8].equals("Operational") ? /*DiskStatus.OPERATIONAL : DiskStatus.DECOMMISSIONED*/ 1 : 0);
		return disk;
	}
}
