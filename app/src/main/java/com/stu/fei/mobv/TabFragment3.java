package com.stu.fei.mobv;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by vlado on 23.11.16.
 */

public class TabFragment3 extends Fragment implements IRefreshFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment3, container, false);
    }

    @Override
    public void refresh() {

    }
}
