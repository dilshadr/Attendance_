package com.example.attendance_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HolidayList_Adapter extends RecyclerView.Adapter<HolidayList_Adapter.viewHolder> {


    List<Holiday_List> Holiday_Lists;

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView HolidayTitle, HolidateDate;
        ImageView songCoverImage;

        public viewHolder(View view) {
            super(view);

            HolidayTitle = (TextView) view.findViewById(R.id.HolidayTitle);
            HolidateDate = (TextView) view.findViewById(R.id.Holiday_date);
            songCoverImage = (ImageView) view.findViewById(R.id.coverImage);


        }
    }

    public HolidayList_Adapter(List<Holiday_List> Holiday_Lists) {
        this.Holiday_Lists = Holiday_Lists;

    }


    @NonNull
    @Override
    public HolidayList_Adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_holiday_list, parent, false);

        return new HolidayList_Adapter.viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HolidayList_Adapter.viewHolder holder, int position) {

        Holiday_List holiday_list = Holiday_Lists.get(position);
        holder.HolidayTitle.setText(holiday_list.getHolidayTitle());
        holder.HolidateDate.setText(holiday_list.getHolidateDate());
        holder.songCoverImage.setImageBitmap(holiday_list.getCoverImage());
//        holder.songCoverImage.setImageBitmap(.get);
//        Picasso.get().load(Holiday_List.get(position).getCoverImage()).into(holder.songCoverImage);



    }

    @Override
    public int getItemCount() {
        return Holiday_Lists.size();
    }


}

