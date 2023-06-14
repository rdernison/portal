package nl.uwv.otod.otod_portal.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;

public class MiddlewareReaderTest {

	private MiddlewareReader middlewareReader = new MiddlewareReader();
	@Test
	public void testReadMiddleware() {
		var middleware = middlewareReader.readMiddlewareLibraries();
		assertNotNull(middleware);
		assertNotSame(0, middleware.size());
		
		var aMiddleware = middleware.get(0);
		assertEquals("Oracle Database 19c", aMiddleware.getName());
	}
}
