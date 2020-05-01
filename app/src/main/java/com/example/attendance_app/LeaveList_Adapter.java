package com.example.attendance_app;

        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import java.util.List;

public class LeaveList_Adapter extends RecyclerView.Adapter<LeaveList_Adapter.viewHolder> {

    List<Leave_List> leave_lists;

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView appliedDate, TlSpproval, HrApproval;
        ImageView approvtllogo,approvhrlogo;

        public viewHolder(View view) {
            super(view);

            appliedDate = (TextView) view.findViewById(R.id.valApplieddate);
            HrApproval = (TextView) view.findViewById(R.id.val_hrAppr);
            TlSpproval = (TextView) view.findViewById(R.id.val_Tlapprov);
            approvtllogo = (ImageView) view.findViewById(R.id.logo_Tlapprov);
            approvhrlogo = (ImageView) view.findViewById(R.id.logo_hrAppr);


        }
    }

    public LeaveList_Adapter(List<Leave_List> leave_lists) {
        this.leave_lists = leave_lists;

    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_leave_list, parent, false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Leave_List leave_list = leave_lists.get(position);
        holder.appliedDate.setText(leave_list.getAppliedDate());
//        holder.TlSpproval.setText(leave_list.getTlApproval());
//        holder.HrApproval.setText(leave_list.getHrApproval());
        Log.i("TAG", "onBindViewHolder: "+leave_list.getTlApproval());
        Log.i("TAG", "onBindViewHolder: "+leave_list.getHrApproval());
        if (leave_list.getHrApproval().equals("true")){
            holder.approvhrlogo.setImageResource(R.drawable.applylogo);
        }
        if (leave_list.getTlApproval().equals("true")){

            holder.approvtllogo.setImageResource(R.drawable.applylogo);
        }


    }

    @Override
    public int getItemCount() {
        Log.i("TAG", "getItemCount: "+leave_lists.size());
        return leave_lists.size();
    }


}
