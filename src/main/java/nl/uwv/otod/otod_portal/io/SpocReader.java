package nl.uwv.otod.otod_portal.io;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SpocReader extends GenericReader {

	public List<String> readSpocs() throws UnsupportedEncodingException, IOException {
		var spocs = new ArrayList<String>();
		var path = Paths.get(inputPath, "spoc.csv");
		var allLines = Files.readAllLines(path);
		for(var line : allLines) {
			spocs.add(line);
		}
		
		return spocs;
	}
}
