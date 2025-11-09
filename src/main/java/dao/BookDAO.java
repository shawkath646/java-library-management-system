package dao;

import models.Book;
import utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    
    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, publisher, category, quantity) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setString(4, book.getCategory());
            stmt.setInt(5, book.getQuantity());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, publisher = ?, category = ?, quantity = ? WHERE book_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setString(4, book.getCategory());
            stmt.setInt(5, book.getQuantity());
            stmt.setInt(6, book.getBookId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE book_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            return false;
        }
    }
    
    public Book getBookById(int bookId) {
        String sql = "SELECT * FROM books WHERE book_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractBookFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting book by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all books: " + e.getMessage());
        }
        
        return books;
    }
    
    public List<Book> searchBooks(String searchTerm) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR category LIKE ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching books: " + e.getMessage());
        }
        
        return books;
    }
    
    public boolean updateQuantity(int bookId, int quantityChange) {
        String sql = "UPDATE books SET quantity = quantity + ? WHERE book_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantityChange);
            stmt.setInt(2, bookId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating book quantity: " + e.getMessage());
            return false;
        }
    }
    
    public boolean isBookAvailable(int bookId) {
        Book book = getBookById(bookId);
        return book != null && book.getQuantity() > 0;
    }
    
    private Book extractBookFromResultSet(ResultSet rs) throws SQLException {
        return new Book(
            rs.getInt("book_id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getString("publisher"),
            rs.getString("category"),
            rs.getInt("quantity")
        );
    }
}
