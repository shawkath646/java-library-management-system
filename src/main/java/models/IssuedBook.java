package models;

import java.time.LocalDate;

public class IssuedBook {
    private int issueId;
    private int bookId;
    private int memberId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    
    private String bookTitle;
    private String memberName;

    public IssuedBook() {
    }

    public IssuedBook(int issueId, int bookId, int memberId, LocalDate issueDate, LocalDate dueDate, LocalDate returnDate) {
        this.issueId = issueId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public IssuedBook(int bookId, int memberId, LocalDate issueDate, LocalDate dueDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public boolean isOverdue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    @Override
    public String toString() {
        return "IssuedBook{" +
                "issueId=" + issueId +
                ", bookId=" + bookId +
                ", memberId=" + memberId +
                ", issueDate=" + issueDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
