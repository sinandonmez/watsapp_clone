package com.tilda.watsapp1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GirisActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private EditText eAd, eSifre, ePhone, eSmsKodu;
    Button btn_kodGonder, btn_giris;

    private FirebaseDatabase db;
    private DatabaseReference refUsers, refSohbetler, refDurum, refAramalar, refKisiler;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        ePhone = findViewById(R.id.edit_phone);
        eSmsKodu = findViewById(R.id.edit_smsKodu);
        btn_kodGonder = findViewById(R.id.btn_kodGonder);
        btn_giris = findViewById(R.id.btn_giris);
        btn_giris.setEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if(mUser!=null){
            dbUserKayitTelefon();
        }
    }

    public void btnGirisPhone(View v) {
        final String telefon = ePhone.getText().toString();
        if (!ePhone.equals("")) {
            PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    // This callback will be invoked in two situations:
                    // 1 - Instant verification. In some cases the phone number can be instantly
                    //     verified without needing to send or enter a verification code.
                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                    //     detect the incoming verification SMS and perform verification without
                    //     user action.
                    System.out.println("onVerificationCompleted:" + credential);

                    signInWithPhoneAuthCredential(credential);
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.
                    System.out.println("onVerificationFailed" + e.getMessage());

                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                        // ...
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        // ...
                    }

                    // Show a message and update the UI
                    // ...
                }

                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
                    System.out.println("onCodeSent:" + verificationId);
                    System.out.println("token:" + token);

                    // Save verification ID and resending token so we can use them later
                    mVerificationId = verificationId;
                    mResendToken = token;
                    Toast.makeText(GirisActivity.this, "Kod gönderildi.", Toast.LENGTH_SHORT).show();
                    btn_giris.setEnabled(true);
                }
            };

            PhoneAuthProvider.getInstance().verifyPhoneNumber(telefon, 60, TimeUnit.SECONDS, GirisActivity.this, mCallbacks);

        };
    }

    public void btnSmsKodu(View view){
        final String smsKodu = eSmsKodu.getText().toString();
        if (!smsKodu.equals("")) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, smsKodu);
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            System.out.println("signInWithCredential:success");

                            mUser = task.getResult().getUser();
                            dbUserKayitTelefon();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            System.out.println("signInWithCredential:failure   :  " + task.getException().getMessage());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    public void getUserToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        System.out.println(token);

                    }
                });
    }

    public void dbUserKayitTelefon(){
        mUser = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        refUsers = db.getReference("users");
        refUsers.child(mUser.getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final int count = (int) dataSnapshot.getChildrenCount();
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.w("TAG", "getInstanceId failed", task.getException());
                                    return;
                                }

                                // Get new Instance ID token
                                String token = task.getResult().getToken();
                                System.out.println(token);
                                if (count == 0) {
                                    //Eğer kullanıcı kayıtlı değilse kaydet

                                    MainActivity.currentUser = new User(null, mUser.getPhoneNumber(), token, null, null, null);
                                    refUsers.child(MainActivity.currentUser.getPhone()).setValue(MainActivity.currentUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //Mainactivity'e geç
                                            Intent i = new Intent(GirisActivity.this, MainActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    });

                                } else {//eğer kullanıcı kayıtlı ise bilgilerini al
                                    refUsers = db.getReference("users");
                                    refUsers.child(mUser.getPhoneNumber()).child("token").setValue(token);
                                    refUsers.child(mUser.getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            MainActivity.currentUser = dataSnapshot.getValue(User.class);

                                            Intent i = new Intent(GirisActivity.this, MainActivity.class);
                                            startActivity(i);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void btnKisiSec(View view){
        Intent i = new Intent(GirisActivity.this, KisilerActivity.class);
        i.putExtra("requestCode", 1);
        startActivityForResult(i,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            String secilenTelefon = data.getStringExtra("otherUser");
            ePhone.setText(secilenTelefon);
        }
    }
}
