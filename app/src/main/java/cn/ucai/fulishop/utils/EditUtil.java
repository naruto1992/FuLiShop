package cn.ucai.fulishop.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shinelon on 2016/10/17.
 */

public class EditUtil {

    List<EditText> editTextList = new ArrayList<>();
    EditText emptyEditText;

    public EditUtil(EditText[] list) {
        for (int i = 0; i < list.length; i++) {
            this.editTextList.add(list[i]);
        }
    }

    public EditText getEmptyEditText() {
        emptyEditText.setError("不能为空");
        return emptyEditText;
    }

    public boolean isAllEditComplete() {
        for (EditText editText : editTextList) {
            int length = editText.getEditableText().toString().trim().length();
            if (length == 0) {
                emptyEditText = editText;
                return false;
            } else {
                continue;
            }
        }
        return true;
    }
}
