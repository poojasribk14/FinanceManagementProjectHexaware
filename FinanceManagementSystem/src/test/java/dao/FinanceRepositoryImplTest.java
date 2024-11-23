package dao;

import entity.Expense;
import entity.User;
import exception.DatabaseConnectionException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.DBConnUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class FinanceRepositoryImplTest {

    private static IFinanceRepository financeRepository;
    private static Connection connection;

    @BeforeClass
    public static void setUpBeforeClass() throws DatabaseConnectionException, SQLException {
        financeRepository = new FinanceRepositoryImpl();
        connection = DBConnUtil.getConnection();

        cleanDatabase();
        insertInitialData();
    }

    @AfterClass
    public static void tearDownAfterClass() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Before
    public void setUp() throws SQLException {
        cleanDatabase();
        insertInitialData();
    }

    private static void cleanDatabase() throws SQLException {
        try (PreparedStatement ps1 = connection.prepareStatement("DELETE FROM Expenses")) {
            ps1.executeUpdate();
        }
        try (PreparedStatement ps2 = connection.prepareStatement("DELETE FROM Users")) {
            ps2.executeUpdate();
        }
        try (PreparedStatement ps3 = connection.prepareStatement("DELETE FROM ExpenseCategories")) {
            ps3.executeUpdate();
        }
    }

    private static void insertInitialData() throws SQLException {
       
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Users (user_id, username, password, email) VALUES (1, 'john_doe', 'password123', 'john_doe@gmail.com')")) {
            ps.executeUpdate();
        }
       
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO ExpenseCategories (category_id, category_name) VALUES (1, 'Food')")) {
            ps.executeUpdate();
        }
    }

    @Test
    public void testCreateUser() {
        User user = new User(2, "jane_smith", "password456", "jane_smith@gmail.com");
        boolean result = financeRepository.createUser(user);
        assertTrue(result);
    }

    @Test
    public void testCreateExpense() {
        Expense expense = new Expense(0, 1, 100.00, 1, new Date(), "Lunch");
        boolean result = financeRepository.createExpense(expense);
        assertTrue(result);
    }

    @Test
    public void testDeleteUser() {
        boolean result = financeRepository.deleteUser(1);
        assertTrue(result);
    }

    @Test
    public void testDeleteExpense() {
        Expense expense = new Expense(0, 1, 100.00, 1, new Date(), "Lunch");
        financeRepository.createExpense(expense);
        List<Expense> expenses = financeRepository.getAllExpenses(1);
        assertFalse(expenses.isEmpty());
        int expenseId = expenses.get(0).getExpenseId();

        boolean result = financeRepository.deleteExpense(expenseId);
        assertTrue(result);
    }

    @Test
    public void testUpdateExpense() {
        Expense expense = new Expense(0, 1, 100.00, 1, new Date(), "Lunch");
        financeRepository.createExpense(expense);
        List<Expense> expenses = financeRepository.getAllExpenses(1);
        assertFalse(expenses.isEmpty());
        int expenseId = expenses.get(0).getExpenseId();

        Expense updatedExpense = new Expense(expenseId, 1, 150.00, 1, new Date(), "Dinner");
        boolean result = financeRepository.updateExpense(expenseId, updatedExpense);
        assertTrue(result);
    }

    @Test
    public void testGetAllExpenses() {
        Expense expense1 = new Expense(0, 1, 100.00, 1, new Date(), "Lunch");
        Expense expense2 = new Expense(0, 1, 200.00, 1, new Date(), "Dinner");
        financeRepository.createExpense(expense1);
        financeRepository.createExpense(expense2);

        List<Expense> expenses = financeRepository.getAllExpenses(1);
        assertEquals(2, expenses.size());
    }
}
