package com.example.diemdanhdihoc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class TeacherClassroomActivity extends AppCompatActivity {

    private ListView lvDanhSachDiemDanh;
    private TextView txtTenLop;
    public TextView txtSoBuoi, txtSoLuong;
    private SearchView searchViewDSDD;
    private Button btnDDTC;

    private final LoadingDialog loadingDialog = new LoadingDialog(TeacherClassroomActivity.this);
    private final String URL_GET_DSSV = "https://vdili.000webhostapp.com/svlh.php";
    private final String URL_ADD_SV = "https://vdili.000webhostapp.com/themsv.php";
    private final String URL_GET_DIEMDANH="https://vdili.000webhostapp.com/thongtindiemdanh.php"; // diemdanh
    private final String URL_XOA_SV="https://vdili.000webhostapp.com/xoasinhvien.php";
    private final String URL_DDTC="https://vdili.000webhostapp.com/diemdanhthucong.php";
    private final String URL_UPDATE_BH="https://vdili.000webhostapp.com/updatebuoihoc.php";

    private static final int FILE_SELECT_CODE = 0;

    private ArrayList<SinhVien> sinhVienArrayList;
//    private SinhVienAdapter arrayAdapter;
    private SinhVienAdapter arrayAdapter;

    private List<User> userList = new ArrayList<User>();
    private String path;

    private ArrayList<DiemDanh> diemDanhArrayList;


    // truyen du lieu
    private int idLopHoc;
    private String tenLop;
    private String maLopHoc;
//    private String hoTenGV;
    private int soBuoi;
    private int soLuong;

    //


//    private ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_classroom);
        setTitle("Lớp Học");


        txtTenLop = (TextView) findViewById(R.id.classroom_teacher_tenlophoc);
        txtSoBuoi = (TextView) findViewById(R.id.txtSoBuoi);
        txtSoLuong = (TextView) findViewById(R.id.txtSLHocVien);
        lvDanhSachDiemDanh = (ListView) findViewById(R.id.lvDanhSachDiemDanh);
        searchViewDSDD = (SearchView) findViewById(R.id.search_lvDiemDanh);
        btnDDTC = (Button) findViewById(R.id.btnDDTC);

        Intent intent = getIntent();
        idLopHoc = intent.getIntExtra("IdLop",0);
//        hoTenGV = intent.getStringExtra("Hoten");
        tenLop = intent.getStringExtra("TenLop");
        soBuoi = intent.getIntExtra("SoBuoi",0);
        maLopHoc = intent.getStringExtra("MaLop");
        // hien thi thong tin lop
        txtTenLop.setText(tenLop+" - "+maLopHoc);
        txtSoBuoi.setText(""+soBuoi);

        diemDanhArrayList = new ArrayList<>();

        sinhVienArrayList = new ArrayList<>();
        arrayAdapter = new SinhVienAdapter(this, R.layout.dssv_custom, sinhVienArrayList);
//        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, sinhVienArrayList);
//        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, sinhVienArrayList);
        lvDanhSachDiemDanh.setAdapter(arrayAdapter);
//        Toast.makeText(TeacherClassroomActivity.this, "so luongg"+lvDanhSachDiemDanh.getCount(), Toast.LENGTH_SHORT).show();

//        while(sinhVienArrayList.isEmpty()) {
//            txtSoLuong.setText("j" + sinhVienArrayList.size());
//        }


        searchViewDSDD.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                TeacherClassroomActivity.this.arrayAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                TeacherClassroomActivity.this.arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        btnDDTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(arrayAdapter.getSinhVienList().isEmpty()){
//                    Toast.makeText(TeacherClassroomActivity.this, "Chưa chọn sinh viên đi", Toast.LENGTH_SHORT).show();
//                }
                for(SinhVien sv : arrayAdapter.getSinhVienList())
//                    Toast.makeText(TeacherClassroomActivity.this, sv.getMaSinhVien()+" - "+sv.isCheck(), Toast.LENGTH_SHORT).show();
                Log.d("check diem danh",sv.getMaSinhVien()+" - "+sv.isCheck());
                DiemDanhThuCong(idLopHoc,soBuoi);
//                UpdateBuoiHoc();
            }
        });

//        Toast.makeText(TeacherClassroomActivity.this, "Hello World", Toast.LENGTH_SHORT).show();
        GetSVLH(URL_GET_DSSV, idLopHoc);
        GetDiemDanh(LoginAct.checkUser, idLopHoc, 0);
        loadingDialog.startLoadingDialog();
    }

    private void GetSVLH(final String url, final int idLop) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sinhVienArrayList.clear();
                try {
                    JSONArray j = new JSONArray(response);
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = j.getJSONObject(i);
                            sinhVienArrayList.add(new SinhVien(
                                    object.getString("Username"),
                                    object.getString("Hoten")
                            ));
//                            Log.d("a", "lplp" + username + " aha " + url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    soLuong = sinhVienArrayList.size();
//                    txtSoLuong = (TextView) findViewById(R.id.txtSLHocVien);
//                    txtSoLuong.setText(soLuong);
                    txtSoLuong.setText(""+soLuong);
//                    Toast.makeText(TeacherClassroomActivity.this, "so luong "+soLuong, Toast.LENGTH_SHORT).show();
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
                params.put("idlophoc", String.valueOf(idLop));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }



    private void ThemSinhVien(final String username, final String hoTen, final int idLop)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_SV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("true")) {
                    Toast.makeText(TeacherClassroomActivity.this, "Them sinh vien " + username + " thanh cong", Toast.LENGTH_SHORT).show();
                    sinhVienArrayList.add(new SinhVien(username,hoTen));
                    arrayAdapter.notifyDataSetChanged();
//                    GetSVLH(URL_GET_DSSV,  idLopHoc);
                } else {
                    Toast.makeText(TeacherClassroomActivity.this, "Them sinh vien " + username + "," +hoTen+","+idLop+" that bai", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Loi", "Loi :" + error);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username", username);
                params.put("hoten", hoTen);
                params.put("idlophoc", String.valueOf(idLop));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void XoaSinhVien(final String username){
        RequestQueue requestQueue = Volley.newRequestQueue (this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_XOA_SV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("true"))
                {
                    loadingDialog.dismissDialog();
                    Toast.makeText(TeacherClassroomActivity.this,"Xóa sinh viên thành công", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingDialog.dismissDialog();
                    Toast.makeText(TeacherClassroomActivity.this, "Xóa sinh viên không thành công", Toast.LENGTH_SHORT).show();
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
                Log.d("bb","idlop la "+idLopHoc);
                params.put("idlophoc",String.valueOf(idLopHoc));
                params.put("username",username);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void DiemDanhThuCong(final int idLop, final int soBuoiHoc) {
        loadingDialog.startLoadingDialog();
        for(final SinhVien sv : arrayAdapter.getSinhVienList()) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DDTC, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.trim().equals("true")) {
//                        getDiemDanh(LoginAct.checkUser, idLopHoc, 1);
                        Log.d("diem danh thu cong",sv.getMaSinhVien()+" thanh cong");
//                    Toast.makeText(StudentClassroom.this, "Bạn đã điểm danh thành công", Toast.LENGTH_SHORT).show();
                    } else if (response.trim().equals("false")) {
                        Log.d("diem danh thu cong",sv.getMaSinhVien()+" that bai");
//                    Toast.makeText(StudentClassroom.this, "Lỗi hệ thống", Toast.LENGTH_SHORT).show();
                    } else if (response.trim().equals("isset")) {
                        Log.d("diem danh thu cong",sv.getMaSinhVien()+" da diem danh roi");
//                    Toast.makeText(StudentClassroom.this, "Bạn đã điểm danh rồi", Toast.LENGTH_SHORT).show();
                    } else if (response.trim().equals("empty")) {
                        Log.d("diem danh thu cong",sv.getMaSinhVien()+" loi khong xac dinh");
//                    Toast.makeText(StudentClassroom.this, "Mã điểm danh không đúng hoặc đã hết hạn", Toast.LENGTH_SHORT).show();
                    }
//                    loadingDialog.dismissDialog();
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Loi", "Loi :" + error);
//                            loadingDialog.dismissDialog();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", sv.getMaSinhVien());
                    params.put("idlophoc", String.valueOf(idLop));
                    params.put("sobuoi", String.valueOf(soBuoiHoc));
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
        loadingDialog.dismissDialog();
        GetDiemDanh(LoginAct.checkUser, idLopHoc, 0);
        UpdateBuoiHoc();
    }

    private void UpdateBuoiHoc(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_BH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("true")) {
                    txtSoBuoi.setText(""+(soBuoi+1));
                    Toast.makeText(TeacherClassroomActivity.this, "Cập nhật buổi học thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TeacherClassroomActivity.this, "Cập nhật buổi học không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Loi cap nhat", "Loi :" + error);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("idlophoc", String.valueOf(idLopHoc));
                params.put("sobuoi", String.valueOf(soBuoi));
                params.put("key", GenerateKey());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private String GenerateKey(){
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

    private void GetDiemDanh(final String username, final int idlophoc, final int usertype){
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
//                            Toast.makeText(TeacherClassroomActivity.this, "size "+diemDanhArrayList.size(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(TeacherClassroomActivity.this, "zxc"+diemDanhArrayList.size(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();
//                    arrayAdapter.notifyDataSetChanged();
                }
                catch(JSONException e)
                {
                    Log.d("Loi", "Loizz :" + e);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Loi", "Loicc :" + error);
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

    private void exportCSV(){
        String fName = "data.xls";
//        File sdCard = Environment.getDataDirectory();
        File sdCard = Environment.getExternalStorageDirectory();
        Toast.makeText(this, ""+sdCard.canWrite(), Toast.LENGTH_SHORT).show();
        File directory = new File(sdCard.getAbsolutePath()+"/Điểm Danh Đi Học");
        directory.mkdirs();
        File file = new File(directory, fName);

        WorkbookSettings workbookSettings = new WorkbookSettings();
        workbookSettings.setLocale(new Locale("en","EN"));
        WritableWorkbook workbook;
        try {
//            int a = 1;
            workbook = Workbook.createWorkbook(file, workbookSettings);
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);
            try {
                sheet.addCell(new Label(0,0,"Mã sinh viên"));
                sheet.addCell(new Label(1,0,"Họ tên"));
                for(int i=0;i<soBuoi;i++)
                {
                    sheet.addCell(new Label(i+2,0,"Buổi "+(i+1)));
                }

                for(int i=0;i<soLuong;i++)
                {
                    sheet.addCell(new Label(0,i+1,""+sinhVienArrayList.get(i).getMaSinhVien()));
                    sheet.addCell(new Label(1,i+1,""+sinhVienArrayList.get(i).getHoTen()));
                }


                for(int i=0;i<soLuong;i++)
                {
//                    int a=0;
                    String[] checkDiemDanh = new String[soBuoi];
                    int b = (diemDanhArrayList.size()/soLuong)*(i+1);
//                    Log.d("Loi","Loi a la: "+a);
                    Log.d("Loi","Loi a la: "+b);
//                    Toast.makeText(this, "a: "+a, Toast.LENGTH_SHORT).show();
                    for(int a=0;a<soBuoi;a++)
                    {
                        // a = 0 ; sobuoi = 3 ; b = 6;
                        // 0 1 2
                        // 6-3+0
                        checkDiemDanh[a] = ( diemDanhArrayList.get(b-soBuoi+a).getDiemDanh() == 0 ) ? "" : "x";
                    }
//                    a = b;
//                    String dd[] = new String[diemDanhArrayList.size()/soLuong];
//                    for(int k=i;k<diemDanhArrayList.size();i++)
                    for(int j=0;j<soBuoi;j++)
                    {
                        Log.d("Loi","Loi la: "+checkDiemDanh[j]);
//                        diemDanhArrayList.get(j).getDiemDanh();
                        sheet.addCell(new Label(j+2,i+1,""+checkDiemDanh[j]));
                    }
//                        sheet.addCell(new Label());
                }
            } catch (RowsExceededException e) {
                // th
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }


            workbook.write();
            Toast.makeText(this, "Xuất tệp thành công tại "+file.getPath(), Toast.LENGTH_SHORT).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
                Log.d("Loi tep",""+e.toString());
                Toast.makeText(this, "Xuất tệp Lỗi "+e.toString(), Toast.LENGTH_SHORT).show();
            }
            //createExcel(excelSheet);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_teacher_classroom, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void dialogAddSV(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_sv);

        final EditText edtMSV = (EditText) dialog.findViewById(R.id.dialog_add_sv_Msv);
        final EditText edtHoTen = (EditText) dialog.findViewById(R.id.dialog_add_sv_Hoten);
        Button btnAdd = (Button) dialog.findViewById(R.id.dialog_add_sv_btnAdd);
        Button btnCancel = (Button) dialog.findViewById(R.id.dialog_add_sv_btnCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maSV = edtMSV.getText().toString();
                String hoTen = edtHoTen.getText().toString();
                if(maSV.isEmpty() || maSV.equals("") || hoTen.isEmpty() || hoTen.equals(""))
                {
                    Toast.makeText(TeacherClassroomActivity.this, "Khong duoc de trong ma sinh vien va ho ten", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ThemSinhVien(maSV,hoTen, idLopHoc);
//                    Toast.makeText(TeacherClassroomActivity.this, "MA sinh vien "+maSV+" da duoc them vao lop", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void dialogAddSVExcel(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_sv_excel);

//        TextView txtTips = (TextView) dialog.findViewById(R.id.dialog_add_sv_excel_txt);
//        ImageView imgTips = (ImageView) dialog.findViewById(R.id.dialog_add_sv_excel_pic);
        Button btnOK = (Button) dialog.findViewById(R.id.dialog_add_sv_excel_btnOK);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooser();
                dialog.cancel();
            }
        });

        dialog.show();

    }

    private void fileChooser(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("test", "File Uri: " + uri.toString());
                    // Get the path
                    path = uri.getPath();
                    String fName = new File(path).getName();
                    Log.d("test", "File Name: " + fName);
                    String extension = fName.substring(fName.lastIndexOf("."));
//                    if(extensionz
                    Toast.makeText(this, "Ban da chon file: "+extension, Toast.LENGTH_SHORT).show();
                    Log.d("Chon file",""+extension);
                    Log.d("de xem ",""+LoginAct.checkUser);

                    if(extension.equals(".xls"))
                    {
                        try {
                            readAccountDataa();
                            Log.d("af","hello minaa");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Log.d("cawq","clgt");
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "Bạn cần chọn file có định dạng .xls", Toast.LENGTH_SHORT).show();
                    }

//                    try {
//                        readAccountData();
//                        Log.d("af","hello minaa");
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                        Log.d("cawq","clgt");
//                    }
//                    ("test", "File Path: " + file);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
//            case FILE_SELECT_CODE:
//                if (resultCode == RESULT_OK) {
//                    // Get the Uri of the selected file
//                    Uri uri = data.getData();
//                    Log.d("test", "File Uri: " + uri.toString());
//                    // Get the path
////                    File file = new File(uri.getPath());
//                    path = uri.getPath();
//                    Toast.makeText(this, "File Path "+path, Toast.LENGTH_SHORT).show();
////                    ("test", "File Path: " + file);
//                    // Get the file instance
//                    // File file = new File(path);
//                    // Initiate the upload
//                }
//                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void readAccountDataa() throws FileNotFoundException {
//        InputStream is = getResources().openRawResource(R.raw.test);
//        FileInputStream is = openFileInput("file:///storage/emulated/0/newfolder/data.xls");
        Log.d("aaa","hello mina");
        FileInputStream p_file = new FileInputStream(new File(path));
//        BufferedReader reader = new BufferedReader(new InputStreamReader(p_file));
        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");
            Workbook m_workBook = Workbook.getWorkbook(p_file,ws);
            //p_sheetNo is excel sheet no which u want to read
            Sheet sheet = m_workBook.getSheet(0);

            if(sheet.getColumns()!=2 && sheet.getCell(0,0).getContents().equals("Mã sinh viên")
                    && sheet.getCell(1,0).getContents().equals("Họ tên"))
            {
                Log.d("k dung mau ",""+sheet.getColumns()+", "+sheet.getCell(0,0).getContents()+", "+sheet.getCell(1,0).getContents());
                Toast.makeText(this, "File không đúng mẫu", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                Log.d("dung mau roi",""+sheet.getColumns()+", "+sheet.getCell(0,0).getContents()+", "+sheet.getCell(1,0).getContents());

                for (int j = 1; j < sheet.getRows(); j++) {
                    final User user = new User();
                    String msv = sheet.getCell(0, j).getContents();
                    String hoTen = sheet.getCell(1, j).getContents();

                    Log.d("maa sv",""+msv+","+hoTen);

//                    for (int k = 0; k < sheet.getColumns(); k++) {
//                        Cell column1_cell = sheet.getCell(k, j);
//                        if(column1_cell.getContents().equals("")) break;
//                        if(k==0)
//                        {
//                            user.setUserName(column1_cell.getContents());
//                            user.setPassWord(column1_cell.getContents());
//                        }
//                        Log.d("Doc file " + j, "" + column1_cell.getContents());
//                        if (column1_cell.getContents().equals("can1"))
//                            Log.d("Hi", "Mạnh Cần");
//                        if (column1_cell.getContents().equals("Mạnh Cần"))
//                            Log.d("Hi", "gosu");
//                    }
//                continue;


//                Log.d("Test","so 2: "+tokens[1].indexOf(tokens[1]));

//                    userList.add(user);

                    Log.d("Test", "Da tao: " + user.toString());

//                    RequestQueue requestQueue = Volley.newRequestQueue(this);
//                    Log.d("b", "cac");
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_SV, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            Log.d("b", "cacd");
//                            if (response.trim().equals("true")) {
////                    Toast.makeText(LoginAct.this, "Thanh cong", Toast.LENGTH_SHORT).show();
//                                Log.d("b", "thanh cong");
//                            } else {
////                    Toast.makeText(LoginAct.this, "That bai", Toast.LENGTH_SHORT).show();
//                                Log.d("b", "that bai");
//                            }
//                        }
//                    },
//                            new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    Log.d("A", "Loi: " + error.toString());
//                                }
//                            }
//                    ) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            Map<String, String> params = new HashMap<>();
//                            params.put("username", user.getUserName());
//                            params.put("hoten", user.getHoTen());
//                            params.put("idlophoc", String.valueOf(idLopHoc));
//
//                            return params;
//                        }
//                    };
//                    requestQueue.add(stringRequest);

                }
                GetSVLH(URL_GET_DSSV, idLopHoc);
            }
            } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (BiffException biffException) {
            biffException.printStackTrace();
        }
    }


    private void onReadClick() throws IOException {
        Log.d("Ghi","reading XLSX file from resources");
//        FileInputStream is = new FileInputStream(new File(path));getResources().openRawResource(R.raw.testz);
        InputStream p_file = getResources().openRawResource(R.raw.book1);
        try
        {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");
            Workbook m_workBook = Workbook.getWorkbook(p_file,ws);
            //p_sheetNo is excel sheet no which u want to read
            Sheet sheet = m_workBook.getSheet(0);

            for (int j = 1; j < sheet.getRows(); j++)
            {
                for(int k=0 ;k<sheet.getColumns();k++)
                {
                    Cell column1_cell = sheet.getCell(k, j);
                    Log.d("Doc file "+j,"" + column1_cell.getContents());
                    if(column1_cell.getContents().equals("can1"))
                        Log.d("Hi","Mạnh Cần");
                    if(column1_cell.getContents().equals("Mạnh Cần"))
                        Log.d("Hi","gosu");
                    if(column1_cell.getContents().equals(""))
                        Log.d("Hi","zzz");
                    if(column1_cell.getContents().equals(" "))
                        Log.d("Hi","rong");
                    if(column1_cell.getContents().isEmpty())
                        Log.d("Hi","rong vvl");
                }
//                continue;
            }
        }
        catch (BiffException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
//            case R.id.teacher_classroom_dssv:
//                try {
//                    onReadClick();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
////                dialogDSSV();
//                Toast.makeText(this, ""+diemDanhArrayList.size()+", "+soLuong+", "+soBuoi, Toast.LENGTH_SHORT).show();
//                break;
            case R.id.teacher_classroom_addsv:
                dialogAddSV();
                break;
            case R.id.teacher_classroom_addsvexcel:
                dialogAddSVExcel();
                break;
            case R.id.teacher_classroom_exportexcel:
                exportCSV();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}