package nl.uwv.otod.otod_portal.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDescriptionReader extends GenericReader {

	public List<String> readProjectDescriptions() throws UnsupportedEncodingException, IOException {
		var projectDescriptions = new ArrayList<String>();
		var inputDir = new File(inputPath);
		try (var in = new FileInputStream(new File(inputDir, "project_omschrijving.csv"));
				var br = new BufferedReader(new InputStreamReader(in, "utf-8"))) {
			var line = "";
			while ((line = br.readLine()) != null) {
				projectDescriptions.add(line);
			}
			
		}
		
		return projectDescriptions;
	}
}
