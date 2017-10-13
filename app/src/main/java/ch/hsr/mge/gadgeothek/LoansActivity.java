package ch.hsr.mge.gadgeothek;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import ch.hsr.mge.gadgeothek.domain.Loan;
import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;

public class LoansActivity extends AppCompatActivity {

    private List<Loan> loanList;

    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private RecyclerView.LayoutManager layoutManager;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loans);

        getSupportActionBar().setTitle("Loans");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        textViewEmpty = (TextView) findViewById(R.id.empty_view);

        recyclerView.setHasFixedSize(true);
        layoutManager  = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fetchLoans();

        // show that the list is empty
        if(loanList == null){
            recyclerView.setVisibility(View.GONE);
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            textViewEmpty.setVisibility(View.GONE);
        }

        adapter = new ListAdapter(loanList);
        recyclerView.setAdapter(adapter);
    }

    private void fetchLoans() {
        LibraryService.getLoansForCustomer(new Callback<List<Loan>>() {
            @Override
            public void onCompletion(List<Loan> input) {
                loanList = input;
            }

            @Override
            public void onError(String message) {
                Log.d("Error fetchLoans(): ", message);
            }
        });
    }
}
