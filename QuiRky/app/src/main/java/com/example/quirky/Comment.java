package com.example.quirky;

/**
 * This is the comment that will be stored to a Database. The Comment will have
 * the content of the comment, the user name, and the time it was made.
 *
 * @author Raymart Bless C. Datuin
 *
 */
public class Comment {

    private String content, uname, id;

    /**
     * Empty constructor because Firestore tutorial told me to...
     */
    public Comment() {}

    public Comment(String content, String uname) {
        this.content = content;
        this.uname = uname;
        this.id = QRCodeController.SHA256(content); // FIXME: Comment should definitely not make a method call to QRCodeController... Make a comment controller i guess..
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

    public String getId() {
        return this.id;
    }
}
