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
import com.example.laundry_ujikom.Adapter.UserAdapter;
import com.example.laundry_ujikom.Model.Outlet;
import com.example.laundry_ujikom.Model.PaketLaundry;
import com.example.laundry_ujikom.Model.User;
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

public class Laundry_user extends AppCompatActivity implements UserAdapter.OnItemClickListener {
    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle mtoggle;
    private NavigationView nv;
    private RecyclerView mDataList;
    private UserAdapter mAdapter;
    private List<User> mUser;
    private User user;
    private ProgressBar mProgressCircle;
    private FloatingActionButton mFAB;

    private EditText search_Bar;
    private LoadingDialog loadingDialog;

    private DatabaseReference dbr;
    private ValueEventListener mDBListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_user);
        loadingDialog = new LoadingDialog(Laundry_user.this);
        mDataList =(RecyclerView)findViewById(R.id.data_paket3);
        mProgressCircle = (ProgressBar) findViewById(R.id.progress_circle3);
        mDataList.setHasFixedSize(true);
        mDataList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        search_Bar = (EditText) findViewById(R.id.search_package3);

        user = new User();
        mUser = new ArrayList<>();

        mAdapter = new UserAdapter(getApplicationContext(), mUser);

        mAdapter.setOnItemClickListener(Laundry_user.this);

        mDataList.setAdapter(mAdapter);


        mdrawer = (DrawerLayout) findViewById(R.id.nav_drawer3);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        mtoggle = new ActionBarDrawerToggle(this, mdrawer,R.string.Open,R.string.Close);
        mdrawer.addDrawerListener(mtoggle);
        mtoggle.syncState();



        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);


        dbr = FirebaseDatabase.getInstance().getReference("user");
        navDrawer();
        forRecycler();
        ToStore();
        ItemSearch();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void navDrawer(){
        nv = (NavigationView) findViewById(R.id.nav_view3);
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
                mUser.clear();

                for (DataSnapshot outletSnapshot : dataSnapshot.getChildren()){
                    User user = outletSnapshot.getValue(User.class);
                    user.setKey(outletSnapshot.getKey());
                    mUser.add(user);
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
        mFAB = (FloatingActionButton) findViewById(R.id.navigation_add3);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), create_user.class));
            }
        });
    }


    @Override
    public void onItemClick(User user) {

    }

    @Override
    public void onDeleteClick(int position) {
        User selecteduser = mUser.get(position);
        final String selectedKey = selecteduser.getKey();

        loadingDialog.startLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismissDialog();
                dbr.child(selectedKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Laundry_user.this, "Berhasil Menghapus data", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Laundry_user.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 2000);
    }

    @Override
    public void onEditClick(User user) {

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
        ArrayList<User> filterList = new ArrayList<>();

        for (User user : mUser){
            if (user.getNama().toLowerCase().contains(text.toLowerCase()) && user.getNama() != null){
                filterList.add(user);
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
