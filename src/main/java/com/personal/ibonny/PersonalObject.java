package com.personal.ibonny;

import com.personal.ibonny.JSoupHandler.JSoupProcessor;
import com.personal.ibonny.JSoupHandler.JSoupSelector;

//@JSoupProcessor("testout.xml")
@JSoupProcessor("https://www.w3schools.com/xml/plant_catalog.xml")
public class PersonalObject {
    @JSoupSelector("CATALOG PLANT:nth-child(2) COMMON")
    private String fourth;

    @JSoupSelector("first fifth")
    private int fifth;

    public String getFourth() {
        return fourth;
    }

    public void setFourth(String fourth) {
        this.fourth = fourth;
    }

    public int getFifth() {
        return fifth;
    }

    public void setFifth(int fifth) {
        this.fifth = fifth;
    }
}
