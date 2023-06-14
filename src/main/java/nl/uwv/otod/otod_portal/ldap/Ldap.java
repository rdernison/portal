package nl.uwv.otod.otod_portal.ldap;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Ldap
{
    public static void main(String[]args) {
    	new Ldap().login("rde062tmp", "Theraphosidae#");
    }
    
    public boolean login(String username, String password) {
    	var loginSuccessful = false;
        var environment = new Hashtable<String, String>();
//    	var cn  = "CN=rde062,OU=uwv.nl,OU=AppplicationNet,OU=Hosting,DC=uwv,DC=wpol,Dc=nl";
        var cn  = "CN=rde062,OU=Accounts,OU=OToD,DC=T-dc,DC=ba,DC=uwv,DC=nl";
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        // ip: 
        environment.put(Context.PROVIDER_URL, "ldap://10.178.128.47:12345"/*"ldap://10.178.128.19:389"* /"ldap://10.74.1.2:389"*/); // domain controller
//        environment.put(Context.SECURITY_AUTHENTICATION, "bindwithcredentials");
//        environment.put(Context.SECURITY_AUTHENTICATION, "simple");
        environment.put(Context.SECURITY_AUTHENTICATION, "simple");
        environment.put(Context.SECURITY_PRINCIPAL, cn/*"cn=" + username + ",ou=users,dc=T-dc.ba.uwv,dc=nl"*/);
//        environment.put(Context.SECURITY_PRINCIPAL, "rde062tmp@T-dc.ba.uwv.nl");
        var enc = new BCryptPasswordEncoder();//StandardPasswordEncoder();//DelegatingPasswordEncoder(arg0, arg1);//LdapShaPasswordEncoder();
        var encryptedPassword = /*encrypt*/enc.encode(password);
        System.out.println("Encrypted password: " + encryptedPassword);
        environment.put(Context.SECURITY_CREDENTIALS, encryptedPassword);
        
     // Specify SSL
//        environment.put(Context.SECURITY_PROTOCOL, "ssl");


        try 
        {
            var context = new InitialDirContext(environment);
            System.out.println("Connected...");
            System.out.println(context.getEnvironment());
            loginSuccessful = true;
            context.close();
        } 
        catch (AuthenticationNotSupportedException exception) 
        {
            System.out.println("The authentication is not supported by the server");
            exception.printStackTrace();
        }

        catch (AuthenticationException exception)
        {
            System.out.println("Incorrect password or username: " + exception.toString());
            exception.printStackTrace();
        }

        catch (NamingException exception)
        {
            System.out.println("Error when trying to create the context");
            exception.printStackTrace();
        }
        
        System.out.println("Done");
        
        return loginSuccessful;
    }
    
    private String encrypt(final String password) {
    	String result = null;
    	try {
		    System.out.println("updating password...\n");
		    var quotedPassword = "\"" + password + "\"";
		    var unicodePwd = quotedPassword.toCharArray();
		    var pwdArray = new byte[unicodePwd.length * 2];
		    for (int i = 0; i < unicodePwd.length; i++) {
			pwdArray[i * 2 + 1] = (byte) (unicodePwd[i] >>> 8);
			pwdArray[i * 2 + 0] = (byte) (unicodePwd[i] & 0xff);
		    }
		    System.out.print("encoded password: ");
		    for (int i = 0; i < pwdArray.length; i++)
		    {
			System.out.print(pwdArray[i] + " ");
		    }
		    System.out.println();
	//	    ModificationItem[] mods = new ModificationItem[1];
	//	    mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("UnicodePwd", pwdArray));
	//	    ldapContext.modifyAttributes("cn=" + username + BASE_NAME, mods);
		    result = new String(pwdArray);
		}
		catch (Exception e)
		{
		    System.out.println("update password error: " + e);
		}
    	return result;
    }
}
