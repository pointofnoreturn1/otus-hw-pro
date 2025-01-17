package ru.otus.cassette;

import ru.otus.denomination.Denomination;

public class CassetteImpl implements Cassette {
    private final Denomination denomination;
    private final int capacity;
    private int banknotesAmount;

    public CassetteImpl(Denomination denomination, int capacity, int banknotesAmount) {
        this.denomination = denomination;
        this.capacity = capacity;
        this.banknotesAmount = banknotesAmount;
    }

    @Override
    public Denomination getDenomination() {
        return denomination;
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
        return banknotesAmount < capacity;
    }

    @Override
    public long getBalance() {
        return (long) banknotesAmount * denomination.intValue();
    }
}
