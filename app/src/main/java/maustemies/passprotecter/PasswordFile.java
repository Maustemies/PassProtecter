package maustemies.passprotecter;

import android.support.v4.util.ArrayMap;

/**
 * Created by Maustemies on 25.11.2016.
 */

public class PasswordFile {

    public static final String MAIN_PASSWORD_TAG = "MainPassword";
    public static final String PASSWORD_LIST_OBJECT_TAG = "Passwords";

    String MainPassword;
    ArrayMap<String, String> Passwords;
}
