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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.laundry_ujikom.Adapter.CustomerAdapter;
import com.example.laundry_ujikom.Adapter.OutletAdapter;
import com.example.laundry_ujikom.Model.Customer;
import com.example.laundry_ujikom.Model.Outlet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class laundry_customer extends AppCompatActivity {
    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle mtoggle;
    private NavigationView nv;
    private RecyclerView mDataList;
    private CustomerAdapter mAdapter;
    private List<Customer> mCustomer;
    private Customer customer;
    private ProgressBar mProgressCircle;
    private FloatingActionButton mFAB;

    private EditText search_Bar;
    private LoadingDialog loadingDialog;

    private DatabaseReference dbr;
    private ValueEventListener mDBListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_customer);
        loadingDialog = new LoadingDialog(laundry_customer.this);
        mDataList =(RecyclerView)findViewById(R.id.data_paket5);
        mProgressCircle = (ProgressBar) findViewById(R.id.progress_circle5);
        mDataList.setHasFixedSize(true);
        mDataList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        search_Bar = (EditText) findViewById(R.id.search_package5);

        customer = new Customer();
        mCustomer = new ArrayList<>();

        mAdapter = new CustomerAdapter(getApplicationContext(), mCustomer);

//        mAdapter.setOnItemClickListener(laundry_customer.this);

        mDataList.setAdapter(mAdapter);


        mdrawer = (DrawerLayout) findViewById(R.id.nav_drawer5);
        Toolbar toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        mtoggle = new ActionBarDrawerToggle(this, mdrawer,R.string.Open,R.string.Close);
        mdrawer.addDrawerListener(mtoggle);
        mtoggle.syncState();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbr = FirebaseDatabase.getInstance().getReference("Customer");


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
        nv = (NavigationView) findViewById(R.id.nav_view5);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

//                if (id == R.id.navigation_home){
//                    startActivity(new Intent(getApplicationContext(), home.class));
//                    finish();
//                }

                return true;
            }
        });
    }

    private void forRecycler(){
        mDBListener = dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mCustomer.clear();

                for (DataSnapshot customersnapshot : dataSnapshot.getChildren()){
                    Customer customer = customersnapshot.getValue(Customer.class);
//                    customer.setMkey(customersnapshot.getKey());
                    mCustomer.add(customer);
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
        mFAB = (FloatingActionButton) findViewById(R.id.navigation_add5);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), create_customer.class));
            }
        });
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
        ArrayList<Customer> filterList = new ArrayList<>();

        for (Customer customer : mCustomer){
            if (customer.getNama_customer().toLowerCase().contains(text.toLowerCase()) && customer.getNama_customer() != null){
                filterList.add(customer);
            }
        }

        mAdapter.fiterList(filterList);
    }
}
