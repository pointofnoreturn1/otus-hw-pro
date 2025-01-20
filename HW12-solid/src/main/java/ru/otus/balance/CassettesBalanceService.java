package ru.otus.balance;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.ATM;
import ru.otus.cassette.Cassette;

@SuppressWarnings("java:S6548")
public class CassettesBalanceService implements CassettesBalance {
    private static final Logger log = LoggerFactory.getLogger(CassettesBalanceService.class);

    @Override
    public void displayBalance(ATM atm) {
        for (var cassette : atm.cassettes()) {
            log.info(
                    "{}₽ cassette: {} banknotes available",
                    cassette.getDenomination().intValue(),
                    cassette.getBanknotesAmount());
        }
    }

    @Override
    public long getBalance(List<? extends Cassette> cassettes) {
        return cassettes.stream().map(Cassette::getBalance).reduce(0L, Long::sum);
    }
}
