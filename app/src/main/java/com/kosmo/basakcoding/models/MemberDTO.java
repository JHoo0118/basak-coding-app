package com.kosmo.basakcoding.models;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class MemberDTO {
    @SerializedName("memberId")
    private Integer memberId;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("username")
    private String username;

    @SerializedName("emailValidate")
    private String emailValidate;

    @SerializedName("registeredAt")
    private Date registeredAt;

    @SerializedName("loginMethod")
    private String loginMethod;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("emailSecret")
    private String emailSecret;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailValidate() {
        return emailValidate;
    }

    public void setEmailValidate(String emailValidate) {
        this.emailValidate = emailValidate;
    }

    public Date getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
    }

    public String getLoginMethod() {
        return loginMethod;
    }

    public void setLoginMethod(String loginMethod) {
        this.loginMethod = loginMethod;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmailSecret() {
        return emailSecret;
    }

    public void setEmailSecret(String emailSecret) {
        this.emailSecret = emailSecret;
    }
}
