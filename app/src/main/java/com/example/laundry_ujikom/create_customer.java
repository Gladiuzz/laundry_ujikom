package com.example.laundry_ujikom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.laundry_ujikom.Model.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class create_customer extends AppCompatActivity {
    private EditText name,address,phone_number;
    private Spinner gender;
    private LoadingDialog loadingDialog;
    private String id_user,id;
    private Customer customer;
    private Button btn_customer;

    private DatabaseReference dbr;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_customer);
        name = (EditText) findViewById(R.id.name_customer);
        address = (EditText) findViewById(R.id.addres_customer);
        phone_number = (EditText) findViewById(R.id.phone_customer);
        gender = (Spinner) findViewById(R.id.gender_customer);
        btn_customer = (Button) findViewById(R.id.btn_submit_customer);

        mAuth = FirebaseAuth.getInstance();
        id_user = mAuth.getUid();

        loadingDialog = new LoadingDialog(create_customer.this);
        dbr = FirebaseDatabase.getInstance().getReference("Customer");

        customer = new Customer();

        addCustomer();
    }

    private void addCustomer() {
       btn_customer.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String nama_customer = customer.setNama_customer(name.getText().toString().trim());
               String alamat_customer = customer.setAlamat(address.getText().toString().trim());
               String phone_customer = customer.setTlp(phone_number.getText().toString().trim());
               String gender_customer = customer.setGender(gender.getSelectedItem().toString());

               id = dbr.push().getKey();
               Customer customer = new Customer(id,nama_customer,alamat_customer,gender_customer,phone_customer);
               dbr.child(id).setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                       Toast.makeText(create_customer.this, "Berhasil menambah data", Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(getApplicationContext(), laundry_customer.class));
                   }
               });

           }
       });
    }
}
