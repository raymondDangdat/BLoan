package comq.example.raymond.bloan.Model;

public class LoanHistoryModel {
    private double amount, interest, totalDue;
    private String  status,uId, uName, accountNumber, bvn;
    private long dateSent;

    public LoanHistoryModel() {
    }

    public LoanHistoryModel(double amount, double interest, double totalDue,
                            String status, String uId, String uName,
                            String accountNumber, String bvn, long dateSent) {
        this.amount = amount;
        this.interest = interest;
        this.totalDue = totalDue;
        this.status = status;
        this.uId = uId;
        this.uName = uName;
        this.accountNumber = accountNumber;
        this.bvn = bvn;
        this.dateSent = dateSent;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public double getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(double totalDue) {
        this.totalDue = totalDue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public long getDateSent() {
        return dateSent;
    }

    public void setDateSent(long dateSent) {
        this.dateSent = dateSent;
    }
}
