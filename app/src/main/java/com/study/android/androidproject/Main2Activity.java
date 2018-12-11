package com.study.android.androidproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Messenger;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.kakaonavi.KakaoNaviWebViewActivity;


public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static TextView ID;
    private static final String TAG = "lecture";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.SEND_SMS}, 1
            );
        }

        Intent intent = getIntent();
        String id2 = intent.getExtras().getString("loginid"); /*String형*/
        System.out.println(id2);
        /*테스트 결과 id 값은 확실히 들어옴 */
        /*이제 그 값을 nav_Header_main 에 있는 textview 에 뿌려주기만 하면 되는데 ...*/

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.nav_header_main, null);
        ID = view.findViewById(R.id.ID);

        /*이 부분이 mainactivity에서 데이터를 수신하는 역할을 한다. loginid를 textview id 에 저장 !*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View nav_header_view = navigationView.getHeaderView(0);
        TextView ID = nav_header_view.findViewById(R.id.ID);

        ID.setText(id2 + "님 환영합니다.");


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(Main2Activity.this, NfcActivity.class);
            // Handle the camera action

            startActivity(intent);


        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
            // Handle the camera action

            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(Main2Activity.this, Main4Activity.class);
            // Handle the camera action

            startActivity(intent);

        } else if (id == R.id.nav_manage) {
            // Handle the camera action
            Intent intent = new Intent(Main2Activity.this, SplashActivity.class);
            // Handle the camera action

            startActivity(intent);


        } else if (id == R.id.nav_share) {
            String url = "daummaps://open?page=routeSearch";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            // Handle the camera action

            startActivity(intent);


        } else if (id == R.id.nav_send) {
            Messenger messenger = new Messenger(getApplicationContext());
            messenger.sendMessageTo("[01035242947]", "[저 오늘 지각입니다.]");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class Messenger {
        private Context mContext;

        public Messenger(Context mContext) {
            this.mContext = mContext;
        }

        public void sendMessageTo(String phoneNum, String message) {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNum, null, message, null, null);

            Toast.makeText(mContext, "Message completed", Toast.LENGTH_SHORT).show();
        }
    }
}