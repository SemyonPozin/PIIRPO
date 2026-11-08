package service;

import domain.Customer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerService {

    private static final String CUSTOMERS_FILE = "./src/main/java/Storage/customers.ser";

    private List<Customer> getCustomers() {
        System.out.println("[CustomerService] Reading customers from: " + CUSTOMERS_FILE);

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(CUSTOMERS_FILE)
        )) {
            List<Customer> customers = (List<Customer>) ois.readObject();

            System.out.println(
                    "[CustomerService] Successfully loaded "
                            + customers.size()
                            + " customer(s)"
            );

            return customers;

        } catch (FileNotFoundException ex) {
            System.out.println(
                    "[CustomerService] Customers file not found. Starting with empty list."
            );
            return new ArrayList<>();

        } catch (EOFException ex) {
            System.out.println(
                    "[CustomerService] Customers file is empty. Starting with empty list."
            );
            return new ArrayList<>();

        } catch (ClassNotFoundException ex) {
            System.err.println(
                    "[CustomerService] Customer class was not found while deserializing."
            );
            ex.printStackTrace();
            return new ArrayList<>();

        } catch (IOException ex) {
            System.err.println(
                    "[CustomerService] Failed to read customers file."
            );
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    private boolean saveCustomers(List<Customer> customers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(CUSTOMERS_FILE)
        )) {
            oos.writeObject(customers);

            System.out.println(
                    "[CustomerService] Customers successfully saved."
            );
            return true;

        } catch (IOException ex) {
            System.err.println(
                    "[CustomerService] Failed to save customers."
            );
            ex.printStackTrace();
            return false;
        }
    }

    public boolean topUp(Customer customer) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("введите сколько хотите положить");
        double amount;
        try {
            amount = scanner.nextDouble();
        } catch (Exception ex) {
            System.out.println("Некорректная сумма");
            return false;
        }
        if (amount <= 0) {
            System.out.println("Сумма должна быть больше нуля");
            return false;
        }

        double previousBalance = customer.getBalance();
        customer.setBalance(previousBalance + amount);

        List<Customer> customers = getCustomers();
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

        if (!saveCustomers(customers)) {
            customer.setBalance(previousBalance);
            return false;
        }

        System.out.println("ваш текущий баланс " + customer.getBalance());
        return true;
    }

    public Customer login(String name) {
        List<Customer> customers = getCustomers();

        Customer loggedCustomer = customers.stream()
                .filter(c -> c.name.equals(name))
                .findFirst()
                .orElse(null);

        if (loggedCustomer != null)
            return loggedCustomer;

        System.out.println(
                "[CustomerService] Customer not found. Creating new customer."
        );

        Customer newUser = new Customer(
                customers.size() + 1,
                name
        );
        customers.add(newUser);
        saveCustomers(customers);

        System.out.println(
                "[CustomerService] New customer created: "
                        + newUser.name
                        + " (id=" + newUser.id + ")"
        );

        return newUser;
    }
}
