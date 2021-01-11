package com.example.diemdanhdihoc;

import android.content.Context;
import android.net.http.RequestQueue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.Volley;

import java.util.List;

public class UserAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<User> userList;

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    private class ViewHolder{
//        RequestQueue requestQueue = Volley
    }


}
