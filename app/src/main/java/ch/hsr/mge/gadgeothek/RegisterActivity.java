package ch.hsr.mge.gadgeothek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;
import ch.hsr.mge.gadgeothek.util.ValidationUtil;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etName;
    private EditText etStudentNumber;

    private Button btnSubmitRegistration;

    private final String token = "token";
    private final String customer = "customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = (EditText)findViewById(R.id.newEmail);
        etPassword = (EditText)findViewById(R.id.newPassword);
        etName = (EditText)findViewById(R.id.newUsername);
        etStudentNumber = (EditText)findViewById(R.id.studentNumber);
        btnSubmitRegistration = (Button)findViewById(R.id.newRegistration);

        btnSubmitRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateUserInputs()) {
                    processRegistration();
                }
            }
        } );
    }

    private boolean validateUserInputs() {
        boolean isValid = true;

        if (!ValidationUtil.isEmailValid(etEmail.getText().toString())) {
            etEmail.setError(getString(R.string.error_field_required));
            isValid = false;
        }

        if (!ValidationUtil.isPasswordValid(etPassword.getText().toString())) {
            etPassword.setError(getString(R.string.error_field_required));
            isValid = false;
        }

        if (TextUtils.isEmpty(etName.getText().toString())) {
            etName.setError(getString(R.string.error_field_required));
            isValid = false;
        }

        if (TextUtils.isEmpty(etStudentNumber.getText().toString())) {
            etStudentNumber.setError(getString(R.string.error_field_required));
            isValid = false;
        }
        return isValid;
    }

    private void processRegistration() {
        // disable the button until the registration is over
        btnSubmitRegistration.setEnabled(false);

        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        LibraryService.register(
                email, password,
                etName.getText().toString(),
                etStudentNumber.getText().toString(),
                new Callback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean input) {
                        if (input) {
                            proceedAutoLogin(email, password);
                        } else {
                            btnSubmitRegistration.setEnabled(true);
                            Snackbar.make(findViewById(R.id.layoutRegister), "Registration failed", Snackbar.LENGTH_INDEFINITE).show();
                        }
                    }
                    @Override
                    public void onError(String message) {
                        btnSubmitRegistration.setEnabled(true);
                        Snackbar.make(findViewById(R.id.layoutRegister), message, Snackbar.LENGTH_INDEFINITE).show();
                    }
                }
        );
    }

    private void proceedAutoLogin(String email, String password) {
        LibraryService.login(email, password, new Callback<Boolean>() {
            @Override
            public void onCompletion(Boolean input) {
                if (input) {

                    SharedPreferences sharedPreferencesLogin = getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferencesLogin.edit();
                    editor.putString(token, LibraryService.getLoginToken().getSecurityToken());
                    editor.putString(customer, LibraryService.getLoginToken().getCustomerId());
                    editor.commit();

                    Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
                    /*
                    alte Activity von Task löschen, damit die neue Task als
                    letztes von der Stack weggenommen wird.
                     */
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Auto Login failed", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
