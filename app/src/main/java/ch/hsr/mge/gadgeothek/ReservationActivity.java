package ch.hsr.mge.gadgeothek;

import android.app.DialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ReservationAdapter reservationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        getSupportActionBar().setTitle("Reservation");

        recyclerView = (RecyclerView) findViewById(R.id.rvReservations);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        reservationAdapter = new ReservationAdapter();
        recyclerView.setAdapter(reservationAdapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        final int position = viewHolder.getAdapterPosition(); //get position which is swipe
                        removeReservation(position);
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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
                if(input == null || input.size() == 0){
                    recyclerView.setVisibility(View.GONE);
                    findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                } else {
                    reservationAdapter.setReservations(input);
                    reservationAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    findViewById(R.id.empty_view).setVisibility(View.GONE);
                }
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
            Snackbar.make(recyclerView,
                    String.format("Reservation failed for %s", gadget.getName()),
                    Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    @Override
    public void onReservationError(String message) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_INDEFINITE).show();
    }

    private void removeReservation(final int position) {
        Reservation toBeDeleted = reservationAdapter.getReservation(position);
        LibraryService.deleteReservation(toBeDeleted, new Callback<Boolean>() {
            @Override
            public void onCompletion(Boolean input) {
                if (input) {
                    reservationAdapter.removeReservation(position);
                } else {
                    reservationAdapter.notifyItemChanged(position);
                }
            }
            @Override
            public void onError(String message) {
                Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
