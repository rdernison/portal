package nl.uwv.otod.otod_portal.scraper;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class OldOtodPortalScraperTest {

	private OldOtodPortalScraper scraper = new OldOtodPortalScraper();
	@Test
	public void testOpenOldOtodPortal() {
		var servers = scraper.readServers();
	}
}
