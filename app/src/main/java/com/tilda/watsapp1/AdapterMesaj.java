package com.tilda.watsapp1;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterMesaj extends RecyclerView.Adapter<AdapterMesaj.MyViewHolder> {
    private Context context;
    private ArrayList<Mesaj> mesajlar;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public AdapterMesaj(Context context, ArrayList<Mesaj> mesajlar) {
        this.context = context;
        this.mesajlar = mesajlar;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_mesaj, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Mesaj m = mesajlar.get(position);
        holder.mesaj.setText(m.getMesaj());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = cal.getTimeZone();
        sdf.setTimeZone(timeZone);
        String timeString = sdf.format(new Date((long) m.getTarihSaat()));
        holder.saat.setText(timeString);

        //System.out.println(mesajlar.get(position).getFrom() + " = " + mUser.getPhoneNumber());
        if (mesajlar.get(position).getFrom().equals(mUser.getPhoneNumber())) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            holder.bosluk_sol.setLayoutParams(param);
            holder.cardview.setBackgroundColor(Color.rgb(217, 237, 199));
        } else {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.0f
            );
            holder.bosluk_sol.setLayoutParams(param);
            holder.cardview.setBackgroundColor(Color.WHITE);
        }
        if (m.getImageUrl() != null && !m.getImageUrl().equals("")) {
            holder.resim.setVisibility(View.VISIBLE);
            Picasso.get().load(m.getImageUrl()).into(holder.resim);
        }
        else {
            holder.resim.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mesajlar.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mesaj;
        private TextView saat;
        private TextView bosluk_sol;
        private CardView cardview;
        private ImageView resim;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mesaj = itemView.findViewById(R.id.txt_mesaj);
            saat = itemView.findViewById(R.id.txt_saat);
            bosluk_sol = itemView.findViewById(R.id.bosluk_sol);
            cardview = itemView.findViewById(R.id.cardview_mesaj);
            resim = itemView.findViewById(R.id.img_mesaj_resim);
        }
    }
}