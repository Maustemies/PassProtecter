package maustemies.passprotecter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateFileActivity extends AppCompatActivity implements FileManager.FileManagerCreateFileInterface {

    EditText editTextFileName;
    EditText editTextMainPassword;
    EditText editTextMainPasswordConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_file);

        editTextFileName = (EditText) findViewById(R.id.editTextFileName);
        editTextMainPassword = (EditText) findViewById(R.id.editTextMainPassword);
        editTextMainPasswordConfirmation = (EditText) findViewById(R.id.editTextMainPasswordConfirmation);

        Button buttonCreatePasswordFile = (Button) findViewById(R.id.buttonCreatePasswordFile);
        buttonCreatePasswordFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String givenMainPassword = editTextMainPassword.getText().toString();
                String givenMainPasswordConfirmation = editTextMainPasswordConfirmation.getText().toString();
                if(!givenMainPassword.equals("") && !givenMainPasswordConfirmation.equals("")) {
                    if(givenMainPassword.equals(givenMainPasswordConfirmation)) {
                        FileManager fileManager = new FileManager(getApplicationContext(), CreateFileActivity.this);
                        String ecryptedMainPassword = SecurityEngine.EncryptString(givenMainPassword, givenMainPassword);
                        fileManager.createFile(MODE_PRIVATE, editTextFileName.getText().toString(), ecryptedMainPassword);
                    }
                    else {
                        Toast.makeText(CreateFileActivity.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(CreateFileActivity.this, "Password fields cannot be empty!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onError(String error) {
        Toast.makeText(CreateFileActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFileCreated(String fileName) {
        Log.d(Constants.CREATE_FILE_ACTIVITY_TAG, "onFileCreated with fileName: "+ fileName);

        Intent intent = new Intent();
        intent.putExtra(Constants.FILENAME_TAG, fileName);
        setResult(RESULT_OK, intent);
        finish();
    }
}
