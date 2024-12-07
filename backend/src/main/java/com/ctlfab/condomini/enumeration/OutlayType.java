package com.ctlfab.condomini.enumeration;

public enum OutlayType {
    ORDINARY("Ordinaria"),
    EXTRAORDINARY("Straordinaria");

    private String outlay;

    OutlayType(String outlay) {
        this.outlay = outlay;
    }

    public String getOutlay() {
        return outlay;
    }
}
