package nl.uwv.otod.otod_portal.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nl.uwv.otod.otod_portal.model.Os;

public class OsReader {

	public List<Os> readOses() throws IOException {
		List<Os> oses = new ArrayList<>();
		try (InputStream in = getClass().getResourceAsStream("/otod-overview-oses.csv" );
				BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				Os os = makeOs(line);
				oses.add(os);
			}
		}
		
		return oses;
	}
	
	private Os makeOs(String line) {
		String[] subs = line.split(",");
		int number = Integer.parseInt(subs[0]);
		String name = subs[1];
		Os os = new Os();
		os.setName(name);
		os.setNumber(number);
		
		var enabled  = Boolean.parseBoolean(subs[2]);
		os.setEnabled(enabled);
		return os;
	}
}
