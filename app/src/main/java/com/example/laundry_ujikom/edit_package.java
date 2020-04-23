package com.example.laundry_ujikom;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class edit_package extends AppCompatActivity {
    public String id_paket,id_outlet,nama_paket,jenis_paket;
    public Integer harga_paket;
    private RadioGroup mJenis;
    private RadioButton mJenisOption;
    private Button btn_submit;
    private EditText NamaPaket, HargaPaket;
    private PaketLaundry paketLaundry;
    private String Id, value;

    private DatabaseReference dbr;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_package);

        id_paket = getIntent().getStringExtra("id_paket");
        id_outlet = getIntent().getStringExtra("id_outlet");
        nama_paket = getIntent().getStringExtra("testing");
        harga_paket = getIntent().getIntExtra("harga_paket", 0);
        jenis_paket = getIntent().getStringExtra("jenis_paket");

        mJenis = (RadioGroup) findViewById(R.id.hah1);
        btn_submit = (Button) findViewById(R.id.btn_edit);
        NamaPaket = (EditText) findViewById(R.id.nama_Edit);
        HargaPaket = (EditText) findViewById(R.id.Harga_Edit);

        NamaPaket.setText(nama_paket);
        HargaPaket.setText(String.valueOf(harga_paket));

        loadingDialog = new LoadingDialog(edit_package.this);

        dbr = FirebaseDatabase.getInstance().getReference("paket");

        paketLaundry = new PaketLaundry();

        EditPaket();

    }

    public void EditPaket(){
        mJenis.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_selimut_Edit:
                        value = "Selimut";
                        Toast.makeText(edit_package.this, value, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_kiloan_Edit:
                        value = "Kiloan";
                        break;
                }


            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer harga;

//                Random random = new Random();
//                randomnumber = random.nextInt(1000-100)  + 100;

                String nama_paket = paketLaundry.setNama_paket(NamaPaket.getText().toString().trim());
                String harga_paket = HargaPaket.getText().toString();
                String Id_outlet = id_outlet;
                String jenis_paket = value;

                harga = paketLaundry.setHarga(Integer.parseInt(harga_paket));

                if (TextUtils.isEmpty(nama_paket)){
                    Toast.makeText(edit_package.this, "Nama paket tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(harga_paket)){
                    Toast.makeText(edit_package.this, "Harga paket tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }

                Id = id_paket;
                PaketLaundry PL = new PaketLaundry(Id, Id_outlet, harga, nama_paket,jenis_paket);

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
                        Toast.makeText(edit_package.this, "Berhasil menambah data", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Laundry_package.class));
                    }
                });

            }
        });
    }
}
