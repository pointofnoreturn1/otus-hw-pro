package ru.otus.cassette;

import ru.otus.denomination.Denomination;

public interface Cassette {
    Denomination getDenomination();

    int getBanknotesAmount();

    void deposit();

    void withdraw(int amount);

    boolean hasFreeSpace();

    long getBalance();
}
