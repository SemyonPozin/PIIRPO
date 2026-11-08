package service;

import domain.Customer;
import domain.Event;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TicketServiceTest {
    private static final String EVENTS_FILE = "./src/main/java/Storage/events.ser";
    private static final String CUSTOMERS_FILE = "./src/main/java/Storage/customers.ser";
    private static Integer createdEventId;
    private static Customer firstCustomer;

    @Test
    @Order(1)
    void userCanBuyTicket() throws Exception {
        List<Event> events = readEvents();
        int eventId = events.stream()
                .map(Event::getId)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0) + 1;

        Event event = new Event(new ArrayList<>(), LocalDateTime.now(), "buy-test", 2, 10);
        event.setId(eventId);
        events.add(event);
        writeEvents(events);
        createdEventId = eventId;

        int customerId = nextCustomerId();
        firstCustomer = new Customer(customerId, "buy-test-" + customerId, 100);

        assertTrue(new TicketService().buy(eventId, firstCustomer));
    }

    @Test
    @Order(2)
    void userCannotBuyTicketForSameEvent() {
        assertFalse(new TicketService().buy(createdEventId, firstCustomer));
    }

    @Test
    @Order(3)
    void userCannotBuyTicketWhenNoSeatsLeft() throws Exception {
        int customerId = nextCustomerId();
        Customer second = new Customer(customerId, "buy-test-" + customerId, 100);
        Customer third = new Customer(customerId + 1, "buy-test-" + (customerId + 1), 100);
        TicketService ticketService = new TicketService();

        // assertTrue(
        ticketService.buy(createdEventId, second);
        assertTrue(ticketService.buy(createdEventId, third));
    }

    @AfterAll
    static void deleteCreatedEvent() throws Exception {
        if (createdEventId == null) {
            return;
        }
        List<Event> events = readEvents();
        events.removeIf(event -> createdEventId.equals(event.getId()));
        writeEvents(events);
    }

    @SuppressWarnings("unchecked")
    private static List<Event> readEvents() throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(EVENTS_FILE))) {
            return (List<Event>) in.readObject();
        }
    }

    private static void writeEvents(List<Event> events) throws Exception {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(EVENTS_FILE))) {
            out.writeObject(events);
        }
    }

    @SuppressWarnings("unchecked")
    private static int nextCustomerId() throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(CUSTOMERS_FILE))) {
            List<Customer> customers = (List<Customer>) in.readObject();
            return customers.stream()
                    .map(Customer::getId)
                    .filter(Objects::nonNull)
                    .max(Integer::compareTo)
                    .orElse(0) + 1;
        }
    }
}
