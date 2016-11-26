package maustemies.passprotecter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Maustemies on 26.11.2016.
 * R.I.P. Fidel Castro. Y en eso llegó Fidel...
 */

public class PasswordAddPopUp extends Activity {

    EditText editTextPassword;
    EditText editTextPasswordConfirmation;
    EditText editTextIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_add_pop_up);

        PopUpifyView();

        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPasswordConfirmation = (EditText) findViewById(R.id.editTextPasswordConfirmation);
        editTextIdentifier = (EditText) findViewById(R.id.editTextIdentifier);

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
                String givenPassword = editTextPassword.getText().toString();
                String givenPasswordConfirmation = editTextPasswordConfirmation.getText().toString();
                String givenIdentifier = editTextIdentifier.getText().toString();

                if(!givenPassword.equals("") && !givenPasswordConfirmation.equals("") && !givenIdentifier.equals("")) {
                    if (givenPassword.equals(givenPasswordConfirmation)) {
                        Intent intent = new Intent();
                        intent.putExtra(Constants.PASSWORD_TAG, givenPassword);
                        intent.putExtra(Constants.PASSWORD_IDENTIFIER_TAG, givenIdentifier);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(PasswordAddPopUp.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(PasswordAddPopUp.this, "Service name and password fields cannot be empty!", Toast.LENGTH_LONG).show();
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
