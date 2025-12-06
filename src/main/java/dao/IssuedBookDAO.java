package dao;

import models.IssuedBook;
import utils.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IssuedBookDAO {
    
    public boolean issueBook(IssuedBook issuedBook) {
        String sql = "INSERT INTO issued_books (book_id, member_id, issue_date, due_date) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, issuedBook.getBookId());
            stmt.setInt(2, issuedBook.getMemberId());
            stmt.setDate(3, Date.valueOf(issuedBook.getIssueDate()));
            stmt.setDate(4, Date.valueOf(issuedBook.getDueDate()));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                BookDAO bookDAO = new BookDAO();
                return bookDAO.updateQuantity(issuedBook.getBookId(), -1);
            }
            
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error issuing book: " + e.getMessage());
            return false;
        }
    }
    
    public boolean returnBook(int issueId, LocalDate returnDate) {
        IssuedBook issuedBook = getIssuedBookById(issueId);
        if (issuedBook == null || issuedBook.isReturned()) {
            return false;
        }
        
        String sql = "UPDATE issued_books SET return_date = ? WHERE issue_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(returnDate));
            stmt.setInt(2, issueId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                BookDAO bookDAO = new BookDAO();
                return bookDAO.updateQuantity(issuedBook.getBookId(), 1);
            }
            
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error returning book: " + e.getMessage());
            return false;
        }
    }
    
    public IssuedBook getIssuedBookById(int issueId) {
        String sql = "SELECT ib.*, b.title, m.name FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.book_id " +
                     "JOIN members m ON ib.member_id = m.member_id " +
                     "WHERE ib.issue_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, issueId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractIssuedBookFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting issued book by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    public List<IssuedBook> getAllIssuedBooks() {
        List<IssuedBook> issuedBooks = new ArrayList<>();
        String sql = "SELECT ib.*, b.title, m.name FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.book_id " +
                     "JOIN members m ON ib.member_id = m.member_id " +
                     "ORDER BY ib.issue_date DESC";
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                issuedBooks.add(extractIssuedBookFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all issued books: " + e.getMessage());
        }
        
        return issuedBooks;
    }
    
    public List<IssuedBook> getCurrentlyIssuedBooks() {
        List<IssuedBook> issuedBooks = new ArrayList<>();
        String sql = "SELECT ib.*, b.title, m.name FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.book_id " +
                     "JOIN members m ON ib.member_id = m.member_id " +
                     "WHERE ib.return_date IS NULL " +
                     "ORDER BY ib.due_date";
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                issuedBooks.add(extractIssuedBookFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting currently issued books: " + e.getMessage());
        }
        
        return issuedBooks;
    }
    
    public List<IssuedBook> getOverdueBooks() {
        List<IssuedBook> issuedBooks = new ArrayList<>();
        String sql = "SELECT ib.*, b.title, m.name FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.book_id " +
                     "JOIN members m ON ib.member_id = m.member_id " +
                     "WHERE ib.return_date IS NULL AND ib.due_date < ? " +
                     "ORDER BY ib.due_date";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                issuedBooks.add(extractIssuedBookFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting overdue books: " + e.getMessage());
        }
        
        return issuedBooks;
    }
    
    public List<IssuedBook> getIssuedBooksByMember(int memberId) {
        List<IssuedBook> issuedBooks = new ArrayList<>();
        String sql = "SELECT ib.*, b.title, m.name FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.book_id " +
                     "JOIN members m ON ib.member_id = m.member_id " +
                     "WHERE ib.member_id = ? " +
                     "ORDER BY ib.issue_date DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                issuedBooks.add(extractIssuedBookFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting issued books by member: " + e.getMessage());
        }
        
        return issuedBooks;
    }
    
    public List<IssuedBook> getIssuedBooksByBook(int bookId) {
        List<IssuedBook> issuedBooks = new ArrayList<>();
        String sql = "SELECT ib.*, b.title, m.name FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.book_id " +
                     "JOIN members m ON ib.member_id = m.member_id " +
                     "WHERE ib.book_id = ? " +
                     "ORDER BY ib.issue_date DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                issuedBooks.add(extractIssuedBookFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting issued books by book: " + e.getMessage());
        }
        
        return issuedBooks;
    }
    
    public boolean hasUnreturnedBooks(int memberId) {
        String sql = "SELECT COUNT(*) FROM issued_books WHERE member_id = ? AND return_date IS NULL";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking unreturned books: " + e.getMessage());
        }
        
        return false;
    }
    
    public List<IssuedBook> getReturnedBooks() {
        List<IssuedBook> returnedBooks = new ArrayList<>();
        String sql = "SELECT ib.*, b.title, m.name FROM issued_books ib " +
                     "JOIN books b ON ib.book_id = b.book_id " +
                     "JOIN members m ON ib.member_id = m.member_id " +
                     "WHERE ib.return_date IS NOT NULL " +
                     "ORDER BY ib.return_date DESC";
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                returnedBooks.add(extractIssuedBookFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting returned books: " + e.getMessage());
        }
        
        return returnedBooks;
    }
    
    private IssuedBook extractIssuedBookFromResultSet(ResultSet rs) throws SQLException {
        IssuedBook issuedBook = new IssuedBook(
            rs.getInt("issue_id"),
            rs.getInt("book_id"),
            rs.getInt("member_id"),
            rs.getDate("issue_date").toLocalDate(),
            rs.getDate("due_date").toLocalDate(),
            rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null
        );
        
        issuedBook.setBookTitle(rs.getString("title"));
        issuedBook.setMemberName(rs.getString("name"));
        
        return issuedBook;
    }
}
