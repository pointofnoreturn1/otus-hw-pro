package ru.otus.deposit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.ATM;
import ru.otus.cassette.Cassette;
import ru.otus.denomination.Denomination;

@SuppressWarnings("java:S6548")
public class DepositService implements Deposit {
    private static final Logger log = LoggerFactory.getLogger(DepositService.class);

    @Override
    public void deposit(Denomination denomination, ATM atm) {
        try {
            var availableCassette = atm.cassettes().stream()
                    .filter(it -> it.getDenomination().equals(denomination))
                    .filter(Cassette::hasFreeSpace)
                    .findFirst()
                    .orElseThrow(
                            () -> new RuntimeException("Can't deposit money: there are no available cassettes with "
                                    + denomination.intValue() + "₽ denomination"));

            availableCassette.deposit();
            log.info("{}₽ was deposited", denomination.intValue());
        } catch (RuntimeException e) {
            log.info(e.getMessage());
        }
    }
}
