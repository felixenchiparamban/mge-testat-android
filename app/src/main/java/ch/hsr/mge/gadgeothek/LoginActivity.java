package ch.hsr.mge.gadgeothek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;
import ch.hsr.mge.gadgeothek.service.LoginToken;
import ch.hsr.mge.gadgeothek.util.LibraryServiceUtil;
import ch.hsr.mge.gadgeothek.util.ValidationUtil;

public class LoginActivity extends AppCompatActivity {

    private static final String DEFAULT_SERVER = "DEFAULT_SERVER";

    private boolean isLoginOnProgress = false;

    private EditText etSignIn;
    private EditText etPassword;
    private Button btnSignIn;
    private Button btnRegister;

    private final String token = "token";
    private final String customer = "customer";

    private Spinner spinnerServerUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LibraryService.setServerAddress("http://mge5.dev.ifs.hsr.ch/public");
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

        spinnerServerUrls = (Spinner) findViewById(R.id.spServerUrls);
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, LibraryServiceUtil.generateServerUrls());
        spinnerServerUrls.setAdapter(spinnerAdapter);
        setServerAddress();

        spinnerServerUrls.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                storeSelectedServer(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SharedPreferences sharedPreferencesLogin = getSharedPreferences("Login", MODE_PRIVATE);

        if(sharedPreferencesLogin.getString(token, null) != null){
            String toke = sharedPreferencesLogin.getString(token, null);
            String customerID = sharedPreferencesLogin.getString(customer, null);
            LibraryService.setLoginToken(new LoginToken(toke, customerID));
            this.handleLoginCompletion(true);
        }
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
            SharedPreferences sharedPreferencesLogin = getSharedPreferences("Login", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferencesLogin.edit();
            editor.putString(token, LibraryService.getLoginToken().getSecurityToken());
            editor.putString(customer, LibraryService.getLoginToken().getCustomerId());
            editor.commit();

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

    private void setServerAddress() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        int prefServer = prefs.getInt(DEFAULT_SERVER, LibraryServiceUtil.getDefaultServerValue());
        LibraryService.setServerAddress(LibraryServiceUtil.getServerAddress(prefServer));
        spinnerServerUrls.setSelection(prefServer - 1);
    }

    private void storeSelectedServer(int selectedServer) {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(DEFAULT_SERVER, selectedServer);
        editor.commit();

        LibraryService.setServerAddress(LibraryServiceUtil.getServerAddress(selectedServer));
    }
}
