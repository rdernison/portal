package nl.uwv.otod.otod_portal.ldap;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.util.SettingsUtil;

@Log4j2
public class LdapTest {

	private String cn  = "CN=rde062,OU=uwv.nl,OU=AppplicationNet,OU=Hosting,DC=uwv,DC=wpol,Dc=nl";

	private static String ldapUrl = "ldaps://10.178.128.46:3389";
	// 192.168.196.13
	// in t-dc.ba.uwv.nl/OToD/Accounts/totem
	// opo001-tmp / HatseFlatse01#
	// default keystore password: changeit
	// alias: uwv
	@Disabled
	@Test
	public void testConnectOtodUser() {
		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
				  new javax.net.ssl.HostnameVerifier(){

				      public boolean verify(String hostname,
				             javax.net.ssl.SSLSession sslSession) {
				          return hostname.equals("localhost"); // or return true
				      }
				  });
		try {
			var ldapUsername = (String)SettingsUtil.readSetting("LDAP_USERNAME");
			var ldapPassword = (String)SettingsUtil.readSetting("LDAP_PASSWORD");
			Hashtable<String, String> ldapProperties = new Hashtable<>();
			ldapProperties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			ldapProperties.put(Context.PROVIDER_URL, 
					"ldaps://uwvm3vwtapp0001.T-dc.ba.uwv.nl:3389" 
					/* 
					"ldap://uwvm3vwtapp0001.T-dc.ba.uwv.nl:389" 
					"" "ldaps://10.178.128.46:3389"*/);
//			ldapProperties.put(Context.PROVIDER_URL,"ldap://10.178.128.46:389");
			//ldapProperties.put(Context.PROVIDER_URL,"ldap://192.168.196.13:389");
			ldapProperties.put(Context.SECURITY_AUTHENTICATION, "simple");
			ldapProperties.put(Context.SECURITY_PRINCIPAL, ldapUsername + "@T-dc.ba.uwv.nl");

//			ldapProperties.put(Context.SECURITY_PRINCIPAL, "CN = uwvm3vwtapp0001.T-dc.ba.uwv.nl");//, ou=NewHires, o=JNDITutorial");
			ldapProperties.put(Context.SECURITY_CREDENTIALS, ldapPassword);
			
			try {
				DirContext ctx = new InitialDirContext(ldapProperties);
			
				var environment = ctx.getEnvironment();
				var keyset = environment.keys();
				
				while (keyset.hasMoreElements()) {
					var key = keyset.nextElement();
					log.info("Found object: {} -> {}", key, environment.get(key));
				}
			} catch(NamingException ne) {
				ne.printStackTrace();
				throw new RuntimeException(ne);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Disabled
	@Test
	private void testAuthenticate() {
		try {
			DirContext ctx = authenticate();
			ctx.getNameInNamespace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static DirContext authenticate() throws NamingException {
	    return authenticate(null, null);
	}

	private static DirContext authenticate(String username, String password) throws NamingException {
	    String userType = username != null ? "App user" : "Master user";
	    System.out.println("INSIDE Authenticate method:  authenticating-> "+ userType);
	    String MASTER_USER = "";
	    String MASTER_PASSWORD = "changeit";
String userSearchBase = "@T-dc.ba.uwv.nl";
	    String user = username != null ? username+userSearchBase : MASTER_USER;
	    String initialContextFactory = "com.sun.jndi.ldap.LdapCtxFactory"; 
	    String securityAuthentication = "simple";

	    Hashtable<String, String> env = new Hashtable<>();
	    env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
	    env.put(Context.SECURITY_AUTHENTICATION, securityAuthentication);
	    env.put(Context.PROVIDER_URL, ldapUrl);
	    String principal = "CN=" + user;
	    System.out.println("principal-> "+principal);
	    env.put(Context.SECURITY_PRINCIPAL, principal);
	    env.put(Context.SECURITY_CREDENTIALS, password != null ? password : MASTER_PASSWORD);
	    // Specify SSL
	    env.put(Context.SECURITY_PROTOCOL, "ssl");
	    DirContext ctx = new InitialDirContext(env);

	    return ctx;
	}
	
	@Disabled
	@Test
	public void test010178128160Ldap() throws NamingException {
		// Set up the environment for creating the initial context
		Hashtable<String, Object> env = new Hashtable<String, Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://10.178.128.160:389");

		// Authenticate as S. User and password "mysecret"
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, 
		        "cn=S. User, ou=NewHires, o=JNDITutorial");
		env.put(Context.SECURITY_CREDENTIALS, "HatseFlatse00!");

		// Create the initial context
		DirContext ctx = new InitialDirContext(env);
	}
	
	@Test
	public void test010178128018Ldap() throws NamingException {
		// Set up the environment for creating the initial context
		
		String username = "rde062-ldap";
		String password = "HatseFlatse00!";
		String base = ",OU=OToD,DC=T-dc,DC=ba,DC=uwv,DC=nl";
		Hashtable<String, Object> env = new Hashtable<String, Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://10.178.128.18:389");

		// Authenticate as S. User and password "mysecret"
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, 
		        /*"CN=" + username +",OU=Accounts" + base*/"CN=rde062-ldap,OU=totem,OU=Accounts,OU=OToD,DC=T-dc,DC=ba,DC=uwv,DC=nl");
		env.put(Context.SECURITY_CREDENTIALS, password);

		// Create the initial context
		DirContext ctx = new InitialDirContext(env);
	}
	
}
