package com.personal.ibonny;

@JSoupProcessor("testout.xml")
public class PersonalObject {
    @JSoupSelector("first second third fourth")
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
