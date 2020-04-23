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

import com.example.laundry_ujikom.Model.Transaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class edit_transaction extends AppCompatActivity {
    EditText additional_cost;
    Spinner status,status_paid;
    Button edit_transaction;
    String id_transaksi,id_customer,batas,tgl,dibayar,status_transaksi,tgl_bayar,id_outlet,id_user;
    Integer biaya_tambahan;

    private LoadingDialog loadingDialog;

    private DatabaseReference dbr;
    private Transaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);



        initcomponent();
        initIntent();
        loadingDialog = new LoadingDialog(edit_transaction.this);

        dbr = FirebaseDatabase.getInstance().getReference("transaction");
        transaction = new Transaction();

        edit_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTransaksi();
            }
        });

    }

    private void initIntent() {
//        I.putExtra("id_transaksi", transaction.getId_transaction());
//        I.putExtra("id_customer", transaction.getId_Customer());
//        I.putExtra("batas", transaction.getBatas_waktu());
//        I.putExtra("biaya_tambahan", transaction.getBiaya_tambahan());
//        I.putExtra("tgl", transaction.getTgl());
//        I.putExtra("dibayar", transaction.getDibayar());
//        I.putExtra("status", transaction.getStatus());
//        I.putExtra("tgl_bayar", transaction.getTgl_bayar());
//        I.putExtra("id_outlet", transaction.getId_outlet());
//        I.putExtra("id_user", transaction.getId_user());

        id_transaksi = getIntent().getStringExtra("id_transaksi");
        id_customer = getIntent().getStringExtra("id_customer");
        batas = getIntent().getStringExtra("batas");
        biaya_tambahan = getIntent().getIntExtra("biaya_tambahan",0);
        tgl = getIntent().getStringExtra("tgl");
        dibayar = getIntent().getStringExtra("dibayar");
        status_transaksi = getIntent().getStringExtra("status");
        tgl_bayar = getIntent().getStringExtra("tgl_bayar");
        id_outlet = getIntent().getStringExtra("id_outlet");
        id_user = getIntent().getStringExtra("id_user");

        additional_cost.setText(String.valueOf(biaya_tambahan));
    }

    private void editTransaksi() {
        Integer harga;
        String biaya_Tambahan = additional_cost.getText().toString();
        harga = transaction.setBiaya_tambahan(Integer.parseInt(biaya_Tambahan));
        String statuss = transaction.setStatus(status.getSelectedItem().toString());
        String statuss_paid = transaction.setDibayar(status_paid.getSelectedItem().toString());
        if (statuss_paid.equals("dibayar")){
            Calendar c1 = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
            String str1 = dateFormat.format(c1.getTime());

            tgl_bayar = str1;
        }

        Transaction transaction1 = new Transaction(id_transaksi,id_outlet,id_customer,tgl,batas,tgl_bayar,statuss,statuss_paid
        ,id_user,harga);

        dbr.child(id_transaksi).setValue(transaction1).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                Toast.makeText(edit_transaction.this, "Berhasil menambah data", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), laundry_transaksi.class));
            }
        });
    }

    private void initcomponent() {
        additional_cost = (EditText) findViewById(R.id.additional_cost_transaction);
        status = (Spinner) findViewById(R.id.status_edit);
        status_paid = (Spinner) findViewById(R.id.status_paid_edit);
        edit_transaction = (Button) findViewById(R.id.btn_submit6);
    }
}
