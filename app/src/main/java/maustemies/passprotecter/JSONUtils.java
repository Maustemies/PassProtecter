package maustemies.passprotecter;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Maustemies on 25.11.2016.
 */

public class JSONUtils {

    public static String CreatePasswordFileInJSON(String password) {
        return "{ " +
                "\""+PasswordFile.MAIN_PASSWORD_TAG+"\" : \""+password+"\"," +
                "\""+PasswordFile.PASSWORD_LIST_OBJECT_TAG+"\" : { }" +
                "}";
    }

    public static PasswordFile GetPasswordFileFromString(String data) {
        Log.d(Constants.JSON_UTILS_TAG, "GetPasswordFileFromString with data: " + data);

        PasswordFile passwordFile = new PasswordFile();

        try {
            JSONObject mainObject = new JSONObject(data);
            passwordFile.MainPassword = mainObject.getString(PasswordFile.MAIN_PASSWORD_TAG);
            JSONObject passwordListObject = mainObject.getJSONObject(PasswordFile.PASSWORD_LIST_OBJECT_TAG);

            Iterator<String> keys = passwordListObject.keys();
            Log.d(Constants.JSON_UTILS_TAG, "GetPasswordFileFromString found keys: " + keys);

            ArrayMap<String, String> passwordList = new ArrayMap<>();
            while(keys.hasNext()) {
                String key = (String) keys.next();
                passwordList.put(key, passwordListObject.getString(key));
            }

            passwordFile.Passwords = passwordList;
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.w(Constants.JSON_UTILS_TAG, "Error in creating a PasswordFile object from String: " + e);
        }

        return passwordFile;
    }
}
