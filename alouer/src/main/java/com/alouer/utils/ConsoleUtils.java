package com.alouer.utils;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConsoleUtils {
    public static void printMultipleListsAsTable(List<?>... lists) {
        System.out.println();

        if (lists == null || lists.length == 0) {
            System.out.println("No data provided.");
            return;
        }

        int dataSize = lists[1].size();
        for (int i = 1; i < lists.length; i++) {
            if (lists[i].size() != dataSize) {
                System.out.println("All data lists must have the same length.");
                return;
            }
        }

        List<String> headers = new ArrayList<>();
        for (Object header : lists[0]) {
            headers.add(header.toString());
        }

        List<Integer> maxWidths = new ArrayList<>();

        for (String header : headers) {
            maxWidths.add(header.length());
        }

        for (int i = 1; i < lists.length; i++) {
            List<?> currentList = lists[i];

            for (int j = 0; j < currentList.size(); j++) {
                String value = currentList.get(j).toString();

                int maxWidth = Math.max(maxWidths.get(i - 1), value.length());

                maxWidths.set(i - 1, maxWidth);
            }
        }

        printFormattedRow(headers, maxWidths);
        printDividerRow(maxWidths);

        for (int i = 0; i < dataSize; i++) {
            List<String> row = new ArrayList<>();
            for (int j = 1; j < lists.length; j++) {
                if (i < lists[j].size()) {
                    row.add(lists[j].get(i).toString());
                } else {
                    row.add("");
                }
            }
            printFormattedRow(row, maxWidths);
        }
    }

    public static void printTable(List<?> list, List<String> excludedProperties) {
        System.out.println();
        if (list == null || list.isEmpty()) {
            return;
        }

        Object firstObject = list.get(0);
        Class<?> clazz = firstObject.getClass();

        List<String> headers = new ArrayList<>();
        List<Integer> maxWidths = new ArrayList<>();

        headers.add("ID");
        maxWidths.add("ID".length());

        boolean hasStartTimeGetter = false;
        boolean hasEndTimeGetter = false;

        for (Method method : clazz.getDeclaredMethods()) {
            if (isGetter(method)) {
                String propertyName = getPropertyName(method);
                if (propertyName.equals("Start Time")) {
                    hasStartTimeGetter = true;
                } else if (propertyName.equals("End Time")) {
                    hasEndTimeGetter = true;
                }
            }
        }

        if (hasStartTimeGetter) {
            headers.add("Start Time");
            maxWidths.add("Start Time".length());
        }
        if (hasEndTimeGetter) {
            headers.add("End Time");
            maxWidths.add("End Time".length());
        }

        List<String> otherHeaders = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (isGetter(method)) {
                String propertyName = getPropertyName(method);
                if (!excludedProperties.contains(propertyName)
                        && !propertyName.equals("Start Time")
                        && !propertyName.equals("End Time")) {
                    otherHeaders.add(propertyName);
                }
            }
        }

        for (String header : otherHeaders) {
            headers.add(header);
            maxWidths.add(header.length());
        }

        for (Object obj : list) {
            try {
                int id = getId(obj);
                maxWidths.set(0, Math.max(maxWidths.get(0), String.valueOf(id).length()));

                for (Method method : clazz.getDeclaredMethods()) {
                    if (isGetter(method)) {
                        String propertyName = getPropertyName(method);
                        if (!excludedProperties.contains(propertyName)) {
                            Object value = method.invoke(obj);
                            int width = (value != null ? formatValue(value).length() : 4);
                            int index = headers.indexOf(propertyName);
                            if (index != -1 && width > maxWidths.get(index)) {
                                maxWidths.set(index, width);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        printFormattedRow(headers, maxWidths);
        printDividerRow(maxWidths);

        for (Object obj : list) {
            try {
                List<String> row = new ArrayList<>();
                int id = getId(obj);
                row.add(String.valueOf(id));

                for (String header : headers) {
                    for (Method method : clazz.getDeclaredMethods()) {
                        if (isGetter(method) && getPropertyName(method).equals(header)) {
                            Object value = method.invoke(obj);
                            row.add(formatValue(value));
                        }
                    }
                }

                printFormattedRow(row, maxWidths);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String formatValue(Object value) {
        if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else if (value instanceof LocalDate) {
            return ((LocalDate) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else if (value instanceof Date) {
            return new java.text.SimpleDateFormat("yyyy-MM-dd").format(value);
        }
        return value != null ? value.toString() : "null";
    }

    private static void printFormattedRow(List<String> row, List<Integer> maxWidths) {
        StringBuilder formattedRow = new StringBuilder("|");
        for (int i = 0; i < row.size(); i++) {
            formattedRow.append(String.format(" %-" + maxWidths.get(i) + "s |", row.get(i)));
        }
        System.out.println(formattedRow.toString());
    }

    private static void printDividerRow(List<Integer> maxWidths) {
        StringBuilder dividerRow = new StringBuilder("|");
        for (int width : maxWidths) {
            for (int i = 0; i < width + 2; i++) {
                dividerRow.append("-");
            }
            dividerRow.append("|");
        }
        System.out.println(dividerRow.toString());
    }

    private static boolean isGetter(Method method) {
        return !method.getName().equals("getId") && method.getName().startsWith("get")
                && method.getParameterCount() == 0;
    }

    private static String getPropertyName(Method method) {
        String name = method.getName().substring(3);
        StringBuilder formattedName = new StringBuilder();
        for (char c : name.toCharArray()) {
            if (Character.isUpperCase(c) && formattedName.length() > 0) {
                formattedName.append(" ");
            }
            formattedName.append(c);
        }
        return formattedName.toString().substring(0, 1).toUpperCase() + formattedName.toString().substring(1);
    }

    private static Integer getId(Object obj) throws Exception {
        Method[] methods = obj.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("getId") && method.getReturnType() == Integer.class) {
                return (Integer) method.invoke(obj);
            }
        }
        throw new IllegalArgumentException("No valid getId method found.");
    }

    public static void printSystemLogo() {
        System.out.println(
                "                                                                                                                    \n"
                        +
                        "               AAA               lllllll                                                                            \n"
                        +
                        "              A:::A              l:::::l                                                                            \n"
                        +
                        "             A:::::A             l:::::l                                                                            \n"
                        +
                        "            A:::::::A            l:::::l                                                                            \n"
                        +
                        "           A:::::::::A            l::::l    ooooooooooo   uuuuuu    uuuuuu      eeeeeeeeeeee    rrrrr   rrrrrrrrr   \n"
                        +
                        "          A:::::A:::::A           l::::l  oo:::::::::::oo u::::u    u::::u    ee::::::::::::ee  r::::rrr:::::::::r  \n"
                        +
                        "         A:::::A A:::::A          l::::l o:::::::::::::::ou::::u    u::::u   e::::::eeeee:::::eer:::::::::::::::::r \n"
                        +
                        "        A:::::A   A:::::A         l::::l o:::::ooooo:::::ou::::u    u::::u  e::::::e     e:::::err::::::rrrrr::::::r\n"
                        +
                        "       A:::::A     A:::::A        l::::l o::::o     o::::ou::::u    u::::u  e:::::::eeeee::::::e r:::::r     r:::::r\n"
                        +
                        "      A:::::AAAAAAAAA:::::A       l::::l o::::o     o::::ou::::u    u::::u  e:::::::::::::::::e  r:::::r     rrrrrrr\n"
                        +
                        "     A:::::::::::::::::::::A      l::::l o::::o     o::::ou::::u    u::::u  e::::::eeeeeeeeeee   r:::::r            \n"
                        +
                        "    A:::::AAAAAAAAAAAAA:::::A     l::::l o::::o     o::::ou:::::uuuu:::::u  e:::::::e            r:::::r            \n"
                        +
                        "   A:::::A             A:::::A   l::::::lo:::::ooooo:::::ou:::::::::::::::uue::::::::e           r:::::r            \n"
                        +
                        "  A:::::A               A:::::A  l::::::lo:::::::::::::::o u:::::::::::::::u e::::::::eeeeeeee   r:::::r            \n"
                        +
                        " A:::::A                 A:::::A l::::::l oo:::::::::::oo   uu::::::::uu:::u  ee:::::::::::::e   r:::::r            \n"
                        +
                        "AAAAAAA                   AAAAAAAllllllll   ooooooooooo       uuuuuuuu  uuuu    eeeeeeeeeeeeee   rrrrrrr            \n"
                        +
                        "                                                                                                                    ");

    }
}
