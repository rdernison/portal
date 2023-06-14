package nl.uwv.otod.otod_portal.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.model.Middleware;

@Log4j2
public class MiddlewareReader extends GenericReader {

	public List<Middleware> readAllMiddleware() throws IOException {
		var line = "";
		var allMiddleware = new ArrayList<Middleware>();
		File inputDir = new File(inputPath);
		try (var br =  new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputDir, "middleware.csv"))))) {
			while ((line = br.readLine()) != null) {
				var software = convertLine(line);
				allMiddleware.add(software);
			}
		}

		return allMiddleware;
	}
	
	private Middleware convertLine(String line) {
		var idName = line.split(",");
		var middleware = new Middleware();
		middleware.setId(Long.parseLong(idName[0]));
		middleware.setName(idName[1]);
		return middleware;
	}
	
	public List<Middleware> readMiddlewareLibraries() {
		var middlewareItems = new ArrayList<Middleware>();
		try(var inputStream = getClass().getResourceAsStream("/middleware-libraries.txt");
				var bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				var middleware = makeMiddleware(line);
				middlewareItems.add(middleware);
			}
			
		} catch(IOException e) {
			log.error(e.toString());
		}
		
		return middlewareItems;
		
	}
	
	private Middleware makeMiddleware(String line) {
		var middleware = new Middleware();
		var subs = line.split("\\t");
		var name = subs[2];
		middleware.setEnabled(true);
		middleware.setName(name);
		return middleware;
	}
}
