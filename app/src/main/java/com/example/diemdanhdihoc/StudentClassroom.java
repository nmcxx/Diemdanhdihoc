package com.example.diemdanhdihoc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentClassroom extends AppCompatActivity {

    private final String URL_GET_DIEMDANH="https://vdili.000webhostapp.com/diemdanh.php";
    private final String URL_DIEMDANH ="https://vdili.000webhostapp.com/svdd.php";

    private ListView lvDanhSachDiemDanh;
    private Button btnDiemDanh;
    private TextView txtSoBuoi, txtNgay, txtTenLop;

    private final LoadingDialog loadingDialog = new LoadingDialog(this);

    private ArrayList<DiemDanh> diemDanhArrayList;
    private ArrayAdapter arrayAdapter;
//    private DiemDanh diemDanh;

    private static String keyDiemDanh;

    // truyen du lieu act
    private int idLopHoc;
    private String tenLop;
    private String tenGV;
    private String maLop;
    private int soBuoi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_classroom);

        lvDanhSachDiemDanh = (ListView) findViewById(R.id.lvThongKeDD);
        btnDiemDanh = (Button) findViewById(R.id.btnDiemDanh);
        txtSoBuoi = (TextView) findViewById(R.id.txtSoBuoi_student);
        txtNgay = (TextView) findViewById(R.id.txtNgay_Student);
        txtTenLop = (TextView) findViewById(R.id.classroom_student_tenlophoc);

        // get du lieu tu act
        Intent intent = getIntent();
        idLopHoc = intent.getIntExtra("IdLopHoc",0);
        tenLop = intent.getStringExtra("TenLop");
        tenGV = intent.getStringExtra("TenGV");
        soBuoi = intent.getIntExtra("SoBuoi",0);
        maLop = intent.getStringExtra("MaLop");

        //
        txtSoBuoi.setText(""+soBuoi);
        txtTenLop.setText(tenLop+"-"+maLop+"-"+tenGV);


//        Toast.makeText(this, ""+idLopHoc, Toast.LENGTH_SHORT).show();
        
        btnDiemDanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });

        diemDanhArrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, diemDanhArrayList);
        lvDanhSachDiemDanh.setAdapter(arrayAdapter);

        getDiemDanh(LoginAct.checkUser, idLopHoc, 1);
    }

    private void scanCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(ScanCapture.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result!=null)
        {
            if(result.getContents()!=null)
            {
                keyDiemDanh = result.getContents();
                loadingDialog.startLoadingDialog();
                diemDanh(idLopHoc,LoginAct.checkUser,soBuoi,keyDiemDanh);
//                edtKey.setText(result.getContents());
            }
            else
            {
                Toast.makeText(this,"Khong co ket qua", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    private void diemDanh(final int idLop,final String username, final int soBuoiHoc, final String maDiemDanh) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DIEMDANH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("true"))
                {
                    getDiemDanh(LoginAct.checkUser, idLopHoc, 1);
                    Toast.makeText(StudentClassroom.this, "Bạn đã điểm danh thành công", Toast.LENGTH_SHORT).show();
                }
                else if(response.trim().equals("false"))
                {
                    Toast.makeText(StudentClassroom.this, "Lỗi hệ thống", Toast.LENGTH_SHORT).show();
                }
                else if(response.trim().equals("isset"))
                {
                    Toast.makeText(StudentClassroom.this, "Bạn đã điểm danh rồi", Toast.LENGTH_SHORT).show();
                }
                else if(response.trim().equals("empty"))
                {
                    Toast.makeText(StudentClassroom.this, "Mã điểm danh không đúng hoặc đã hết hạn", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismissDialog();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Loi", "Loi :" + error);
                        loadingDialog.dismissDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("idlophoc", String.valueOf(idLop));
                params.put("sobuoi", String.valueOf(soBuoiHoc));
                params.put("madiemdanh", maDiemDanh);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void getDiemDanh(final String username, final int idlophoc, final int usertype){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_DIEMDANH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                diemDanhArrayList.clear();
                try {
                    JSONArray j = new JSONArray(response);
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = j.getJSONObject(i);
//                            Toast.makeText(StudentClassroom.this,"Lop Hoc"+object.getInt("Idlophoc"),Toast.LENGTH_SHORT).show();
                            diemDanhArrayList.add(new DiemDanh(
                                    object.getString("Username"),
                                    object.getString("Hoten"),
                                    object.getInt("Sobuoi"),
                                    object.getInt("Diemdanh"))
                            );
//                            Toast.makeText(TeacherMainAct.this,"Xóa lớp",Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
                catch(JSONException e)
                {
                    Log.d("Loi", "Loi :" + e);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Loi", "Loi :" + error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("idlophoc", String.valueOf(idlophoc));
                params.put("usertype", String.valueOf(usertype));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}