package maustemies.passprotecter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Maustemies on 25.11.2016.
 */

public class PasswordQueryPopUp extends Activity {

    String passwordToTestAgainst;

    EditText editTextMainPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_query_pop_up);

        PopUpifyView();

        editTextMainPassword = (EditText) findViewById(R.id.editTextMainPassword);

        passwordToTestAgainst = getIntent().getStringExtra(Constants.MAIN_PASSWORD_TAG);
        Log.d(Constants.PASSWORD_POPUP_TAG, "onCreate with passwordToTestAgainst: " + passwordToTestAgainst);

        Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        Button buttonOk = (Button) findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String givenPassword = editTextMainPassword.getText().toString();
                String decryptedMainPassword = SecurityEngine.DecryptString(givenPassword, passwordToTestAgainst);
                if(givenPassword.equals(decryptedMainPassword)) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.MAIN_PASSWORD_TAG, givenPassword);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    Toast.makeText(PasswordQueryPopUp.this, "Wrong password given", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void PopUpifyView() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.8), (int)(height*0.5));
    }
}
