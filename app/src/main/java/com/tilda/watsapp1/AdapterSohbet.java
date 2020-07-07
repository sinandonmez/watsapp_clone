package com.tilda.watsapp1;

import android.content.Context;
import android.content.Intent;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.SQLOutput;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterSohbet extends RecyclerView.Adapter<AdapterSohbet.MyViewHolder>{
    private Context context;
    private ArrayList<User> kisiler;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private FirebaseDatabase db;
    private DatabaseReference refUsers;

    public AdapterSohbet(Context context, ArrayList<User> kisiler) {
        this.context = context;
        this.kisiler = kisiler;

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        db = FirebaseDatabase.getInstance();
        refUsers = db.getReference("users");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_sohbet, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User u = kisiler.get(position);
//        String dateString = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date((long) u.getLastMessageTime()));
//        holder.tarih.setText(dateString);

        String agoString = Util.getFormattedDate((long) u.getLastMessageTime(), context);
        holder.tarih.setText(agoString);

        holder.mesaj.setText(u.getLastMessage());
        holder.from.setText(u.getName());

        if (u.getImageUrl() != null && !u.getImageUrl().equals(""))
            Picasso.get().load(u.getImageUrl()).into(holder.resim);
        else
            holder.resim.setImageDrawable(context.getDrawable(R.drawable.kisi_image_bos));

        //Okunmamış mesaj sayısı
        if(u.getUnreadMessageCount()>0){
            holder.unread.setVisibility(View.VISIBLE);
            holder.unread.setText(String.valueOf(u.getUnreadMessageCount()));
        }
        else{
            holder.unread.setVisibility(View.INVISIBLE);
            holder.unread.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return kisiler.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tarih;
        private TextView from;
        private TextView mesaj;
        private ImageView resim;
        private TextView unread;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tarih = itemView.findViewById(R.id.cv_sohbet_tarih);
            from = itemView.findViewById(R.id.cv_sohbet_from);
            mesaj = itemView.findViewById(R.id.cv_sohbet_mesaj);
            resim = itemView.findViewById(R.id.cv_sohbet_resim);
            unread = itemView.findViewById(R.id.cv_sohbet_unread);

            resim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, AvatarActivity.class);
                    i.putExtra("otherUser", kisiler.get(getAdapterPosition()));
                    context.startActivity(i);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String otherEmail = kisiler.get(getAdapterPosition()).getPhone();

                    //Kullanıcı bilgilerini al
                    refUsers = db.getReference("users").child(otherEmail);
                    refUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User otherUser = dataSnapshot.getValue(User.class);

                            Intent i = new Intent(context, MesajActivity.class);
                            i.putExtra("otherUser", otherUser);
                            context.startActivity(i);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    }
}
