package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Serializable {
    public final Integer id;
    public String name;
    public double balance;
    List<Ticket> tickets;

    public Customer(Integer id) {
        this.id = id;
        tickets = new ArrayList<>();
    }

    public Customer(Integer id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        tickets = new ArrayList<>();
    }

    public Customer(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.balance = 2;
        tickets = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }
}
