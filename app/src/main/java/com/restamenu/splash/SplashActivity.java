package com.restamenu.splash;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.restamenu.R;
import com.restamenu.main.MainActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splash_background)ImageView splashBackgroundImage;
    @BindView(R.id.splash_cursor) ImageView splashCursorImage;
    @BindView(R.id.restamenu_text) ImageView restamenuTextImage;
    @BindView(R.id.electronic_menu_text) ImageView electronicMenuText;
    @BindView(R.id.title_container) FrameLayout titleContainer;

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

        splashBackgroundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });


        ViewAnimator
                // Splash alpha animation
                .animate(splashBackgroundImage)
                .startDelay(50)
                .duration(700)
                .interpolator(new LinearInterpolator())
                .alpha(0, 1)

                // Dot scale animation
                .thenAnimate(splashCursorImage)
                .duration(200)
                .interpolator(new AccelerateDecelerateInterpolator())
                .scale(0, 1)

                // From dot to line animation
                .thenAnimate(splashCursorImage)
                .duration(100)
                .interpolator(new LinearInterpolator())
                .scaleY(1, 5f)
                .scaleX(1, 0.25f)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        splashCursorImage.setImageResource(R.drawable.rectangle_shape);
                    }
                })
                // line moving animation
                .thenAnimate(splashCursorImage)
                .startDelay(350)
                .duration(350)
                .interpolator(new AccelerateDecelerateInterpolator())
                .translationX(0, -getResources().getDimension(R.dimen.splash_title_width)/ 2)

                // moving container with title
                .andAnimate(titleContainer)
                .duration(350)
                .interpolator(new AccelerateDecelerateInterpolator())
                .translationX(0, -getResources().getDimension(R.dimen.splash_title_width) / 2)

                // slide animation of container
                .andAnimate(restamenuTextImage)
                .duration(350)
                .interpolator(new AccelerateDecelerateInterpolator())
                .translationX(-getResources().getDimension(R.dimen.splash_title_width), 0)
                .alpha(1.f, 1.f)

                .thenAnimate(splashCursorImage)
                .duration(250)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        splashCursorImage.setImageResource(R.drawable.circle_shape);
                    }
                })
                .scaleY(5f, 5f)

                // from line to dot
                .thenAnimate(splashCursorImage)
                .duration(175)
                .interpolator(new LinearInterpolator())
                .scaleY(2.5f, 1)
                .scaleX(0.25f, 1)

                // dot anim alpha
                .thenAnimate(splashCursorImage)
                .startDelay(30)
                .duration(100)
                .interpolator(new LinearInterpolator())
                .alpha(1, 0)

                // move title up
                .thenAnimate(restamenuTextImage)
                .startDelay(600)
                .duration(350)
                .interpolator(new AccelerateDecelerateInterpolator())
                .translationY(0, -getResources().getDimension(R.dimen.title_shift_margin))

                // move desc down
                .andAnimate(electronicMenuText)
                .duration(350)
                .interpolator(new AccelerateDecelerateInterpolator())
                .translationY(0, getResources().getDimension(R.dimen.title_shift_margin))
                .alpha(0, 1)

                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                })



                .start();



    }
}
