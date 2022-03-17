// Author: Raymart
// Contact me Through discord for any questions

package com.example.quirky;

import java.util.Date;

/**
 * Represents a comment. Holds the message, author, and time it was posted.
 */
public class Comment {
    private String content, uname, id;
    private Date timestamp;

    /**
     * Empty constructor because Firestore tutorial told me to...
     */
    public Comment() {}

    public Comment(String content, String uname, Date timestamp) {
        this.content = content;
        this.id = QRCodeController.SHA256(content); // FIXME: Comment should definitely not make a method call to QRCodeController... Make a comment controller i guess..
        this.uname = uname;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getId() {return this.id;}
}
