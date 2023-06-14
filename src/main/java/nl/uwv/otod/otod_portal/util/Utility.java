package nl.uwv.otod.otod_portal.util;

import javax.servlet.http.HttpServletRequest;

public class Utility {
	public static String getSiteURL(HttpServletRequest request) {
        var siteURL = request.getRequestURL().toString();
        if (!(siteURL.contains("1972") || siteURL.contains("8080"))) {
        	siteURL = siteURL.replace("http:", "https:");
        }
        return siteURL.replace(request.getServletPath(), "");
    }
}
