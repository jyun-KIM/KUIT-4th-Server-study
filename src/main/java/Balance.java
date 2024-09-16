public class Balance {

    private int balance;
    public Balance() {

    }

    public int getBalance() {
        return balance;
    }

    public void add(NaturalNumber naturalNumber) {
        this.balance += naturalNumber.getNaturalNumber();
    }

    public void subtract(NaturalNumber naturalNumber) {
        if (naturalNumber.getNaturalNumber() > this.balance) {
            throw new IllegalArgumentException();
        }

        this.balance -= naturalNumber.getNaturalNumber();
    }
}
