package ch.hsr.mge.gadgeothek;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.mge.gadgeothek.domain.Gadget;
import ch.hsr.mge.gadgeothek.domain.Loan;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;

        public ViewHolder(View v, TextView view) {
            super(v);
            textView = view;
        }
    }

    private List<Loan> loanList;

    public ListAdapter(List<Loan> loanList){
        if(loanList == null){
            this.loanList = new ArrayList<>();
        } else {
            this.loanList = loanList;
        }
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.rowlayout, parent, false);
        CardView cardView = v.findViewById(R.id.card_view);
        TextView textView = (TextView)cardView.findViewById(R.id.textView);

        ViewHolder viewHolder = new ViewHolder(v, textView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Loan loan = loanList.get(position);
        holder.textView.setText(loan.getGadget().getName());
    }

    @Override
    public int getItemCount() {
        return loanList.size();
    }
}
