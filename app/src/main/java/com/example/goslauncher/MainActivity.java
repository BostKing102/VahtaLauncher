package com.example.goslauncher;

import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity{

    private Intent MenuActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar();

        super.onCreate(savedInstanceState);
        MenuActivity = new Intent(this, MenuActivity.class);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        updateDateAndTime();
        startUpdateInterface();
        setFullScreenMode();

    }

    private void startUpdateInterface(){
        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    while (!isInterrupted()){
                        Thread.sleep(1000); // todo fix warning
                        runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void run() {

                                updateDateAndTime();
                                updateSimInfo();
                                System.out.println(getBatteryProcent());
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        t.start();
    }

    public void setFullScreenMode () {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
    }

    public void updateSimInfo() {
        setSimOperatorName();
        setSimCardStatusVisible();
    }

    public void setSimOperatorName() {
        TextView simOneOperatorNameActivity = findViewById(R.id.simOneOperName);

        simOneOperatorNameActivity.setText(getOperatorName());
    }

    public String getOperatorName () {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        String operatorName = telephonyManager.getSimOperatorName();

        if (operatorName.isEmpty()) {
            operatorName = "Ожидание...";
        }

        return (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_READY) ? "Вставьте SIM" : operatorName;
    }

    public void setSimCardStatusVisible () {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        boolean isSimReady = telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
        ImageView simCardStatusImg = findViewById(R.id.noSimOneIcon);

        if (isSimReady) {
            simCardStatusImg.setVisibility(ImageView.INVISIBLE);
        } else {
            simCardStatusImg.setVisibility(ImageView.VISIBLE);
        }
    }

    public void updateDateAndTime() {
        TextView clockTextInActivity = findViewById(R.id.mainMenuClock);
        TextView dateTextInActivity = findViewById(R.id.mainMenuDate);

        clockTextInActivity.setText(getNowFormatedTime());
        dateTextInActivity.setText(getNowFormatedDate());
    }

    public static String getNowFormatedDate() {
        LocalDate nowDate = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy EE");

        return nowDate.format(format);
    }

    public static String getNowFormatedTime() {

        LocalTime nowTime = LocalTime.now();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH : mm");

        return nowTime.format(timeFormat);

    }

    public int getBatteryProcent() {
        BatteryManager bm = (BatteryManager) getApplicationContext().getSystemService(BATTERY_SERVICE);

        return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            startActivity(MenuActivity);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}