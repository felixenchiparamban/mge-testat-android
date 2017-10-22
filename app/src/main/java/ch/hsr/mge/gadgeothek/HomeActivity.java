package ch.hsr.mge.gadgeothek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;

public class HomeActivity extends AppCompatActivity {

    private View btnLoans;
    private View btnReserve;

    private final String token = "token";
    private final String customer = "customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnLoans = findViewById(R.id.loansCard);
        btnReserve = findViewById(R.id.reservationsCard);

        btnLoans.setOnClickListener(new View.OnClickListener(){
             @Override
            public void onClick(View view) {
                 Intent i = new Intent(HomeActivity.this, LoansActivity.class);
                 startActivity(i);
            }
        });

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ReservationActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_logout) {
            LibraryService.logout(new Callback<Boolean>() {
                @Override
                public void onCompletion(Boolean input) {
                    SharedPreferences sharedPreferencesLogin = getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferencesLogin.edit();
                    editor.putString(token, null);
                    editor.putString(customer, null);
                    editor.commit();

                    Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onError(String message) {
                    Log.d("error logout:", message);
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
