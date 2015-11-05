package com.wafflestudio.siksha.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.Animations;
import com.wafflestudio.siksha.util.Fonts;

/**
 * Created by Gyu Kang on 2015-11-05.
 */
public class SplashDialog extends Dialog {
    private ImageView progressView;

    public SplashDialog(Context context) {
        super(context, R.style.SplashDialog);
        setContentView(R.layout.splash_dialog);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        initialize();
    }

    private void initialize() {
        progressView = (ImageView) findViewById(R.id.progress_view);
        TextView explanationView = (TextView) findViewById(R.id.splash_dialog_explanation_view);
        TextView titleView = (TextView) findViewById(R.id.splash_dialog_title_view);
        TextView messageView = (TextView) findViewById(R.id.splash_dialog_message_view);

        explanationView.setTypeface(Fonts.fontBMJua);
        titleView.setTypeface(Fonts.fontBMJua);
        messageView.setTypeface(Fonts.fontAPAritaDotumMedium);
    }

    public void start() {
        Animations.rotate(progressView, 0.0f, 360.0f, 1000, true);
        show();
    }

    public void quit() {
        Animations.clear(progressView);
        dismiss();
    }
}
