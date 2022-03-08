package com.example.quirky;


// From youtube.com
// URL: https://www.youtube.com/watch?v=HEJg-hvj0nE
// Author: https://www.youtube.com/channel/UCoQp_Duwqh0JWEZrg4DT2Ug

// This will add the comment to the firebase Database.
public class AddComment {
    private String content, uid, uimg, uname;
    private Object timestamp;

    public AddComment(String content, String uid, String uimg, String uname) {
        this.content = content;
        this.uid = uid;
        this.uimg = uimg;
        this.uname = uname;
//    this.timestamp = ServerValue.TIMESTAMP;

    }

    public String getContent() {
        return content;
    }

    public String getUid() {
        return uid;
    }

    public String getUimg() {
        return uimg;
    }

    public String getUname() {
        return uname;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
