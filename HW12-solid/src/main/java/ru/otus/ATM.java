package ru.otus;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.balance.CassettesBalance;
import ru.otus.banknote.Banknote;
import ru.otus.cassette.Cassette;
import ru.otus.deposit.Deposit;
import ru.otus.withdraw.Withdraw;

public class ATM {
    private static final Logger log = LoggerFactory.getLogger(ATM.class);

    private final Withdraw withdrawService;
    private final Deposit depositService;
    private final CassettesBalance cassettesBalanceService;
    private final List<? extends Cassette> cassettes;

    public ATM(
            Withdraw withdrawService,
            Deposit depositService,
            CassettesBalance cassettesBalanceService,
            List<? extends Cassette> cassettes) {
        this.withdrawService = withdrawService;
        this.depositService = depositService;
        this.cassettesBalanceService = cassettesBalanceService;
        this.cassettes = cassettes;
    }

    public void deposit(Banknote banknote) {
        try {
            depositService.deposit(banknote, cassettes);
        } catch (RuntimeException e) {
            log.info(e.getMessage());
        }
    }

    public void withdraw(int amount) {
        try {
            withdrawService.withdraw(amount, cassettes);
        } catch (RuntimeException e) {
            log.info(e.getMessage());
        }
    }

    public void displayBalance() {
        cassettesBalanceService.displayBalance(cassettes);
    }
}
