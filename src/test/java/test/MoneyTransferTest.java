package test;

import org.junit.jupiter.api.Test;
import data.DataHelper;
import page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static data.DataHelper.*;

public class MoneyTransferTest {

    @Test
    public void shouldTransferMoneyFirstToSecond() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = generateValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCard(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);

        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }
}
