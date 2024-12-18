package com.alouer;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testValidClientLogin() {
        String input = "yes\n1\n1\njohn.doe@example.com\njohn123\n4\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        outputStream.reset();

        Terminal.run(true);

        String output = outputStream.toString();
        assertTrue(output.contains("Available commands:"));
    }

    @Test
    public void testInvalidClientLogin() {
        String input = "yes\n1\n1\nwrong.email@example.com\nwrongpassword\n-1\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        outputStream.reset();

        Terminal.run(true);

        String output = outputStream.toString();
        assertTrue(output.contains("Invalid client credentials"));
    }

    @Test
    public void testValidInstructorLogin() {
        String input = "yes\n1\n2\nbob.brown@example.com\nbob123\n3\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        outputStream.reset();

        Terminal.run(true);

        String output = outputStream.toString();
        assertTrue(output.contains("Available commands:"));
    }

    @Test
    public void testInvalidInstructorLogin() {
        String input = "yes\n1\n2\nwrong.email@example.com\nwrongpassword\n-1\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        outputStream.reset();

        Terminal.run(true);

        String output = outputStream.toString();
        assertTrue(output.contains("Invalid instructor credentials"));
    }

    @Test
    public void testValidAdminLogin() {
        String input = "yes\n1\n3\nadmin@example.com\npassword123\n4\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        outputStream.reset();

        Terminal.run(true);

        String output = outputStream.toString();
        assertTrue(output.contains("Available commands:"));
    }

    @Test
    public void testInvalidAdminLogin() {
        String input = "yes\n1\n3\nwrong.email@example.com\nwrongpassword\n-1\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        outputStream.reset();

        Terminal.run(true);

        String output = outputStream.toString();
        assertTrue(output.contains("Invalid admin credentials"));
    }
}
