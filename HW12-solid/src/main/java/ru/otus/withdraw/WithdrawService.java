package ru.otus.withdraw;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.ATM;
import ru.otus.balance.CassettesBalance;
import ru.otus.cassette.Cassette;
import ru.otus.denomination.Denomination;

@SuppressWarnings({"java:S112", "java:S6548", "java:S1192"})
public class WithdrawService implements Withdraw {
    private static final Logger log = LoggerFactory.getLogger(WithdrawService.class);
    private final CassettesBalance cassettesBalance;

    public WithdrawService(CassettesBalance cassettesBalance) {
        this.cassettesBalance = cassettesBalance;
    }

    @Override
    public void withdraw(int amount, ATM atm) {
        var cassettes = atm.cassettes();

        try {
            if (cassettesBalance.getBalance(cassettes) < amount) {
                throw new RuntimeException("Can't withdraw " + amount + "₽: not enough money");
            }

            if (!amountIsValid(amount, cassettes)) {
                throw new RuntimeException("Amount " + amount + "₽ is invalid");
            }

            var banknotes = calculateDenominations(amount, cassettes);
            if (empty(banknotes)) {
                throw new RuntimeException("Can't withdraw " + amount + "₽: not enough money");
            }

            for (var cassette : cassettes) {
                var denomination = cassette.getDenomination();
                int withdrawalAmount = banknotes.get(denomination);
                if (withdrawalAmount == 0) {
                    continue;
                }
                cassette.withdraw(withdrawalAmount);

                log.info("{}x{}₽ were given out", withdrawalAmount, denomination.intValue());
            }
        } catch (RuntimeException e) {
            log.info(e.getMessage());
        }
    }

    private boolean amountIsValid(int amount, List<? extends Cassette> cassettes) {
        var minDenominationCassette = cassettes.stream()
                .min(Comparator.comparingInt(it -> it.getDenomination().intValue()))
                .orElseThrow(() -> new RuntimeException("Can't withdraw " + amount + "₽: not enough money"));

        return amount % minDenominationCassette.getDenomination().intValue() == 0;
    }

    private Map<Denomination, Integer> calculateDenominations(int amount, List<? extends Cassette> cassettes) {
        Map<Denomination, Integer> requestedBanknotes = new EnumMap<>(Denomination.class);
        for (var cassette : cassettes) {
            requestedBanknotes.put(cassette.getDenomination(), 0);
        }

        int remainder = amount;
        for (var cassette : sort(cassettes)) {
            var denomination = cassette.getDenomination();
            int banknotesAmount = cassette.getBanknotesAmount();
            while (banknotesAmount > requestedBanknotes.get(denomination) && remainder / denomination.intValue() > 0) {
                requestedBanknotes.putIfAbsent(denomination, 0);
                requestedBanknotes.put(denomination, requestedBanknotes.get(denomination) + 1);
                remainder -= denomination.intValue();
            }
        }

        return requestedBanknotes;
    }

    private List<Cassette> sort(List<? extends Cassette> cassettes) {
        List<Cassette> copy = new ArrayList<>(cassettes);

        return copy.stream()
                .sorted(Comparator.comparingInt(
                                (Cassette c) -> c.getDenomination().intValue())
                        .reversed())
                .toList();
    }

    private boolean empty(Map<Denomination, Integer> banknotes) {
        for (var banknote : banknotes.entrySet()) {
            if (banknote.getValue() > 0) {
                return false;
            }
        }

        return true;
    }
}
