package maustemies.passprotecter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements FileManager.FileManagerMainActivityInterface {

    Button buttonChooseFile;
    Button buttonCreateFile;

    FileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fileManager = new FileManager(getApplicationContext(), this);

        buttonChooseFile = (Button) findViewById(R.id.buttonChooseFile);
        buttonChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openFile("text/html");
                openFolder();
            }
        });

        buttonCreateFile = (Button) findViewById(R.id.buttonCreateFile);
        buttonCreateFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateFileActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_FILE_CREATOR);
            }
        });
    }

    public void openFolder()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(String.valueOf(getApplicationContext().getFilesDir()));
        intent.setDataAndType(uri, "*/*");
        startActivity(Intent.createChooser(intent, "Open folder"));
    }

    /**
     * http://stackoverflow.com/questions/8945531/pick-any-kind-file-via-an-intent-on-android
     * @param minmeType
     */
    private void openFile(String minmeType) {
        Log.d(Constants.MAIN_ACTIVITY_TAG, "openFile() with type: " + minmeType);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(minmeType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // special intent for Samsung file manager
        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        // if you want any file type, you can skip next line
        sIntent.putExtra("CONTENT_TYPE", minmeType);
        sIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Intent chooserIntent;
        if (getPackageManager().resolveActivity(sIntent, 0) != null){
            // it is device with samsung file manager
            chooserIntent = Intent.createChooser(sIntent, "Open file");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent});
        }
        else {
            chooserIntent = Intent.createChooser(intent, "Open file");
        }

        try {
            startActivityForResult(chooserIntent, Constants.REQUEST_CODE_FILE_CHOOSER);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // TODO: Start the settings activity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_FILE_CREATOR:
            {
                if(resultCode == RESULT_OK) {
                    String filePath = data.getStringExtra(CreateFileActivity.FILEPATH_TAG);
                    Log.d(Constants.MAIN_ACTIVITY_TAG, "Selected file in: " + filePath);
                    Toast.makeText(MainActivity.this, "Selected file in: " + filePath, Toast.LENGTH_LONG).show();
                    fileManager.readFile(filePath);
                }
            }
            case Constants.REQUEST_CODE_FILE_CHOOSER:
            {
                if(resultCode == RESULT_OK) {
                    String filePath = data.getDataString();
                    Log.d(Constants.MAIN_ACTIVITY_TAG, "Selected file in: " + filePath);
                    Toast.makeText(MainActivity.this, "Selected file in: " + filePath, Toast.LENGTH_LONG).show();
                    fileManager.readFile(filePath);
                }
            }
        }
    }

    @Override
    public void onError(String error) {
        Toast.makeText(MainActivity.this, "Error" + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFileDataRead(String data) {
        Log.d(Constants.MAIN_ACTIVITY_TAG, "onFileDataRead with data: " + data);
        PasswordFile passwordFile = JSONUtils.GetPasswordFileFromString(data);
        Log.d(Constants.MAIN_ACTIVITY_TAG, "onFileDataRead password file: " + passwordFile);
    }
}
