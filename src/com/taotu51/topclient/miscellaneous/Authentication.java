package com.taotu51.topclient.miscellaneous;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.directory.Attributes;
import javax.naming.ldap.InitialLdapContext;


public class Authentication {

	public static String [] getValidLdapLoginInfo(String username, String password) {
		//System.out.println(password + "===========-------==============" + username);
		String [] userInfo = new String[2];
		Hashtable<String, Object> env = new Hashtable<String, Object>();

		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://chands2.bwh.harvard.edu:389/");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "uid=" + username
				+ ",ou=SRS,ou=Apps,dc=channing,dc=harvard,dc=edu");
		env.put(Context.SECURITY_CREDENTIALS, password);
		
		try {
			//System.out.println("=========================" + username);
			InitialLdapContext ctx = new InitialLdapContext(env, null);
			//System.out.println(ctx.toString());
			Attributes attrs = ctx.getAttributes("uid=" + username + ",ou=SRS,ou=Apps,dc=channing,dc=harvard,dc=edu");
			//System.out.println(attrs.toString());
			System.out.println(attrs.get("cn").get());
			System.out.println(attrs.get("uid").get());
			userInfo[0] = attrs.get("cn").get().toString();
			userInfo[1] = attrs.get("uid").get().toString();
			ctx.close();
		} catch (Exception e) {
			e.printStackTrace(); 
			System.out.println("00000000000000000000000000000");
		}

		return userInfo;
	}

}
