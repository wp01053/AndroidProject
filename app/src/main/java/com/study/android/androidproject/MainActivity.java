package com.study.android.androidproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Build.ID;

public class MainActivity extends Activity {
    private static final String TAG = "lecture";

    EditText userId, userPwd;
    Button loginBtn, joinBtn;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userId = (EditText) findViewById(R.id.userId);
        userPwd = (EditText) findViewById(R.id.userPwd);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        joinBtn = (Button) findViewById(R.id.joinBtn);
        loginBtn.setOnClickListener(btnListener);
        joinBtn.setOnClickListener(btnListener);


    }


    static class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.0.90:8081/sendDataToAndroid/data.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id=" + strings[0] + "&pwd=" + strings[1] + "&type=" + strings[2];
                osw.write(sendMsg);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }

    View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loginBtn: // 로그인 버튼 눌렀을 경우
                    String loginid = userId.getText().toString();
                    String loginpwd = userPwd.getText().toString();
                    try {


                        String result = new CustomTask().execute(loginid, loginpwd, "login").get();

                        if (result.equals("true")) {

                            Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
//                            Intent intent1 = new Intent(MainActivity.this, NfcActivity.class);
                            /* intent.putExtra("loginid", loginid); 이 부분이 main2activity로 데이터를 보내주는 역할*/
                            intent.putExtra("loginid", loginid); /*송신*/
//                            intent1.putExtra("loginid" , loginid);
                            startActivity(intent);
//                            startActivity(intent1);
                            SharedPreferences pref = getSharedPreferences("ID", Activity.MODE_PRIVATE);
                            //공용으로 쓰도록 저장 시키는 것


                            editor = pref.edit();
                            editor.putString("userId", userId.getText().toString());
                            Log.d(TAG, "MainActivity SharedPreferences에 아이디값 저장되는지 확인 " + userId.getText().toString());
//                            Date dt = new Date();
//                            Log.d(TAG ,dt.toString());
//
//                            SimpleDateFormat full_sdf = new SimpleDateFormat("hh:mm:ss");
//                            Log.d(TAG,full_sdf.format(dt).toString());
//
//                            SimpleDateFormat sdf = new SimpleDateFormat("hh");
//                            Log.d(TAG,sdf.format(dt).toString()+"시");
                            editor.commit();


                            finish();
                        } else if (result.equals("false")) {
                            Toast.makeText(MainActivity.this, "아이디or 비밀번호가 다름", Toast.LENGTH_LONG).show();
                            userId.setText("");
                            userPwd.setText("");

                        } else if (result.equals("noId")) {

                            Toast.makeText(MainActivity.this, "존재하지 않는 아이디", Toast.LENGTH_LONG).show();
                            userId.setText("");
                            userPwd.setText("");
                        }
                    } catch (Exception e) {
                    }
                    break;
                case R.id.joinBtn: // 회원가입
                    String joinid = userId.getText().toString();
                    String joinpwd = userPwd.getText().toString();
                    try {
                        String result = new CustomTask().execute(joinid, joinpwd, "join").get();
                        if (result.equals("id")) {

                            Toast.makeText(MainActivity.this, "존재하는 아이디입니다.", Toast.LENGTH_LONG).show();
                            userId.setText("");
                            userPwd.setText("");
                        }  if (result.equals("ok")) {

                            userId.setText("");
                            userPwd.setText("");
                            Toast.makeText(MainActivity.this, "회원가입을 축하합니다.", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                    }
                    break;
            }
        }
    };
}

