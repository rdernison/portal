package javaxt.security;

import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ActiveDirectoryTest {

	private static Logger logger = LogManager.getLogger();
	@Disabled
	@Test
	public void testGetConnection() {
		try {
			ActiveDirectory.getConnection("rde062", "Leiden0001#");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public byte[] encodePassword(String password) {
		byte[] pwdArray = null;
		try
		{
		    System.out.println("updating password...\n");
		    String quotedPassword = "\"" + password + "\"";
		    char unicodePwd[] = quotedPassword.toCharArray();
		    /*byte */pwdArray/*[]*/ = new byte[unicodePwd.length * 2];
		    for (int i = 0; i < unicodePwd.length; i++)
		    {
			pwdArray[i * 2 + 1] = (byte) (unicodePwd[i] >>> 8);
			pwdArray[i * 2 + 0] = (byte) (unicodePwd[i] & 0xff);
		    }
		    System.out.print("encoded password: ");
		    for (int i = 0; i < pwdArray.length; i++)
		    {
			System.out.print(pwdArray[i] + " ");
		    }
		    /*
		    System.out.println();
		    ModificationItem[] mods = new ModificationItem[1];
		    mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("UnicodePwd", pwdArray));
		    ldapContext.modifyAttributes("cn=" + username + BASE_NAME, mods);*/
		}
		catch (Exception e)
		{
		    System.out.println("update password error: " + e);
		}
		return pwdArray;
	}
	
	@Test
	public void testVerifyUsername() throws NamingException {
		var context = new InitialLdapContext();
		var username = "rde062";
		var user = ActiveDirectory.getUser(username, context);
		logger.info("User : {}", user);
	}
}
