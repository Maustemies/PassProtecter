package maustemies.passprotecter;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.mobapphome.mahencryptorlib.MAHEncryptor;

/**
 * Created by Maustemies on 25.11.2016.
 */

public class SecurityEngine {

    public static String EncryptString(String password, String input) {
        try {
            MAHEncryptor mahEncryptor = MAHEncryptor.newInstance(password);
            String encrypted = mahEncryptor.encode(input);
            return encrypted;
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d(Constants.SECURITY_ENGINE_TAG, "EncryptString error - could not encrypt string because: " + e);
            return "";
        }
    }

    public static String DecryptString(String password, String input) {
        try {
            MAHEncryptor mahEncryptor = MAHEncryptor.newInstance(password);
            String decrypted = mahEncryptor.decode(input);
            return decrypted;
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d(Constants.SECURITY_ENGINE_TAG, "DecryptString - error: " + e);
            return "";
        }
    }

    public static PasswordFile EncryptPasswordFile(String password, PasswordFile input) {
        Log.d(Constants.SECURITY_ENGINE_TAG, "EncryptPasswordFile with password: "+ password);

        PasswordFile encryptedPasswordFile = new PasswordFile();
        encryptedPasswordFile.MainPassword = EncryptString(password, input.MainPassword);

        ArrayMap<String, String> encryptedPasswords = new ArrayMap<>();
        ArrayMap<String, String> passwords = input.Passwords;
        for(int i = 0; i < passwords.size(); i++) {
            String key = passwords.keyAt(i);
            String value = passwords.valueAt(i);
            String encryptedPassword = EncryptString(password, value);
            encryptedPasswords.put(key, encryptedPassword);
        }

        encryptedPasswordFile.Passwords = encryptedPasswords;
        return encryptedPasswordFile;
    }

    public static PasswordFile DecryptPasswordFile(String password, PasswordFile input) {
        Log.d(Constants.SECURITY_ENGINE_TAG, "DecryptPasswordFile with password: " + password);

        PasswordFile decryptedPasswordFile = new PasswordFile();
        decryptedPasswordFile.MainPassword = DecryptString(password, input.MainPassword);

        ArrayMap<String, String> decryptedPasswords = new ArrayMap<>();
        ArrayMap<String, String> encryptedPasswords = input.Passwords;
        for(int i = 0; i < encryptedPasswords.size(); i++) {
            String key = encryptedPasswords.keyAt(i);
            String value = encryptedPasswords.valueAt(i);
            String decryptedPassword = DecryptString(password, value);
            decryptedPasswords.put(key, decryptedPassword);
        }

        decryptedPasswordFile.Passwords = decryptedPasswords;
        return decryptedPasswordFile;
    }
}
