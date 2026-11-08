package service;

import domain.Customer;
import domain.Event;
import domain.Ticket;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TicketService {
    private static final String EVENTS_FILE = "./src/main/java/Storage/events.ser";
    private static final String CUSTOMERS_FILE = "./src/main/java/Storage/customers.ser";

    public boolean buy(int eventId, Customer customer) {
        List<Event> events;
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(EVENTS_FILE)
        )) {
            events = (List<Event>) ois.readObject();
        } catch (Exception ex) {
            System.out.println("Не удалось прочитать данные");
            return false;
        }

        Event event = events.stream()
                .filter(e -> e.getId() != null && e.getId() == eventId)
                .findFirst()
                .orElse(null);
        if (event == null) {
            System.out.println("Мероприятие не найдено");
            return false;
        }

        List<Ticket> tickets = event.getTickets();
        if (tickets == null) {
            tickets = new ArrayList<>();
            event.setTickets(tickets);
        }

        boolean hasTickets =
                tickets
                        .stream()
                        .filter(e -> !e.isAvailbale())
                        .count() < event.ticketsCount;
        boolean hasEnoughMoney = customer.getBalance() >= event.getTicketPrice();
        boolean alreadyHasTicket = tickets.stream()
                .anyMatch(t -> !t.isAvailbale()
                        && t.getCustomer() != null
                        && t.getCustomer().getId() != null
                        && customer.getId() != null
                        && t.getCustomer().getId().equals(customer.getId()));

        if (!hasTickets) {
            System.out.println("Нет свободных мест");
        }
        if (!hasEnoughMoney) {
            System.out.println("Недостаточно средств");
        }
        if (alreadyHasTicket) {
            System.out.println("У вас уже есть билет на это мероприятие");
        }
        if (!hasTickets || !hasEnoughMoney || alreadyHasTicket) {
            return false;
        }

        Ticket ticket = new Ticket(
                (int) tickets.stream().filter(t -> !t.isAvailbale()).count(),
                customer,
                event,
                event.getTicketPrice()
        );
        ticket.setAvailbale(false);
        tickets.add(ticket);
        customer.setBalance(customer.getBalance() - event.getTicketPrice());

        if (saveEvents(events) && saveCustomer(customer)) {
            System.out.println("Билет успешно куплен");
            return true;
        }
        System.out.println("Не удалось сохранить данные");
        return false;
    }

    public boolean returnTicket(int eventId, Customer customer) {
        List<Event> events;
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(EVENTS_FILE)
        )) {
            events = (List<Event>) ois.readObject();
        } catch (Exception ex) {
            System.out.println("Не удалось прочитать данные");
            return false;
        }

        Event event = events.stream()
                .filter(e -> e.getId() != null && e.getId() == eventId)
                .findFirst()
                .orElse(null);
        if (event == null) {
            System.out.println("Мероприятие не найдено");
            return false;
        }

        List<Ticket> tickets = event.getTickets();
        if (tickets == null || tickets.isEmpty()) {
            System.out.println("У вас нет билета на это мероприятие");
            return false;
        }

        Ticket ticket = tickets.stream()
                .filter(t -> !t.isAvailbale()
                        && t.getCustomer() != null
                        && t.getCustomer().getId() != null
                        && t.getCustomer().getId().equals(customer.getId()))
                .findFirst()
                .orElse(null);
        if (ticket == null) {
            System.out.println("У вас нет билета на это мероприятие");
            return false;
        }

        tickets.remove(ticket);
        customer.setBalance(customer.getBalance() + ticket.getPrice());

        if (saveEvents(events) && saveCustomer(customer)) {
            System.out.println("Билет успешно отменён");
            return true;
        }
        System.out.println("Не удалось сохранить данные");
        return false;
    }


    private boolean saveEvents(List<Event> events) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(EVENTS_FILE)
        )) {
            oos.writeObject(events);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private boolean saveCustomer(Customer customer) {
        List<Customer> customers;
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(CUSTOMERS_FILE)
        )) {
            customers = (List<Customer>) ois.readObject();
        } catch (Exception ex) {
            return false;
        }

        boolean updated = false;
        for (int i = 0; i < customers.size(); i++) {
            if (customer.getId() != null && customer.getId().equals(customers.get(i).getId())) {
                customers.set(i, customer);
                updated = true;
                break;
            }
        }
        if (!updated) {
            customers.add(customer);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(CUSTOMERS_FILE)
        )) {
            oos.writeObject(customers);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
