package ch.hsr.mge.gadgeothek;

import android.app.DialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ch.hsr.mge.gadgeothek.adapter.ReservationAdapter;
import ch.hsr.mge.gadgeothek.domain.Gadget;
import ch.hsr.mge.gadgeothek.domain.Reservation;
import ch.hsr.mge.gadgeothek.fragments.NewReservationFragment;
import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;

public class ReservationActivity extends AppCompatActivity implements NewReservationFragment.NewReservationDialogListener{

    private RecyclerView rvReservations;
    private RecyclerView.LayoutManager layoutManager;
    private ReservationAdapter reservationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        getSupportActionBar().setTitle("Reservation");

        rvReservations = (RecyclerView) findViewById(R.id.rvReservations);
        rvReservations.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvReservations.setLayoutManager(layoutManager);

        reservationAdapter = new ReservationAdapter();
        rvReservations.setAdapter(reservationAdapter);

        FloatingActionButton fabNewReservation = (FloatingActionButton) findViewById(R.id.fabNewReservation);
        fabNewReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewReservationDialog();
            }
        });

        fetchReservation();
    }

    private void fetchReservation() {
        LibraryService.getReservationsForCustomer(new Callback<List<Reservation>>() {
            @Override
            public void onCompletion(List<Reservation> input) {
                reservationAdapter.setReservations(input);
                reservationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Log.d("Error fetchReservation:", message);
            }
        });
    }

    private void openNewReservationDialog() {
        DialogFragment dialog = NewReservationFragment.newInstance();
        dialog.show(getFragmentManager(), "NewReservationFragment");
    }

    @Override
    public void onReservationCompletion(Gadget gadget, boolean success) {
        if (success) {
            Toast.makeText(this, String.format("%s is reserved successfully", gadget.getName()), Toast.LENGTH_LONG).show();
            fetchReservation();
        } else {
            Snackbar.make(rvReservations,
                    String.format("Reservation failed for %s", gadget.getName()),
                    Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    @Override
    public void onReservationError(String message) {
        Snackbar.make(rvReservations, message, Snackbar.LENGTH_INDEFINITE).show();
    }
}
