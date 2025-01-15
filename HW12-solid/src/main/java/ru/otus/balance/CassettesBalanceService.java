package ru.otus.balance;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cassette.Cassette;

@SuppressWarnings("java:S6548")
public class CassettesBalanceService implements CassettesBalance {
    private static final Logger log = LoggerFactory.getLogger(CassettesBalanceService.class);
    private static CassettesBalanceService instance;

    private CassettesBalanceService() {}

    public static CassettesBalanceService getInstance() {
        if (instance == null) {
            instance = new CassettesBalanceService();
        }

        return instance;
    }

    @Override
    public void displayBalance(List<? extends Cassette> cassettes) {
        for (var cassette : cassettes) {
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
