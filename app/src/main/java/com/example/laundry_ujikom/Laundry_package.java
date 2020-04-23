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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laundry_ujikom.Adapter.PackageAdapter;
import com.example.laundry_ujikom.Model.PaketLaundry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Laundry_package extends AppCompatActivity implements PackageAdapter.OnItemClickListener {
    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle mtoggle;
    private NavigationView nv;
    private RecyclerView mDataList;
    private PackageAdapter mAdapter;
    private List<PaketLaundry> mPackage;
    private PaketLaundry mLaundry;
    private ProgressBar mProgressCircle;
    private FloatingActionButton mFAB;

    private EditText search_Bar;
    private LoadingDialog loadingDialog;
    private Dialog customDialog;
    private TextView namaPaket,jenisPaket,idPaket,hargaPaket;
    private ImageView icn_dialog;

    private DatabaseReference dbr;
    private ValueEventListener mDBListener;
//    private FirebaseRecyclerAdapter<PaketLaundry , PackageAdapter.PackageViewHolder>   mFirebaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_package);

        loadingDialog = new LoadingDialog(Laundry_package.this);
        mDataList =(RecyclerView)findViewById(R.id.data_paket);
        mProgressCircle = (ProgressBar) findViewById(R.id.progress_circle);
        mDataList.setHasFixedSize(true);
        mDataList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        search_Bar = (EditText) findViewById(R.id.search_package);

        mLaundry = new PaketLaundry();
        mPackage = new ArrayList<>();

        mAdapter = new PackageAdapter(getApplicationContext(), mPackage);

        mAdapter.setOnItemClickListener(Laundry_package.this);

        mDataList.setAdapter(mAdapter);


        mdrawer = (DrawerLayout) findViewById(R.id.nav_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        mtoggle = new ActionBarDrawerToggle(this, mdrawer,R.string.Open,R.string.Close);
        mdrawer.addDrawerListener(mtoggle);
        mtoggle.syncState();



        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);


        dbr = FirebaseDatabase.getInstance().getReference("paket");
        navDrawer();

        forRecycler();

        ToStore();

        ItemSearch();

        setCustomDialog();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void navDrawer(){
        nv = (NavigationView) findViewById(R.id.nav_view);
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
                mPackage.clear();

                for (DataSnapshot packageSnapshot : dataSnapshot.getChildren()){
                    PaketLaundry paketLaundry = packageSnapshot.getValue(PaketLaundry.class);
                    paketLaundry.setkey(packageSnapshot.getKey());
                    mPackage.add(paketLaundry);
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
        mFAB = (FloatingActionButton) findViewById(R.id.navigation_add);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), create_package.class));
            }
        });
    }

    @Override
    public void onItemClick(PaketLaundry LP) {

        Toast.makeText(this, "Item Click", Toast.LENGTH_SHORT).show();
        customDialog = new Dialog(Laundry_package.this);
//        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.paket_dialog);
        customDialog.setCancelable(true);
//        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        namaPaket = customDialog.findViewById(R.id.namaPaket);
        jenisPaket = customDialog.findViewById(R.id.jenisPaket);
        hargaPaket = customDialog.findViewById(R.id.hargaPaket);
//        idPaket = customDialog.findViewById(R.id.idPaket);
        icn_dialog = customDialog.findViewById(R.id.icn_dialog);

        switch (LP.jenis){
            case "Kiloan":
                Glide.with(Laundry_package.this).load(R.drawable.ic_basket).into(icn_dialog);
                break;
            case "Selimut":
                Glide.with(Laundry_package.this).load(R.drawable.ic_blanket).into(icn_dialog);
                break;
            case "Kaos,Kemeja,Celana,dll" :
                Glide.with(Laundry_package.this).load(R.drawable.ic_tshirt).into(icn_dialog);
                break;
        }

        icn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        namaPaket.setText(LP.getNama_paket());
        jenisPaket.setText(LP.getJenis());
        hargaPaket.setText(String.valueOf(formatRupiah.format(LP.getHarga())));
//        idPaket.setText(LP.getId_paket());
        customDialog.show();

    }

    @Override
    public void onDeleteClick(int position) {
        PaketLaundry selectedPackage = mPackage.get(position);
        final String selectedKey = selectedPackage.getkey();

        loadingDialog.startLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismissDialog();
                dbr.child(selectedKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Laundry_package.this, "Berhasil Menghapus data", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Laundry_package.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 2000);




    }

    @Override
    public void onEditClick(PaketLaundry LP) {
        Intent I = new Intent(Laundry_package.this, edit_package.class);
        I.putExtra("id_paket", LP.getId_paket());
        I.putExtra("id_outlet", LP.getId_outlet());
        I.putExtra("testing", LP.getNama_paket());
        I.putExtra("harga_paket", LP.getHarga());
        I.putExtra("jenis_paket", LP.getJenis());
        startActivity(I);

        Toast.makeText(this, LP.getNama_paket(), Toast.LENGTH_SHORT).show();
    }



    private void ItemSearch() {
        search_Bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }



    private void filter(String text) {
        ArrayList<PaketLaundry> filterList = new ArrayList<>();

        for (PaketLaundry paketLaundry : mPackage){
            if (paketLaundry.getNama_paket().toLowerCase().contains(text.toLowerCase()) && paketLaundry.getNama_paket() != null){
                filterList.add(paketLaundry);
            }
        }

        mAdapter.fiterList(filterList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbr.removeEventListener(mDBListener);
    }

    public void setCustomDialog(){




    }
}
