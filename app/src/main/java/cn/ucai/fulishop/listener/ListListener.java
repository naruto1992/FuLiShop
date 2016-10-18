package cn.ucai.fulishop.listener;

/**
 * Created by Administrator on 2016/10/15.
 */

public interface ListListener {

    interface OnItemClickListener {
        void onItemClick(int position, int itemType);
    }

    interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

}
