package com.example.diemdanhdihoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentMain extends AppCompatActivity {

    private final String URL_GET_LOP="https://vdili.000webhostapp.com/user.php";
    private final String URL_GET_THONGTIN="https://vdili.000webhostapp.com/thongtin.php";
    private final String URL_DOIMATKHAU = "https://vdili.000webhostapp.com/doimatkhau.php";

    private final LoadingDialog loadingDialog = new LoadingDialog(this);

    private ListView lvDanhSachLop;
    private ArrayList<LopHoc> lopHocArrayList;
    private ArrayAdapter arrayAdapter;

    private User user;

//    public static String tenLop;
//    public static String tenGiaoVien;
//    public static int idLop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        if(LoginAct.checkUser==null || LoginAct.checkUser=="")
        {
            Intent intent = new Intent(StudentMain.this,LoginAct.class );
            startActivity(intent);
            finish();
        }

        lvDanhSachLop = (ListView) findViewById(R.id.lvDanhSachLopStudent);
        lopHocArrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, lopHocArrayList);
        lvDanhSachLop.setAdapter(arrayAdapter);

        lvDanhSachLop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                tenLop = lopHocArrayList.get(position).getTenLop();
//                tenGiaoVien = lopHocArrayList.get(position).getUserName();
//                idLop = lopHocArrayList.get(position).getId();

                Intent intent = new Intent(StudentMain.this,StudentClassroom.class);
                intent.putExtra("TenLop", lopHocArrayList.get(position).getTenLop());
                intent.putExtra("TenGV", lopHocArrayList.get(position).getHoTen());
                intent.putExtra("IdLopHoc", lopHocArrayList.get(position).getId());
                intent.putExtra("MaLop", lopHocArrayList.get(position).getMaLop());
                intent.putExtra("SoBuoi", lopHocArrayList.get(position).getSoBuoi());
                startActivity(intent);
            }
        });

        getDanhSachLop(LoginAct.checkUser,1);
        getThongTin(LoginAct.checkUser);
    }

    public void getDanhSachLop(final String username, final int usertype){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_LOP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                lopHocArrayList.clear();
                try {
                    JSONArray j = new JSONArray(response);
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = j.getJSONObject(i);
//                            Toast.makeText(StudentMain.this,"Lop Hoc"+object.getInt("Idlophoc"),Toast.LENGTH_SHORT).show();
                            lopHocArrayList.add(new LopHoc(
                                    object.getInt("Idlophoc"),
                                    object.getString("Malop"),
                                    object.getString("Username"),
                                    object.getString("Tenlop"),
                                    object.getString("Hoten"),
                                    object.getInt("Sobuoi"))
                            );
//                            Toast.makeText(TeacherMainAct.this,"Xóa lớp",Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Log.d("Loi", "Loi :" + e);
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
                params.put("usertype", String.valueOf(usertype));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getThongTin(final String username){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_THONGTIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray j = new JSONArray(response);
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = j.getJSONObject(i);
                            user = new User(
                                    object.getString("Username"),
                                    object.getString("Password"),
                                    object.getString("Hoten"),
                                    object.getInt("Usertype")
                            );
//                            Log.d("a", "lplp" + username + " aha " + url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void changePass(final String username, final String password){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DOIMATKHAU, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("true"))
                {
                    LoginAct.checkPass = password;
                    Toast.makeText(StudentMain.this,"Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();
                }
                else
                {
                    Toast.makeText(StudentMain.this, "Đổi mật khẩu không thành công", Toast.LENGTH_SHORT).show();
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
                params.put("password", password);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void dialogThongTin(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thongtin);

        final EditText edtUsername, edtHoten;
        Button btnOK;

        edtUsername = (EditText) dialog.findViewById(R.id.dialog_thongtin__edt_username);
        edtHoten = (EditText) dialog.findViewById(R.id.dialog_thongtin__edt_hoten);
        btnOK = (Button) dialog.findViewById(R.id.dialog_thongtin_btn_xacnhan);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        edtUsername.setText(String.valueOf(user.getUserName()));
        edtHoten.setText(user.getHoTen());
        edtUsername.setEnabled(false);
        edtHoten.setEnabled(false);

        dialog.show();
    }

    public void dialogDoiMatKhau(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_doimatkhau);

        final EditText edtOldPass, edtNewPass, edtTryNewPass;
        Button btnDoiMatKhau, btnHuy;
        edtOldPass = (EditText) dialog.findViewById(R.id.dialog_doimatkhau_edtOldPass);
        edtNewPass = (EditText) dialog.findViewById(R.id.dialog_doimatkhau_edtNewPass);
        edtTryNewPass = (EditText) dialog.findViewById(R.id.dialog_doimatkhau_edtTryNewPass);
        btnDoiMatKhau = (Button) dialog.findViewById(R.id.dialog_doimatkhau_btnDoiMatKhau);
        btnHuy = (Button) dialog.findViewById(R.id.dialog_doimatkhau_btnHuy);



        btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = edtOldPass.getText().toString();
                String newPass = edtNewPass.getText().toString();
                String tryNewPass = edtTryNewPass.getText().toString();
                if(oldPass.isEmpty() || newPass.isEmpty() || tryNewPass.isEmpty())
                {
                    Log.d("vai lon",""+oldPass.length());
                    Toast.makeText(StudentMain.this, "Cần phải nhập đầy đủ thông tin",Toast.LENGTH_SHORT).show();
                }
                else if(oldPass.equals(LoginAct.checkPass)==false)
                {
                    Toast.makeText(StudentMain.this, "Mật khẩu cũ không chính xác",Toast.LENGTH_SHORT).show();
                }
                else if(oldPass.equals(newPass)==true)
                {
                    Toast.makeText(StudentMain.this, "Mật khẩu mới phải khác mật khẩu cũ", Toast.LENGTH_SHORT).show();
                }
                else if(newPass.equals(tryNewPass)==false)
                {
                    Toast.makeText(StudentMain.this, "Nhập lại mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                }
                else
                {
//                    Toast.makeText(TeacherMainAct.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    changePass(LoginAct.checkUser, newPass);
                    dialog.dismiss();
                    loadingDialog.startLoadingDialog();
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_teacher_main, menu);
        menu.findItem(R.id.item1).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item2:
                dialogThongTin();
                break;
            case R.id.item3:
                dialogDoiMatKhau();
                break;
            case R.id.item4:
                LoginAct.checkUser = "";
                LoginAct.checkPass = "";
                Intent intent = new Intent(StudentMain.this, LoginAct.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}