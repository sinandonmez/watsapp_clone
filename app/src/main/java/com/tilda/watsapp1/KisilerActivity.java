package com.tilda.watsapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KisilerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<User> kisiler = new ArrayList<>();
    private AdapterKisiler adapterKisiler;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private FirebaseDatabase db;
    private DatabaseReference ref;
    private ValueEventListener listener;

    private int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisiler);

        requestCode = getIntent().getIntExtra("requestCode", 0);

        recyclerView = findViewById(R.id.recyclerView_kisiler1);
        adapterKisiler = new AdapterKisiler(this, kisiler, requestCode);
        recyclerView.setAdapter(adapterKisiler);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        getContactNumbers(getContentResolver());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ref.removeEventListener(listener);
    }

    public void getContactNumbers(ContentResolver cr)
    {
        System.out.println("ContentResolver");
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        // use the cursor to access the contacts
        kisiler.clear();
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            // get display name
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            // get phone number
            System.out.println(name + " " + phoneNumber);
            if(PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber.replace(" ",""))){
                User u = new User(name, phoneNumber.replace(" ",""), null, null, null, null);
                kisiler.add(u);
            }

        }
        adapterKisiler.notifyDataSetChanged();

    }


}
