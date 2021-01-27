package com.example.diemdanhdihoc;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SinhVienAdapter extends BaseAdapter implements Filterable {

    private TeacherClassroomActivity context;
    private int layout;
//    private List<SinhVien> sinhVienList;

    private ArrayList<SinhVien> svList;
    private ArrayList<SinhVien> sinhVienList;

    public SinhVienAdapter(TeacherClassroomActivity context, int layout, ArrayList<SinhVien> sinhVienList) {
        this.context = context;
        this.layout = layout;
        this.sinhVienList = sinhVienList;
        svList = sinhVienList;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        int count;
        if(sinhVienList.size()>0){
            count=getCount();
        }
        else
        {
            count=1;
        }
        return count;
    }

    @Override
    public int getCount() {
        return sinhVienList.size();
    }

    @Override
    public Object getItem(int position) {
        return sinhVienList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public Filter getFilter() {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                FilterResults results = new FilterResults();

                if(sinhVienList==null)
                {
                    sinhVienList=new ArrayList(svList);
                }
                //If there's nothing to filter on, return the original data for your list
                if(charSequence == null || charSequence.length() == 0)
                {
                    results.values = svList;
                    results.count = svList.size();
                }
                else
                {
                    ArrayList<SinhVien> filtList = new ArrayList<>();
                    charSequence = charSequence.toString().toLowerCase();
                    for(int i=0; i < svList.size() ; i++) {
                        SinhVien data = (SinhVien) svList.get(i);
                        String da = data.getMaSinhVien() ;
                        if (da.toLowerCase().contains(charSequence)) {
                            filtList.add(svList.get(i));
                        }
                    }

                    results.count = filtList.size();
                    results.values = filtList;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                sinhVienList = (ArrayList) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }


    public ArrayList<SinhVien> getSinhVienList(){
        ArrayList<SinhVien> sinhViens = new ArrayList<SinhVien>();
        for(SinhVien sv : sinhVienList)
        {
            if (sv.isCheck()==true)
                sinhViens.add(sv);
        }
        return sinhViens;
    }

    private class ViewHolder{
        TextView txtSinhVien;
        ToggleButton toggleButtonCheck;
        Button btnXoaSV;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtSinhVien = (TextView) convertView.findViewById(R.id.dssv_custom_msv);
            holder.toggleButtonCheck = (ToggleButton) convertView.findViewById(R.id.dssv_custom_tgbtnTrangThai);
            holder.btnXoaSV = (Button) convertView.findViewById(R.id.btnXoaSV);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.toggleButtonCheck.setChecked(false);
        final SinhVien sinhVien = sinhVienList.get(position);

        holder.txtSinhVien.setText(sinhVien.toString());


        holder.toggleButtonCheck.setChecked(sinhVien.isCheck());

        holder.toggleButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.toggleButtonCheck.isChecked())
                {
                    Toast.makeText(context, ""+sinhVien.getMaSinhVien()+", "+holder.toggleButtonCheck.isChecked(), Toast.LENGTH_SHORT).show();
                    sinhVien.setCheck(true);
                    holder.toggleButtonCheck.setChecked(true);
                    for(SinhVien sv : sinhVienList)
                        Log.d("sinh vien list",""+sv.test());
                }
                else
                {
                    Toast.makeText(context, ""+sinhVien.getMaSinhVien()+", "+!holder.toggleButtonCheck.isChecked(), Toast.LENGTH_SHORT).show();
                    sinhVien.setCheck(false);
                    holder.toggleButtonCheck.setChecked(false);
                }
            }
        });

        holder.btnXoaSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có muốn xóa sinh viên này không?")
                        .setCancelable(false)
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sinhVienList.remove(sinhVien);
                                context.soLuong-=1;
//                                int soLuongNew = Integer.parseInt(context.txtSoLuong.getText().toString())-1;
//                                int soLuongNew = Integer.parseInt(context.txtSoLuong.getText().toString())-1;
                                context.txtSoLuong.setText(""+context.soLuong);
                                notifyDataSetChanged();
//                                XoaLop(urlxoalop,lopHocArrayList.get(vitri).getId());
                                context.XoaSinhVien(sinhVien.getMaSinhVien());
//                                        Toast.makeText(context,"Xóa sinh viên thành công",Toast.LENGTH_SHORT).show();
//                                loadingDialog.startLoadingDialog();
                            }
                        })
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                        Toast.makeText(context,"ok",Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Hệ Thống");
                alertDialog.show();
            }
        });
//        holder.toggleButtonCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked)
//                {
//                    Toast.makeText(context, ""+sinhVien.getMaSinhVien()+", "+isChecked, Toast.LENGTH_SHORT).show();
//                    sinhVien.setCheck(true);
//                    holder.toggleButtonCheck.setChecked(true);
//                }
//                else
//                {
//                    Toast.makeText(context, ""+sinhVien.getMaSinhVien()+", "+isChecked, Toast.LENGTH_SHORT).show();
//                    sinhVien.setCheck(false);
//                    holder.toggleButtonCheck.setChecked(false);
//                }
//            }
//        });

//        Log.d("haiz","day ne "+sinhVien.getMaSinhVien());
//        if(sinhVien.getMaSinhVien().equals("a")) {
//            holder.toggleButtonCheck.setChecked(true);
//            Log.d("haiz","day ne "+sinhVien.getMaSinhVien());
//        }

        return convertView;
    }
}
