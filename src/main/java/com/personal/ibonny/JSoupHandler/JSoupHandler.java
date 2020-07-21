package com.personal.ibonny.JSoupHandler;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Scanner;

public class JSoupHandler {
    public static <T> T loadData(Class<T> input) throws NotJSoupObjectException {
        if (input.isAnnotationPresent(JSoupProcessor.class) == false) {
            throw new NotJSoupObjectException();
        }

        String filename = input.getAnnotation(JSoupProcessor.class).value();

        Document doc;

        if (filename.startsWith("http")) {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            try {
                HttpGet request = new HttpGet(filename);

                CloseableHttpResponse response = httpClient.execute(request);

                HttpEntity entity = response.getEntity();

                String result = EntityUtils.toString(entity);

                doc = Jsoup.parse(result, "", Parser.xmlParser());
            } catch (IOException e) {
                e.printStackTrace();

                return null;
            }
        } else {
            File inFile = new File(filename);

            Scanner s;

            try {
                s = new Scanner(inFile);

                s.useDelimiter("\\Z");
            } catch (FileNotFoundException e) {
                e.printStackTrace();

                return null;
            }

            doc = Jsoup.parse(s.next(), "", Parser.xmlParser());
        }

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

                String value;

                try {
                    value = doc.select(indexValue).first().text();
                } catch(NullPointerException npe) {
                    npe.printStackTrace();

                    return null;
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
            }
        }

        return po;
    }
}
