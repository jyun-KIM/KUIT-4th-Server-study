public class Account {
    private Balance balance;

    private Account(Balance balance) {
        this.balance = balance;
    }

    public Account(Balance balance, NaturalNumber naturalNumber) {
        this.balance = balance;

        deposit(naturalNumber);
    }

    public static Account createAccountEmpty(Balance balance) {
        return new Account(balance);
    }

    public static Account createAccountWithInitialDeposit(Balance balance, NaturalNumber naturalNumber) {
        Account account = new Account(balance);
        account.deposit(naturalNumber);
        return account;
    }

    public int getBalance() {
        return this.balance.getBalance();
    }

    public void deposit(NaturalNumber naturalNumber) {

        this.balance.add(naturalNumber);
    }

    public void withdraw(NaturalNumber naturalNumber) {

        this.balance.subtract(naturalNumber);
    }
}
