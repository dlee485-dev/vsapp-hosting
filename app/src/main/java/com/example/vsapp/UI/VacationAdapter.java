package com.example.vsapp.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vsapp.R;
import com.example.vsapp.entities.Vacation;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private List<Vacation> mVacations;
    private final Context context;
    private final LayoutInflater mInflater;

    public VacationAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class VacationViewHolder extends RecyclerView.ViewHolder {

        private final TextView vacationTitle;
        private final TextView vacationHotel;
        private final TextView vacationDates;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);

            vacationTitle = itemView.findViewById(R.id.vacationTitle);
            vacationHotel = itemView.findViewById(R.id.vacationHotel);
            vacationDates = itemView.findViewById(R.id.vacationDates);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                final Vacation current = mVacations.get(position);

                Intent intent = new Intent(context, VacationDetails.class);
                intent.putExtra("id", current.getVacationID());
                intent.putExtra("title", current.getVacationTitle());
                intent.putExtra("hotelname", current.getHotelName());
                intent.putExtra("startdate", current.getStartDate());
                intent.putExtra("enddate", current.getEndDate());
                intent.putExtra("categoryID", current.getCategoryID());

                context.startActivity(intent);
            });
        }
    }

    @NonNull
    @Override
    public VacationAdapter.VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.vacation_list_item, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationAdapter.VacationViewHolder holder, int position) {
        if (mVacations != null) {
            Vacation current = mVacations.get(position);

            holder.vacationTitle.setText(current.getVacationTitle());
            holder.vacationHotel.setText("Hotel: " + current.getHotelName());
            holder.vacationDates.setText("From " + current.getStartDate() + " to " + current.getEndDate());
        }
    }

    @Override
    public int getItemCount() {
        if (mVacations != null)
            return mVacations.size();
        else
            return 0;
    }

    public void setVacations(List<Vacation> vacations) {
        mVacations = vacations;
        notifyDataSetChanged();
    }
}
