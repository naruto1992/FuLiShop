package cn.ucai.fulishop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ucai.fulishop.R;

/**
 * Created by Shinelon on 2016/10/13.
 */

public class FragmentBoutique extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_boutique, container, false);
    }
}
