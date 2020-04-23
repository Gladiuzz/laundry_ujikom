package com.example.laundry_ujikom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.laundry_ujikom.Model.PaketLaundry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class create_package extends AppCompatActivity {
    private RadioGroup mJenis;
    private RadioButton mJenisOption;
    private Button btn_submit;
    private EditText NamaPaket, HargaPaket;
    private PaketLaundry paketLaundry;
    private String Id, value,id_outlet,id_user;
    private int randomnumber;
    private FirebaseAuth mAuth;

    private LoadingDialog loadingDialog;


    private DatabaseReference dbr,dbr_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_package);

        mJenis = (RadioGroup) findViewById(R.id.hah);
        btn_submit = (Button) findViewById(R.id.btn_submit1);
        NamaPaket = (EditText) findViewById(R.id.nama_paket);
        HargaPaket = (EditText) findViewById(R.id.Harga_paket);

        mAuth = FirebaseAuth.getInstance();
        id_user = mAuth.getUid();


        loadingDialog = new LoadingDialog(create_package.this);

        dbr = FirebaseDatabase.getInstance().getReference("paket");
        dbr_user = FirebaseDatabase.getInstance().getReference("user").child(id_user);

        paketLaundry = new PaketLaundry();





//        int radiButtonId = mJenis.getCheckedRadioButtonId();
//        switch (radiButtonId){
//            case R.id.rb_selimut:
//                value = "Selimut";
//                Toast.makeText(create_package.this, value, Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.rb_kiloan:
//                value = "Kiloan";
//                break;
//        }




        AddPaket();
//        CheckButton();
    }

    public void AddPaket(){
        mJenis.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_selimut:
                        value = "Selimut";
                        Toast.makeText(create_package.this, value, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_kiloan:
                        value = "Kiloan";
                        break;
                }


            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Integer harga;

//                Random random = new Random();
//                randomnumber = random.nextInt(1000-100)  + 100;

                final String nama_paket = paketLaundry.setNama_paket(NamaPaket.getText().toString().trim());
                String harga_paket = HargaPaket.getText().toString();
                final String jenis_paket = value;

                harga = paketLaundry.setHarga(Integer.parseInt(harga_paket));

                if (TextUtils.isEmpty(nama_paket)){
                    Toast.makeText(create_package.this, "Nama paket tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(harga_paket)){
                    Toast.makeText(create_package.this, "Harga paket tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }

                dbr_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        id_outlet = dataSnapshot.child("id_outlet").getValue(String.class);
                        Id = dbr.push().getKey();
                        PaketLaundry PL = new PaketLaundry(Id, id_outlet, harga, nama_paket,jenis_paket);
                        dbr.child(Id).setValue(PL).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                loadingDialog.startLoadingDialog();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingDialog.dismissDialog();
                                    }
                                }, 2000);
                                Toast.makeText(create_package.this, "Berhasil menambah data", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Laundry_package.class));
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





            }
        });
    }

//    public void CheckButton(){
//        int radioId = mJenis.getCheckedRadioButtonId();
//
//        mJenisOption = findViewById(radioId);
//
//        Toast.makeText(this, "pilih" + mJenisOption.getText(), Toast.LENGTH_SHORT).show();
//    }
}
