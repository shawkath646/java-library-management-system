package dao;

import models.Member;
import utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    
    public boolean addMember(Member member) {
        String sql = "INSERT INTO members (name, email, phone, address) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getPhone());
            stmt.setString(4, member.getAddress());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding member: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateMember(Member member) {
        String sql = "UPDATE members SET name = ?, email = ?, phone = ?, address = ? WHERE member_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getPhone());
            stmt.setString(4, member.getAddress());
            stmt.setInt(5, member.getMemberId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating member: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteMember(int memberId) {
        String sql = "DELETE FROM members WHERE member_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, memberId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting member: " + e.getMessage());
            return false;
        }
    }
    
    public Member getMemberById(int memberId) {
        String sql = "SELECT * FROM members WHERE member_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractMemberFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting member by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    public Member getMemberByEmail(String email) {
        String sql = "SELECT * FROM members WHERE email = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractMemberFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting member by email: " + e.getMessage());
        }
        
        return null;
    }
    
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY name";
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                members.add(extractMemberFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all members: " + e.getMessage());
        }
        
        return members;
    }
    
    public List<Member> searchMembers(String searchTerm) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE name LIKE ? OR email LIKE ? OR phone LIKE ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                members.add(extractMemberFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching members: " + e.getMessage());
        }
        
        return members;
    }
    
    public boolean emailExists(String email, int excludeMemberId) {
        String sql = "SELECT COUNT(*) FROM members WHERE email = ? AND member_id != ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setInt(2, excludeMemberId);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
        }
        
        return false;
    }
    
    private Member extractMemberFromResultSet(ResultSet rs) throws SQLException {
        return new Member(
            rs.getInt("member_id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("address")
        );
    }
}
