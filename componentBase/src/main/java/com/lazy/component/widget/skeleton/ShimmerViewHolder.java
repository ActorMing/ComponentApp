package com.lazy.component.widget.skeleton;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lazy.component.commpoentbase.R;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ethanhua on 2017/7/29.
 */

public class ShimmerViewHolder extends RecyclerView.ViewHolder {

    public ShimmerViewHolder(LayoutInflater inflater, ViewGroup parent, int innerViewResId) {
        super(inflater.inflate(R.layout.layout_shimmer, parent, false));
        ViewGroup layout = (ViewGroup) itemView;
        inflater.inflate(innerViewResId, layout, true);
    }
}
