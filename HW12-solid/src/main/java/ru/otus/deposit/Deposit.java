package ru.otus.deposit;

import ru.otus.ATM;
import ru.otus.denomination.Denomination;

public interface Deposit {
    void deposit(Denomination denomination, ATM atm);
}
