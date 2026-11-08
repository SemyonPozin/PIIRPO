package repository;

import domain.Ticket;

import java.util.List;


public interface TicketRepository {
    public List<Ticket> getAll(int eventId);
}

