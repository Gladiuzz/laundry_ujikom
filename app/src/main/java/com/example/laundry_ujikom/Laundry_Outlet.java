package com.example.laundry_ujikom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.laundry_ujikom.Adapter.OutletAdapter;
import com.example.laundry_ujikom.Adapter.PackageAdapter;
import com.example.laundry_ujikom.Model.Outlet;
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

import java.util.ArrayList;
import java.util.List;

public class Laundry_Outlet extends AppCompatActivity implements OutletAdapter.OnItemClickListener {
    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle mtoggle;
    private NavigationView nv;
    private RecyclerView mDataList;
    private OutletAdapter mAdapter;
    private List<Outlet> mOutlet;
    private Outlet outlet;
    private ProgressBar mProgressCircle;
    private FloatingActionButton mFAB;

    private EditText search_Bar;
    private LoadingDialog loadingDialog;

    private DatabaseReference dbr;
    private ValueEventListener mDBListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry__outlet);
        loadingDialog = new LoadingDialog(Laundry_Outlet.this);
        mDataList =(RecyclerView)findViewById(R.id.data_paket2);
        mProgressCircle = (ProgressBar) findViewById(R.id.progress_circle2);
        mDataList.setHasFixedSize(true);
        mDataList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        search_Bar = (EditText) findViewById(R.id.search_package2);

        outlet = new Outlet();
        mOutlet = new ArrayList<>();

        mAdapter = new OutletAdapter(getApplicationContext(), mOutlet);

        mAdapter.setOnItemClickListener(Laundry_Outlet.this);

        mDataList.setAdapter(mAdapter);


        mdrawer = (DrawerLayout) findViewById(R.id.nav_drawer2);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        mtoggle = new ActionBarDrawerToggle(this, mdrawer,R.string.Open,R.string.Close);
        mdrawer.addDrawerListener(mtoggle);
        mtoggle.syncState();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbr = FirebaseDatabase.getInstance().getReference("Outlet");

        navDrawer();
        forRecycler();
        ItemSearch();
        ToStore();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void navDrawer(){
        nv = (NavigationView) findViewById(R.id.nav_view2);
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
                mOutlet.clear();

                for (DataSnapshot outletSnapshot : dataSnapshot.getChildren()){
                    Outlet outlet = outletSnapshot.getValue(Outlet.class);
                    outlet.setMkey(outletSnapshot.getKey());
                    mOutlet.add(outlet);
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
        mFAB = (FloatingActionButton) findViewById(R.id.navigation_add2);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), create_outlet.class));
            }
        });
    }

    @Override
    public void onItemClick(Outlet outlet) {

    }

    @Override
    public void onDeleteClick(int position) {
        Outlet selectedoutlet = mOutlet.get(position);
        final String selectedKey = selectedoutlet.getMkey();

        loadingDialog.startLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismissDialog();
                dbr.child(selectedKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Laundry_Outlet.this, "Berhasil Menghapus data", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Laundry_Outlet.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 2000);

    }

    @Override
    public void onEditClick(Outlet outlet) {

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
        ArrayList<Outlet> filterList = new ArrayList<>();

        for (Outlet outlet : mOutlet){
            if (outlet.getNama().toLowerCase().contains(text.toLowerCase()) && outlet.getNama() != null){
                filterList.add(outlet);
            }
        }

        mAdapter.fiterList(filterList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbr.removeEventListener(mDBListener);
    }

}
