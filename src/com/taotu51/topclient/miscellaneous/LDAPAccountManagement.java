package com.taotu51.topclient.miscellaneous;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.InitialLdapContext;

public class LDAPAccountManagement {
	
	public boolean addAccount(String userID, String password) {
		//System.out.println(password + "===========-------==============" + username);
		Hashtable<String, Object> env = new Hashtable<String, Object>();

		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://chands2.bwh.harvard.edu:389/");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "uid=rezca,ou=People,dc=channing,dc=harvard,dc=edu");
		env.put(Context.SECURITY_CREDENTIALS, "zc970703");
		System.out.println(userID+"xxxxxxxxxxxxxxxxxxxxxxxx");
		try {
			//System.out.println("=========================" + username);
			InitialLdapContext ctx = new InitialLdapContext(env, null);
			Attributes attrs = new BasicAttributes();
			attrs.put("objectclass", "inetOrgPerson");
			attrs.put("uid", userID);
			attrs.put("cn", "user");
			attrs.put("sn", userID);
			attrs.put("userPassword", password);
			
			ctx.createSubcontext("uid=" + userID + ",ou=SRS,ou=Apps,dc=channing,dc=harvard,dc=edu", attrs);
			//System.out.println(ctx.createSubcontext("cn=" + userID + ",ou=SRS,ou=Apps,dc=channing,dc=harvard,dc=edu", attrs));
			ctx.close();
		} catch (Exception e) {
			e.printStackTrace(); 
			System.out.println("00000000000000000000000000000");
			return false;
		}

		return true;
	}
	
	public boolean deleteAccount(String userID) {
		//System.out.println(password + "===========-------==============" + username);
		Hashtable<String, Object> env = new Hashtable<String, Object>();

		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://chands2.bwh.harvard.edu:389/");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "uid=rezca,ou=People,dc=channing,dc=harvard,dc=edu");
		env.put(Context.SECURITY_CREDENTIALS, "zc970703");
		System.out.println(userID+"xxxxxxxxxxxxxxxxxxxxxxxx");
		try {
			//System.out.println("=========================" + username);
			InitialLdapContext ctx = new InitialLdapContext(env, null);			
			ctx.destroySubcontext("uid=" + userID + ",ou=SRS,ou=Apps,dc=channing,dc=harvard,dc=edu");
			//System.out.println(ctx.createSubcontext("cn=" + userID + ",ou=SRS,ou=Apps,dc=channing,dc=harvard,dc=edu", attrs));
			ctx.close();
		} catch (Exception e) {
			e.printStackTrace(); 
			System.out.println("00000000000000000000000000000");
			return false;
		}

		return true;
	}
}
