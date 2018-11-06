package com.lawyerhub.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.lawyerhub.enums.UserRole;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@IgnoreExtraProperties
@NoArgsConstructor
public class UserModel implements Serializable {
    private String idToken;
    private String name;
    private String email;
    private Uri photoUri;
    private String photo;
    private UserRole role;
    private String specialisation;
    private String city;
    private float consultationFee;

    public List<AppointmentModel> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<AppointmentModel> appointmentList) {
        this.appointmentList = appointmentList;
    }

    private List<AppointmentModel> appointmentList;

    public float getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(float consultationFee) {
        this.consultationFee = consultationFee;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void createUser(String id, String name, String email, String photo, Uri uri) {
        this.idToken = id;
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.photoUri = uri;

    }


//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("idToken", idToken);
//        result.put("name", name);
//        result.put("email", email);
//        result.put("photo", photo);
//        result.put("role", role);
//
//        return result;
//    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
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

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}