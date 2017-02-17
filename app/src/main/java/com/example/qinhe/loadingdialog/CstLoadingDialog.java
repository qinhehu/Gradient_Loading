package com.example.qinhe.loadingdialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

public class CstLoadingDialog extends Dialog {

    private ImageView image;

    public CstLoadingDialog(Context context) {
        super(context);
        setCanceledOnTouchOutside(false);
    }

    public CstLoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog_layout);
        image = (ImageView)findViewById(R.id.img_Loading_Dialog);
        image.setImageDrawable(new CircleLoadingDrawable(getContext()));

        setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getRepeatCount() == 0) {
                   return true;
                }
                return false;
            }
        });
    }
}
