package comq.example.raymond.bloan.Model;

public class BVNModel {
    private String account, bvn, firstName, otherName, surname, phone;

    public BVNModel() {
    }

    public BVNModel(String account, String bvn,
                    String firstName, String otherName, String surname, String phone) {
        this.account = account;
        this.bvn = bvn;
        this.firstName = firstName;
        this.otherName = otherName;
        this.surname = surname;
        this.phone = phone;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
