package com.cienciacomputacao.osqr;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.cienciacomputacao.osqr.adapter.PagerAdapter;
import com.cienciacomputacao.osqr.fragment.Fragment;

public class GenerateCodeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private int oldPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_code);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                callSelectAndDeselect(position);
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        if (oldPosition > 0){
            previousFragment();
        }else{
            super.onBackPressed();
        }
    }

    public void nextFragment() {
        int position = oldPosition + 1;
        viewPager.setCurrentItem(position, true);
        callSelectAndDeselect(position);
    }

    public void previousFragment(){
        int position = oldPosition - 1;
        viewPager.setCurrentItem(position, true);
        callSelectAndDeselect(position);
    }

    private void callSelectAndDeselect(int position) {
        if (oldPosition != position) {

            Fragment fragDeselected = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, oldPosition);
            fragDeselected.onDeselected();

            Fragment fragSelected = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, position);
            fragSelected.onSelected();

            oldPosition = position;
        }
    }
}
