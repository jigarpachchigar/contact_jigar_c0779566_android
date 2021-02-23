package com.termtwo.contact_jigar_c0779566_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity  extends AppCompatActivity implements View.OnClickListener{

    EditText edtfname, edtlname, edtphone, edtaddress, edtemail;
    Button btnshow;
    TextView tvmsg;
    boolean  isCheck = false;

    DataBaseHelper mDatabase;

    ArrayList<String> number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtfname = findViewById(R.id.edtFname);
        edtlname = findViewById(R.id.edtLname);
        edtphone = findViewById(R.id.edtPhone);
        edtaddress = findViewById(R.id.edtAddress);
        edtemail = findViewById(R.id.edtEmail);
        tvmsg =  findViewById(R.id.tvDisplay);
        btnshow = findViewById(R.id.btnDisplay);

        findViewById(R.id.btnAddcontact).setOnClickListener(this);
        findViewById(R.id.tvDisplay).setOnClickListener(this);

        btnshow.setOnClickListener(this);

        mDatabase = new DataBaseHelper(this);
        loadData();

        tvmsg.setText("Total Conatcts : " + loadData());
    }

    private int loadData() {

        Cursor c = mDatabase.getAllData();
        number = new ArrayList<>();

        if (c.moveToFirst()){

            do {
                number.add(c.getString(3));

            }while (c.moveToNext());
            c.close();

        }
        return c.getCount();

    }

    @Override
    protected void onStart() {

        super.onStart();
        edtfname.setText("");
        edtlname.setText("");
        edtphone.setText("");
        edtaddress.setText("");
        edtemail.setText("");
        loadData();
        tvmsg.setText("Total Contacts :"+loadData());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnAddcontact:
                addContact();
                break;


            case R.id.btnDisplay:
                Intent intent = new Intent(this, DisplayContact.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

        }
    }

    private void addContact() {

        String fName = edtfname.getText().toString().trim();
        String lName = edtlname.getText().toString().trim();
        String mPhone = edtphone.getText().toString().trim();
        String mAddress = edtaddress.getText().toString().trim();
        String mEmail = edtemail.getText().toString().trim();

        if (fName.isEmpty()){
            edtfname.setError("Please Enter First Name");
            edtfname.requestFocus();
            return;
        }

        if (lName.isEmpty()){
            edtlname.setError("Please Enter Last Name");
            edtlname.requestFocus();
            return;
        }

        if (mPhone.isEmpty()){
            edtphone.setError("Please Enter Phone Number");
            edtphone.requestFocus();
            return;
        }

        if (mAddress.isEmpty()){
            edtaddress.setError("Please Enter Address");
            edtaddress.requestFocus();
            return;
        }

        if (mEmail.isEmpty()){
            edtemail.setError("Please Enter Email ID");
            edtemail.requestFocus();
            return;
        }

        for (int i =0;i<number.size();i++){
            if (number.contains(mPhone)){
                isCheck = true;
            }
            else {
                isCheck = false;
            }
        }


        if (!isCheck){

            if (mDatabase.addUserContact(fName,lName,mPhone,mAddress,mEmail)){
                Toast.makeText(MainActivity.this, "New Contact Saved", Toast.LENGTH_LONG).show();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Confirm Exit..!!!");
                alertDialogBuilder.setMessage("Contact Added Suceesfully !");
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });

                tvmsg.setText("Total Contacts :"+loadData());

            }else {
                Toast.makeText(MainActivity.this, "New Contact Saved", Toast.LENGTH_LONG).show();
            }
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sorry! Contact Already Exists");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }
}