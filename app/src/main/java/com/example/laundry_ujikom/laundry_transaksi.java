package com.example.laundry_ujikom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laundry_ujikom.Adapter.PackageAdapter;
import com.example.laundry_ujikom.Adapter.TransactionAdapter;
import com.example.laundry_ujikom.Adapter.UserAdapter;
import com.example.laundry_ujikom.Model.PaketLaundry;
import com.example.laundry_ujikom.Model.Transaction;
import com.example.laundry_ujikom.Model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class laundry_transaksi extends AppCompatActivity implements TransactionAdapter.OnItemClickListener {
    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle mtoggle;
    private NavigationView nv;
    private RecyclerView mDataList;
    private TransactionAdapter mAdapter;
    private List<Transaction> mTransaction;
    private Transaction transaction;
    private ProgressBar mProgressCircle;
    private FloatingActionButton mFAB;

    private EditText search_Bar;
    private LoadingDialog loadingDialog;

    private DatabaseReference dbr,dbr_paket,dbr_detail;
    private ValueEventListener mDBListener;
    private Dialog customDialog;
    String ya,test,vbv,xdf,dfj,vdf,hur,waw,testw,testing,vespa;
    Integer kop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_transaksi);

        loadingDialog = new LoadingDialog(laundry_transaksi.this);
        mDataList =(RecyclerView)findViewById(R.id.data_paket6);
        mProgressCircle = (ProgressBar) findViewById(R.id.progress_circle6);
        mDataList.setHasFixedSize(true);
        mDataList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        search_Bar = (EditText) findViewById(R.id.search_package6);


        transaction = new Transaction();
        mTransaction = new ArrayList<>();

        mAdapter = new TransactionAdapter(getApplicationContext(), mTransaction);

        mAdapter.setOnItemClickListener(laundry_transaksi.this);

        mDataList.setAdapter(mAdapter);


        mdrawer = (DrawerLayout) findViewById(R.id.nav_drawer6);
        Toolbar toolbar = findViewById(R.id.toolbar6);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        mtoggle = new ActionBarDrawerToggle(this, mdrawer,R.string.Open,R.string.Close);
        mdrawer.addDrawerListener(mtoggle);
        mtoggle.syncState();



        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);


        dbr = FirebaseDatabase.getInstance().getReference("transaction");
        dbr_paket = FirebaseDatabase.getInstance().getReference("paket");
        dbr_detail = FirebaseDatabase.getInstance().getReference("");

        navDrawer();

        forRecycler();

        ToStore();


//        ItemSearch();
//
//        setCustomDialog();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void navDrawer(){
        nv = (NavigationView) findViewById(R.id.nav_view6);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.navigation_home){
                    startActivity(new Intent(getApplicationContext(), home.class));
                    finish();
                }

                return true;
            }
        });
    }

    private void forRecycler(){
        mDBListener = dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mTransaction.clear();

                for (DataSnapshot transactionSnapshot : dataSnapshot.getChildren()){
                    Transaction transaction = transactionSnapshot.getValue(Transaction.class);
//                    transaction.setkey(transactionSnapshot.getKey());
                    mTransaction.add(transaction);
                }

                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void ToStore(){
        mFAB = (FloatingActionButton) findViewById(R.id.navigation_add6);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), create_transaction.class));
            }
        });
    }

    @Override
    public void onItemClick(final Transaction transaction) {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();


        customDialog = new Dialog(laundry_transaksi.this);
//        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.invoice_row);
        customDialog.setCancelable(true);

        final TextView asd = customDialog.findViewById(R.id.hargaPaket111);
        final TextView zxc = customDialog.findViewById(R.id.hargaPaket112);
        final TextView qwe = customDialog.findViewById(R.id.hargaPaket113);
//        idPaket = customDialog.findViewById(R.id.idPaket);
        final TextView iop = customDialog.findViewById(R.id.hargaPaket114);
        final TextView kll = customDialog.findViewById(R.id.hargaPaket115);
        final TextView fgh = customDialog.findViewById(R.id.harga116);
        final TextView qaz = customDialog.findViewById(R.id.harga117);
        final ImageView gambar_invoice = customDialog.findViewById(R.id.icn_dialog_hah);

        final String id_customer = transaction.getId_Customer();
        final String id_transaksi = transaction.getId_transaction();
        iop.setText(transaction.getTgl());
        kll.setText(transaction.getStatus());
        fgh.setText(transaction.getDibayar());

        DatabaseReference dbr_transaction = FirebaseDatabase.getInstance().getReference("transaction");
        dbr_transaction.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot transactionsnapshot: dataSnapshot.getChildren()){
                    vbv = transactionsnapshot.child("id_Customer").getValue(String.class);
                    test = transactionsnapshot.child("id_transaction").getValue(String.class);

                    DatabaseReference dbr_customer = FirebaseDatabase.getInstance().getReference("Customer")
                            .child(id_customer);
                    dbr_customer.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        for (DataSnapshot customersnapshot: dataSnapshot.getChildren()){
//                                            ya = customersnapshot.child("nama_customer").getValue(String.class);

                            waw = dataSnapshot.child("nama_customer").getValue(String.class);
                            asd.setText(waw);
//                            holder.nama_customer.setText(String.valueOf(dataSnapshot.child("nama_customer").getValue()));
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
                                if (dfj.equals(id_transaksi)){

                                    DatabaseReference dbr_paket = FirebaseDatabase.getInstance().getReference("paket").child(xdf);
                                    dbr_paket.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for (DataSnapshot paketsnapshot: dataSnapshot.getChildren()){
                                            vdf = dataSnapshot.child("nama_paket").getValue(String.class);
                                            hur = dataSnapshot.child("jenis").getValue(String.class);
                                            kop = dataSnapshot.child("harga").getValue(Integer.class);
//                                            holder.nama_paket.setText(vdf);
                                            qwe.setText(String.valueOf(kop));
                                            zxc.setText(hur);
                                            qaz.setText(vdf);
                                            switch (hur){
                                                case "Kiloan":
                                                    Glide.with(getApplicationContext()).load(R.drawable.ic_basket).into(gambar_invoice);
                                                    break;
                                                case "Selimut":
                                                    Glide.with(getApplicationContext()).load(R.drawable.ic_blanket).into(gambar_invoice);
                                                    break;
                                                case "Kaos,Kemeja,Celana,dll" :
                                                    Glide.with(getApplicationContext()).load(R.drawable.ic_tshirt).into(gambar_invoice);
                                                    break;
                                            }
//                                            holder.harga_laundry.setText(formatRupiah.format(kop));




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
        customDialog.show();

    }

    @Override
    public void onDeleteClick(int position) {

    }

    @Override
    public void onEditClick(Transaction transaction) {
        Intent I = new Intent(laundry_transaksi.this, edit_transaction.class);
        I.putExtra("id_transaksi", transaction.getId_transaction());
        I.putExtra("id_customer", transaction.getId_Customer());
        I.putExtra("batas", transaction.getBatas_waktu());
        I.putExtra("biaya_tambahan", transaction.getBiaya_tambahan());
        I.putExtra("tgl", transaction.getTgl());
        I.putExtra("dibayar", transaction.getDibayar());
        I.putExtra("status", transaction.getStatus());
        I.putExtra("tgl_bayar", transaction.getTgl_bayar());
        I.putExtra("id_outlet", transaction.getId_outlet());
        I.putExtra("id_user", transaction.getId_user());
        startActivity(I);

        Toast.makeText(this, "go to edit", Toast.LENGTH_SHORT).show();
    }

//    private void ItemSearch() {
//        search_Bar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                filter(editable.toString());
//            }
//        });
//    }

//    private void filter(String text) {
//        ArrayList<PaketLaundry> filterList = new ArrayList<>();
//
//        dbr.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot transaksisnapshot: dataSnapshot.getChildren()){
//                    final String zxc = transaksisnapshot.child("id_transaction").getValue(String.class);
//
//                    dbr_paket.orderByChild("id_transaction").addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for (DataSnapshot detailsnapshot: dataSnapshot.getChildren()){
//                                String qwe = detailsnapshot.child("id_transaction").getValue(String.class);
//                                String idn = detailsnapshot.child("id_paket").getValue(String.class);
//                                if (zxc.equals(qwe)){
//                                    dbr_paket.child(idn).addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            String sdfg = dataSnapshot.child("nama_paket").getValue(String.class);
//                                            filterList.add(paketLaundry);
//                                            mAdapter.fiterList(sdfg);
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    })
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        for (Transaction transaction : mTransaction){
//            if (transaction.getNama().toLowerCase().contains(text.toLowerCase()) && paketLaundry.getNama_paket() != null){
//                filterList.add(paketLaundry);
//            }
//        }
//
//        mAdapter.fiterList(filterList);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbr.removeEventListener(mDBListener);
    }
}
