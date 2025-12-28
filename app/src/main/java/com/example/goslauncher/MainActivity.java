package com.example.goslauncher;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SignalStrength;
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
    private Integer BATTERY_FRAME = 0;

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
                            @RequiresApi(api = Build.VERSION_CODES.P)
                            @Override
                            public void run() {

                                updateDateAndTime();
                                updateSimInfo();
                                updateBatteryIcon();
                                updateSignalStrenght();
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

    public int getBatteryIconForProcent(int Procent) {
        int icon;

        if (isBatteryCharging()) {
            icon = getBatteryChargingAnimFrameAndUpdateFrame();
        } else if (Procent >= 85) {
            icon = R.drawable.battery_level_5;
        } else if (Procent >= 70) {
            icon = R.drawable.battery_level_4;
        } else if (Procent >= 55) {
            icon = R.drawable.battery_level_3;
        } else if (Procent >= 40) {
            icon = R.drawable.battery_level_2;
        } else if (Procent >= 25) {
            icon = R.drawable.battery_level_1;
        } else if (Procent >= 10) {
            icon = R.drawable.battery_level_0;
        } else {
            icon = R.drawable.battery_level_alert;
        }

        return icon;
    }

    public int getBatteryProcent() {
        BatteryManager bm = (BatteryManager) getApplicationContext().getSystemService(BATTERY_SERVICE);

        return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    public int getBatteryChargingAnimFrameAndUpdateFrame () {
        updateBatteryFrame();

        int[] BATTERY_FRAMES = {
            R.drawable.battery_level_alert,
            R.drawable.battery_level_0,
            R.drawable.battery_level_1,
            R.drawable.battery_level_2,
            R.drawable.battery_level_3,
            R.drawable.battery_level_4,
            R.drawable.battery_level_5
        };

        return BATTERY_FRAMES[BATTERY_FRAME];
    }

    public void updateBatteryFrame () {
        BATTERY_FRAME++;

        if (BATTERY_FRAME > 6) {
            BATTERY_FRAME = 0;
        }
    }

    public void updateBatteryIcon() {
        ImageView view = findViewById(R.id.batteryIcon);
        int icon = getBatteryIconForProcent(getBatteryProcent());
        view.setImageResource(icon);
    }
    
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void updateSignalStrenght () {
        ImageView view = findViewById(R.id.signalStrenght);

        int icon = getSignalStrenghtIcon(getSignalStrenght());
        view.setImageResource(icon);
    }
    
    @RequiresApi(api = Build.VERSION_CODES.P)
    public int getSignalStrenghtIcon(int signalStrenght) {
     
        int[] SIGNAL_FRAMES = {
                R.drawable.signal_strenght_0,
                R.drawable.signal_strenght_1,
                R.drawable.signal_strenght_2,
                R.drawable.signal_strenght_3,
                R.drawable.signal_strenght_4,
        };
        
        return SIGNAL_FRAMES[signalStrenght];
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public int getSignalStrenght() {

        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        SignalStrength strenght = manager.getSignalStrength();

        return strenght != null ? strenght.getLevel() : 0;
    }

    public Boolean isBatteryCharging() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);

        assert batteryStatus != null;
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
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