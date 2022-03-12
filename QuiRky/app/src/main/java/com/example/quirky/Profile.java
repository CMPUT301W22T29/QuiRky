package com.example.quirky;

import java.util.ArrayList;

public class Profile {
    private String uname;
    private String email;
    private String phone;

    // Array list of QRCode id's to track which codes the user has scanned. TODO: determine if the array should hold QRCodes or just their id's.
    private ArrayList<String> scanned;
    public Profile(String uname, String email, String phone, ArrayList<String> scanned) {
        this.uname = uname;
        this.email = email;
        this.phone = phone;
        this.scanned = scanned;
    }

    public Profile(String uname) {
        this.uname = uname;
        this.scanned = new ArrayList<>();
        this.email = "";
        this.phone = "";
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<String> getScanned() {
        return scanned;
    }

    public void addScanned(String qrId) {
        scanned.add(qrId);
    }

    public void removeScanned(String qrId) {
        scanned.remove(qrId);
    }
}
