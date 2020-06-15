package com.example.odometryapp_v10.Main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.odometryapp_v10.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    private ArrayList<RecyclerViewItem> recyclerViewItems;
    private OnItemClickListener listener;
    public static boolean isSecondaryRecyclerView_View = false;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView functionNameTextView;
        public TextView functionParametersTextView;

        public RecyclerViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            functionNameTextView = itemView.findViewById(R.id.recyclerViewFunctionNameTextView);
            functionParametersTextView = itemView.findViewById(R.id.recyclerViewFunctionParametersTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public RecyclerViewAdapter(ArrayList<RecyclerViewItem> itemArrayList) {
        recyclerViewItems = itemArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(isSecondaryRecyclerView_View) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.secondary_recyclerview_item, parent, false);
            RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(v, listener);
            return recyclerViewHolder;
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
            RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(v, listener);
            return recyclerViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        RecyclerViewItem recyclerViewItem = recyclerViewItems.get(position);
        holder.functionNameTextView.setText(recyclerViewItem.getFunctionName());
        holder.functionParametersTextView.setText(recyclerViewItem.getFunctionParameters());
        if(holder.functionParametersTextView.getText().toString().isEmpty() && isSecondaryRecyclerView_View) {
            holder.functionParametersTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }
}
