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
import com.example.vsapp.entities.Excursion;

import org.jetbrains.annotations.NonNls;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    private List<Excursion> mExcursions;
    private final Context context;
    private final LayoutInflater mInflater;
    class ExcursionViewHolder extends RecyclerView.ViewHolder {
        private final TextView excursionItemView;
        private final TextView excursionItemView2;

        private ExcursionViewHolder(View itemView) {
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.textView2);
            excursionItemView2 = itemView.findViewById(R.id.textView3);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Excursion current = mExcursions.get(position);
                    Intent intent = new Intent(context, ExcursionDetails.class);
                    intent.putExtra("id", current.getExcursionID());
                    intent.putExtra("title", current.getExcursionTitle());
                    intent.putExtra("date", current.getExcursionDate());
                    intent.putExtra("vacID", current.getVacationID());
                    context.startActivity(intent);
                }
            });
        }
    }
    public ExcursionAdapter(Context context) {
        mInflater=LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.excursion_list_item,parent,false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        if(mExcursions!=null) {
            Excursion current=mExcursions.get(position);
            String title=current.getExcursionTitle();
            int vacayID=current.getExcursionID();
            holder.excursionItemView.setText(title);
            holder.excursionItemView2.setText(Integer.toString(vacayID));
        }
        else {
            holder.excursionItemView.setText("No excursion title");
            holder.excursionItemView2.setText("No vacation id");
        }
    }

    public void setExcursions(List<Excursion> excursions) {
        mExcursions=excursions;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        if (mExcursions!=null) return mExcursions.size();
        else return 0;
    }

}
