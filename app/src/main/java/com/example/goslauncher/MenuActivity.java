package com.example.goslauncher;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuActivity extends AppCompatActivity {

    int MENU_SELECT = 4;
    int[] MENUS = new int[] {
            R.id.contactsMenu,
            R.id.messagesMenu,
            R.id.callsMenu,
            R.id.organaizerMenu,
            R.id.gameMenu,
            R.id.multimediaMenu,
            R.id.settingsMenu,
            R.id.profilesMenu,
            R.id.filesMenu
    };
    String[] MENUS_NAMES = new String[] {
            "Контакты",
            "Сообщения",
            "Звонки",
            "Органайзер",
            "Игры",
            "Мультимедия",
            "Настройки",
            "Профили звука",
            "Файлы"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        setFullScreenMode();
    }

    public void getSelectedMenuIconCoords() {
        ImageView selectedMenuView = findViewById(MENUS[MENU_SELECT]);
        int[] coords = new int[2];

        Handler handler = new Handler(getMainLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {
                selectedMenuView.post(() -> {
                    selectedMenuView.getLocationOnScreen(coords);

                    ImageView selectBox = findViewById(R.id.selectBox);

                    int newX = (int) (coords[0] + selectedMenuView.getWidth() / 2);
                    int newY = (int) (coords[1] + selectedMenuView.getHeight() / 2);

                    float targetX = newX - (float) selectBox.getWidth() / 2;
                    float targetY = newY - (float) selectBox.getHeight() / 2;

                    selectBox.animate()
                            .x(targetX)
                            .y(targetY)
                            .setDuration(100)
                            .start();
                });
            }
        });
    }

    public void setMenuToolBarName(String name) {
        TextView toolBarTextView = findViewById(R.id.toolBarText);

        toolBarTextView.setText(name);
    }

    public void setNextMenuSelect(int Steps) {

        for (int i = 0; i < Steps; i++) {
            MENU_SELECT = MENU_SELECT + 1;
            if (MENU_SELECT >= 9) {
                MENU_SELECT = 0;
            }
        }
        setMenuToolBarName(MENUS_NAMES[MENU_SELECT]);
        getSelectedMenuIconCoords();
    }

    public void setPervMenuSelect(int Steps) {

        for (int i = 0; i < Steps; i++) {
            MENU_SELECT = MENU_SELECT - 1;
            if (MENU_SELECT <= -1) {
                MENU_SELECT = 8;
            }
        }
        setMenuToolBarName(MENUS_NAMES[MENU_SELECT]);
        getSelectedMenuIconCoords();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            setNextMenuSelect(1);
        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            setPervMenuSelect(3);
        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            setPervMenuSelect(1);
        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            setNextMenuSelect(3);
        }



        return super.onKeyDown(keyCode, event);
    }


    public void setFullScreenMode () {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_menu);
    }
}