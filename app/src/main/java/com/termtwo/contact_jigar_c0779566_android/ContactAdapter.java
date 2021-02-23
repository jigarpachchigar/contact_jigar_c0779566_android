package com.termtwo.contact_jigar_c0779566_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactAdapter extends ArrayAdapter {

    Context mContext;
    int lres;
    List<UserContact> contactList;
    DataBaseHelper mDatabase;
    ArrayList<UserContact> arraylist;

    public ContactAdapter(@NonNull Context mContext, int layoutRes, List<UserContact> uContactList, DataBaseHelper mDatabase) {
        super(mContext, layoutRes,uContactList);

        this.mContext = mContext;
        this.lres = layoutRes;
        this.contactList = uContactList;
        this.arraylist = new ArrayList<UserContact>();
        this.arraylist.addAll(uContactList);
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        View v = inflater.inflate(lres,null);
        TextView tvfname = v.findViewById(R.id.tv_Fname);
        TextView tvlname = v.findViewById(R.id.tv_Lname);
        TextView tvphone = v.findViewById(R.id.tv_Phone);
        TextView tvaddress = v.findViewById(R.id.tv_Address);
        TextView tvemail = v.findViewById(R.id.tv_Email);

        final UserContact ucontact = contactList.get(position);
        tvfname.setText(ucontact.getFname().toUpperCase());
        tvlname.setText(ucontact.getLname());
        tvphone.setText(ucontact.getPhone());
        tvaddress.setText(ucontact.getAddress());
        tvemail.setText(ucontact.getEmail());

        v.findViewById(R.id.btnEditContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact(ucontact);
            }
        });

        v.findViewById(R.id.btnDeleteContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact(ucontact);
            }
        });

        return  v;

    }

    private void updateContact(final UserContact ucontact) {


        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Update Contact");
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View customLayout = inflater.inflate(R.layout.update_contact_details, null);
        builder.setView(customLayout);

        final EditText updateFName = customLayout.findViewById(R.id.update_Fname);
        final EditText updateLName = customLayout.findViewById(R.id.update_Lname);
        final EditText updatePhone = customLayout.findViewById(R.id.update_phone);
        final EditText updateAddress = customLayout.findViewById(R.id.update_Address);
        final EditText updateEmail = customLayout.findViewById(R.id.update_email);

        updateFName.setText(ucontact.getFname());
        updateLName.setText(ucontact.getLname());
        updatePhone.setText(ucontact.getPhone());
        updateAddress.setText(ucontact.getAddress());
        updateEmail.setText(ucontact.getEmail());


        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        customLayout.findViewById(R.id.btn_update_emp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = updateFName.getText().toString().trim();
                String lname = updateLName.getText().toString().trim();

                String phone = updatePhone.getText().toString().trim();
                String address = updateAddress.getText().toString().trim();
                String email = updateEmail.getText().toString().trim();

                if (fname.isEmpty()){
                    updateFName.setError("Please Enter First Name");
                    updateFName.requestFocus();
                    return;

                }

                if (lname.isEmpty()){
                    updateLName.setError("Please Enter Last Name");
                    updateLName.requestFocus();
                    return;
                }

                if (phone.isEmpty()){
                    updatePhone.setError("Please Enter Phone");
                    updatePhone.requestFocus();
                    return;
                }

                if (address.isEmpty()){
                    updateAddress.setError("Please Enter Address");
                    updateAddress.requestFocus();
                    return;
                }

                if (email.isEmpty()){
                    updateEmail.setError("Please Enter Email ID");
                    updateEmail.requestFocus();
                    return;
                }

                if (mDatabase.updateContactDetail(ucontact.getId(),fname,lname,phone,address,email)){
                    Toast.makeText(mContext, "Contact Updated", Toast.LENGTH_SHORT).show();
                    loadData();
                }else {
                    Toast.makeText(mContext, "Contact Not Updated", Toast.LENGTH_SHORT).show();
                }

                alertDialog.dismiss();
            }
        });


    }

    private void deleteContact(final UserContact ucontact) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Are you sure want to delete the contact?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if( mDatabase.deleteUcontact(ucontact.getId())){
                    loadData();
                }


            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void loadData() {

        String sql = "SELECT * FROM contact";
        Cursor c = mDatabase.getAllData();
        contactList.clear();
        if (c.moveToFirst()){


            do {
                contactList.add(new UserContact(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4),c.getString(5)));

            }while (c.moveToNext());
            c.close();

        }
        notifyDataSetChanged();
    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        contactList.clear();
        if (charText.length() == 0) {
            contactList.addAll(arraylist);
        } else {
            for (UserContact uc : arraylist) {
                if (uc.getPhone().toLowerCase(Locale.getDefault())
                        .contains(charText) || uc.getFname().toLowerCase(Locale.getDefault())
                        .contains(charText)|| uc.getLname().toLowerCase(Locale.getDefault())
                        .contains(charText) || uc.getAddress().toLowerCase(Locale.getDefault())
                        .contains(charText) || uc.getEmail().toLowerCase(Locale.getDefault())
                        .contains(charText))
                {
                    contactList.add(uc);
                }
            }
        }
        notifyDataSetChanged();
    }


}
