package com.example.irmin.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;

public class RegisterActivity extends AppCompatActivity {

    private ArrayAdapter adapter, adapter1, adapter2, adapter3;
    private Spinner spinner;
    private String userID;
    private String userPassword;
    private String userPassword_check;
    private String category;
    private String userName;
    private String userTel;
    private String userAreaname;
    private String userAddress;
    private String userImg;
    private AlertDialog dialog;
    private boolean validate = false;

    static final int gallery=2002;
    Button gallerypopBtn;
    static Context mContext;
    ImageView image;
    String temp="";
    static ProgressDialog pd;
    private static final int REQUEST_CAMERA = 1;

    private void init() {
        image=(ImageView)findViewById(R.id.image);
        mContext=this;
        gallerypopBtn=(Button)findViewById(R.id.gallerypopBtn);
        gallerypopBtn.setOnClickListener((View.OnClickListener) this);
        permission_init();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Spinner spin1 = (Spinner) findViewById(R.id.address1);
        final Spinner spin2 = (Spinner) findViewById(R.id.address2);
        final Spinner spin3 = (Spinner) findViewById(R.id.address3);

        spinner = (Spinner) findViewById(R.id.category);
        adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        adapter1 = ArrayAdapter.createFromResource(this, R.array.areaname, android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adapter1);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter1.getItem(i).equals("제주")) {
                    adapter2 = ArrayAdapter.createFromResource(RegisterActivity.this, R.array.제주, android.R.layout.simple_spinner_dropdown_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adapter2);
                } else if (adapter1.getItem(i).equals("서울")) {
                    adapter2 = ArrayAdapter.createFromResource(RegisterActivity.this, R.array.서울, android.R.layout.simple_spinner_dropdown_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adapter2);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter2.getItem(i).equals("제주시")) {
                    adapter3 = ArrayAdapter.createFromResource(RegisterActivity.this, R.array.제주시, android.R.layout.simple_spinner_dropdown_item);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin3.setAdapter(adapter3);
                } else if (adapter2.getItem(i).equals("서귀포시")) {
                    adapter3 = ArrayAdapter.createFromResource(RegisterActivity.this, R.array.서귀포시, android.R.layout.simple_spinner_dropdown_item);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin3.setAdapter(adapter3);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final EditText passwordText_check = (EditText) findViewById(R.id.passwordText_check);
        final EditText userNameText = (EditText) findViewById(R.id.userNameText);
        final EditText phoneText = (EditText) findViewById(R.id.phoneText);
        final EditText userAddressText = (EditText) findViewById(R.id.userAddressText);
        final EditText userImgText = (EditText) findViewById(R.id.userImgText);

        final Button validateButton = (Button) findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                String userID = idText.getText().toString();
                if (validate) {
                    return;
                }
                if (userID.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디는 빈 칸일 수 없습니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                idText.setEnabled(false);
                                validate = true;
                                idText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                validateButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                String userID = idText.getText().toString();
                String userPassword = passwordText.getText().toString();
                String userPassword_check = passwordText_check.getText().toString();
                String category = spinner.getSelectedItem().toString();
                String userArea1 = spin1.getSelectedItem().toString();
                String userArea2 = spin2.getSelectedItem().toString();
                String userArea3 = spin3.getSelectedItem().toString();
                String userName = userNameText.getText().toString();
                String userTel = phoneText.getText().toString();
                String userAddress = userAddressText.getText().toString();
                String userImg = userImgText.getText().toString();
                String userArea = userArea1+" "+userArea2+" "+userArea3;

                if (!userPassword.equals(userPassword_check)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("비밀번호를 확인하세요.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                if (!validate) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("먼저 중복 체크를 해주세요.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                if (userID.equals("") || userPassword.equals("") || category.equals("") || userName.equals("") || userTel.equals("") || userArea1.equals("") || userArea2.equals("") || userArea3.equals("") || userAddress.equals("") || userImg.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("빈 칸 없이 입력해주세요.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("회원 등록에 성공했습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("회원 등록에 실패했습니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, category, userName, userTel, userArea, userAddress, userImg, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });

        Button getButton = (Button)findViewById(R.id.gallerypopBtn);
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/-");
                startActivityForResult(intent, gallery);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    void permission_init(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {	//권한 거절
            // Request missing location permission.
            // Check Permissions Now

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {
                // 이전에 권한거절
                // Toast.makeText(this,getString(R.string.limit),Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(
                        this, new String[]{android.Manifest.permission.CAMERA},
                        REQUEST_CAMERA);
            }

        } else {	//권한승인
            //Log.e("onConnected","else");
            // mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent intent=new Intent();
        Bitmap bm;
        if(resultCode==RESULT_OK){
            switch(requestCode){
                case gallery:
                    try {
                        bm = MediaStore.Images.Media.getBitmap( getContentResolver(), data.getData());
                        bm=resize(bm);
                        Bitmap selPhoto = null;
                        selPhoto=(Bitmap) bm;
                        image.setImageBitmap(selPhoto);//썸네일
                        BitMapToString(selPhoto);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }catch(OutOfMemoryError e){
                        Toast.makeText(getApplicationContext(), "이미지 용량이 너무 큽니다.", Toast.LENGTH_SHORT).show();
                    }
                    setResult(RESULT_OK, intent);
                    break;

                default:
                    setResult(RESULT_CANCELED, intent);
                    break;

            }
        }else{
            setResult(RESULT_CANCELED, intent);
        }
    }

    @SuppressLint("NewApi")
    private Bitmap resize(Bitmap bm){

        Configuration config=getResources().getConfiguration();
		/*if(config.smallestScreenWidthDp>=800)
			bm = Bitmap.createScaledBitmap(bm, 400, 240, true);//이미지 크기로 인해 용량초과
		else */if(config.smallestScreenWidthDp>=600)
            bm = Bitmap.createScaledBitmap(bm, 300, 180, true);
        else if(config.smallestScreenWidthDp>=400)
            bm = Bitmap.createScaledBitmap(bm, 200, 120, true);
        else if(config.smallestScreenWidthDp>=360)
            bm = Bitmap.createScaledBitmap(bm, 180, 108, true);
        else
            bm = Bitmap.createScaledBitmap(bm, 160, 96, true);
        return bm;
    }

    /**
     * bitmap을 string으로
     * @param bitmap
     * @return
     */
    public void BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);	//bitmap compress
        byte [] arr=baos.toByteArray();
        String image= Base64.encodeToString(arr, Base64.DEFAULT);
        try{
            temp= URLEncoder.encode(image,"utf-8");
        }catch (Exception e){
            Log.e("exception",e.toString());
        }
    }

    /**
     * string을 bitmap으로
     * @param image
     * @return
     */
    public static Bitmap StringToBitMap(String image){
        Log.e("StringToBitMap","StringToBitMap");
        try{
            byte [] encodeByte=Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            Log.e("StringToBitMap","good");
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
