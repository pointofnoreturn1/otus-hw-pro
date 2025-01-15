package ru.otus.cassette;

import ru.otus.banknote.Banknote;
import ru.otus.banknote.Denomination;

public class CassetteImpl implements Cassette {
    private final Banknote banknote;
    private final int initialCapacity;
    private int banknotesAmount;

    public CassetteImpl(Banknote banknote, int initialCapacity, int banknotesAmount) {
        this.banknote = banknote;
        this.initialCapacity = initialCapacity;
        this.banknotesAmount = banknotesAmount;
    }

    @Override
    public Denomination getDenomination() {
        return banknote.denomination();
    }

    @Override
    public int getBanknotesAmount() {
        return banknotesAmount;
    }

    @Override
    public void deposit() {
        banknotesAmount++;
    }

    @Override
    public void withdraw(int amount) {
        banknotesAmount -= amount;
    }

    @Override
    public boolean hasFreeSpace() {
        return banknotesAmount < initialCapacity;
    }

    @Override
    public long getBalance() {
        return (long) banknotesAmount * banknote.denomination().intValue();
    }
}
