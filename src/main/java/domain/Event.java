package domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Event implements Serializable {
    public Integer id;
    public LocalDateTime date;
    public String name;
    public List<Ticket> tickets;
    public int ticketsCount;
    public double ticketPrice;

    public void setId(Integer id) {
        this.id = id;
    }

    public Event(Integer id) {
        this.id = id;
    }

    public Event(List<Ticket> tickets, LocalDateTime date, String name, int ticketsCount, double ticketPrice) {
        this.tickets = tickets;
        this.date = date;
        this.name = name;
        this.ticketsCount = ticketsCount;
        this.ticketPrice = ticketPrice;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Integer getId() {
        return id;
    }
}
