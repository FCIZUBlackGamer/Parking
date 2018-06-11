package com.example.user.parking;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by USER on 4/11/2018.
 */
public class adapter extends RecyclerView.Adapter<adapter.VHolder> {
    List<owner_data> row_items;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(owner_data item);
    }

    public adapter(List<owner_data> row_items, OnItemClickListener listener) {
        this.row_items = row_items;
        this.listener = listener;
    }

    @Override
    public adapter.VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.map_list_garage_item,parent,false);
        return new VHolder(v);
    }

    @Override
    public void onBindViewHolder(adapter.VHolder holder, int position) {
        final owner_data noti_item = row_items.get(position);
        holder.bind(row_items.get(position), listener);

        holder.name.setText(noti_item.getName());
        holder.position.setText(noti_item.getPosition());
        holder.address.setText(noti_item.getAddress());

    }

    @Override
    public int getItemCount() {
        return row_items.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{
        TextView name, position, address;
        public VHolder(View itemView) {
            super( itemView );
            name = (TextView)itemView.findViewById(R.id.text1);
            position = (TextView)itemView.findViewById(R.id.text3);
            address = (TextView)itemView.findViewById(R.id.text2);
        }
        public void bind(final owner_data item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
