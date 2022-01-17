package com.webcheckers.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.webcheckers.util.Message.Type;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the {@linkplain Message} component.
 *
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 */
@Tag("Util-Tier")
public class MessageTest {

    /**
     * tests creating an info Message
     */
    @Test
    public void testCreateInfoMessage(){
        Message msg = Message.info("Info Message");
        assertTrue(msg instanceof Message);
        assertEquals("Info Message", msg.getText());
        assertEquals(Type.INFO, msg.getType());
    }

    /**
     * tests creating an error Message
     */
    @Test
    public void testCreateErrorMessage(){
        Message msg = Message.error("Error Message");
        assertTrue(msg instanceof Message);
        assertEquals("Error Message", msg.getText());
        assertEquals(Type.ERROR, msg.getType());
    }

    /**
     * tests the Message isSuccessful method
     */
    @Test
    public void testSuccessful(){
        Message msg = Message.info("Some message");
        assertTrue(msg.isSuccessful());
        msg = Message.error("Some message");
        assertFalse(msg.isSuccessful());
    }

    /**
     * tests the Message toString method
     */
    @Test
    public void testToString(){
        Message msg = Message.info("Some message");
        assertTrue(msg.isSuccessful());
        assertEquals("{Msg INFO 'Some message'}", msg.toString());
    }

    /**
     * tests the Message equals method
     */
    @Test
    public void testEquals(){
        String  msgString = "A message";
        Message msg = Message.info(msgString);
        Message msgSame = Message.info(msgString);
        Message msgDiff = Message.info("Different message");
        Message msgSameDiffType = Message.error(msgString);
        
        assertTrue(msg.equals(msg));
        assertTrue(msg.equals(msgSame));
        assertFalse(msg.equals(null));
        assertFalse(msg.equals(msgDiff));
        assertFalse(msg.equals(msgSameDiffType));
        assertFalse(msg.equals(msgString));
    }
}
