package ch.hsr.mge.gadgeothek;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private View btnLoans;
    private View btnReserve;

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

}
