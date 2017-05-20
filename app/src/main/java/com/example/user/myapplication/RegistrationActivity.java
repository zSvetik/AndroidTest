package com.example.user.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.myapplication.utils.HashGeneratorUtils;
import com.example.user.myapplication.utils.ValidationFields;

public class RegistrationActivity extends Activity {
    // Email, password edittext
    EditText txtUserName, txtPassword, txtConfirmPassword;

    // login button
    Button btnRegistration;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    SessionManager session;

    // Repository with users email && password
    // RepoSingleton repoSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        // repoSingleton = repoSingleton.getInstance();

        // Email, Password input text
        txtUserName = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);

        // Login button
        btnRegistration = (Button) findViewById(R.id.btnRegistration);


        // Login button click event
        btnRegistration.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get username, password from EditText
                String username = txtUserName.getText().toString();
                String password = txtPassword.getText().toString();
                String confirmPassword = txtConfirmPassword.getText().toString();

                // Check if username, password is filled
                if (username.trim().length() > 0) {
                    if (ValidationFields.isValidEmail(username)) {
                        if (password.trim().length() > 0 && confirmPassword.trim().length() > 0) {
                            if (!isContainUser(username)) {
                                if (password.equals(confirmPassword)) {
                                    if (ValidationFields.isValidPassword(password)) {
                                        addUser(username, password);

                                        // Staring LoginActivity
                                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(i);
                                        finish();

                                    } else {
                                        alert.showAlertDialog(RegistrationActivity.this, "Password failed..", "Password incorrect (min length = 4)", false);
                                    }
                                } else {
                                    alert.showAlertDialog(RegistrationActivity.this, "Password failed..", "Password incorrect", false);
                                }
                            } else {
                                alert.showAlertDialog(RegistrationActivity.this, "Email failed..", "Email has already registered", false);
                            }
                        } else {
                            alert.showAlertDialog(RegistrationActivity.this, "Password failed..", "Please enter password", false);
                        }
                    } else {
                        alert.showAlertDialog(RegistrationActivity.this, "Email failed..", "Email incorrect (example: foo@google.com, min length = 6)", false);
                    }
                } else {
                    alert.showAlertDialog(RegistrationActivity.this, "Email failed..", "Please enter email", false);
                }

            }
        });
    }

    private boolean isContainUser(String email) {
        SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, new String[] {"EMAIL"}, "EMAIL = ?", new String[] {email}, null, null, null);
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }

    private void addUser(String email, String password) {
        SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues user = new ContentValues();
        user.put("EMAIL", email);
        user.put("PASSWORD", HashGeneratorUtils.generateSHA256(password));
        db.insert(DatabaseHelper.TABLE_NAME, null, user);
        db.close();
    }
}