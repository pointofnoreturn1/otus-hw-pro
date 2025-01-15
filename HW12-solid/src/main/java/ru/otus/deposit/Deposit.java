package ru.otus.deposit;

import java.util.List;
import ru.otus.banknote.Banknote;
import ru.otus.cassette.Cassette;

public interface Deposit {
    void deposit(Banknote banknote, List<? extends Cassette> cassettes);
}
