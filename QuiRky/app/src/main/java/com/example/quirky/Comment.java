// Author: Raymart
// Contact me Through discord for any questions

package com.example.quirky;

import java.util.Date;

/**
 * Represents a comment. Holds the message, author, and time it was posted.
 */
public class Comment {
    private String content, uname;
    private Date timestamp;

    /**
     * Empty constructor because Firestore tutorial told me to...
     */
    public Comment() {}

    public Comment(String content, String uname, Date timestamp) {
        this.content = content;
        this.uname = uname;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public String getUname() {
        return uname;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
