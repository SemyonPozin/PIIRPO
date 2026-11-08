import domain.Customer;
import domain.Event;
import repository.EventRepository;
import repository.EventRepositoryImpl;
import service.CustomerService;
import service.EventService;
import service.TicketService;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        EventService eventService = new EventService(new EventRepositoryImpl());
        TicketService ticketService = new TicketService();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Добро пожаловать");
        System.out.println("Введите логин");
        String login = scanner.nextLine();
        CustomerService customerService = new CustomerService();
        Customer customer = customerService.login(login);

        int option = -1;
        while (option != 0) {
            System.out.println("1 чтобы посмотреть доступные мероприятия и их места");
            System.out.println("2 чтобы купить билет");
            System.out.println("3 чтобы отменить билет");
            System.out.println("4 чтобы добавить мероприятие");
            System.out.println("5 чтобы пополнить баланс");
            System.out.println("0 чтобы выйти");
            option = scanner.nextInt();
            if(option == 1) {
                eventService.getEvents();
            } else if(option == 2) {
                System.out.println("Введите нзвание мероприятия");
                int eventId = scanner.nextInt();
                ticketService.buy(eventId, customer);
            } else if(option == 3) {
                System.out.println("Введите нзвание мероприятия");
                int eventId = scanner.nextInt();
                ticketService.returnTicket(eventId, customer);
            } else if(option == 4) {
                if(eventService.addEvent())
                    System.out.println("Успешно добавлено");
                else System.out.println("Ошибка добавления");
            } else if(option == 5) {
                customerService.topUp(customer);
            }
        }
        return;
    }

}