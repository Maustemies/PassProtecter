package maustemies.passprotecter;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Created by Maustemies on 25.11.2016.
 */

public class FileManager {

    public interface FileManagerMainActivityInterface {
        public void onError(String error);
        public void onFileDataRead(String data);
        public void onPasswordFileOverwritten(PasswordFile passwordFile);
    }

    public interface FileManagerCreateFileInterface {
        public void onError(String error);
        public void onFileCreated(String fileName);
    }

    FileManagerMainActivityInterface mFileManagerMainActivityInterface;
    FileManagerCreateFileInterface mFileManagerCreateFileInterface;
    Context mContext;

    public FileManager(Context context, FileManagerMainActivityInterface fileManagerInterface) {
        mContext = context;
        mFileManagerMainActivityInterface = fileManagerInterface;
    }

    public FileManager(Context context, FileManagerCreateFileInterface fileManagerCreateFileInterface) {
        mContext = context;
        mFileManagerCreateFileInterface = fileManagerCreateFileInterface;
    }

    public void readFile(String fileName) {
        Log.d(Constants.FILE_MANAGER_TAG, "readFile with name: " + fileName);

        try {
            FileInputStream fileInputStream = mContext.openFileInput(fileName);
            StringBuffer stringBuffer = new StringBuffer();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\r\n");
            }
            fileInputStream.close();
            mFileManagerMainActivityInterface.onFileDataRead(stringBuffer.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.w(Constants.FILE_MANAGER_TAG, "Error in readFile: " + e);
            mFileManagerMainActivityInterface.onError("Could not read password file contents");
        }
    }

    public void createFile(int mode, String fileName, String password) {
        Log.d(Constants.FILE_MANAGER_TAG, "createFile()");

        fileName += ".pass";
        String stuffToWriteToFile = JSONUtils.CreatePasswordFileInJSON(password);

        try {
            FileOutputStream fileOutputStream = mContext.openFileOutput(fileName, mode);
            fileOutputStream.write(stuffToWriteToFile.getBytes());
            fileOutputStream.close();
            mFileManagerCreateFileInterface.onFileCreated(fileName);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.w(Constants.FILE_MANAGER_TAG, "Error in createFile: " + e);
            mFileManagerCreateFileInterface.onError("Could not create the password file");
        }
    }

    public void writeOverPasswordFile(int mode, String fileName, String password, PasswordFile passwordFile) {
        Log.d(Constants.FILE_MANAGER_TAG, "writeOverPasswordFile with mode: " + mode + ", fileName: " + fileName + ", password: " + password);


        PasswordFile givenPasswordFile = SecurityEngine.EncryptPasswordFile(password, passwordFile);

        // Encrypt received passwordFile -> turn it into string -> save it
        String passwordFileInJSONString = JSONUtils.CreatePasswordFileJSONStringFromPasswordFile(givenPasswordFile);
        if(passwordFileInJSONString.equals("")) {
            mFileManagerMainActivityInterface.onError("Could not parse given password file when writing it over!");
            Log.w(Constants.FILE_MANAGER_TAG, "Error in writing over password file - password file conversion to string failed");
            return;
        }

        try {
            FileOutputStream fileOutputStream = mContext.openFileOutput(fileName, mode);
            fileOutputStream.write(passwordFileInJSONString.getBytes());
            fileOutputStream.close();
            mFileManagerMainActivityInterface.onPasswordFileOverwritten(givenPasswordFile);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.w(Constants.FILE_MANAGER_TAG, "Error in writing over password file");
            mFileManagerMainActivityInterface.onError("Could not write over the password file!");
        }
    }
}
