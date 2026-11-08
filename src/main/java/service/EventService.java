package service;

import domain.Event;
import domain.Ticket;
import repository.EventRepository;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public boolean addEvent() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите имя");
            String name = scanner.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            System.out.println("Введите дату и время в формате ДД-ММ-ГГГГ ЧЧ:ММ");
            String dateInput = scanner.nextLine();
            LocalDateTime dateTime = LocalDateTime.parse(dateInput, formatter);

            System.out.println("Введите количество билетов");
            int count = scanner.nextInt();

            System.out.println("Введите цену билета");
            double ticketPrice = scanner.nextDouble();

            if(eventRepository.add(new Event(new ArrayList<Ticket>(count), dateTime, name, count, ticketPrice)))
                return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public void getEvents() {
        List<Event> events = new ArrayList<>();
        try {
            events = eventRepository.getAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if(events.isEmpty()) {
            System.out.println("Пока не событий");
            return;
        }
        for(var event : events) {
            String template = "событие {0}\n{1}\nСостоится {2}\nСвободных мест{3}\n";
            long soldTickets = event.tickets == null ? 0 : event.tickets.stream().filter(ticket -> !ticket.isAvailbale()).count();
            long availableTickets = event.ticketsCount - soldTickets;
            System.out.println(MessageFormat.format(
                    template,
                    event.getId(),
                    event.getName(),
                    event.getDate(),
                    availableTickets
            ));
        }
    }
}
