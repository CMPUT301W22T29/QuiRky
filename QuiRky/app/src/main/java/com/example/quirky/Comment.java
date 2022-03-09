// Author: Raymart
// Contact me Through discord for any questions

package com.example.quirky;

import java.util.Date;

// This will add the comment to the firebase Database.
public class Comment implements Comparable<Comment>{
    private String content, uname;
    private Date timestamp;

    public Comment(String content, String uname, Date timestamp) {
        this.content = content;
        this.uname = uname;
        this.timestamp = timestamp;
    }

    public int compareTo(Comment compareComment) {
        return this.timestamp.compareTo(compareComment.getTimestamp());
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
