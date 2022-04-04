/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import org.junit.*;

import static org.junit.Assert.*;

import java.util.Date;

public class CommentTest {

    // Test the constructor initialises values correctly
    @Test
    public void constructor() {

        Comment c = new Comment("content", "user");

        assertEquals("content", c.getContent());
        assertEquals("user", c.getUname());
    }


//    @Test
//    public void compare() {
//        Date before = new Date(10);
//        Date after = new Date(10000);
//
//        Comment OldComment = new Comment("", "", before);
//        Comment YoungComment = new Comment("", "", after);
//
//        int result;
//        result = OldComment.compareTo(YoungComment);
//        assertTrue(result < 0);
//
//        result = YoungComment.compareTo(OldComment);
//        assertTrue(result > 0);
//
//        result = OldComment.compareTo(OldComment);
//        assertEquals(0, result);
//    }
}
