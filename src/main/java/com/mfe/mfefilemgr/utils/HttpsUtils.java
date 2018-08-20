package com.mfe.mfefilemgr.utils;

import org.eclipse.jetty.util.security.Credential;
import org.eclipse.jetty.util.security.Password;

public class HttpsUtils {
    public static String OBFPassword(String plainPasswd){
        return Password.obfuscate((new Password(plainPasswd)).toString());
    }
}
