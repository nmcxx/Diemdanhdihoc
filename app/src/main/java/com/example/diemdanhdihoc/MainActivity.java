package com.example.diemdanhdihoc;
//package com.google.zxing.integration.android.IntentIntegrator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.QRCode;

public class MainActivity extends AppCompatActivity {

    private Button btnGQR, btnScan;
    private EditText edtKey;
    private ImageView imgvQR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtKey = findViewById(R.id.txtKey);
        imgvQR = findViewById(R.id.imgvQR);
        btnGQR = findViewById(R.id.btnGQR);
        btnScan = findViewById(R.id.btnScan);

        btnGQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                String generateKey = GenerateKey();
                try {

                    BitMatrix bitMatrix = qrCodeWriter.encode(generateKey, BarcodeFormat.QR_CODE, 200, 200);
                    Bitmap bitmap = Bitmap.createBitmap(200,200,Bitmap.Config.RGB_565);
                    edtKey.setText(generateKey);

                    for(int i=0;i<200;i++)
                    {
                        for(int k=0;k<200;k++)
                        {
                            bitmap.setPixel(i,k,bitMatrix.get(i,k)? Color.BLACK : Color.WHITE);
                        }
                    }

                    imgvQR.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanCode();
            }
        });
    }

    private void ScanCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(ScanCapture.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result!=null)
        {
            if(result.getContents()!=null)
            {
                edtKey.setText(result.getContents());
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

    //    public void QRButton(View view)
//    {
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        try {
//            BitMatrix bitMatrix = qrCodeWriter.encode(edtKey.getText().toString(), BarcodeFormat.QR_CODE, 200, 200);
//            Bitmap bitmap = Bitmap.createBitmap(200,200,Bitmap.Config.RGB_565);
//
//            for(int i=0;i<200;i++)
//            {
//                for(int k=0;k<200;k++)
//                {
//                    bitmap.setPixel(i,k,bitMatrix.get(i,k)? Color.BLACK : Color.WHITE);
//                }
//            }
//
//            imgvQR.setImageBitmap(bitmap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


}