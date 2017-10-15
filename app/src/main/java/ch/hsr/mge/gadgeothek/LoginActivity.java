package ch.hsr.mge.gadgeothek;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;
import ch.hsr.mge.gadgeothek.util.ValidationUtil;

public class LoginActivity extends AppCompatActivity {

    private boolean isLoginOnProgress = false;

    private EditText etSignIn;
    private EditText etPassword;
    private Button btnSignIn;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LibraryService.setServerAddress("http://mge5.dev.ifs.hsr.ch/public");
        setContentView(R.layout.activity_login);

        etSignIn = (EditText)findViewById(R.id.email);
        etPassword = (EditText)findViewById(R.id.password);

        btnSignIn = (Button)findViewById(R.id.login);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        btnRegister = (Button)findViewById(R.id.register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    private void attemptLogin() {
        // only one login task should run at the same time
        if (isLoginOnProgress) {
            return;
        }

        etSignIn.setError(null);
        etPassword.setError(null);

        boolean cancel = false;
        View focusView = null;

        String email = etSignIn.getText().toString();
        String password = etPassword.getText().toString();

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getString(R.string.error_field_required));
            focusView = etPassword;
            cancel = true;
        } else if (!ValidationUtil.isPasswordValid(password)) {
            etPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            etSignIn.setError(getString(R.string.error_field_required));
            focusView = etSignIn;
            cancel = true;
        } else if (!ValidationUtil.isEmailValid(email)) {
            etSignIn.setError(getString(R.string.error_invalid_email));
            focusView = etSignIn;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // disable the sign in button and
            // perform the user login attempt.
            btnSignIn.setEnabled(false);
            LibraryService.login(email, password, new Callback<Boolean>() {
                @Override
                public void onCompletion(Boolean input) {
                    handleLoginCompletion(input);
                }

                @Override
                public void onError(String message) {
                    handleLoginError(message);
                }
            });
        }

    }

    private void handleLoginCompletion(Boolean success){
        isLoginOnProgress = false;
        btnSignIn.setEnabled(true);

        if (success) {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        } else {
            etPassword.setError(getString(R.string.error_incorrect_password));
            etPassword.requestFocus();
        }
    }

    private void handleLoginError(String message){
        isLoginOnProgress = false;
        btnSignIn.setEnabled(true);

        Snackbar.make(findViewById(R.id.loginLayout), message, Snackbar.LENGTH_INDEFINITE).show();
    }


}
