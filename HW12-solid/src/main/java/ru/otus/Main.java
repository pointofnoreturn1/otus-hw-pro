package ru.otus;

import static ru.otus.banknote.Denomination.*;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.balance.CassettesBalanceService;
import ru.otus.banknote.Banknote;
import ru.otus.cassette.Cassette;
import ru.otus.cassette.CassetteImpl;
import ru.otus.deposit.DepositService;
import ru.otus.withdraw.WithdrawService;

@SuppressWarnings({"java:S1171", "java:S3599"})
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        var cassettes = new ArrayList<Cassette>() {
            {
                add(new CassetteImpl(new Banknote(HUNDRED), 100, 10));
                add(new CassetteImpl(new Banknote(FIVE_HUNDRED), 100, 0));
                add(new CassetteImpl(new Banknote(THOUSAND), 100, 10));
                add(new CassetteImpl(new Banknote(FIVE_THOUSAND), 100, 10));
            }
        };
        ATM atm = new ATM(
                WithdrawService.getInstance(),
                DepositService.getInstance(),
                CassettesBalanceService.getInstance(),
                cassettes);

        atm.displayBalance();
        atm.withdraw(500);
        atm.displayBalance();

        log.info("=========================================");

        atm.deposit(new Banknote(FIVE_HUNDRED));
        atm.displayBalance();
    }
}
