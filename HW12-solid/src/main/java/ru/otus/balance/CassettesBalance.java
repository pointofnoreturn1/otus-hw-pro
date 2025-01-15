package ru.otus.balance;

import java.util.List;
import ru.otus.cassette.Cassette;

public interface CassettesBalance {
    void displayBalance(List<? extends Cassette> cassettes);

    long getBalance(List<? extends Cassette> cassettes);
}
