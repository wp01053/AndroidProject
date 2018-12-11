package com.study.android.androidproject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NfcActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private static String tagNum = null;
    private TextView tagDesc;
    private TextView ID;
    private static final String TAG = "lecture";
    String userId;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        tagDesc = findViewById(R.id.tagDesc);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = pendingIntent.getActivity(this, 0, intent, 0);
//        Intent intent1 = getIntent();
//        String id2 = intent1.getExtras().getString("loginid"); /*String형*/
//
//        System.out.println(id2);
//        /*테스트 결과 id 값은 확실히 들어옴 */
//        /*이제 그 값을 nav_Header_main 에 있는 textview 에 뿌려주기만 하면 되는데 ...*/
//        Log.d(TAG, id2 + "뜨냐");
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.activity_nfc, null);
//
//
//        ID = view.findViewById(R.id.ID);
//
//        ID.setText(id2 + "님 환영합니다.");

        getPreferences();
    }

    private void getPreferences() {

        //oncreate 부분에서 getPreferences() 메소드를 불러와서
        //mainActivity d에서 저장한 userId 정보를 getString 해서 쓸 수 있따. ..
        //SharedPreferences pref = getSharedPreferences("ID", Activity.MODE_PRIVATE);
        //이 구문에서 id 값이 t생성한 xml 값과 일치해야 사용 할 수 있다.. 샛기야
        SharedPreferences pref = getSharedPreferences("ID", Activity.MODE_PRIVATE);
        userId = pref.getString("userId", ""); //key, value(defaults)
        ID = findViewById(R.id.ID);
        Log.d(TAG, "..?"+userId);
        Log.d(TAG, "제발좀 떠라.." + ID);
        ID.setText(userId+"님 환영합니다.");



    }

    @Override
    protected void onPause() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter
                    .enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }




    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            byte[] tagId = tag.getId();
            Date dt = new Date();
            //출석체크가 완료되고 난 후 시간을 비교해서
            //지각인지 아닌지 확인하는 구문
             SimpleDateFormat dateFormat = new  SimpleDateFormat("HH:mm:ss");
            Date date1 = null;
            try {
                date1 = dateFormat.parse("12:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date date2 = null;
            try {
                date2 = dateFormat.parse(dateFormat.format(dt).toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String result = String.valueOf(date2.after(date1));
            Log.d(TAG, result+"첫번째 result 값");
            if(result == "true"){
                result="지각입니다.";

            }
            if(result == "false"){
                result="정상적으로 출석";
            }
            Log.d(TAG, date1 + "date1 값");
            Log.d(TAG, date2+"date2값 ");
            Log.d(TAG, result+"결과값 출력");
//sharedPreferences pref 에 날짜 지금 현재 시간 정보 저장 완료

//            Date dt = new Date();
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
//            SimpleDateFormat sdf = new SimpleDateFormat("hh");
//            Log.d("DATE",sdf.format(dt).toString()+"시");
//텍스트뷰에 현재 시간과 날짜를 출력
            tagDesc.setText(currentDateTimeString);
            tagDesc.setText(currentDateTimeString+"  "+ "\n출석체크 완료"+"\n"+result);

            Log.d(TAG,"제대로 된 현재시간"+ currentDateTimeString);

            SharedPreferences pref = getSharedPreferences("ID", Activity.MODE_PRIVATE);

            editor = pref.edit();
            editor.putString("datee",currentDateTimeString);
            Log.d(TAG , "NfcActivity SharedPreferences에 날짜 시간 저장되는지 확인 " +  currentDateTimeString);
            editor.commit();


            tagNum = toHexString(tagId);
            if (toHexString(tagId).equals("046B95EA2F4D80")) {
                Toast.makeText(NfcActivity.this, userId + "님 " + "출석체크 완료.", Toast.LENGTH_SHORT).show();

               if (date2.after((date1))==true){
                   Toast.makeText(NfcActivity.this, "지각하셧네용", Toast.LENGTH_SHORT).show();
               }
               else if (date2.after((date1))==false){

                    Toast.makeText(NfcActivity.this, "정상적으로 출석", Toast.LENGTH_SHORT).show();
               }

//                System.out.println(date2.after(date1));
//                //date2 가 date1 보다 뒤의 시간일때 이후의 시간일 때 true 아니면 false
//                //현재 date2 는 13 : 32분 date1은 13:36분 그래서 false
//                System.out.println(date1.after(date2));
//                //여기서는 true가 나와야함 찍어봤을때 제대로 나옴 ㅇㅇ

            }
        }

    }

    public static final String CHARS = "0123456789ABCDEF";

    public static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; ++i) {
            sb.append(CHARS.charAt((data[i] >> 4) & 0x0F)).append(
                    CHARS.charAt(data[i] & 0x0F));

        }
        return sb.toString();
    }

}
