package com.alouer;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.alouer.models.Client;
import com.alouer.models.Instructor;
import com.alouer.collections.ClientCollection;
import com.alouer.collections.InstructorCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TerminalTest {
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
        String input = "CLIENT\nclient@example.com\nclientPassword\nlogOut\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Client client = new Client("Test", "Client", "client@example.com", "clientPassword");
        ClientCollection.add(client);

        Terminal.run(true);

        String output = outputStream.toString();
        assertTrue(output.contains("Available commands:"));
    }

    @Test
    public void testInvalidClientLogin() {
        String input = "CLIENT\nwrong@example.com\nwrongPassword\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Terminal.run(true);

        String output = outputStream.toString();
        assertTrue(output.contains("Invalid client credentials."));
    }

    @Test
    public void testValidInstructorLogin() {
        String input = "INSTRUCTOR\ninstructor@example.com\ninstructorPassword\nlogOut\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Instructor instructor = new Instructor("Test", "Instructor", "instructor@example.com", "instructorPassword");
        InstructorCollection.add(instructor);

        Terminal.run(true);

        String output = outputStream.toString();
        assertTrue(output.contains("Available commands:"));
    }

    @Test
    public void testInvalidInstructorLogin() {
        String input = "INSTRUCTOR\nwrong@example.com\nwrongPassword\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Terminal.run(true);

        String output = outputStream.toString();
        assertTrue(output.contains("Invalid instructor credentials."));
    }

    @Test
    public void testValidAdminLogin() {
        String input = "ADMINISTRATOR\nadmin@example.com\npassword123\nlogOut\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Terminal.run(true);

        String output = outputStream.toString();
        assertTrue(output.contains("Available commands:"));
    }

    @Test
    public void testInvalidAdminLogin() {
        String input = "ADMINISTRATOR\nwrongAdmin@example.com\nwrongPassword\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Terminal.run(true);

        String output = outputStream.toString();
        assertTrue(output.contains("Invalid admin credentials."));
    }
}
