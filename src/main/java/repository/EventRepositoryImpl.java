package repository;

import domain.Event;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class EventRepositoryImpl implements EventRepository {

    public EventRepositoryImpl() {
    }

    @Override
    public List<Event> getAll() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("./src/main/java/Storage/events.ser")
        )) {
            return (List<Event>) ois.readObject();
        }
    }

    @Override
    public boolean add(Event event) {
        ArrayList<Event> events = new ArrayList<>();
        try {
            events = (ArrayList<Event>) this.getAll();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }

        try(ObjectOutputStream ois = new ObjectOutputStream(
                new FileOutputStream(Path.of("./src/main/java/Storage/events.ser").toString())
        )) {
            event.setId(events.size() + 1);
            events.add(event);
            ois.writeObject(events);
            return true;
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage() + " " + e.getClass());
            return false;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}
