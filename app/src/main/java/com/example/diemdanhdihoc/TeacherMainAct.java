package com.example.diemdanhdihoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherMainAct extends AppCompatActivity {

    private String urlhienthilop = "https://vdili.000webhostapp.com/user.php";
    private String urlxoalop = "https://vdili.000webhostapp.com/xoalop.php";
    private String urlthongtin = "https://vdili.000webhostapp.com/thongtin.php";
    private String urldoimatkhau = "https://vdili.000webhostapp.com/doimatkhau.php";
    private String urltaolop = "https://vdili.000webhostapp.com/taolop.php";

    private final LoadingDialog loadingDialog = new LoadingDialog(TeacherMainAct.this);

    private ListView lvLopHoc;
    private ArrayList<LopHoc> lopHocArrayList;
    private ArrayAdapter arrayAdapter, arrayAdapterUser;

    private User user;

    private ArrayList<User> userArrayList;

//    public static String tenLop;
//    public static int idLop;

  //  private EditText edtHoten;
 //   private Button btnDialogOK;
    //private LopHocAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);
        setTitle("Lớp Học");

        if(LoginAct.checkUser==null || LoginAct.checkUser=="")
        {
            Intent intent = new Intent(TeacherMainAct.this,LoginAct.class );
            startActivity(intent);
            finish();
        }
        lvLopHoc = (ListView) findViewById(R.id.lvDanhSachLop);

        lopHocArrayList = new ArrayList<>();
        GetData(urlhienthilop, LoginAct.checkUser, 0);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lopHocArrayList);
        lvLopHoc.setAdapter(arrayAdapter);

        lvLopHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                tenLop = lopHocArrayList.get(position).getTenLop();
//                idLop = lopHocArrayList.get(position).getId();
//                Toast.makeText(TeacherClassroomActivity.this, "Loi"+idLop, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TeacherMainAct.this, TeacherClassroomActivity.class);
                intent.putExtra("IdLop",lopHocArrayList.get(position).getId());
                intent.putExtra("TenLop",lopHocArrayList.get(position).getTenLop());
                intent.putExtra("MaLop",lopHocArrayList.get(position).getMaLop());
                intent.putExtra("SoBuoi",lopHocArrayList.get(position).getSoBuoi());
//                intent.putExtra("")
                startActivity(intent);
            }
        });

        lvLopHoc.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               // menu
                ShowMenu(view, position);
//                Toast.makeText(TeacherMainAct.this,""+lopHocArrayList.get(position).getMaLop(),Toast.LENGTH_SHORT).show();

                return false;
            }
        });

//        GetData(urlhienthilop, LoginAct.checkUser);
        GuiYeuCau(urlthongtin,LoginAct.checkUser);
        

    }

    private void ShowMenu(View view, final int vitri) {
        PopupMenu popupMenu = new PopupMenu(TeacherMainAct.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_xoalop, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.item_xoalop:
                        AlertDialog.Builder builder = new AlertDialog.Builder(TeacherMainAct.this);
                        builder.setMessage("Bạn có muốn xóa lớp học này không?")
                                .setCancelable(false)
                                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        XoaLop(urlxoalop,lopHocArrayList.get(vitri).getId());
//                                        Toast.makeText(TeacherMainAct.this,"Xóa lớp học thành công",Toast.LENGTH_SHORT).show();
                                        loadingDialog.startLoadingDialog();
                                    }
                                })
                                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        Toast.makeText(TeacherMainAct.this,"ok",Toast.LENGTH_SHORT).show();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setTitle("Hệ Thống");
                        alertDialog.show();

//                        Toast.makeText(TeacherMainAct.this,"Xóa lớp",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void XoaLop(String url, final int idlophoc){
        RequestQueue requestQueue = Volley.newRequestQueue (this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    if(response.trim().equals("true"))
                    {
//                        GetData(url);
                        loadingDialog.dismissDialog();
                        Toast.makeText(TeacherMainAct.this,"Xóa lớp thành công", Toast.LENGTH_SHORT).show();
                        GetData(urlhienthilop,LoginAct.checkUser,0);
                    }
                    else
                    {
                        loadingDialog.dismissDialog();
                        Toast.makeText(TeacherMainAct.this, "Xóa lớp không thành công", Toast.LENGTH_SHORT).show();
                    }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("A", "Loi: "+error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("bb","idlop la "+idlophoc);
                params.put("idlophoc",String.valueOf(idlophoc));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void GuiYeuCau(final String url, final String username) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                            Log.d("a", "lplp" + username + " aha " + url);
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

    private void GetData(final String url, final String username, final int usertype) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                lopHocArrayList.clear();
                try {
                    JSONArray j = new JSONArray(response);
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = j.getJSONObject(i);
//                            Toast.makeText(TeacherMainAct.this,"Lop Hoc"+object.getInt("Idlophoc"),Toast.LENGTH_SHORT).show();
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

    private void ChangePass(final String url, final String username, final String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("true"))
                {
                    LoginAct.checkPass = password;
                    Toast.makeText(TeacherMainAct.this,"Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();
                }
                else
                {
                    Toast.makeText(TeacherMainAct.this, "Đổi mật khẩu không thành công", Toast.LENGTH_SHORT).show();
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

    public String GenerateKey(){
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        int n=6;
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    private void CreateClassRoom(final String url, final String username, final String tenLop) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("true"))
                {
                    Toast.makeText(TeacherMainAct.this,"Tạo lớp thành công", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();
                    GetData(urlhienthilop,LoginAct.checkUser,0);
                }
                else
                {
                    loadingDialog.dismissDialog();
                    Toast.makeText(TeacherMainAct.this, "Tạo lớp không thành công", Toast.LENGTH_SHORT).show();
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
                params.put("tenlop", tenLop);
                params.put("key", GenerateKey());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }




    private void DialogThongTin(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // xoa title
        dialog.setContentView(R.layout.dialog_thongtin);

        final EditText edtUsername, edtHoten;
        Button btnOK;

        //arrayAdapterUser = new ArrayAdapter(this,android.R.layout.simple_list_item_1, userArrayList);
        edtUsername = (EditText) dialog.findViewById(R.id.dialog_thongtin__edt_username);
        edtHoten = (EditText) dialog.findViewById(R.id.dialog_thongtin__edt_hoten);
        btnOK = (Button) dialog.findViewById(R.id.dialog_thongtin_btn_xacnhan);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edtUsername.setText(String.valueOf(user.getUserType()));
                dialog.cancel();
            }
        });

        edtUsername.setText(String.valueOf(user.getUserName()));
        edtHoten.setText(user.getHoTen());
//        Toast.makeText(this, ""+user.getHoTen(), Toast.LENGTH_SHORT).show();
//        user.setUserName("A");
//        user.setHoTen("a");
//        edtUsername.setText(user.getUserName());
       // edtHoten.setText(user.getHoTen());
        edtUsername.setEnabled(false);
        edtHoten.setEnabled(false);



        dialog.show();

    }

    private void DialogDoiMatKhau(){
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
                    Toast.makeText(TeacherMainAct.this, "Cần phải nhập đầy đủ thông tin",Toast.LENGTH_SHORT).show();
                }
                else if(oldPass.equals(LoginAct.checkPass)==false)
                {
                    Toast.makeText(TeacherMainAct.this, "Mật khẩu cũ không chính xác",Toast.LENGTH_SHORT).show();
                }
                else if(oldPass.equals(newPass)==true)
                {
                    Toast.makeText(TeacherMainAct.this, "Mật khẩu mới phải khác mật khẩu cũ", Toast.LENGTH_SHORT).show();
                }
                else if(newPass.equals(tryNewPass)==false)
                {
                    Toast.makeText(TeacherMainAct.this, "Nhập lại mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                }
                else
                {
//                    Toast.makeText(TeacherMainAct.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    ChangePass(urldoimatkhau, LoginAct.checkUser, newPass);
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

    private void DialogTaoLop(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_taolop);

        final EditText edtTenLop = (EditText) dialog.findViewById(R.id.dialog_taolop_edtTenLop);
        Button btnTaoLop, btnHuy;
        btnTaoLop = (Button) dialog.findViewById(R.id.dialog_taolop_btnTaoLop);
        btnHuy = (Button) dialog.findViewById(R.id.dialog_taolop_btnHuy);

        btnTaoLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenLop = edtTenLop.getText().toString();
                if(tenLop.isEmpty())
                {
                    Toast.makeText(TeacherMainAct.this,"Chưa nhập tên lớp",Toast.LENGTH_SHORT).show();
                }
                else if(tenLop.length()>30)
                {
                    Toast.makeText(TeacherMainAct.this,"Tên lớp không được quá 30 ký tự "+tenLop,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CreateClassRoom(urltaolop,LoginAct.checkUser,tenLop);
                    loadingDialog.startLoadingDialog();
                    dialog.dismiss();
//                    Toast.makeText(TeacherMainAct.this,"Tạo lớp thành công",Toast.LENGTH_SHORT).show();
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

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item1:
//                Toast.makeText(this,"Tao lop hoc",Toast.LENGTH_SHORT).show();
                DialogTaoLop();
                break;
            case R.id.item2:
                DialogThongTin();
//                Toast.makeText(this,"Thông tin cá nhân",Toast.LENGTH_SHORT).show();
                break;
            case R.id.item3:
                DialogDoiMatKhau();
//                Toast.makeText(this,"Đổi mật khẩu",Toast.LENGTH_SHORT).show();
                break;
            case R.id.item4:
                //Toast.makeText(this,"Đăng xuất",Toast.LENGTH_SHORT).show();
                Log.d("loi","Click Item 4");
                LoginAct.checkUser = "";
                LoginAct.checkPass = "";
                Intent intent = new Intent(TeacherMainAct.this, LoginAct.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);


    }
}