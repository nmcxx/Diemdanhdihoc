package com.example.diemdanhdihoc;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ThongKeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ThongKeHocSinhAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_thongke);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_thongke);

        setRecyclerView();

    }

    private void setRecyclerView()
    {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ThongKeHocSinhAdapter(this, getList());
        recyclerView.setAdapter(adapter);
    }

    private List<ThongKeHocSinh> getList(){
        List<ThongKeHocSinh> thongKeHocSinhs = new ArrayList<>();
        thongKeHocSinhs.add(new ThongKeHocSinh("4123", "Can", 2));
        thongKeHocSinhs.add(new ThongKeHocSinh("453", "Tuan", 5));

        thongKeHocSinhs.add(new ThongKeHocSinh("112313123", "Khang", 5));
        return thongKeHocSinhs;
    }
}
