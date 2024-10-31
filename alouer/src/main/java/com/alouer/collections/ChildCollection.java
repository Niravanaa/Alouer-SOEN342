package com.alouer.collections;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import com.alouer.models.Child;

public class ChildCollection {
    private static List<Child> children = new ArrayList<>();
    private static int nextId = 0;

    public static List<Child> getChildren() {
        return children;
    }

    public static Child find(int id) {
        for (Child child : children) {
            if (child.getId() == id) {
                return child;
            }
        }
        return null;
    }

    public static boolean add(Child child) {
        child.setId(nextId);
        children.add(child);
        nextId++;
        return true;
    }

    public static boolean validateChild(int clientId, String firstName, String lastName, LocalDate dateOfBirth) {
        for (Child child : children) {
            if (child.getParentId() == clientId &&
                    child.getFirstName().equalsIgnoreCase(firstName) &&
                    child.getLastName().equalsIgnoreCase(lastName) &&
                    child.getDateOfBirth().equals(dateOfBirth)) {
                return true;
            }
        }
        return false;
    }

    public static boolean createChild(int clientId, String firstName, String lastName, LocalDate dateOfBirth) {
        Child newChild = new Child(firstName, lastName, dateOfBirth, clientId);

        add(newChild);

        return true;
    }
}
