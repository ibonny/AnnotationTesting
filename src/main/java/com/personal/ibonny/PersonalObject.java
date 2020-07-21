package com.personal.ibonny;

import com.personal.ibonny.JSoupHandler.JSoupProcessor;
import com.personal.ibonny.JSoupHandler.JSoupSelector;

import java.util.List;

//@JSoupProcessor("testout.xml")
@JSoupProcessor("https://www.w3schools.com/xml/plant_catalog.xml")
public class PersonalObject {
    @JSoupSelector("CATALOG PLANT:nth-child(2) COMMON")
    private String fourth;

    @JSoupSelector("first fifth")
    private int fifth;

    @JSoupSelector("CATALOG PLANT")
    private List<Plant> plants;

    public String getFourth() {
        return fourth;
    }

    public int getFifth() {
        return fifth;
    }
}
