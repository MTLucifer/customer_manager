package service;

import model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerManagement implements CustomerService {
    private static Map<Integer, Customer> customers;

    static {
        customers = new HashMap<>();
        customers.put(1, new Customer(1, "John", "john@codegym.vn", "Hanoi"));
        customers.put(2, new Customer(2, "Bill", "bill@codegym.vn", "Danang"));
        customers.put(3, new Customer(3, "Alex", "alex@codegym.vn", "Saigon"));
        customers.put(4, new Customer(4, "Adam", "adam@codegym.vn", "Beijin"));
        customers.put(5, new Customer(5, "Sophia", "sophia@codegym.vn", "Miami"));
        customers.put(6, new Customer(6, "Rose", "rose@codegym.vn", "Newyork"));
    }

    private String url = "jdbc:mysql://localhost:3306/demo_customer?useSSL=false";
    private String user = "root";
    private String password = "Doi2thutoc";

    public Connection getJDBC() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(this.url, this.user, this.password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    private static final String INSERT_CUSTOMERS_SQL = "INSERT INTO customers (name, email, address) VALUES (?, ?, ?);";
    private static final String SELECT_CUSTOMER_BY_ID = "select id,name,email,address from customers where id =?";
    private static final String SELECT_ALL_CUSTOMERS = "select * from customers";
    private static final String DELETE_CUSTOMERS_SQL = "delete from customers where id = ?;";
    private static final String UPDATE_CUSTOMERS_SQL = "update customers set name = ?,email= ?, address =? where id = ?;";

    public void insertCustomer(Customer customer) {
        System.out.println(INSERT_CUSTOMERS_SQL);
        // try-with-resource statement will auto close the connection.
        try (Connection connection = getJDBC(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CUSTOMERS_SQL)) {
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getAddress());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer selectCustomer(int id) {
        Customer customer = null;
        // Step 1: Establishing a Connection
        try (Connection connection = getJDBC();
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CUSTOMER_BY_ID);) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String address = rs.getString("address");
                customer = new Customer(id, name, email, address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    public List<Customer> selectAllCustomers() {

        // using try-with-resources to avoid closing resources (boiler plate code)
        List<Customer> customers = new ArrayList<>();
        // Step 1: Establishing a Connection
        try (Connection connection = getJDBC();

             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CUSTOMERS);) {
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String address = rs.getString("address");
                customers.add(new Customer(id, name, email, address));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public void deleteCustomer(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getJDBC(); PreparedStatement statement = connection.prepareStatement(DELETE_CUSTOMERS_SQL);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateCustomer(Customer customer) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getJDBC(); PreparedStatement statement = connection.prepareStatement(UPDATE_CUSTOMERS_SQL);) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getAddress());
            statement.setInt(4, customer.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }
}
