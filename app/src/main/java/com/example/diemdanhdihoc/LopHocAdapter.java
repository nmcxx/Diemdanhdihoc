package com.example.diemdanhdihoc;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class LopHocAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<LopHoc> lopHocList;

    public LopHocAdapter(Context context, int layout, List<LopHoc> lopHocList) {
        this.context = context;
        this.layout = layout;
        this.lopHocList = lopHocList;
    }

    @Override
    public int getCount() {
        return lopHocList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtTenLopHoc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtTenLopHoc = (TextView) convertView.findViewById(R.id.txtTenLopHoc);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        LopHoc lopHoc = lopHocList.get(position);

        holder.txtTenLopHoc.setText(lopHoc.getTenLop());

        return convertView;
    }
}
