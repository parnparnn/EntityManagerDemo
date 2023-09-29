/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitymanagerdemo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import model.Address;
import model.Customer;

/**
 *
 * @author sarun
 */
public class EntityManagerDemo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //createData(1L, "Antony", "Balla", "tballa@mail.com", 1L, "Ritherdon Rd", "London", "8QE", "UK");
        //createData(2L, "John", "Henry", "jh@mail.com", 2L, "123/4 Viphavadee Rd.", "Bangkok", "TH", "10900");
        //createData(3L, "Marry", "Jane", "mj@mail.com", 3L, "123/5 Viphavadee Rd.", "Bangkok", "TH", "10900");
        //createData(4L, "Peter", "Parker", "pp@mail.com", 4L, "543/21 Fake Rd.", "Nonthaburi", "TH", "20900");
        //(5L, "Bruce", "Wayn", "bw@mail.com", 5L, "678/90 Unreal Rd.", "Pathumthani", "TH", "30500");
        //List<Customer> cusList = printAllCustomer();
        List<Customer> cusList = printCustomerByCity("Bangkok");
        showCustomer(cusList);
    }

    public static void createData(
            Long customerId,
            String firstName,
            String lastName,
            String email,
            Long addressId,
            String street,
            String city,
            String zipCode,
            String country
    ) {
        Customer customer = new Customer(customerId, firstName, lastName, email);
        Address address = new Address(addressId, street, city, zipCode, country);
        address.setCustomerFk(customer);
        customer.setAddressId(address);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EntityManagerDemoPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        try {
            em.persist(address);
            em.persist(customer);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static void showCustomer(List<Customer> cusList) {
        System.out.println("------------------------------------------");
        for (Customer customer : cusList) {
            System.out.println("First Name: " + customer.getFirstname());
            System.out.println("Last Name: " + customer.getLastname());
            System.out.println("Email: " + customer.getEmail());
            Address address = customer.getAddressId();
            if (address != null) {
                System.out.println("Street: " + address.getStreet());
                System.out.println("City: " + address.getCity());
                System.out.println("Country: " + address.getCountry());
                System.out.println("Zip Code: " + address.getZipcode());
            }
            System.out.println("------------------------------------------");
        }

    }

    public static List<Customer> printAllCustomer() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EntityManagerDemoPU");
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("Customer.findAll");
        List<Customer> cusList = (List<Customer>) query.getResultList();
        return cusList;
    }

    public static List<Customer> printCustomerByCity(String city) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EntityManagerDemoPU");
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("SELECT a FROM Address a WHERE a.city = :city");
        query.setParameter("city", city);
        List<Address> addressList = query.getResultList();
        List<Customer> cusList = new ArrayList<>();
        if (!addressList.isEmpty()) {
            for (Address address : addressList) {
                Customer customer = address.getCustomerFk();
                if (customer != null) {
                    cusList.add(customer);
                }
            }
        }
        return cusList;
    }

    public void persist(Object object) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EntityManagerDemoPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
