package com.example.laundry_ujikom.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundry_ujikom.Model.Outlet;
import com.example.laundry_ujikom.Model.PaketLaundry;
import com.example.laundry_ujikom.R;

import java.util.ArrayList;
import java.util.List;

public class OutletAdapter extends RecyclerView.Adapter<OutletAdapter.OutletViewHolder> {
    private Context mcontext;
    private List<Outlet> mOutlet;
    private OutletAdapter.OnItemClickListener mListener;


    public OutletAdapter(Context context, List<Outlet> outlets) {
        mcontext = context;
        mOutlet = outlets;

    }

    @NonNull
    @Override
    public OutletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.outlet_row, parent,false);
        return new OutletViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OutletViewHolder holder, int position) {
        Outlet OutletCurrent = mOutlet.get(position);
        holder.OutletName.setText(OutletCurrent.getNama());
        holder.Outletalamat.setText(OutletCurrent.getAlamat());
        holder.OutletTlp.setText(OutletCurrent.getTlp());

    }

    public void fiterList(ArrayList<Outlet> filterList) {
        mOutlet = filterList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mOutlet.size();
    }

    public class OutletViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener
            , MenuItem.OnMenuItemClickListener{
        CardView card_package;
        View mView;
        TextView OutletName, Outletalamat, OutletTlp;
        ImageView outlet_img;

        public OutletViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            card_package = (CardView) itemView.findViewById(R.id.card_Outlet);
            OutletName = (TextView) itemView.findViewById(R.id.Outlet_name);
            Outletalamat = (TextView) itemView.findViewById(R.id.Outlet_alamat);
            OutletTlp = (TextView) itemView.findViewById(R.id.Outlet_tlp);
            outlet_img = (ImageView) itemView.findViewById(R.id.icn_outlet);

//
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (menuItem.getItemId()){
                        case 1:
                            mListener.onEditClick(mOutlet.get(position));
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void onClick(View view) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(mOutlet.get(position));
                }
            }

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Action");
            MenuItem Edit = contextMenu.add(Menu.NONE, 1, 1, "Edit Item");
            MenuItem Delete = contextMenu.add(Menu.NONE, 2, 2, "Delete Item");
            Edit.setOnMenuItemClickListener(this);
            Delete.setOnMenuItemClickListener(this);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Outlet outlet);

        void onDeleteClick(int position);

        void onEditClick(Outlet outlet);
    }

    public void setOnItemClickListener(OutletAdapter.OnItemClickListener listener){
        mListener = listener;
    }


}
