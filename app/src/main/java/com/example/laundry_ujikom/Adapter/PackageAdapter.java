package com.example.laundry_ujikom.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.laundry_ujikom.Laundry_package;
import com.example.laundry_ujikom.Model.PaketLaundry;
import com.example.laundry_ujikom.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder> {
    private Context mcontext;
    private List<PaketLaundry> mPackage;
    private OnItemClickListener mListener;

    public PackageAdapter(Context context, List<PaketLaundry> packages){
        mcontext = context;
        mPackage = packages;

    }


    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_row, parent,false);
        final PackageViewHolder vHolder = new PackageViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        final PaketLaundry laundryCurrent = mPackage.get(position);



        holder.packageName.setText(laundryCurrent.getNama_paket());
        holder.packageJenis.setText(laundryCurrent.getJenis()) ;
        holder.packageHarga.setText(formatRupiah.format(laundryCurrent.getHarga()));
//        if (laundryCurrent.jenis.equals("b")){
//            Glide.with(mcontext).load(R.drawable.tanjirou).into(holder.jenis_img);
//        }

        switch (laundryCurrent.jenis){
            case "Kiloan":
                Glide.with(mcontext).load(R.drawable.ic_basket).into(holder.jenis_img);
                break;
            case "Selimut":
                Glide.with(mcontext).load(R.drawable.ic_blanket).into(holder.jenis_img);
                break;
            case "Kaos,Kemeja,Celana,dll" :
                Glide.with(mcontext).load(R.drawable.ic_tshirt).into(holder.jenis_img);
                break;
        }
    }

    public void fiterList(ArrayList<PaketLaundry> filterList) {
        mPackage = filterList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mPackage.size();
    }

    public class PackageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener
    , MenuItem.OnMenuItemClickListener {
        CardView card_package;
        View mView;
        TextView packageName, packageJenis, packageHarga;
        ImageView jenis_img;
        public PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            card_package = (CardView) itemView.findViewById(R.id.card_package);
            packageName = (TextView) itemView.findViewById(R.id.laundry_name);
            packageJenis = (TextView) itemView.findViewById(R.id.laundry_category);
            packageHarga = (TextView) itemView.findViewById(R.id.laundry_price);
            jenis_img = (ImageView) itemView.findViewById(R.id.icn_jenis);


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
                            mListener.onEditClick(mPackage.get(position));
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
                    mListener.onItemClick(mPackage.get(position));
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
        void onItemClick(PaketLaundry LP);

        void onDeleteClick(int position);

        void onEditClick(PaketLaundry LP);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
