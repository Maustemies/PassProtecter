package maustemies.passprotecter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FileManager.FileManagerMainActivityInterface {

    Button buttonChooseFile;
    Button buttonCreateFile;
    ListView listView;

    FileManager fileManager;
    PasswordFile tempEncryptedPasswordFile;
    PasswordFile tempDecryptedPasswordFile;
    private List<PasswordListViewItem> passwordListViewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fileManager = new FileManager(getApplicationContext(), this);
        InitViews();
        passwordListViewItems = new ArrayList<>();
    }

    private void InitViews() {
        listView = (ListView) findViewById(R.id.listView);

        buttonChooseFile = (Button) findViewById(R.id.buttonChooseFile);
        buttonChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OpenFileActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_FILE_CHOOSER);
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
        Log.d(Constants.MAIN_ACTIVITY_TAG, "onActivityResult with requestCode: " + requestCode);
        switch (requestCode) {
            case Constants.REQUEST_CODE_FILE_CREATOR:
            {
                if(resultCode == RESULT_OK) {
                    String fileName = data.getStringExtra(Constants.FILENAME_TAG);
                    Log.d(Constants.MAIN_ACTIVITY_TAG, "Selected file: " + fileName);
                    Toast.makeText(MainActivity.this, "Selected file: " + fileName, Toast.LENGTH_LONG).show();
                    fileManager.readFile(fileName);
                }
                break;
            }
            case Constants.REQUEST_CODE_FILE_CHOOSER:
            {
                if(resultCode == RESULT_OK) {
                    String fileName = data.getStringExtra(Constants.FILENAME_TAG);
                    Log.d(Constants.MAIN_ACTIVITY_TAG, "Selected file: " + fileName);
                    Toast.makeText(MainActivity.this, "Selected file: " + fileName, Toast.LENGTH_LONG).show();
                    fileManager.readFile(fileName);
                }
                break;
            }
            case Constants.REQUEST_CODE_PASSWORD_POPUP:
            {
                Log.d(Constants.MAIN_ACTIVITY_TAG, "Result from password popup: " + resultCode);
                if(resultCode == RESULT_OK) {
                    buttonCreateFile.setVisibility(View.GONE);
                    buttonChooseFile.setVisibility(View.GONE);

                    String password = data.getStringExtra(Constants.MAIN_PASSWORD_TAG);
                    tempDecryptedPasswordFile = DecryptPasswordFile(password, tempEncryptedPasswordFile);
                    FillListViewFromJSON(tempDecryptedPasswordFile);
                }
                break;
            }
        }
    }

    private PasswordFile DecryptPasswordFile(String password, PasswordFile passwordFile) {
        return SecurityEngine.DecryptPasswordFile(password, passwordFile);
    }

    private void FillListViewFromJSON(PasswordFile passwordFile) {
        Log.d(Constants.MAIN_ACTIVITY_TAG, "FillListViewWithPasswordFile");

        if(listView == null) return;
        listView.setVisibility(View.VISIBLE);

        ArrayMap<String, String> passwords = passwordFile.Passwords;
        passwordListViewItems = new ArrayList<>();
        for(int i = 0; i < passwords.size(); i++) {
            PasswordListViewItem passwordListViewItem = new PasswordListViewItem();
            passwordListViewItem.Key = passwords.keyAt(i);
            passwordListViewItem.Value = passwords.valueAt(i);
            passwordListViewItems.add(passwordListViewItem);
        }

        PasswordListViewAdapter passwordListViewAdapter = new PasswordListViewAdapter(this, passwordListViewItems);
        listView.setAdapter(passwordListViewAdapter);
        passwordListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String error) {
        Toast.makeText(MainActivity.this, "Error" + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFileDataRead(String data) {
        Log.d(Constants.MAIN_ACTIVITY_TAG, "onFileDataRead with data: " + data);

        PasswordFile passwordFile = JSONUtils.GetPasswordFileFromString(data);
        tempEncryptedPasswordFile = passwordFile;

        Intent intent = new Intent(MainActivity.this, PasswordQueryPopUp.class);
        intent.putExtra(Constants.MAIN_PASSWORD_TAG, tempEncryptedPasswordFile.MainPassword);
        startActivityForResult(intent, Constants.REQUEST_CODE_PASSWORD_POPUP);
    }
}
