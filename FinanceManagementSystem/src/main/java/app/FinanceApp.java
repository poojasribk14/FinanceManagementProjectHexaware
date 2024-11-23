package app;

import dao.FinanceRepositoryImpl;
import dao.IFinanceRepository;
import entity.Expense;
import entity.User;
import exception.ExpenseNotFoundException;
import exception.UserNotFoundException;

import java.util.List;
import java.util.Scanner;

public class FinanceApp {

    // Initialize FinanceRepositoryImpl to call methods
    private static IFinanceRepository financeRepository = new FinanceRepositoryImpl();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Display menu options
        while (true) {
            System.out.println("\n==== Finance Management System ====");
            System.out.println("1. Add User");
            System.out.println("2. Add Expense");
            System.out.println("3. Delete User");
            System.out.println("4. Delete Expense");
            System.out.println("5. Update Expense");
            System.out.println("6. View Expenses");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline character

            switch (choice) {
                case 1:
                    addUser();
                    break;
                case 2:
                    addExpense();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    deleteExpense();
                    break;
                case 5:
                    updateExpense();
                    break;
                case 6:
                    viewExpenses();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Add a user
    private static void addUser() {
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        User user = new User(0, username, password, email);  // userId is 0 because it'll be auto-generated
        boolean success = financeRepository.createUser(user);
        if (success) {
            System.out.println("User added successfully!");
        } else {
            System.out.println("Error adding user.");
        }
    }

    // Add an expense
    private static void addExpense() {
        System.out.print("Enter User ID: ");
        int userId = scanner.nextInt();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter Category ID: ");
        int categoryId = scanner.nextInt();
        scanner.nextLine();  
        System.out.print("Enter Description: ");
        String description = scanner.nextLine();

        Expense expense = new Expense(0, userId, amount, categoryId, new java.util.Date(), description);
        boolean success = financeRepository.createExpense(expense);
        if (success) {
            System.out.println("Expense added successfully!");
        } else {
            System.out.println("Error adding expense.");
        }
    }

    // Delete a user
    private static void deleteUser() {
        System.out.print("Enter User ID to delete: ");
        int userId = scanner.nextInt();
        boolean success = financeRepository.deleteUser(userId);
        if (success) {
            System.out.println("User deleted successfully!");
        } else {
            System.out.println("Error deleting user or user not found.");
        }
    }

    // Delete an expense
    private static void deleteExpense() {
        System.out.print("Enter Expense ID to delete: ");
        int expenseId = scanner.nextInt();
        boolean success = financeRepository.deleteExpense(expenseId);
        if (success) {
            System.out.println("Expense deleted successfully!");
        } else {
            System.out.println("Error deleting expense or expense not found.");
        }
    }

    // Update an expense
    private static void updateExpense() {
        System.out.print("Enter Expense ID to update: ");
        int expenseId = scanner.nextInt();
        System.out.print("Enter new Amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter new Category ID: ");
        int categoryId = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        System.out.print("Enter new Description: ");
        String description = scanner.nextLine();

        Expense expense = new Expense(expenseId, 0, amount, categoryId, new java.util.Date(), description); // User ID not required for update
        boolean success = financeRepository.updateExpense(expenseId, expense);
        if (success) {
            System.out.println("Expense updated successfully!");
        } else {
            System.out.println("Error updating expense.");
        }
    }

    // View all expenses 
    private static void viewExpenses() {
        System.out.print("Enter User ID to view expenses: ");
        int userId = scanner.nextInt();
        List<Expense> expenses = financeRepository.getAllExpenses(userId);
        if (expenses.isEmpty()) {
            System.out.println("No expenses found for the given user.");
        } else {
            for (Expense expense : expenses) {
                System.out.println("Expense ID: " + expense.getExpenseId());
                System.out.println("Amount: " + expense.getAmount());
                System.out.println("Category ID: " + expense.getCategoryId());
                System.out.println("Date: " + expense.getDate());
                System.out.println("Description: " + expense.getDescription());
                System.out.println("-------------------------");
            }
        }
    }
}
