package maustemies.passprotecter;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public static String CreatePasswordFileJSONStringFromPasswordFile(PasswordFile passwordFile) {
        Log.d(Constants.JSON_UTILS_TAG, "CreatePasswordFileJSONStringFromPasswordFile()");

        String result = "";

        try {
            result = "{ \""+PasswordFile.MAIN_PASSWORD_TAG+"\" : \""+passwordFile.MainPassword+"\"," +
                    "\""+PasswordFile.PASSWORD_LIST_OBJECT_TAG+"\" : {";

            ArrayMap<String, String> passwordList = passwordFile.Passwords;
            for(int i = 0; i < passwordList.size(); i++) {
                String passwordObject = "\"" + passwordList.keyAt(i) + "\" : \"" + passwordList.valueAt(i) + "\"";
                if(i < passwordList.size()-1) passwordObject += ",";
                result += passwordObject;
            }

            result += " } }";

        }
        catch (Exception e) {
            e.printStackTrace();
            Log.w(Constants.JSON_UTILS_TAG, "Error in CreatePasswordFileJsonStringFromPasswordFile : " + e);
            result = "";
        }

        return result;
    }
}
