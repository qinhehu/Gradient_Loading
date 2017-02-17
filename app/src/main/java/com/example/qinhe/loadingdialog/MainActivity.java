package com.example.qinhe.loadingdialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CstLoadingDialog cstLoadingDialog = new CstLoadingDialog(this);
        cstLoadingDialog.show();
    }
}
