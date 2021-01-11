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
    private TextView txtTenLop, txtSoBuoi, txtSoLuong, txtNgay;
    private Switch swDiemDanhThuCong;
    private SearchView searchViewDSDD;

    private final LoadingDialog loadingDialog = new LoadingDialog(TeacherClassroomActivity.this);
    private final String URL_GET_DSSV = "https://vdili.000webhostapp.com/svlh.php";
    private final String URL_ADD_SV = "https://vdili.000webhostapp.com/themsv.php";
    private final String URL_GET_DIEMDANH="https://vdili.000webhostapp.com/diemdanh.php";

    private static final int FILE_SELECT_CODE = 0;

    private ArrayList<SinhVien> sinhVienArrayList;
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

//    System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
//    System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
//    System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");

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

//        Toast.makeText(TeacherClassroomActivity.this, "Hello World", Toast.LENGTH_SHORT).show();
        GetSVLH(URL_GET_DSSV, idLopHoc);
        getDiemDanh(LoginAct.checkUser, idLopHoc, 0);
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



    private void themSinhVien(final String username, final String hoTen, final int idLop)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_SV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("true")) {
                    Toast.makeText(TeacherClassroomActivity.this, "Them sinh vien " + username + " thanh cong", Toast.LENGTH_SHORT).show();
                    GetSVLH(URL_GET_DSSV,  idLopHoc);
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

    private void GetData(String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue (this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Toast.makeText(TeacherMainAct.this, response.toString(), Toast.LENGTH_SHORT).show();
                        sinhVienArrayList.clear();
                        for(int i=0;i<response.length();i++)
                        {
                            try{
                                JSONObject object = response.getJSONObject(i);
                                Log.d("a",""+i);
                                sinhVienArrayList.add(new SinhVien(
                                        object.getString("Username"),
                                        object.getString("Hoten")
                                ));
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TeacherClassroomActivity.this, "Loi", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
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
//            Label label = new Label(0, 2, "SECOND");
//            Label label1 = new Label(0,1,"first");
//            Label label0 = new Label(0,0,"HEADING");
//            Label label3 = new Label(1,0,"Heading2");
//            Label label4 = new Label(1,1,String.valueOf(a));
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
//                sheet.addCell(new Label(2,0,));
//                sheet.addCell(label);
//                sheet.addCell(label1);
//                sheet.addCell(label0);
//                sheet.addCell(label4);
//                sheet.addCell(label3);
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
//        StringBuilder data = new StringBuilder();
//        data.append("Test,Test2,Test3");
//        data.append("\n,,");
//        for(int i=0;i<5;i++)
//        {
//            data.append("\n\n"+String.valueOf(i)+","+String.valueOf(i*i));
//        }
//
//        try{
//            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
//            out.write(data.toString().getBytes());
//            out.close();
//
//            Context context = getApplicationContext();
//            File file = new File(getFilesDir(), "data.csv");
//            file.createNewFile();
//            Toast.makeText(this, "Xuat tep thanh cong\n"+getFilesDir(), Toast.LENGTH_SHORT).show();
////            Uri path = FileProvider.getUriForFile(context,"com.example.diemdanhdihoc.fileprovider",file);
////            Intent fileIntent = new Intent(Intent.ACTION_SEND);
////            fileIntent.setType("text/csv");
////            fileIntent.putExtra(Intent.EXTRA_SUBJECT,"Data");
////            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
////            fileIntent.putExtra(Intent.EXTRA_STREAM,path);
////            startActivity(Intent.createChooser(fileIntent,"Send mail"));
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_teacher_classroom, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void dialogDSSV(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // xoa title
        dialog.setContentView(R.layout.dialog_thongtin);

        SearchView timKiemSV = (SearchView) dialog.findViewById(R.id.dialog_dssv_timkiem);
        ListView DSSV = (ListView) dialog.findViewById(R.id.dialog_dssv_listsv);

        dialog.show();

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
                    themSinhVien(maSV,hoTen, idLopHoc);
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
//                    if(extension)
                    Toast.makeText(this, "Ban da chon file: "+extension, Toast.LENGTH_SHORT).show();
                    Log.d("Chon file",""+extension);

                    if(extension.equals(".csv"))
                    {
                        try {
                            readAccountData();
                            Log.d("af","hello minaa");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Log.d("cawq","clgt");
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "Bạn cần chọn file có định dạng .csv", Toast.LENGTH_SHORT).show();
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

    private void readAccountData() throws FileNotFoundException {
//        InputStream is = getResources().openRawResource(R.raw.test);
//        FileInputStream is = openFileInput("file:///storage/emulated/0/newfolder/data.xls");
        Log.d("aaa","hello mina");
        FileInputStream is = new FileInputStream(new File(path));
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";
        try {
            String checkDinhDang = reader.readLine();
            Log.d("Dinh dang",""+ checkDinhDang+" - "+ checkDinhDang.equals("Mã sinh viên;Họ tên"));
            if(!checkDinhDang.equals("Mã sinh viên;Họ tên"))
            {
                Toast.makeText(this, "File không đúng mẫu", Toast.LENGTH_SHORT).show();
            }
            else {

//            }
//            Log.d("Test", "Dong dau " + reader.readLine());
//            reader.readLine();
                while ((line = reader.readLine()) != null) {
                    Log.d("Test", "Dong thu " + line);
                    final String[] tokens = line.split(";");


                    final User user = new User();
                    Log.d("Test", "so 1: " + tokens.length);

//                Log.d("Test","so 2: "+tokens[1].indexOf(tokens[1]));

                    if (tokens.length > 2) {
                        Log.d("Loi dinh dang", "Loi dinh dang");
                    }

                    if (tokens.length == 2) {
                        if (tokens[0] != "") {
                            Log.d("Test", "so 1: " + tokens[0].length());
                            user.setUserName(tokens[0]);
                            // Log.d("Test","so 1: "+tokens[0].length());
                        } else {
                            user.setUserName("");
                        }
                        //Log.d("Test","so 2: "+tokens[1].length());
                        if (tokens[1] != "") {
                            Log.d("Test", "so 2: " + tokens[1].length());
                            user.setHoTen(tokens[1]);
                        } else {
                            user.setHoTen("");
                        }
                    } else if (tokens.length == 1) {
                        if (tokens[0] != "") {
                            user.setUserName(tokens[0]);
                            user.setHoTen("");
                            Log.d("Test", "so 1: " + tokens[0].length());
                        } else {
                            user.setUserName("");
                            user.setHoTen("");
                        }
                    } else if (tokens.length == 0) {
                        user.setUserName("");
                        user.setHoTen("");
                    }

                    userList.add(user);
//                ArrayAdapter<User> arrayAdapter = new ArrayAdapter<User>(this,android.R.layout.simple_list_item_1,userList);
//                lvAcc.setAdapter(arrayAdapter);
//                arrayAdapter.notifyDataSetChanged();

                    Log.d("Test", "Da tao: " + user.toString());

                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    Log.d("b", "cac");
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_SV, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("b", "cacd");
                            if (response.trim().equals("true")) {
//                    Toast.makeText(LoginAct.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                                Log.d("b", "thanh cong");
                            } else {
//                    Toast.makeText(LoginAct.this, "That bai", Toast.LENGTH_SHORT).show();
                                Log.d("b", "that bai");
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("A", "Loi: " + error.toString());
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("username", user.getUserName());
                            params.put("hoten", user.getHoTen());
                            params.put("idlophoc", String.valueOf(idLopHoc));

                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);

                }
                GetSVLH(URL_GET_DSSV, idLopHoc);
            }
        } catch (IOException e) {
            Log.d("Loi", "Loi doc dong " + line, e);
            e.printStackTrace();
        }

    }

//    public static String getPath(Context context, Uri uri) throws URISyntaxException {
//        if ("content".equalsIgnoreCase(uri.getScheme())) {
//            String[] projection = { "_data" };
//            Cursor cursor = null;
//
//            try {
//                cursor = context.getContentResolver().query(uri, projection, null, null, null);
//                int column_index = cursor.getColumnIndexOrThrow("_data");
//                if (cursor.moveToFirst()) {
//                    return cursor.getString(column_index);
//                }
//            } catch (Exception e) {
//                // Eat it
//            }
//        }
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//    }

    //

    public void onReadClick() throws IOException {
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
//        InputStream stream = getResources().openRawResource(R.raw.testz);
//        InputStream ExcelFileToRead = getResources().openRawResource(R.raw.testz);
//        XSSFWorkbook  wb = new XSSFWorkbook(ExcelFileToRead);
//
//        XSSFWorkbook test = new XSSFWorkbook();
//
//        XSSFSheet sheet = wb.getSheetAt(0);
//        XSSFRow row;
//        XSSFCell cell;
//
//        Iterator rows = sheet.rowIterator();
//
//        while (rows.hasNext())
//        {
//            row=(XSSFRow) rows.next();
//            Iterator cells = row.cellIterator();
//            while (cells.hasNext())
//            {
//                cell=(XSSFCell) cells.next();
//
//                if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING)
//                {
//                    Log.d("Ghi file",""+cell.getStringCellValue()+" ");
//                }
//                else if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC)
//                {
//                    Log.d("Ghi file",""+cell.getNumericCellValue()+" ");
//                }
//                else
//                {
//                    //U Can Handel Boolean, Formula, Errors
//                }
//            }
//            System.out.println();
//        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.teacher_classroom_dssv:
                try {
                    onReadClick();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                dialogDSSV();
                Toast.makeText(this, ""+diemDanhArrayList.size()+", "+soLuong+", "+soBuoi, Toast.LENGTH_SHORT).show();
                break;
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