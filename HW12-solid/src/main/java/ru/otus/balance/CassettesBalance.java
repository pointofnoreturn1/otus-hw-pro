package ru.otus.balance;

import java.util.List;
import ru.otus.ATM;
import ru.otus.cassette.Cassette;

public interface CassettesBalance {
    void displayBalance(ATM atm);

    long getBalance(List<? extends Cassette> cassettes);
}
