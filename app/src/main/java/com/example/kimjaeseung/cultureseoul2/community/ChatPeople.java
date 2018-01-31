package com.example.kimjaeseung.cultureseoul2.community;

import java.io.Serializable;

/**
 * Created by kimjaeseung on 2017. 9. 24..
 */

public class ChatPeople implements Serializable {
    private String uid;
    private String name;
    private String email;
    private String photo;
    private String firebaseKey;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }
}
