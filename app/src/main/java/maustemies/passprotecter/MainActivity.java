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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FileManager.FileManagerMainActivityInterface {

    Button buttonChooseFile;
    Button buttonCreateFile;
    ListView listView;
    TextView textViewInstructions;

    FileManager fileManager;
    static String tempFileName;
    static String tempMainPassword;
    static PasswordFile tempEncryptedPasswordFile;
    static PasswordFile tempDecryptedPasswordFile;
    private static List<PasswordListViewItem> passwordListViewItems;

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
        textViewInstructions = (TextView) findViewById(R.id.textViewInstructions);

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
        switch (id) {
            case R.id.action_settings:
            {
                break;
            }
            case R.id.action_addPassword:
            {
                if(listView.getVisibility() == View.VISIBLE) {
                    Intent intent = new Intent(MainActivity.this, PasswordAddPopUp.class);
                    startActivityForResult(intent, Constants.REQUEST_CODE_PASSWORD_ADD_POPUP);
                }
                else {
                    Toast.makeText(MainActivity.this, "Cannot add password before loading a password file!", Toast.LENGTH_LONG).show();
                }
                break;
            }
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
                    tempFileName = fileName;
                    Log.d(Constants.MAIN_ACTIVITY_TAG, "Selected file: " + fileName);
                    fileManager.readFile(fileName);
                }
                break;
            }
            case Constants.REQUEST_CODE_FILE_CHOOSER:
            {
                if(resultCode == RESULT_OK) {
                    String fileName = data.getStringExtra(Constants.FILENAME_TAG);
                    tempFileName = fileName;
                    Log.d(Constants.MAIN_ACTIVITY_TAG, "Selected file: " + fileName);
                    fileManager.readFile(fileName);
                }
                break;
            }
            case Constants.REQUEST_CODE_PASSWORD_QUERY_POPUP:
            {
                if(resultCode == RESULT_OK) {
                    buttonCreateFile.setVisibility(View.GONE);
                    buttonChooseFile.setVisibility(View.GONE);

                    String password = data.getStringExtra(Constants.MAIN_PASSWORD_TAG);
                    tempMainPassword = password;
                    tempDecryptedPasswordFile = DecryptPasswordFile(password, tempEncryptedPasswordFile);
                    FillListViewFromJSON(tempDecryptedPasswordFile);
                }
                break;
            }
            case Constants.REQUEST_CODE_PASSWORD_ADD_POPUP:
            {
                if(resultCode == RESULT_OK) {
                    String givenPasswordId = data.getStringExtra(Constants.PASSWORD_IDENTIFIER_TAG);
                    String givenPassword = data.getStringExtra(Constants.PASSWORD_TAG);

                    // Add the new password and its identifier to the temporary password file
                    ArrayMap<String, String> passwords = tempDecryptedPasswordFile.Passwords;
                    passwords.put(givenPasswordId, givenPassword);
                    tempDecryptedPasswordFile.Passwords = passwords;

                    // Write over the previous file
                    fileManager.writeOverPasswordFile(MODE_PRIVATE, tempFileName, tempMainPassword, tempDecryptedPasswordFile);
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

        listView.setVisibility(View.VISIBLE);
        textViewInstructions.setVisibility(View.VISIBLE);

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
        startActivityForResult(intent, Constants.REQUEST_CODE_PASSWORD_QUERY_POPUP);
    }

    @Override
    public void onPasswordFileOverwritten(PasswordFile passwordFile) {
        Log.d(Constants.MAIN_ACTIVITY_TAG, "onDataAddedToFile");

        tempEncryptedPasswordFile = passwordFile;
        tempDecryptedPasswordFile = DecryptPasswordFile(tempMainPassword, tempEncryptedPasswordFile);
        FillListViewFromJSON(tempDecryptedPasswordFile);
    }
}
