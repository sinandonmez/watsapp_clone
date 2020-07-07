package com.tilda.watsapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AvatarActivity extends AppCompatActivity {

    private User otherUser;
    private TextView txt_isim;
    private ImageView img_resim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        txt_isim = findViewById(R.id.txt_avatar_baslik);
        img_resim = findViewById(R.id.img_avatar_resim);

        otherUser = (User) getIntent().getSerializableExtra("otherUser");

        txt_isim.setText(otherUser.getName());

        if (otherUser.getImageUrl() != null && !otherUser.getImageUrl().equals(""))
            Picasso.get().load(otherUser.getImageUrl()).into(img_resim);
        else
            img_resim.setImageDrawable(getDrawable(R.drawable.kisi_image_bos));

    }
}