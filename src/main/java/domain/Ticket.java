package domain;

import java.io.Serializable;

public class Ticket implements Serializable {
    public final Integer id;

    public Ticket(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Ticket(Integer id, Customer customer, Event event, double price) {
        this.id = id;
        this.customer = customer;
        this.event = event;
        this.price = price;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Customer customer;
    public Event event;
    public double price;

    public boolean isAvailbale() {
        return isAvailbale;
    }

    public void setAvailbale(boolean availbale) {
        isAvailbale = availbale;
    }

    public boolean isAvailbale = true;
}
