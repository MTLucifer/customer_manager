package service;

import model.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerService {
    void insertCustomer(Customer customer) throws SQLException;

    Customer selectCustomer(int id);

    List<Customer> selectAllCustomers();

    void deleteCustomer(int id) throws SQLException;

    boolean updateCustomer(Customer customer) throws SQLException;
}
