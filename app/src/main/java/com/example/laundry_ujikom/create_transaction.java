package com.example.laundry_ujikom;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.laundry_ujikom.Adapter.CustomerAdapter;
import com.example.laundry_ujikom.Model.Customer;
import com.example.laundry_ujikom.Model.Detail_Transaction;
import com.example.laundry_ujikom.Model.Outlet;
import com.example.laundry_ujikom.Model.PaketLaundry;
import com.example.laundry_ujikom.Model.Transaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class create_transaction extends AppCompatActivity {
    private EditText DueDate,Discount;
    private Spinner namapaket, namacustomer;
    private String batas_waktu,nama,day,item_paket,item_customer,id_transaction;
    private String keys_customer, keys_paket, str1,bvbv,bvbv2;
    private Button btn_transaction;
    private Customer customer;
    private PaketLaundry paketLaundry;
    private ArrayList<String> mCustomer;
    private ArrayList<String> mPaketLaundries;
    private Transaction transaction;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    Outlet outlet, nama1;
    ArrayList<Outlet> mOutlet;

    private LoadingDialog loadingDialog;

    private DatabaseReference dbr_paket,dbr_customer,dbr_transaction,dbr_detail,dbr_user;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);

        initComponent();
        mCustomer = new ArrayList<>();
        mPaketLaundries = new ArrayList<>();
        transaction = new Transaction();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        loadingDialog = new LoadingDialog(create_transaction.this);

        // ngambil tanggal sekarang
        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
        str1 = dateFormat.format(c1.getTime());
        DueDate.setText(str1);

        //ngambil batas waktu pembayaran
        for (int i = 0;i < 7; i++){
            Calendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DATE,i);
            day = dateFormat.format(calendar.getTime());
        }

        dbr_customer = FirebaseDatabase.getInstance().getReference("Customer");
        dbr_paket = FirebaseDatabase.getInstance().getReference("paket");
        dbr_transaction = FirebaseDatabase.getInstance().getReference("transaction");
        dbr_user = FirebaseDatabase.getInstance().getReference("user");
        dbr_detail = FirebaseDatabase.getInstance().getReference("detail_transaction");

        spinnerCustomer();
        spinnerPaket();


//        dbr_detail


        btn_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTransaction();
            }
        });


    }

    private void spinnerPaket() {
        dbr_paket.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                for (DataSnapshot paketSnapshot : dataSnapshot.getChildren()){
                    paketLaundry = paketSnapshot.getValue(PaketLaundry.class);
                    keys_paket = paketSnapshot.child("nama_paket").getValue(String.class);
                    mPaketLaundries.add(keys_paket);

                final ArrayAdapter<String> test2 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,mPaketLaundries);
                test2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                namapaket.setAdapter(test2);
                namapaket.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        item_paket = adapterView.getItemAtPosition(i).toString();
                        if (keys_paket.equals(item_paket)){
                            bvbv2 = dataSnapshot.child(item_paket).getKey();
                        }

                        dbr_paket.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot paketsnapshot : dataSnapshot.getChildren()) {
                                    if (paketsnapshot.child("nama_paket").getValue(String.class).equals(item_paket)) {
                                        bvbv2 = paketsnapshot.getKey();
                                    }
//                                    String haha = dataSnapshot.child(bvbv).child("id_Customer").getValue(String.class);
                                    Toast.makeText(create_transaction.this, bvbv2, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void spinnerCustomer() {
        dbr_customer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for (DataSnapshot customerSnapshot : dataSnapshot.getChildren()){
                    customer = dataSnapshot.getValue(Customer.class);
                    keys_customer = customerSnapshot.child("nama_customer").getValue(String.class);
                    mCustomer.add(keys_customer);


                final ArrayAdapter<String> test = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,mCustomer);
                test.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                namacustomer.setAdapter(test);
                namacustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        item_customer = adapterView.getItemAtPosition(i).toString();
                        if (keys_customer.equals(item_customer)){
                            bvbv = dataSnapshot.child(item_customer).getKey();
                        }
//                        Toast.makeText(create_transaction.this, keys_customer, Toast.LENGTH_SHORT).show();

                        dbr_customer.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot customersnapshot : dataSnapshot.getChildren()){
                                            if (customersnapshot.child("nama_customer").getValue(String.class).equals(item_customer)){
                                                bvbv = customersnapshot.getKey();
                                            }
                                            String haha = dataSnapshot.child(bvbv).child("id_Customer").getValue(String.class);
                                            Toast.makeText(create_transaction.this, bvbv, Toast.LENGTH_SHORT).show();

//                                            customer = customersnapshot.getValue(Customer.class);
//                                            String nm = customersnapshot.getKey();
//                                            keys_customer = customersnapshot.child(nm).child("id_Customer").getValue(String.class);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addTransaction() {
        final String tgl = str1;
        final String tgl_bayar = "Belum Bayar";
        final String status = "baru";
        final String dibayar = "belum_dibayar";
        final Integer biaya_tambahan = 0;

        final String id_user = currentUser.getUid();

        dbr_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String id_outlet = dataSnapshot.child("id_outlet").getValue(String.class);
                id_transaction = dbr_transaction.push().getKey();
                Transaction transaction = new Transaction(id_transaction,id_outlet,bvbv,tgl,day,tgl_bayar,status
                ,dibayar,id_user,biaya_tambahan);
                dbr_transaction.child(id_transaction).setValue(transaction).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String id_detail = dbr_detail.push().getKey();
                        Detail_Transaction detail_transaction = new Detail_Transaction(id_detail,id_transaction,bvbv2);
                        dbr_detail.child(id_detail).setValue(detail_transaction).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingDialog.startLoadingDialog();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingDialog.dismissDialog();
                                    }
                                }, 2000);
                                Toast.makeText(create_transaction.this, "Berhasil menambah data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initComponent() {
        DueDate = (EditText) findViewById(R.id.date_transaction);
        Discount = (EditText) findViewById(R.id.discount_transactions);
        namapaket = (Spinner) findViewById(R.id.spinner_paket);
        namacustomer = (Spinner) findViewById(R.id.spinner_customer);
        btn_transaction = (Button) findViewById(R.id.btn_submit_transaction);

    }


}
