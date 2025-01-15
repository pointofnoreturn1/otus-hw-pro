package ru.otus.withdraw;

import java.util.List;
import ru.otus.cassette.Cassette;

public interface Withdraw {
    void withdraw(int amount, List<? extends Cassette> cassettes);
}
