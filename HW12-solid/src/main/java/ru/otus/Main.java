package ru.otus;

import static ru.otus.denomination.Denomination.*;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.balance.CassettesBalanceService;
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
                add(new CassetteImpl(HUNDRED, 100, 10));
                add(new CassetteImpl(FIVE_HUNDRED, 100, 0));
                add(new CassetteImpl(THOUSAND, 100, 10));
                add(new CassetteImpl(FIVE_THOUSAND, 100, 10));
            }
        };
        var atm = new ATM(cassettes);
        var cassettesBalanceService = new CassettesBalanceService();
        var withdrawService = new WithdrawService(cassettesBalanceService);
        var depositService = new DepositService();

        cassettesBalanceService.displayBalance(atm);
        withdrawService.withdraw(500, atm);
        cassettesBalanceService.displayBalance(atm);

        log.info("=========================================");

        depositService.deposit(FIVE_HUNDRED, atm);
        cassettesBalanceService.displayBalance(atm);
    }
}
