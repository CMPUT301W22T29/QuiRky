/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import org.junit.*;

import static org.junit.Assert.*;

import com.example.quirky.models.Comment;

public class CommentTest {
    Comment c;
    String content, user;

    // Test the constructor initialises values correctly
    @Test
    public void constructor() {
        content = "content";
        user = "users";
        c = new Comment(content, user);

        assertEquals(content, c.getContent());
        assertEquals(user, c.getUname());
    }

    // Follow up for edge cases
    @Test
    public void constructorEdgeCases() {
        content = "";
        user = "";
        c = new Comment(content, user);

        assertEquals(content, c.getContent());
        assertEquals(user, c.getUname());

        content = "123&\n~{aAzZ";
        user = "123&\n~{aAzZ";
        c = new Comment(content, user);

        assertEquals(content, c.getContent());
        assertEquals(user, c.getUname());
    }

    // Test the content setter method
    @Test
    public void setters() {
        c = new Comment("initial content", "a");

        content = "new content";
        c.setContent(content);
        assertEquals(content, c.getContent());

        content = "";
        c.setContent(content);
        assertEquals(content, c.getContent());

        content = "123&\n~{aAzZ";
        c.setContent(content);
        assertEquals(content, c.getContent());
    }
}
