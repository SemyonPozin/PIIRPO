package repository;

import domain.Event;

import java.io.IOException;
import java.util.List;

public interface EventRepository {
    public List<Event> getAll() throws IOException, ClassNotFoundException;
    boolean add(Event event);
}
