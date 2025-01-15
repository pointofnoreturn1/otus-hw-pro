package ru.otus.banknote;

public enum Denomination {
    HUNDRED(100),
    FIVE_HUNDRED(500),
    THOUSAND(1000),
    FIVE_THOUSAND(5000);

    private final int intValue;

    Denomination(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }
}
