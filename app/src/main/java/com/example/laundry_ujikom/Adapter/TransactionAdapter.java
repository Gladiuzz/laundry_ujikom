package com.example.laundry_ujikom.Adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.solver.widgets.Snapshot;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.laundry_ujikom.Laundry_package;
import com.example.laundry_ujikom.Model.Detail_Transaction;
import com.example.laundry_ujikom.Model.Outlet;
import com.example.laundry_ujikom.Model.PaketLaundry;
import com.example.laundry_ujikom.Model.Transaction;
import com.example.laundry_ujikom.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private Context mcontext;
    private List<Transaction> mTransaction;
    private TransactionAdapter.OnItemClickListener mListener;
    String ya,test,vbv,xdf,dfj,vdf,hur;
    Integer kop;
//    private List<Detail_Transaction> mDetail;

    public TransactionAdapter(Context context, List<Transaction> transactions) {
        mcontext = context;
        mTransaction = transactions;

    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_row, parent,false);
        return new TransactionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final TransactionViewHolder holder, int position) {
        Locale localeID = new Locale("in", "ID");
        final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        Transaction TransactionCurrent = mTransaction.get(position);
        final String nama_customer = TransactionCurrent.getId_Customer();
        final String dfdg = TransactionCurrent.getId_transaction();

        // pertama ambil id paket dari detail transaksi
        DatabaseReference dbr_transaction = FirebaseDatabase.getInstance().getReference("transaction");
        dbr_transaction.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot transactionsnapshot: dataSnapshot.getChildren()){
                    vbv = transactionsnapshot.child("id_Customer").getValue(String.class);
                    test = transactionsnapshot.child("id_transaction").getValue(String.class);

                    DatabaseReference dbr_customer = FirebaseDatabase.getInstance().getReference("Customer")
                            .child(nama_customer);
                    dbr_customer.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        for (DataSnapshot customersnapshot: dataSnapshot.getChildren()){
//                                            ya = customersnapshot.child("nama_customer").getValue(String.class);
                            holder.nama_customer.setText(String.valueOf(dataSnapshot.child("nama_customer").getValue()));
//                                        }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    final DatabaseReference dbr_detail = FirebaseDatabase.getInstance().getReference("detail_transaction");
                    dbr_detail.orderByChild("id_transaction").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot detailsnapshot: dataSnapshot.getChildren()){
                                xdf = detailsnapshot.child("id_paket").getValue(String.class);
                                dfj = detailsnapshot.child("id_transaction").getValue(String.class);
                                if (dfj.equals(dfdg)){

                                    DatabaseReference dbr_paket = FirebaseDatabase.getInstance().getReference("paket").child(xdf);
                                    dbr_paket.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for (DataSnapshot paketsnapshot: dataSnapshot.getChildren()){
                                            vdf = dataSnapshot.child("nama_paket").getValue(String.class);
                                            hur = dataSnapshot.child("jenis").getValue(String.class);
                                            kop = dataSnapshot.child("harga").getValue(Integer.class);
                                        holder.nama_paket.setText(vdf);
                                            switch (hur){
                                                case "Kiloan":
                                                    Glide.with(mcontext).load(R.drawable.ic_basket).into(holder.transaction_img);
                                                    break;
                                                case "Selimut":
                                                    Glide.with(mcontext).load(R.drawable.ic_blanket).into(holder.transaction_img);
                                                    break;
                                                case "Kaos,Kemeja,Celana,dll" :
                                                    Glide.with(mcontext).load(R.drawable.ic_tshirt).into(holder.transaction_img);
                                                    break;
                                            }
                                            holder.harga_laundry.setText(formatRupiah.format(kop));




//                        }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }



                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

//                            DatabaseReference dbr_paket = FirebaseDatabase.getInstance().getReference("paket").child(xdf);
//                            dbr_paket.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    dfj = dataSnapshot.child("nama_paket").getValue(String.class);

//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });
//                        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        holder.tgl_transaksi.setText(TransactionCurrent.getTgl()+" - "+TransactionCurrent.getBatas_waktu());

    }

    @Override
    public int getItemCount() {
        return mTransaction.size();
    }


    public class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener
            , MenuItem.OnMenuItemClickListener{
        CardView card_package;
        View mView;
        TextView nama_paket,nama_customer,tgl_transaksi,harga_laundry,invoice;
        ImageView transaction_img;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            card_package = (CardView) itemView.findViewById(R.id.card_Outlet);
            nama_paket = (TextView) itemView.findViewById(R.id.Transaction_namepaket);
            nama_customer = (TextView) itemView.findViewById(R.id.Transaction_customername);
            tgl_transaksi = (TextView) itemView.findViewById(R.id.Transaction_tgl);
            harga_laundry = (TextView) itemView.findViewById(R.id.Transaction_price);
            invoice = (TextView) itemView.findViewById(R.id.Transaction_invoice);
            transaction_img = (ImageView) itemView.findViewById(R.id.icn_transaction);

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
                            mListener.onEditClick(mTransaction.get(position));
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
                    mListener.onItemClick(mTransaction.get(position));
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
        void onItemClick(Transaction transaction);

        void onDeleteClick(int position);

        void onEditClick(Transaction transaction);
    }

    public void setOnItemClickListener(TransactionAdapter.OnItemClickListener listener){
        mListener = listener;
    }


    public void transaksi_detail(){

    }
}
