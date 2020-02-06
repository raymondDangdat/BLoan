package comq.example.raymond.bloan.Model;

public class CustomersModel {
    private String fullName, phone, bvn, account;

    public CustomersModel() {
    }

    public CustomersModel(String fullName, String phone, String bvn, String account) {
        this.fullName = fullName;
        this.phone = phone;
        this.bvn = bvn;
        this.account = account;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
