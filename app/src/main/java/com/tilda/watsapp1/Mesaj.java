package com.tilda.watsapp1;

import java.security.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Mesaj {
    private String key;
    private String from;
    private String to;
    private String mesaj;
    private String imageUrl;
    private Object tarihSaat;

    public Mesaj() {
    }

    public Mesaj(String key, String from, String to, String mesaj, String imageUrl, Object tarihSaat) {
        this.key = key;
        this.from = from;
        this.to = to;
        this.mesaj = mesaj;
        this.imageUrl = imageUrl;
        this.tarihSaat = tarihSaat;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Object getTarihSaat() {
        return tarihSaat;
    }

    public void setTarihSaat(Object tarihSaat) {
        this.tarihSaat = tarihSaat;
    }
}
