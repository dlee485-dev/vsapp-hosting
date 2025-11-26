package com.example.vsapp.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vsapp.R;

import java.util.ArrayList;
import java.util.List;

public class VacationReportAdapter extends RecyclerView.Adapter<VacationReportAdapter.ReportViewHolder> {

    public static class VacationReportRow {
        public String title;
        public String hotel;
        public String startDate;
        public String endDate;
        public int excursionCount;

        public VacationReportRow(String title, String hotel, String startDate, String endDate, int excursionCount) {
            this.title = title;
            this.hotel = hotel;
            this.startDate = startDate;
            this.endDate = endDate;
            this.excursionCount = excursionCount;
        }
    }

    private List<VacationReportRow> mRows = new ArrayList<>();

    public void setRows(List<VacationReportRow> rows) {
        mRows = rows;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vacation_report_item, parent, false);
        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        VacationReportRow row = mRows.get(position);
        holder.titleText.setText(row.title);
        holder.hotelText.setText(row.hotel);
        holder.datesText.setText(row.startDate + " - " + row.endDate);
        holder.excursionsText.setText("Excursions: " + row.excursionCount);
    }

    @Override
    public int getItemCount() {
        return mRows.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {

        TextView titleText;
        TextView hotelText;
        TextView datesText;
        TextView excursionsText;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.reportTitleText);
            hotelText = itemView.findViewById(R.id.reportHotelText);
            datesText = itemView.findViewById(R.id.reportDatesText);
            excursionsText = itemView.findViewById(R.id.reportExcursionsText);
        }
    }
}
