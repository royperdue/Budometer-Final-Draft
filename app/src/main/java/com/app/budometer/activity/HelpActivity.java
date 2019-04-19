package com.app.budometer.activity;


import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.app.budometer.R;
import com.app.budometer.adapter.HelpAdapter;
import com.app.budometer.views.CircleIndicatorView;
import com.melnykov.fab.FloatingActionButton;

import androidx.annotation.ColorInt;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;


public abstract class HelpActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private CircleIndicatorView circleIndicatorView;
    private ViewPager helpViewPager;
    private HelpAdapter helpAdapter;
    private ImageButton nextImageButton, backImageButton;
    private Button finishButton;
    private FrameLayout buttonsLayout;
    private FloatingActionButton fab;
    private View divider;
    private ArgbEvaluator evaluator;

    private boolean shouldDarkenButtonsLayout = false;
    private boolean shouldUseFloatingActionButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        setStatusBackgroundColor();
        hideActionBar();
        circleIndicatorView = findViewById(R.id.circle_indicator_view);
        nextImageButton = findViewById(R.id.nextImageButton);
        backImageButton = findViewById(R.id.backImageButton);
        finishButton = findViewById(R.id.finishButton);
        buttonsLayout = findViewById(R.id.buttons_layout);
        fab = findViewById(R.id.fab);
        divider = findViewById(R.id.divider);
        helpViewPager = findViewById(R.id.helpViewPager);
        helpViewPager.addOnPageChangeListener(this);
        nextImageButton.setOnClickListener(this);
        backImageButton.setOnClickListener(this);
        finishButton.setOnClickListener(this);
        fab.setOnClickListener(this);
        evaluator = new ArgbEvaluator();
    }

    public void setHelpPagesReady() {
        helpAdapter = new HelpAdapter(getSupportFragmentManager());
        helpViewPager.setAdapter(helpAdapter);
        circleIndicatorView.setPageIndicators(6);
        circleIndicatorView.setActiveIndicatorColor(R.color.primaryText);
        circleIndicatorView.setInactiveIndicatorColor(R.color.secondaryText);
    }

    public void setInactiveIndicatorColor(int color) {
        this.circleIndicatorView.setInactiveIndicatorColor(color);
    }

    public void setActiveIndicatorColor(int color) {
        this.circleIndicatorView.setActiveIndicatorColor(color);
    }

    public void shouldDarkenButtonsLayout(boolean shouldDarkenButtonsLayout) {
        this.shouldDarkenButtonsLayout = shouldDarkenButtonsLayout;
    }

    public void setDividerColor(@ColorInt int color) {
        if (!this.shouldDarkenButtonsLayout)
            this.divider.setBackgroundColor(color);
    }

    public void setDividerHeight(int heightInDp) {
        if (!this.shouldDarkenButtonsLayout)
            this.divider.getLayoutParams().height =
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightInDp,
                            getResources().getDisplayMetrics());
    }

    public void setDividerVisibility(int dividerVisibility) {
        this.divider.setVisibility(dividerVisibility);
    }

    public void setFinishButtonTitle(CharSequence title) {
        this.finishButton.setText(title);
    }

    public void setFinishButtonTitle(@StringRes int titleResId) {
        this.finishButton.setText(titleResId);
    }

    public void shouldUseFloatingActionButton(boolean shouldUseFloatingActionButton) {

        this.shouldUseFloatingActionButton = shouldUseFloatingActionButton;

        if (shouldUseFloatingActionButton) {
            this.fab.setVisibility(View.VISIBLE);
            this.setDividerVisibility(View.GONE);
            this.shouldDarkenButtonsLayout(false);
            this.finishButton.setVisibility(View.GONE);
            this.nextImageButton.setVisibility(View.GONE);
            this.nextImageButton.setFocusable(false);
            this.buttonsLayout.getLayoutParams().height =
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96,
                            getResources().getDisplayMetrics());
        }

    }

    private int darkenColor(@ColorInt int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.9f;
        return Color.HSVToColor(hsv);
    }

    public void setStatusBackgroundColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black_transparent));
        }
    }

    @Override
    public void onClick(View view) {
        int index = view.getId();
        boolean isInLastPage = helpViewPager.getCurrentItem() == helpAdapter.getCount() - 1;
        if (index == R.id.nextImageButton || index == R.id.fab && !isInLastPage) {
            helpViewPager.setCurrentItem(helpViewPager.getCurrentItem() + 1);
        } else if (index == R.id.backImageButton || index == R.id.fab && helpAdapter.getCount() > 0) {
            helpViewPager.setCurrentItem(helpViewPager.getCurrentItem() - 1);
        } else if (index == R.id.finishButton || index == R.id.fab && isInLastPage) {
            onFinishButtonPressed();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position < (helpAdapter.getCount() - 1) && position < 3) {
            helpViewPager.setBackgroundColor((Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.white), getResources().getColor(R.color.white)));
            if (shouldDarkenButtonsLayout) {
                buttonsLayout.setBackgroundColor(darkenColor((Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.white), getResources().getColor(R.color.white))));
                divider.setVisibility(View.GONE);
            }
        } else {
            helpViewPager.setBackgroundColor(getResources().getColor(R.color.white));
            if (shouldDarkenButtonsLayout) {
                buttonsLayout.setBackgroundColor(darkenColor(getResources().getColor(R.color.white)));
                divider.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        int lastPagePosition = helpAdapter.getCount() - 1;
        circleIndicatorView.setCurrentPage(position);
        nextImageButton.setVisibility(position == lastPagePosition && !this.shouldUseFloatingActionButton ? View.GONE : View.VISIBLE);
        finishButton.setVisibility(position == lastPagePosition && !this.shouldUseFloatingActionButton ? View.VISIBLE : View.GONE);
        if (this.shouldUseFloatingActionButton)
            this.fab.setImageResource(position == lastPagePosition ? R.drawable.ic_done_white_24dp : R.drawable.ic_arrow_forward_white_24dp);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void hideActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    protected void onSkipButtonPressed() {
        helpViewPager.setCurrentItem(helpAdapter.getCount());
    }

    abstract public void onFinishButtonPressed();
}
