package com.example.diemdanhdihoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.*;

import java.util.HashMap;
import java.util.Map;

public class LoginAct extends AppCompatActivity {

    public static String checkUser, checkPass;

    private String URL_CHECKLOGIN = "https://vdili.000webhostapp.com/checklogin.php";
    private Button btnLogin;
    private EditText edtUsername, edtPassword;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
//            Manifest.permission.INTERNET,
//            Manifest.permission.CAMERA
    };

    private final LoadingDialog loadingDialog = new LoadingDialog(LoginAct.this);
//    private JSONP
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verifyStoragePermissions(this);

//        if(LoginAct.checkUser!="" || LoginAct.checkUser!=null)
//        {
//            Intent intent = new Intent(home.this,LoginAct.class );
//            startActivity(intent);
//        }

        btnLogin = (Button)findViewById(R.id.btnLogin);
        edtUsername = (EditText)findViewById(R.id.edtUsername);
        edtPassword = (EditText)findViewById(R.id.edtPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkUserName = edtUsername.getText().toString().trim();
                String checkPassWord = edtPassword.getText().toString().trim();
                if(checkUserName.isEmpty() || checkPassWord.isEmpty())
                {
                    Toast.makeText(LoginAct.this, "Chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingDialog.startLoadingDialog();
                    LoginUser(URL_CHECKLOGIN);
                }
            }
        });
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void LoginUser(String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,  new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("gv"))
                        {
                            loadingDialog.dismissDialog();
                            Toast.makeText(LoginAct.this, "GV Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Log.d("b","adaga");
                            checkUser = edtUsername.getText().toString().trim();
                            checkPass = edtPassword.getText().toString().trim();
                            Intent intent = new Intent(LoginAct.this,TeacherMainAct.class);
                            startActivity(intent);
                            finish();
                        }
                        else if(response.trim().equals("hs"))
                        {
                            loadingDialog.dismissDialog();
                            Toast.makeText(LoginAct.this, "HS Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Log.d("b","ahocsinh");
                            checkUser = edtUsername.getText().toString().trim();
                            checkPass = edtPassword.getText().toString().trim();
                            Intent intent = new Intent(LoginAct.this,StudentMain.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            loadingDialog.dismissDialog();
                            Toast.makeText(LoginAct.this, "Tài khoản hoặc mật khẩu sai", Toast.LENGTH_SHORT).show();
                            Log.d("b","ahuhjh");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("A", "Loi: "+error.toString());
                        loadingDialog.dismissDialog();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username",edtUsername.getText().toString().trim());
                params.put("password",edtPassword.getText().toString().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

//    private class AttemptLogin extends AsyncTask<String, String, JSONObject>
//    {
//
//        @Override
//        protected JSONObject doInBackground(String... strings) {
//            String username =
//        }
//    }
}