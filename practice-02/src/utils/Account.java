package utils;

import java.io.Serializable;
import java.util.Random;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private String login;
    private String password;

    private InvestmentType investmentType = InvestmentType.SAVINGS;
    private final String number = String.valueOf(new Random().nextInt(99999999));
    private String cpf;
    private String name;
    private String address;
    private String birth;
    private String phone;

    private double amount;

    public Account() {
    }

    public Account(String accountString) {
        String[] fields = accountString.split(",");
        this.login = fields[0];
        this.password = fields[1];
        this.investmentType = InvestmentType.valueOf(fields[2]);
        this.cpf = fields[3];
        this.name = fields[4];
        this.address = fields[5];
        this.birth = fields[6];
        this.phone = fields[7];
        this.amount = Double.parseDouble(fields[8]);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public InvestmentType getInvestmentType() {
        return investmentType;
    }

    public void setInvestmentType(InvestmentType investmentType) {
        this.investmentType = investmentType;
    }

    public String getNumber() {
        return number;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return login + "," +
                password + "," +
                investmentType + "," +
                cpf + "," +
                name + "," +
                address + "," +
                birth + "," +
                phone + "," +
                amount;
    }
}
