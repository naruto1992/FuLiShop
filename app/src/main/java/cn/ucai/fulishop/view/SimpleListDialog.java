package cn.ucai.fulishop.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import cn.ucai.fulishop.R;
import cn.ucai.fulishop.utils.PhoneUtil;

/**
 * Created by Administrator on 2016/10/13.
 */

public class SimpleListDialog extends Dialog {

    public SimpleListDialog(Context context) {
        super(context);
    }

    public SimpleListDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        Context context;
        String[] items;
        OnClickListener listener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setItems(String[] items, OnClickListener listener) {
            this.items = items;
            this.listener = listener;
            return this;
        }

        public SimpleListDialog create() {
            //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater inflater = LayoutInflater.from(context);
            final SimpleListDialog dialog = new SimpleListDialog(context, R.style.list_dialog_style);
            View layout = inflater.inflate(R.layout.simple_list_layout, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            // 设置窗体宽度
            WindowManager.LayoutParams params = dialog.getWindow()
                    .getAttributes();
            params.width = (int) (PhoneUtil.getScreenWidth(context) * 0.8);
            dialog.getWindow().setAttributes(params);
            //初始化
            ListView listView = (ListView) layout.findViewById(R.id.dialog_list);
            listView.setAdapter(new ArrayAdapter<>(context, R.layout.simple_list_item, items));
            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (listener != null) {
                        listener.onClick(dialog, position);
                    }
                    dialog.dismiss();
                }
            });
            dialog.setContentView(layout);
            return dialog;
        }
    }

}
