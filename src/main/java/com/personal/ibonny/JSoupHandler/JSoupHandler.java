package com.personal.ibonny.JSoupHandler;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Scanner;

public class JSoupHandler {
    private static String loadFromFileOrURL(String name) {
        String data = "";

        if (name.startsWith("http")) {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            try {
                HttpGet request = new HttpGet(name);

                CloseableHttpResponse response = httpClient.execute(request);

                HttpEntity entity = response.getEntity();

                data = EntityUtils.toString(entity);
            } catch (IOException e) {
                e.printStackTrace();

                return null;
            }
        } else {
            File inFile = new File(name);

            Scanner s;

            try {
                s = new Scanner(inFile);

                s.useDelimiter("\\Z");
            } catch (FileNotFoundException e) {
                e.printStackTrace();

                return null;
            }

            data = s.next();
        }

        return data;
    }

    public static <T> T loadData(String baseSelector, String data, Class<T> input) throws NotJSoupObjectException {
        if (input.isAnnotationPresent(JSoupProcessor.class) == false && baseSelector.equals("")) {
            throw new NotJSoupObjectException();
        }

        if (baseSelector == null || baseSelector.equals("")) {
            String filename = input.getAnnotation(JSoupProcessor.class).value();

            data = loadFromFileOrURL(filename);
        }

        Document doc = Jsoup.parse(data, "", Parser.xmlParser());

        T po;

        try {
            System.out.println(input);

            po = input.getDeclaredConstructor().newInstance();
        } catch(Exception e) {
            e.printStackTrace();

            return null;
        }

        Element baseElement;

        if (baseSelector != null) {
            baseElement = doc.select(baseSelector).first();
        } else {
            baseElement = doc;
        }

        for (Field f: po.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(JSoupSelector.class) == false) {
                continue;
            }

            String indexValue = f.getAnnotation(JSoupSelector.class).value();

            String value;

            try {
                value = baseElement.select(indexValue).first().text();
            } catch(NullPointerException npe) {
                // If you don't find the field, skip it.

                continue;
            }

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

            if (f.getType() == double.class || f.getType() == Double.class) {
                try {
                    double doubleValue = Double.parseDouble(value);

                    f.set(po, doubleValue);
                } catch(IllegalAccessException iae) {
                    System.out.println(iae.getMessage());
                }

                continue;
            }

            if (f.getType() == float.class || f.getType() == Float.class) {
                try {
                    float floatValue = Float.parseFloat(value);

                    f.set(po, floatValue);
                } catch(IllegalAccessException iae) {
                    System.out.println(iae.getMessage());
                }
            }

            if (f.getType() == List.class) {
                //                System.out.println("OK, we're here.");

                System.out.println(((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]);

                for (Element ele: baseElement.select(indexValue)) {
                    System.out.println(ele);

                    Object o = loadData(indexValue, ele.toString(), ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0].getClass());
                }
            }
        }

        return po;
    }
}
