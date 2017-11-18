package com.restamenu;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splash_background)ImageView splashBackgroundImage;
    @BindView(R.id.splash_cursor) ImageView splashCursorImage;
    @BindView(R.id.restamenu_text) ImageView restamenuTextImage;
    @BindView(R.id.electronic_menu_text) ImageView electronicMenuText;

    @Override
    protected void onResume() {
        super.onResume();

        boolean isLargeLayout = getResources().getBoolean(R.bool.isLargeLayout);
        if(isLargeLayout) {
            // Tablet Mode
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            // Handset Mode
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        Picasso.with(this).load(R.drawable.splash_background).into(splashBackgroundImage);

        splashCursorImage.setScaleX(0);
        splashCursorImage.setScaleY(0);

        ViewAnimator
                .animate(splashBackgroundImage)
                .startDelay(500)
                .duration(2500)
                .interpolator(new LinearInterpolator())
                .alpha(0, 1)

                .thenAnimate(splashCursorImage)
                .duration(200)
                .interpolator(new AccelerateDecelerateInterpolator())
                .scale(0, 1)

                .thenAnimate(splashCursorImage)
                .duration(350)
                .interpolator(new LinearInterpolator())
                .scaleY(1, 5f)
                .scaleX(1, 0.1f)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        splashCursorImage.setImageResource(R.drawable.rectangle_shape);
                    }
                })
                .thenAnimate(splashCursorImage)
                .duration(500)
                .interpolator(new AccelerateDecelerateInterpolator())
                .translationX(0, -getResources().getDimension(R.dimen.splash_title_width)/ 2 - 16)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        restamenuTextImage.setAlpha(200);
                    }
                })
                .thenAnimate(restamenuTextImage)
                .duration(750)
                .interpolator(new AccelerateDecelerateInterpolator())
                .slideLeft()
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        splashCursorImage.setImageResource(R.drawable.circle_shape);
                    }
                })



                .thenAnimate(splashCursorImage)
                .duration(350)
                .interpolator(new LinearInterpolator())
                .scaleY(2.5f, 1)
                .scaleX(0.1f, 1)

                .thenAnimate(splashCursorImage)
                .duration(350)
                .interpolator(new LinearInterpolator())
                .scale(1, 0)


                .thenAnimate(restamenuTextImage)
                .duration(350)
                .interpolator(new AccelerateDecelerateInterpolator())
                .translationY(0, -40)

                .andAnimate(electronicMenuText)
                .duration(350)
                .interpolator(new AccelerateDecelerateInterpolator())
                .translationY(0, 40)
                .alpha(0, 1)



                .start();

    }
}
