package com.league.pubgleague;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by AkshayeJH on 11/06/17.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter{


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                DepositFragment depositFragment = new DepositFragment();
                return depositFragment;

            case 1:
                WithdrawFragment withdrawFragment = new WithdrawFragment();
                return  withdrawFragment;

//            case 2:
//                ResultsFragment resultsFragment = new ResultsFragment();
//                return resultsFragment;

            default:
                return  null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){

        switch (position) {
            case 0:
                return "Deposit";

            case 1:
                return "Withdraw";

//            case 2:
//                return "Results";

            default:
                return null;
        }

    }

}