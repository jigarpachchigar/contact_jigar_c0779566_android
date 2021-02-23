package com.termtwo.contact_jigar_c0779566_android;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DisplayContact extends AppCompatActivity {

    DataBaseHelper mDatabase;
    EditText etSerchText;
    List<UserContact> ucontactList;

    ListView lvcontacts;
    ContactAdapter contactAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);

        mDatabase = new DataBaseHelper(this);
        lvcontacts = findViewById(R.id.lvuserContact);
        etSerchText = findViewById(R.id.searchView);
        ucontactList = new ArrayList<>();
        loadData();

        contactAdpter = new ContactAdapter(this,R.layout.cell_contact, ucontactList,mDatabase);
        lvcontacts.setTextFilterEnabled(true);
        lvcontacts.setAdapter(contactAdpter);

        etSerchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                String text = etSerchText.getText().toString();
                (contactAdpter).filter(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void loadData() {

        Cursor cursor = mDatabase.getAllData();
        if (cursor.moveToFirst()){

            do {
                ucontactList.add(new UserContact(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)));


            }while (cursor.moveToNext());

            cursor.close();
        }
    }
}