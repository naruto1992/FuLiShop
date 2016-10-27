package cn.ucai.fulishop.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.ucai.fulishop.R;
import cn.ucai.fulishop.application.FuLiShopApplication;
import cn.ucai.fulishop.view.LoadingDialog;

public class BaseFragment extends Fragment {

    protected LoadingDialog loadingDialog;
    protected LocalBroadcastManager broadcastManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadingDialog = new LoadingDialog.Builder(getActivity()).create();
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
    }
}
