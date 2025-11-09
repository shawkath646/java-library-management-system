package models;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private String publisher;
    private String category;
    private int quantity;

    public Book() {
    }

    public Book(int bookId, String title, String author, String publisher, String category, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
        this.quantity = quantity;
    }

    public Book(String title, String author, String publisher, String category, int quantity) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
        this.quantity = quantity;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
