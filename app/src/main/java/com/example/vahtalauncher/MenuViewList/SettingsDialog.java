package com.example.vahtalauncher.MenuViewList;

import com.example.vahtalauncher.util.BaseMenuActivity;

import java.util.ArrayList;

public class SettingsDialog extends BaseMenuActivity {

    @Override
    protected String getMenuTitle() {
        return "Настройки";
    }

    @Override
    protected ArrayList<String> getMenuItems() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Настройки вызовов");
        list.add("Настройки телефона");
        list.add("Параметры экрана");
        list.add("Настройки безопасности");
        list.add("Dual SIM");
        list.add("Bluetooth");
        list.add("Восстановление настроек");
        list.add("Секрет");
        return list;
    }

    @Override
    protected void onMenuItemChosen(int index, String name) {
        switch (index) {
            case 0:
                //pass
                break;
            case 1:
                //pass
                break;
            case 2:
                //pass
                break;
        }
    }
}