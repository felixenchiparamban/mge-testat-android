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
import ch.hsr.mge.gadgeothek.domain.Reservation;
import ch.hsr.mge.gadgeothek.helper.ViewHelper;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder>{

    private List<Reservation> reservationList;

    public ReservationAdapter() {
        this.reservationList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rowlayoutreservation, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(reservationList.isEmpty()){
            holder.emptyView.setVisibility(View.VISIBLE);
        } else {
            final Reservation reservation = reservationList.get(position);

            holder.tvName.setText(reservation.getGadget().getName());
            holder.tvPrimary.setText(String.format("%s - %s", ViewHelper.formatPrice(reservation.getGadget().getPrice()), reservation.getGadget().getCondition()));
            holder.tvSecondary.setText(String.format("Inventory Number: %s by %s", reservation.getGadget().getInventoryNumber(), reservation.getGadget().getManufacturer()));
            holder.tvTimestamp.setText(ViewHelper.formatShortDate(reservation.getReservationDate()));

            holder.ivIconBg.setImageResource(R.drawable.bg_circle);
            holder.ivIconBg.setColorFilter(ViewHelper.getRandomColor());
            holder.ivIconBg.setVisibility(View.VISIBLE);
            holder.tvIconText.setText(String.format("%d", reservation.getWatingPosition()));
            holder.tvIconDesc.setText("Position");

            holder.ivStatus.setBackgroundResource(reservation.isReady() ? R.drawable.ic_ready : R.drawable.ic_waiting);
        }
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public void setReservations(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvName, tvPrimary, tvSecondary, tvTimestamp, tvIconText, tvIconDesc;
        public ImageView ivIconBg, ivStatus;

        public TextView emptyView;

        public ViewHolder(View view){
            super(view);

            this.tvName = itemView.findViewById(R.id.tvGadget);
            this.tvPrimary = itemView.findViewById(R.id.tvPrimary);
            this.tvSecondary = itemView.findViewById(R.id.tvSecondary);
            this.tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            this.tvIconText = itemView.findViewById(R.id.tvIconText);
            this.ivIconBg = itemView.findViewById(R.id.ivIconBg);
            this.tvIconDesc = itemView.findViewById(R.id.tvIconDesc);
            this.ivStatus = itemView.findViewById(R.id.ivStatus);

            this.emptyView = view.findViewById(R.id.empty_view);
        }
    }

    public Reservation getReservation(int position){
        return this.reservationList.get(position);
    }

    public void removeReservation(int position){
        this.reservationList.remove(position);
        this.notifyItemRemoved(position);
    }
}
