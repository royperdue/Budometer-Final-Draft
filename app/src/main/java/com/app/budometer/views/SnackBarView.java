package com.app.budometer.views;

import android.content.Context;
import androidx.annotation.StringRes;
import androidx.core.view.ViewCompat;

import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.budometer.R;
import com.app.budometer.util.BudometerConfig;


public class SnackBarView extends RelativeLayout {
    private TextView txtCaption;
    private Button btnAction;

    public SnackBarView(Context context) {
        this(context, null);
    }

    public SnackBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SnackBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.view_snackbar, this);
        if (isInEditMode()) {
            return;
        }

        int height = getContext().getResources().getDimensionPixelSize(R.dimen.pad80dp);
        ViewCompat.setTranslationY(this, height);
        ViewCompat.setAlpha(this, .6f);

        int padding = getContext().getResources().getDimensionPixelSize(R.dimen.pad16dp);
        setPadding(padding, 0, padding, 0);

        txtCaption = findViewById(R.id.snackBar_text_bottom_caption);
        btnAction = findViewById(R.id.snackBar_button_action);
    }

    public void setText(@StringRes int textResId) {
        txtCaption.setText(textResId);
    }

    public void setOnActionClickListener(@StringRes int textId, final OnClickListener onClickListener) {
        if (textId == 0) {
            textId = R.string.ok;
        }

        btnAction.setText(textId);
        btnAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                hide(new Runnable() {
                    @Override
                    public void run() {
                        onClickListener.onClick(v);
                    }
                });
            }
        });
    }

    public void show(@StringRes int textResId, OnClickListener onClickListener) {
        setText(textResId);
        setOnActionClickListener(0, onClickListener);

        ViewCompat.animate(this)
                .translationY(0f)
                .setDuration(BudometerConfig.ANIM_DURATION)
                .setInterpolator(BudometerConfig.INTERPOLATOR)
                .alpha(1f);
    }

    public void hide() {
        hide(null);
    }

    private void hide(Runnable runnable) {
        ViewCompat.animate(this)
                .translationY(getHeight())
                .setDuration(BudometerConfig.ANIM_DURATION)
                .alpha(0.5f)
                .withEndAction(runnable);
    }
}
