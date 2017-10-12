package ch.hsr.mge.gadgeothek;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class LoansActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loans);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        layoutManager  = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        
    }
}
