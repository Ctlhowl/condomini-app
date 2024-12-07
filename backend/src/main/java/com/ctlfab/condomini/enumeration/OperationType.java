package com.ctlfab.condomini.enumeration;

public enum OperationType {
    IN("Entrata"),
    OUT("Uscita");

    private String opertation;


    OperationType(String operation) {
        this.opertation = operation;
    }

    public String getOperation() {
        return opertation;
    }
}
