package com.personal.ibonny;

import com.personal.ibonny.JSoupHandler.NotJSoupObjectException;

import static com.personal.ibonny.JSoupHandler.JSoupHandler.loadData;

public class MainClass {
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
