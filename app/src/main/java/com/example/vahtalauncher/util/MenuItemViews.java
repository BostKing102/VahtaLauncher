package com.example.vahtalauncher.util;

import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MenuItemViews {
    ConstraintLayout itemView;
    TextView textView;

    public MenuItemViews(ConstraintLayout itemView, TextView textView) {
        this.itemView = itemView;
        this.textView = textView;
    }

    public ConstraintLayout getItemView() {
        return itemView;
    }

    public TextView getTextView() {
        return textView;
    }
}
