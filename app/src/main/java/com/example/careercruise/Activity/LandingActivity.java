package com.example.careercruise.Activity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.example.careercruise.Activity.UserGateways.SignInActivity;
import com.example.careercruise.Adapter.LandingAdapter;
import com.example.careercruise.Fragment.LandingFragments.LandingFragment1;
import com.example.careercruise.Fragment.LandingFragments.LandingFragment2;
import com.example.careercruise.Fragment.LandingFragments.LandingFragment3;
import com.example.careercruise.R;

public class LandingActivity extends AppCompatActivity {

    private LinearLayout dotsLayout;
    private ViewPager viewPager;
    private ImageView[] dots;

    private int[] colors;
    private int currentColor;
    private TextView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        EdgeToEdge.enable(this);

        dotsLayout = findViewById(R.id.dotsLayout);
        viewPager = findViewById(R.id.viewPager);
        next = findViewById(R.id.next);

        // Initialize color array with the colors you want for each fragment
        colors = new int[]{
                getResources().getColor(R.color.colorFragment1),
                getResources().getColor(R.color.colorFragment2),
                getResources().getColor(R.color.colorFragment3)
        };

        // Setting up window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Setting up the adapter and fragments
        LandingAdapter landingAdapter = new LandingAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        landingAdapter.addFragment(new LandingFragment1(), "Fragment1");
        landingAdapter.addFragment(new LandingFragment2(), "Fragment2");
        landingAdapter.addFragment(new LandingFragment3(), "Fragment3");
        viewPager.setAdapter(landingAdapter);

        // Adding dots indicator
        addDotsIndicator(0);

        // Setting initial background color
        ConstraintLayout mainLayout = findViewById(R.id.main);
        mainLayout.setBackgroundColor(colors[0]);
        currentColor = colors[0];

        viewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position);
                // Add Next Button If user Came to Last Page
                if (position == 2) {
                    // Visible If Last Page
                    if (next.getVisibility() != View.VISIBLE) {
                        // If view is not already visible
                        next.setVisibility(View.VISIBLE);
                        // Apply animation
                        Animation fadeInAnimation = AnimationUtils.loadAnimation(LandingActivity.this, android.R.anim.fade_in);
                        next.startAnimation(fadeInAnimation);
                    }
                } else {
                    // Invisible Rest of Page
                    next.setVisibility(View.GONE);
                }

                // Smoothly change the background color based on the current fragment position
                int colorTo = colors[position];
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), currentColor, colorTo);
                colorAnimation.setDuration(600); // duration of the color transition
                colorAnimation.addUpdateListener(animator -> mainLayout.setBackgroundColor((int) animator.getAnimatedValue()));
                colorAnimation.start();
                currentColor = colorTo; // Update the current color
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        next.setOnClickListener(v -> {
            Intent intent = new Intent(LandingActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }

    private void addDotsIndicator(int position) {
        dots = new ImageView[3];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.dot_inactive);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            dotsLayout.addView(dots[i], params);
        }

        if (dots.length > 0) {
            dots[position].setImageResource(R.drawable.dot_active);
        }
    }
}
