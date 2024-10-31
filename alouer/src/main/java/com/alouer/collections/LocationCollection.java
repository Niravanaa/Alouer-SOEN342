package com.alouer.collections;

import java.util.ArrayList;
import java.util.List;
import com.alouer.models.Location;

public class LocationCollection {
    private static List<Location> locations = new ArrayList<>();
    private static int nextId = 0;

    public static List<Location> getLocations() {
        return locations;
    }

    public static Location find(int id) {
        if (id < 0 || id >= locations.size()) {
            return null;
        }
        return locations.get(id);
    }

    public static boolean add(Location location) {
        location.setId(nextId);
        locations.add(location);
        nextId++;
        return true;
    }
}
