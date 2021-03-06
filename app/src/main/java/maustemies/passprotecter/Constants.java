package maustemies.passprotecter;

/**
 * Created by Maustemies on 25.11.2016.
 */

public class Constants {

    // Request Codes
    public static final int REQUEST_CODE_FILE_CREATOR = 10;
    public static final int REQUEST_CODE_FILE_CHOOSER = 11;
    public static final int REQUEST_CODE_PASSWORD_QUERY_POPUP = 12;
    public static final int REQUEST_CODE_PASSWORD_ADD_POPUP = 13;

    // General tags
    public static final String FILENAME_TAG = "fileName";
    public static final String MAIN_PASSWORD_TAG = "MainPassword";
    public static final String PASSWORD_TAG = "password";
    public static final String PASSWORD_IDENTIFIER_TAG = "passwordId";

    // Log tags
    public static final String MAIN_ACTIVITY_TAG = "MainActivity";
    public static final String FILE_MANAGER_TAG = "FileManager";
    public static final String JSON_UTILS_TAG = "JSONUtils";
    public static final String CREATE_FILE_ACTIVITY_TAG = "CreateFileActivity";
    public static final String OPEN_FILE_ACTIVITY_TAG = "OpenFileActivity";
    public static final String PASSWORD_POPUP_TAG = "PasswordQueryPopUp";
    public static final String SECURITY_ENGINE_TAG = "SecurityEngine";
}
