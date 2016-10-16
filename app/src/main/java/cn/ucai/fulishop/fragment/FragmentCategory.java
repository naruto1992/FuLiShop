package cn.ucai.fulishop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulishop.R;
import cn.ucai.fulishop.api.I;
import cn.ucai.fulishop.bean.CategoryGroupBean;
import cn.ucai.fulishop.utils.OkHttpUtils;
import cn.ucai.fulishop.utils.ToastUtil;
import cn.ucai.fulishop.view.CategoryItemView;

/**
 * Created by Shinelon on 2016/10/13.
 */

public class FragmentCategory extends Fragment {

    Context mContext;

    @BindView(R.id.category_list_ll)
    LinearLayout category_list_ll;

    ArrayList<CategoryGroupBean> groupList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        loadCategoryGroupList();
    }

    private void loadCategoryGroupList() {
        final OkHttpUtils<String> utils = new OkHttpUtils<>(mContext);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(String.class)
                .execute(new OkHttpUtils.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result != null) {
                            Gson gson = new Gson();
                            CategoryGroupBean[] list = gson.fromJson(result, CategoryGroupBean[].class);
                            groupList = utils.array2List(list);
                            if (groupList != null && groupList.size() > 0) {
                                initView(groupList);
                            }
                        } else {
                            ToastUtil.showOnUI(getActivity(), "数据获取失败");
                        }
                    }

                    @Override
                    public void onError(String error) {
                        ToastUtil.showOnUI(getActivity(), error);
                    }
                });
    }


    private void initView(ArrayList<CategoryGroupBean> groupList) {
        for (CategoryGroupBean bean : groupList) {
            CategoryItemView cat = new CategoryItemView(mContext);
            cat.init(bean);
            category_list_ll.addView(cat);
        }
    }
}
