package ru.otus.deposit;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.banknote.Banknote;
import ru.otus.cassette.Cassette;

@SuppressWarnings("java:S6548")
public class DepositService implements Deposit {
    private static final Logger log = LoggerFactory.getLogger(DepositService.class);
    private static DepositService instance;

    private DepositService() {}

    public static DepositService getInstance() {
        if (instance == null) {
            instance = new DepositService();
        }

        return instance;
    }

    @Override
    public void deposit(Banknote banknote, List<? extends Cassette> cassettes) {
        var availableCassette = cassettes.stream()
                .filter(it -> it.getDenomination().equals(banknote.denomination()))
                .filter(Cassette::hasFreeSpace)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't deposit money"));

        availableCassette.deposit();
        log.info("{}₽ was deposited", banknote.denomination().intValue());
    }
}
