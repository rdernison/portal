package nl.uwv.otod.otod_portal.init;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.io.MiddlewareReader;
import nl.uwv.otod.otod_portal.model.Middleware;
import nl.uwv.otod.otod_portal.service.MiddlewareService;

@Component
@Log4j2
public class MiddlewareInitializer {
	@Autowired
	private MiddlewareService middlewareService;
	
	public void checkMiddleware() {
		log.info("Checking middleware");
		var allMiddleware = middlewareService.getAll();
		if (!allMiddleware.iterator().hasNext()) {
			readAndStoreMiddleware();
		}

		readAdditionalMiddleware();
	}
	
	private void readAndStoreMiddleware() {
		log.info("No middleware found, reading from file");
		try {
			var allMiddleware = new MiddlewareReader().readAllMiddleware();
			for (var middleware : allMiddleware) {
				log.info("Found middleware: {}", middleware.getName());
				middlewareService.save(middleware);
			}
		} catch (IOException e) {
			log.error("Error storing middleware: {}", e.toString());
		}
	}

	private void readAdditionalMiddleware() {
		log.info("Reading additional middleware");
		var middlewareReader = new MiddlewareReader();
		var middlewareItems = middlewareReader.readMiddlewareLibraries();
		var middlewareInDb = middlewareService.getAll();
		for (var middleware : middlewareItems) {
			if (!contains(middlewareInDb, middleware)) {
				log.info("Middleware not present. Saving it");
				middlewareService.save(middleware);
			}
		}
	}

	private boolean contains(Iterable<Middleware> middlewareInDb, Middleware middleware) {
		var present = false;
		for (var middlewareItemInDb : middlewareInDb) {
			if (middlewareItemInDb.getName().equals(middleware.getName())) {
				present = true;
				break;
			}
		}
		log.info("Is middleware '{}' present? {}", middleware.getName(), present);
		return present;
	}

}
