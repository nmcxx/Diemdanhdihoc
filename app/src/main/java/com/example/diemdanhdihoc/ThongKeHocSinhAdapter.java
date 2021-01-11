package com.example.diemdanhdihoc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ThongKeHocSinhAdapter extends RecyclerView.Adapter<ThongKeHocSinhAdapter.ViewHolder> {

    private Context context;
    private List<ThongKeHocSinh> thongKeHocSinhList;

    public ThongKeHocSinhAdapter(Context context, List<ThongKeHocSinh> thongKeHocSinhList) {
        this.context = context;
        this.thongKeHocSinhList = thongKeHocSinhList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(thongKeHocSinhList != null && thongKeHocSinhList.size()>0)
        {
            ThongKeHocSinh thongKeHocSinh = thongKeHocSinhList.get(position);
            holder.txtMaSV.setText(thongKeHocSinh.getMaSV());
            holder.txtHoTen.setText(thongKeHocSinh.getHoTen());
            holder.txtSoBuoiVang.setText(""+thongKeHocSinh.getSoBuoiVang());
        }
        else
        {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return thongKeHocSinhList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMaSV, txtHoTen, txtSoBuoiVang;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            txtMaSV = itemView.findViewById(R.id.recycler_msv);
            txtHoTen = itemView.findViewById(R.id.recycler_hoten);
            txtSoBuoiVang = itemView.findViewById(R.id.recycler_sobuoivang);
        }
    }
}
