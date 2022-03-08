// Author: Raymart
// Contact me Through discord for any questions

package com.example.quirky;

// From: youtube.com
// URL: https://www.youtube.com/watch?v=HEJg-hvj0nE
// Author: https://www.youtube.com/channel/UCoQp_Duwqh0JWEZrg4DT2Ug

// This will add the comment to the firebase Database.
public class AddComment implements Comparable {
    private String content, uname, qrCodeHashValue;
    private Object timestamp; // Time stamp is used to tell how old the comment was. This could be used to order the comment
    // Could this timeStamp also be an int? That would be really helpful for the sorting....

    public AddComment(String content, String uname) {
        this.content = content;
        this.uname = uname;
//        this.qrCodeHashValue = qrCodeHashValue; // Won't be needed because the comments are already sorted for every QRCodeHashvalue.
//    this.timestamp = ServerValue.TIMESTAMP;
    }

    // The purpose is to sort by timeStamp. The oldest/smallest value is supposed to be first. So descending order i guess???
    // Used the Ascending order variation done in the beginnersbook.

    // From: beginnersbook.com
    // URL: https://beginnersbook.com/2013/12/java-arraylist-of-object-sort-example-comparable-and-comparator/
    // Specifically on the section for "Sorting of ArrayList<Object> with Comparable"
    // Author: Chaitanya Singh
    @Override
    public int compareTo(Object o) {
        AddComment comment = (AddComment) o;
        Object time = comment.getTimestamp(); // Need to be able to get the int value of this

        Integer intTime = (Integer) time; // Dummy command need to be changed. Need to get the integer value of TimeStamp
        // But first need to know how to get that from the Firebase.

        return (Integer) this.timestamp - intTime; // Same difference
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
