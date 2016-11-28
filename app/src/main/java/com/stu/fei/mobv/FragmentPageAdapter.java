package com.stu.fei.mobv;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

/**
 * Created by vlado on 23.11.16.
 */

public class FragmentPageAdapter extends FragmentPagerAdapter {

    private Fragment fragment;
    private int[] imageResId = {
            R.drawable.home,
            R.drawable.location,
            R.drawable.search_white
    };
    private String[] tabtitle = new String[]{"home","location", "search"};

    Context context;

    public FragmentPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        //Fragment fragment;

        if(position == 0){
            fragment = new TabFragment();
        }
        else if(position == 1){
            fragment = new TabFragment2();
        }else{
            fragment = new TabFragment3();
        }

        return fragment;
    }

    public Fragment getActualFragment() {
        return fragment;
    }

    @Override
    public int getCount() {
        return tabtitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        Drawable image = context.getResources().getDrawable(imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("   " + tabtitle[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;


        //return tabtitle[position];
    }
}
