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
    }

    public interface FileManagerCreateFileInterface {
        public void onError(String error);
        public void onFileCreated(String filePath);
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

    public void readFile(String filePath) {
        Log.d(Constants.FILE_MANAGER_TAG, "readFile with path: " + filePath);

        try {
            FileInputStream fileInputStream = mContext.openFileInput(filePath);
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

    public void createFile(int mode, String password) {
        Log.d(Constants.FILE_MANAGER_TAG, "createFile()");

        String FILENAME = "passwords.pass";
        String stuffToWriteToFile = JSONUtils.CreatePasswordFileInJSON(password);

        try {
            FileOutputStream fileOutputStream = mContext.openFileOutput(FILENAME, mode);
            fileOutputStream.write(stuffToWriteToFile.getBytes());
            fileOutputStream.close();
            String filePath = mContext.getFilesDir() + "/" + FILENAME;
            mFileManagerCreateFileInterface.onFileCreated(FILENAME);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.w(Constants.FILE_MANAGER_TAG, "Error in createFile: " + e);
            mFileManagerCreateFileInterface.onError("Could not create the password file");
        }
    }
}
