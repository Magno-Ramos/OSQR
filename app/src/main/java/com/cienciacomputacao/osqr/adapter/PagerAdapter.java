package com.cienciacomputacao.osqr.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cienciacomputacao.osqr.fragment.ClientFragment;
import com.cienciacomputacao.osqr.fragment.FinalFragment;
import com.cienciacomputacao.osqr.fragment.ServiceFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ClientFragment();
            case 1:
                return new ServiceFragment();
            case 2:
            default:
                return new FinalFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Cliente";
            case 1:
                return "Serviços";
            case 2:
            default:
                return "Finalizar";
        }
    }
}
