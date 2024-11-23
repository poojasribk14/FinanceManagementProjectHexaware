 package dao;

import entity.Expense;
import entity.User;
import util.DBConnUtil;
import exception.DatabaseConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinanceRepositoryImpl implements IFinanceRepository {

    @Override
    public boolean createUser(User user) {
        try (Connection connection = DBConnUtil.getConnection()) {
            String sql = "INSERT INTO Users (username, password, email) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException | DatabaseConnectionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createExpense(Expense expense) {
        try (Connection connection = DBConnUtil.getConnection()) {
            // Check if the user ID exists
            String checkUserSql = "SELECT COUNT(*) FROM Users WHERE user_id = ?";
            PreparedStatement checkUserPs = connection.prepareStatement(checkUserSql);
            checkUserPs.setInt(1, expense.getUserId());
            ResultSet rs = checkUserPs.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Check if the category ID exists
                String checkCategorySql = "SELECT COUNT(*) FROM ExpenseCategories WHERE category_id = ?";
                PreparedStatement checkCategoryPs = connection.prepareStatement(checkCategorySql);
                checkCategoryPs.setInt(1, expense.getCategoryId());
                ResultSet rsCategory = checkCategoryPs.executeQuery();
                if (rsCategory.next() && rsCategory.getInt(1) > 0) {
                    // Insert expense if both user ID and category ID exist
                    String sql = "INSERT INTO Expenses (user_id, amount, category_id, date, description) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setInt(1, expense.getUserId());
                    ps.setDouble(2, expense.getAmount());
                    ps.setInt(3, expense.getCategoryId());
                    ps.setDate(4, new java.sql.Date(expense.getDate().getTime()));
                    ps.setString(5, expense.getDescription());
                    return ps.executeUpdate() > 0;
                } else {
                    System.out.println("Error: Category ID does not exist.");
                    return false;
                }
            } else {
                System.out.println("Error: User ID does not exist.");
                return false;
            }
        } catch (SQLException | DatabaseConnectionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(int userId) {
        try (Connection connection = DBConnUtil.getConnection()) {
            String sql = "DELETE FROM Users WHERE user_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException | DatabaseConnectionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteExpense(int expenseId) {
        try (Connection connection = DBConnUtil.getConnection()) {
            String sql = "DELETE FROM Expenses WHERE expense_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, expenseId);
            return ps.executeUpdate() > 0;
        } catch (SQLException | DatabaseConnectionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Expense> getAllExpenses(int userId) {
        List<Expense> expenses = new ArrayList<>();
        try (Connection connection = DBConnUtil.getConnection()) {
            String sql = "SELECT * FROM Expenses WHERE user_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Expense expense = new Expense(
                        rs.getInt("expense_id"),
                        rs.getInt("user_id"),
                        rs.getDouble("amount"),
                        rs.getInt("category_id"),
                        rs.getDate("date"),
                        rs.getString("description")
                );
                expenses.add(expense);
            }
        } catch (SQLException | DatabaseConnectionException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    @Override
    public boolean updateExpense(int expenseId, Expense expense) {
        try (Connection connection = DBConnUtil.getConnection()) {
            String sql = "UPDATE Expenses SET amount = ?, category_id = ?, date = ?, description = ? WHERE expense_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setDouble(1, expense.getAmount());
            ps.setInt(2, expense.getCategoryId());
            ps.setDate(3, new java.sql.Date(expense.getDate().getTime()));
            ps.setString(4, expense.getDescription());
            ps.setInt(5, expenseId);
            return ps.executeUpdate() > 0;
        } catch (SQLException | DatabaseConnectionException e) {
            e.printStackTrace();
            return false;
        }
    }
}
