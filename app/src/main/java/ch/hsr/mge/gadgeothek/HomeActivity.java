package ch.hsr.mge.gadgeothek;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button btnLoans;
    private Button btnReserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnLoans = (Button)findViewById(R.id.btnLoan);
        btnReserve = (Button)findViewById(R.id.btnReservation);

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
