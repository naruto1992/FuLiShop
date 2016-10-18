package cn.ucai.fulishop.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.ucai.fulishop.R;

/**
 * 加载进度对话框
 */
public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {

        private TextView tipTextView;
        private Context mContext;
        private LinearLayout layout;

        public Builder(Context context) {
            this.mContext = context;
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
            layout = (LinearLayout) v.findViewById(R.id.dialog_view);
            tipTextView = (TextView) v.findViewById(R.id.tipTextView);
            // tipTextView.setVisibility(View.GONE);
        }

        public void setLoadMessage(String message) {
            if (tipTextView != null) {
                tipTextView.setVisibility(View.VISIBLE);
                tipTextView.setText(message);
            }
        }

        public LoadingDialog create() {
            LoadingDialog loadingDialog = new LoadingDialog(mContext,
                    R.style.loading_dialog);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
            return loadingDialog;
        }

    }

}
