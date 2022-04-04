package com.example.quirky;

/**
 * This is the comment that will be stored to a Database. The Comment will have
 * the content of the comment, and the user who wrote it
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

    /**
     * Constructor to initialise content and author
     * @param content The contents of the comment
     * @param uname The user who wrote it
     */
    public Comment(String content, String uname) {
        this.content = content;
        this.uname = uname;
        this.id = QRCodeController.SHA256(content);
    }

    /**
     * Getter for content
     * @return The content of the QRCode
     */
    public String getContent() {
        return content;
    }

    /**
     * Getter for author
     * @return The username of the player who wrote it
     */
    public String getUname() {
        return uname;
    }

    /**
     * Setter for content
     * @param content The new content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Getter for ID
     * @return The SHA256 hash of the content, for Firebase storage purposes.
     */
    public String getId() {
        return this.id;
    }
}
