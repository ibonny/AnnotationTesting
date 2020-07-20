package com.personal.ibonny;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.Scanner;

class NotJSoupObjectException extends Exception {

}

public class MainClass {
    public static <T> T loadData(Class<T> input) throws NotJSoupObjectException {
        if (input.isAnnotationPresent(JSoupProcessor.class) == false) {
            throw new NotJSoupObjectException();
        }

        String filename = input.getAnnotation(JSoupProcessor.class).value();

        File inFile = new File(filename);

        Scanner s;

        try {
            s = new Scanner(inFile);

            s.useDelimiter("\\Z");
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            return null;
        }

        Document doc = Jsoup.parse(s.next(), "", Parser.xmlParser());

        T po;

        try {
            po = input.getDeclaredConstructor().newInstance();
        } catch(Exception e) {
            e.printStackTrace();

            return null;
        }

        for (Field f: po.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(JSoupSelector.class)) {
                String indexValue = f.getAnnotation(JSoupSelector.class).value();

                String value = doc.select(indexValue).first().text();

                f.setAccessible(true);

                if (f.getType() == String.class) {
                    try {
                        f.set(po, value);
                    } catch(IllegalAccessException iae) {
                        System.out.println(iae.getMessage());
                    }

                    continue;
                }

                if (f.getType() == int.class || f.getType() == Integer.class) {
                    try {
                        int intValue = Integer.parseInt(value);

                        f.set(po, intValue);
                    } catch(IllegalAccessException iae) {
                        System.out.println(iae.getMessage());
                    }

                    continue;
                }
            }
        }

        return po;
    }

    public static void main(String[] args) {
        System.out.println("OK, it works.");

        PersonalObject po;

        try {
            po = loadData(PersonalObject.class);
        } catch (NotJSoupObjectException e) {
            e.printStackTrace();

            return;
        }

        if (po == null) {
            System.out.println("Error converting XML to POJO");

            return;
        }

        System.out.println(po.getFourth());

        System.out.println(po.getFifth());
    }
}
