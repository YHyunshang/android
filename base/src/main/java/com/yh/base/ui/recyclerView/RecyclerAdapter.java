package com.yh.base.ui.recyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.yh.base.utils.Reflector;

import java.util.ArrayList;
import java.util.List;

/**
 * @description recyclerView的Adapter封装
 *
 * @date: 2021/4/6 4:11 PM
 * @author: zhangzhiyuan
 */
public abstract class RecyclerAdapter<T, VB extends ViewBinding>
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    List<T> list = new ArrayList<>();

    public List<T> getList() {
        return list;
    }

    public void setList(@NonNull List<T> list) {
        this.list = list;
    }

    public RecyclerAdapter(@NonNull List<T> list) {
        this.list = list;
    }

    public RecyclerAdapter() {
    }


    @Override
    public ViewHolder<VB> onCreateViewHolder(
            ViewGroup parent,
            int viewType) {
        return new ViewHolder(
                Reflector.invoke(
                        Reflector.getVbClazz(this, 1),
                        "inflate",
                        new Class[]{LayoutInflater.class, ViewGroup.class, boolean.class},
                        LayoutInflater.from(parent.getContext()), parent, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        convert(holder, list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    protected abstract void convert(ViewHolder<VB> holder, T data, int position);

    public class ViewHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {

        VB viewBinding;

        public VB getViewBinding() {
            return viewBinding;
        }

        public ViewHolder(VB viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }
    }
}

