package com.tilda.watsapp1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterKisiler extends RecyclerView.Adapter<AdapterKisiler.MyViewHolder>{
    private Context context;
    private ArrayList<User> kisiler;
    private int requestCode;

    private FirebaseDatabase db;
    private DatabaseReference refSohbetler, refUsers;
//    private FirebaseAuth mAuth;
//    private FirebaseUser mUser;

    public AdapterKisiler(Context context, ArrayList<User> kisiler, int requestCode) {
        this.context = context;
        this.kisiler = kisiler;
        this.requestCode = requestCode;

//        mAuth = FirebaseAuth.getInstance();
//        mUser = mAuth.getCurrentUser();

        db = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_kisiler, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User u = kisiler.get(position);
        holder.kisi_name.setText(u.getName());
        holder.kisi_phone.setText(u.getPhone());
        Picasso.get().load(u.getImageUrl()).into(holder.kisi_resim);
    }

    @Override
    public int getItemCount() {
        return kisiler.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView kisi_name;
        private TextView kisi_phone;
        private ImageView kisi_resim;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            kisi_name = itemView.findViewById(R.id.cv_kisiler_name);
            kisi_phone = itemView.findViewById(R.id.cv_kisiler_phone);
            kisi_resim = itemView.findViewById(R.id.cv_kisiler_resim);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Kullanıcı bilgilerini al
                    System.out.println("adapter viewholder" + kisiler.get(getAdapterPosition()).getPhone());
                    if (requestCode == 1) {//GirisActivtiy
                        Intent i = new Intent();
                        i.putExtra("otherUser", kisiler.get(getAdapterPosition()).getPhone());
                        ((Activity) context).setResult(Activity.RESULT_OK, i);
                        ((Activity) context).finish();

                    } else if (requestCode == 2) {//SohbetActivtiy
                        refSohbetler = db.getReference("users").child(kisiler.get(getAdapterPosition()).getPhone());
                        refSohbetler.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User otherUser = dataSnapshot.getValue(User.class);
                                if (otherUser == null) {
                                    final User newUser = new User(kisiler.get(getAdapterPosition()).getName(), kisiler.get(getAdapterPosition()).getPhone(), null, null, null, null);
                                    refUsers = db.getReference("users");
                                    refUsers.child(kisiler.get(getAdapterPosition()).getPhone()).setValue(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Intent i = new Intent(context, MesajActivity.class);
                                            i.putExtra("otherUser", newUser);
                                            context.startActivity(i);
                                            ((Activity) context).finish();

                                        }
                                    });
                                } else {
                                    Intent i = new Intent(context, MesajActivity.class);
                                    i.putExtra("otherUser", otherUser);
                                    context.startActivity(i);
                                    ((Activity) context).finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            });
        }
    }
}
