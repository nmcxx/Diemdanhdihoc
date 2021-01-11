package com.example.diemdanhdihoc;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SinhVienAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private int layout;
//    private List<SinhVien> sinhVienList;

    private ArrayList<SinhVien> svList;
    private ArrayList<SinhVien> sinhVienList;

    public SinhVienAdapter(Context context, int layout, ArrayList<SinhVien> sinhVienList) {
        this.context = context;
        this.layout = layout;
        this.sinhVienList = sinhVienList;
        svList = sinhVienList;
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

    private class ViewHolder{
        TextView txtSinhVien;
        ToggleButton toggleButtonCheck;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtSinhVien = (TextView) convertView.findViewById(R.id.dssv_custom_msv);
            holder.toggleButtonCheck = (ToggleButton) convertView.findViewById(R.id.dssv_custom_tgbtnTrangThai);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        SinhVien sinhVien = sinhVienList.get(position);

        holder.txtSinhVien.setText(sinhVien.toString());


//        Log.d("haiz","day ne "+sinhVien.getMaSinhVien());
//        if(sinhVien.getMaSinhVien().equals("a")) {
//            holder.toggleButtonCheck.setChecked(true);
//            Log.d("haiz","day ne "+sinhVien.getMaSinhVien());
//        }

        return convertView;
    }
}
