package com.dolan.dnclientsqlite

import android.view.View

class CustomOnItemClickListener(
    private var position: Int,
    private var onItemCallBack: OnItemCallBack
) : View.OnClickListener {
    override fun onClick(v: View?) {
        onItemCallBack.onItemClicked(v, position)
    }

    interface OnItemCallBack {
        fun onItemClicked(view: View?, position: Int)
    }
}