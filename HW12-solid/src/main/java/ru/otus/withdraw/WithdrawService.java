package ru.otus.withdraw;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.balance.CassettesBalance;
import ru.otus.balance.CassettesBalanceService;
import ru.otus.banknote.Denomination;
import ru.otus.cassette.Cassette;

@SuppressWarnings({"java:S112", "java:S6548"})
public class WithdrawService implements Withdraw {
    private static final Logger log = LoggerFactory.getLogger(WithdrawService.class);
    private static WithdrawService instance;

    private final CassettesBalance cassettesBalance;

    private WithdrawService(CassettesBalance cassettesBalance) {
        this.cassettesBalance = cassettesBalance;
    }

    public static WithdrawService getInstance() {
        if (instance == null) {
            instance = new WithdrawService(CassettesBalanceService.getInstance());
        }

        return instance;
    }

    @Override
    public void withdraw(int amount, List<? extends Cassette> cassettes) {
        if (cassettesBalance.getBalance(cassettes) < amount) {
            throw new RuntimeException("Not enough money");
        }

        if (!amountIsValid(amount, cassettes)) {
            throw new RuntimeException("Amount is invalid");
        }

        var banknotes = calculateDenominations(amount, cassettes);
        if (empty(banknotes)) {
            throw new RuntimeException("Can't withdraw specified amount");
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
    }

    private boolean amountIsValid(int amount, List<? extends Cassette> cassettes) {
        var minDenominationCassette = cassettes.stream()
                .min(Comparator.comparingInt(it -> it.getDenomination().intValue()))
                .orElseThrow(() -> new RuntimeException("Not enough money"));

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
