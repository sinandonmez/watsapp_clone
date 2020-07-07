package com.tilda.watsapp1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MesajActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Mesaj> mesajlar = new ArrayList<>();
    private AdapterMesaj adapterMesaj;
    private LinearLayoutManager manager;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private FirebaseDatabase db;
    private DatabaseReference ref, refSohbetler, refUsers;
    private ValueEventListener listener;


    FirebaseStorage storage;
    StorageReference storageReference;
    Uri galeriUri;
    Uri downloadUri;

    private User otherUser;

    private FloatingActionButton fab_gonder;
    private EditText edit_mesaj;
    private ImageView img_ekle, img_kamera;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesaj);

        fab_gonder = findViewById(R.id.fab_gonder);
        edit_mesaj = findViewById(R.id.edit_mesaj);
        img_ekle = findViewById(R.id.img_ekle);
        img_kamera = findViewById(R.id.img_kamera);
        edit_mesaj.setFocusable(true);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("watsapp_images");

        otherUser = (User) getIntent().getSerializableExtra("otherUser");

        recyclerView = findViewById(R.id.recyclerView_mesaj);
        adapterMesaj = new AdapterMesaj(this, mesajlar);
        recyclerView.setAdapter(adapterMesaj);

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        //ActionBar kullanıcı resmi ve adı yazılıyor
        //setActionBar();
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.simge);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(otherUser.getName());
        //...........


        db = FirebaseDatabase.getInstance();
        ref = db.getReference("users").child(mUser.getPhoneNumber()).child("contacts").child(otherUser.getPhone()).child("messages");
        listener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mesajlar.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Mesaj m = data.getValue(Mesaj.class);
                    System.out.println(m.getMesaj());
                    mesajlar.add(m);
                }
                adapterMesaj.notifyDataSetChanged();

                manager.scrollToPosition(mesajlar.size() - 1);

                //Okunmamış mesaj sayısını sıfırla
                db.getReference("users").child(mUser.getPhoneNumber()).child("contacts").child(otherUser.getPhone()).child("unreadMessageCount").setValue(0);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fab_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refSohbetler = db.getReference("users");
                Mesaj mesaj = new Mesaj(null, mUser.getPhoneNumber(), otherUser.getPhone(), edit_mesaj.getText().toString(), null, ServerValue.TIMESTAMP);
                refSohbetler.child(mUser.getPhoneNumber()).child("contacts").child(otherUser.getPhone()).child("messages").push().setValue(mesaj);
                refSohbetler.child(otherUser.getPhone()).child("contacts").child(mUser.getPhoneNumber()).child("messages").push().setValue(mesaj);

                Map<String, Object> data = otherUser.toMap(null, null, null, edit_mesaj.getText().toString(), ServerValue.TIMESTAMP);
                refSohbetler.child(mUser.getPhoneNumber()).child("contacts").child(otherUser.getPhone()).updateChildren(data);

                data = MainActivity.currentUser.toMap(null, null, null, edit_mesaj.getText().toString(), ServerValue.TIMESTAMP);
                refSohbetler.child(otherUser.getPhone()).child("contacts").child(mUser.getPhoneNumber()).updateChildren(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Okunmamış mesaj sayısı++
                        refSohbetler.child(otherUser.getPhone()).child("contacts").child(mUser.getPhoneNumber()).child("unreadMessageCount").runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                int count = 0;
                                if (mutableData.getValue()!=null){
                                    count = mutableData.getValue(Integer.class);
                                }
                                count++;
                                mutableData.setValue(count);
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                            }
                        });
                        //...............
                    }
                });
                edit_mesaj.setText("");
                edit_mesaj.setFocusable(true);
            }
        });


        img_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galeriIntent = new Intent(Intent.ACTION_PICK);
                galeriIntent.setType("image/*");
                startActivityForResult(galeriIntent, 123);
            }
        });

        img_kamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MesajActivity.this, "Kamerayı aç.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            galeriUri = data.getData();
            resimYukle(galeriUri);
        }
    }


    private void setActionBar() {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.actionbar, null);
        Picasso.get().load(otherUser.getImageUrl()).into((de.hdodenhof.circleimageview.CircleImageView) mCustomView.findViewById(R.id.circularimageView1));

        if (otherUser.getName() == null || otherUser.getName().equals("")) {
            ((TextView) mCustomView.findViewById(R.id.title_text)).setText(otherUser.getPhone());
        } else {
            ((TextView) mCustomView.findViewById(R.id.title_text)).setText(otherUser.getName());
        }

        setTitle("Başlık");
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ref.removeEventListener(listener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mesaj, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void resimYukle(Uri uri) {
        final StorageReference yukleRef = storageReference.child("resim_" + UUID.randomUUID() + ".jpg");
        yukleRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(MesajActivity.this, "Resim yüklendi.", Toast.LENGTH_SHORT).show();

                        yukleRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                refSohbetler = db.getReference("users");
                                Mesaj mesaj = new Mesaj(null, mUser.getPhoneNumber(), otherUser.getPhone(), null, uri.toString(), ServerValue.TIMESTAMP);
                                refSohbetler.child(mUser.getPhoneNumber()).child("contacts").child(otherUser.getPhone()).child("messages").push().setValue(mesaj);
                                refSohbetler.child(otherUser.getPhone()).child("contacts").child(mUser.getPhoneNumber()).child("messages").push().setValue(mesaj);

                                Map<String, Object> data = otherUser.toMap(null, null, null, edit_mesaj.getText().toString(), ServerValue.TIMESTAMP);
                                refSohbetler.child(mUser.getPhoneNumber()).child("contacts").child(otherUser.getPhone()).updateChildren(data);

                                data = MainActivity.currentUser.toMap(null, null, null, edit_mesaj.getText().toString(), ServerValue.TIMESTAMP);
                                refSohbetler.child(otherUser.getPhone()).child("contacts").child(mUser.getPhoneNumber()).updateChildren(data);

                                //Okunmamış mesaj sayısı++
                                refSohbetler.child(otherUser.getPhone()).child("contacts").child(mUser.getPhoneNumber()).child("unreadMessageCount").runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                        int count = mutableData.getValue(Integer.class);
                                        count++;
                                        mutableData.setValue(count);
                                        return Transaction.success(mutableData);
                                    }

                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                                    }
                                });
                                //...............

                                edit_mesaj.setText("");
                                edit_mesaj.setFocusable(true);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MesajActivity.this, "Resim yüklenemedi." + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}