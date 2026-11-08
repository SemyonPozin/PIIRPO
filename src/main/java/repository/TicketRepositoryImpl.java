package repository;

import domain.Event;
import domain.Ticket;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class TicketRepositoryImpl implements TicketRepository {
    @Override
    public List<Ticket> getAll(int eventId) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("./src/main/java/Storage/events.ser")
        )) {
            List<Event> events = (List<Event>) ois.readObject();
            for (Event event : events) {
                if (event.getId() != null && event.getId() == eventId) {
                    return event.getTickets() == null ? new ArrayList<>() : event.getTickets();
                }
            }
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
