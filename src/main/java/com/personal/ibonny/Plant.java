package com.personal.ibonny;

import com.personal.ibonny.JSoupHandler.JSoupSelector;

public class Plant {
    @JSoupSelector("COMMON")
    private String common;

    @JSoupSelector("BOTANICAL")
    private String botanical;

    public Plant() {}
}
