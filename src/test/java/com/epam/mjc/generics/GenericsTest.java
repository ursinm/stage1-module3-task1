package com.epam.mjc.generics;

import org.junit.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GenericsTest {

    Generics generics = new Generics();

    @Test
public void testBoxingMethod() {
    try {
        List<List<String>> list = generics.boxingMethod("name");
        assertEquals("List size is incorrect", 1, list.size());
        assertEquals("Nested list size is incorrect", 1, list.get(0).size());
        assertEquals("Value inside nested list is incorrect", "name", list.get(0).get(0));
    } catch (Exception ex) {
        throw new Error("Method should compile", ex);
    }
}


    @Test
    public void testBoxingMethodIsNotRaw() {
        List<String> list = parseGenericsClass();
        assertFalse("Class shouldn't be empty", list.isEmpty());
        assertTrue("In line " + (list.indexOf(getLineWithString(list, "boxingMethod")) + 1) + ", List shouldn't be raw",
                getLineWithString(list, "boxingMethod").contains("List<List<String>>"));
        assertTrue("In line " + (list.indexOf(getLineWithString(list, "firstList =")) + 1) + ", List shouldn't be raw",
                getLineWithString(list, "firstList =").contains("List<String>"));
        assertTrue("In line " + (list.indexOf(getLineWithString(list, "secondList =")) + 1) + ", List shouldn't be raw",
                getLineWithString(list, "secondList =").contains("List<String>"));

    }

    @Test
    public void testGenericMethod() {
        assertEquals("Generic method failed", generics.genericMethod("string"), "string");
        assertEquals("Generic method failed", generics.genericMethod(generics), generics);
        assertEquals("Generic method failed", generics.genericMethod(100), Integer.valueOf(100));
    }

    @Test
    public void testGenericMethodUseGenerics() {
        List<String> list = parseGenericsClass();
        assertTrue(" genericMethod should use generics",
                getLineWithString(list, "genericMethod").contains("<"));
        assertTrue(" genericMethod should use generics",
                getLineWithString(list, "genericMethod").contains(">"));
    }

    @Test
public void testGenericClone() {
    List<Number> number = new ArrayList<>();
    List<Integer> integer = Arrays.asList(1, 2);
    try {
        generics.cloneMethod(number, integer);
        assertEquals("Size of consumer list is incorrect", 2, number.size());
        assertTrue("Consumer list should contain 1", number.contains(1));
        assertTrue("Consumer list should contain 2", number.contains(2));
    } catch (Exception ex) {
        throw new Error("Clone method failed", ex);
    }
}

    @Test
    public void testGenericCloneHasIsNotRaw() {
        List<String> list = parseGenericsClass();
        assertTrue("In line " + (list.indexOf(getLineWithString(list, "clone")) + 1) + ", List shouldn't be raw",
                getLineWithString(list, "clone").contains("List<? super T>"));
        assertTrue("In line " + (list.indexOf(getLineWithString(list, "clone")) + 1) + ", List shouldn't be raw",
                getLineWithString(list, "clone").contains("List<? extends T>"));
    }

    private String getLineWithString(List<String> list, String str) {
        for (String s : list
        ) {
            if (s.contains(str)) {
                return s;
            }
        }
        return null;
    }

    private List<String> parseGenericsClass() {
        List<String> result = new ArrayList<>();

        try (FileReader f = new FileReader("src/main/java/com/epam/mjc/generics/Generics.java")) {
            StringBuilder sb = new StringBuilder();
            while (f.ready()) {
                char c = (char) f.read();
                if (c == '\n') {
                    result.add(sb.toString());
                    sb = new StringBuilder();
                } else {
                    sb.append(c);
                }
            }
            if (sb.length() > 0) {
                result.add(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
