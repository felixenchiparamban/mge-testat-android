package ch.hsr.mge.gadgeothek.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.mge.gadgeothek.R;
import ch.hsr.mge.gadgeothek.domain.Loan;
import ch.hsr.mge.gadgeothek.helper.ViewHelper;
import ch.hsr.mge.gadgeothek.helper.ViewTimeUnit;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

    private List<Loan> loanList;

    public  ListAdapter(){
        this.loanList = new ArrayList<>();
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rowlayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(loanList.isEmpty()){
            holder.emptyView.setEnabled(true);
        } else {
            final Loan loan = loanList.get(position);
            //holder.emptyView.setEnabled(false);

            holder.tvName.setText(loan.getGadget().getName());
            holder.tvPrimary.setText(loan.getGadget().getManufacturer() + " " + loan.getGadget().getPrice());
            holder.tvSecondary.setText(loan.getGadget().getInventoryNumber());
            holder.tvTimestamp.setText(ViewHelper.formatShortDate(loan.getPickupDate()));

            // remaining time to due date
            long remainingDays = ViewHelper.getRemainingDays(loan.overDueDate());
            ViewTimeUnit unit = ViewHelper.getAppropriateTimeUnit(remainingDays);

            holder.ivIconBg.setImageResource(R.drawable.bg_circle);
            holder.ivIconBg.setColorFilter(ViewHelper.getRandomColor());
            holder.ivIconBg.setVisibility(View.VISIBLE);
            holder.tvIconText.setText(String.format("%d", Math.abs(ViewHelper.getApproximate(remainingDays, unit))));
            holder.tvIconDesc.setText(String.format("%s \n%s", unit.toString().toLowerCase(), remainingDays > 0 ? "left" : "overdue"));

            holder.tvCondition.setText(loan.getGadget().getCondition().toString());
        }
    }

    @Override
    public int getItemCount() {
        return loanList.size();
    }

    public void setLoanList(List<Loan> loanList){
        this.loanList = loanList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvName, tvPrimary, tvSecondary, tvTimestamp, tvIconText, tvCondition, tvIconDesc;
        public ImageView ivIconBg;

        public TextView emptyView;

        public ViewHolder(View view) {
            super(view);
            this.tvName = itemView.findViewById(R.id.tvGadget);
            this.tvPrimary = itemView.findViewById(R.id.tvPrimary);
            this.tvSecondary = itemView.findViewById(R.id.tvSecondary);
            this.tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            this.tvIconText = itemView.findViewById(R.id.tvIconText);
            this.ivIconBg = itemView.findViewById(R.id.ivIconBg);
            this.tvCondition = itemView.findViewById(R.id.tvCondition);
            this.tvIconDesc = itemView.findViewById(R.id.tvIconDesc);

            this.emptyView = view.findViewById(R.id.empty_view);
        }
    }
}
