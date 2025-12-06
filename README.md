<div align="center">

# 📚 Library Management System

### Desktop Application | Java 25 | JavaFX 25 | MySQL 8.0

[![Java](https://img.shields.io/badge/Java-25-007396?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![JavaFX](https://img.shields.io/badge/JavaFX-25.0.1-FF6C37?style=for-the-badge&logo=java&logoColor=white)](https://openjfx.io/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.11-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)

[🚀 Quick Start](#-quick-start) • [✨ Features](#-features) • [🛠️ Tech Stack](#️-technology-stack) • [📦 Build EXE](#-windows-exe-packaging)

</div>

---

## 📋 Table of Contents

- [About](#-about)
- [Purpose](#-purpose)
- [Features](#-features)
- [Technology Stack](#️-technology-stack)
- [Project Structure](#-project-structure)
- [Database Schema](#️-database-schema)
- [Quick Start](#-quick-start)
- [Windows EXE Packaging](#-windows-exe-packaging)
- [Usage Guide](#-usage-guide)
- [Troubleshooting](#-troubleshooting)
- [Roadmap](#-roadmap)
- [Author](#-author)
- [Made By](#-made-by)

---

## 🎯 About

A modern, feature-rich **Library Management System** built with Java 25 and JavaFX 25. This desktop application provides an intuitive interface for managing library operations including books, members, and borrowing/return transactions. Designed with clean architecture, proper separation of concerns, and best practices in mind.

**Perfect for**: Small to medium libraries, educational institutions, personal book collections, and learning modern Java desktop development.

**Keywords**: Library Management System, JavaFX Desktop Application, Java GUI, MySQL Database, JDBC, MVC Pattern, DAO Pattern, Desktop Software, Book Management, Member Management

---

## 🎨 Purpose

This application serves multiple purposes:

- **Library Operations**: Comprehensive system for managing books, members, and transactions
- **Educational Project**: Demonstrates modern Java desktop development best practices
- **Learning Platform**: Showcases MVC architecture, DAO pattern, and JDBC usage
- **Production Ready**: Built with validation, error handling, and user-friendly UI
- **Portfolio Showcase**: Professional-grade desktop application example

---

## ✨ Features

### 📚 **Book Management**

- ✅ Add, edit, and delete books with full metadata
- ✅ Real-time search by title, author, or category
- ✅ Track book quantity and availability
- ✅ Category-based organization
- ✅ Publisher information tracking
- ✅ Automatic inventory updates on issue/return
- ✅ Data export to JSON and PDF formats

### 👥 **Member Management**

- ✅ Complete member registration system
- ✅ Email and phone validation
- ✅ Member profile with full contact details
- ✅ Search members by name or email
- ✅ Track borrowing history per member
- ✅ Member data export capabilities

### 📤 **Issue & Return System**

- ✅ Book borrowing with due date tracking
- ✅ Return processing with overdue detection
- ✅ Automatic quantity management
- ✅ Visual overdue warnings
- ✅ Transaction history logging
- ✅ Fine calculation (if implemented)

### 📊 **Dashboard Analytics**

- ✅ Real-time statistics display
- ✅ Total books count
- ✅ Active members count
- ✅ Currently issued books tracking
- ✅ Overdue books monitoring
- ✅ Quick access to all modules

### 🎨 **User Experience**

- ✅ Clean, modern UI with custom styling
- ✅ Intuitive navigation
- ✅ Form validation with helpful error messages
- ✅ Confirmation dialogs for critical actions
- ✅ Keyboard shortcuts for power users
- ✅ Responsive layout design
- ✅ Professional color scheme

### 🔐 **Data Management**

- ✅ MySQL database integration
- ✅ JDBC with PreparedStatements (SQL injection prevention)
- ✅ Transaction support for data consistency
- ✅ Connection pooling ready
- ✅ Backup and restore capabilities

---

## 🛠️ Technology Stack

### **Core Technologies**

<div align="center">

![Java](https://img.shields.io/badge/Java-25-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-25.0.1-FF6C37?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.11-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

</div>

- **Java 25** - Latest Java version with modern features
- **JavaFX 25.0.1** - Rich GUI framework for desktop applications
- **MySQL 8.0** - Relational database for data persistence
- **JDBC** - Database connectivity with PreparedStatements
- **Maven** - Dependency management and build automation

### **Libraries & Dependencies**

- **MySQL Connector/J 8.0.33** - Official MySQL JDBC driver
- **Gson 2.10.1** - JSON serialization for data export
- **iText 5.5.13.3** - PDF generation for reports
- **Maven Shade Plugin** - Creates executable JAR with dependencies
- **JPackage Plugin** - Native Windows installer generation

### **Architecture & Patterns**

- **MVC Pattern** - Separation of concerns (Model-View-Controller)
- **DAO Pattern** - Database abstraction layer
- **FXML** - Declarative UI design
- **Observer Pattern** - Event handling in JavaFX
- **Singleton Pattern** - Database connection management

---

## 📁 Project Structure

```
library-management-system/
├── 📂 src/main/
│   ├── 📂 java/
│   │   ├── Main.java                    # Application entry point
│   │   ├── 📂 controllers/              # JavaFX controllers
│   │   │   ├── DashboardController.java
│   │   │   ├── BookManagementController.java
│   │   │   ├── MemberManagementController.java
│   │   │   ├── IssueBookController.java
│   │   │   └── ReturnBookController.java
│   │   ├── 📂 dao/                      # Data Access Objects
│   │   │   ├── BookDAO.java
│   │   │   ├── MemberDAO.java
│   │   │   └── IssuedBookDAO.java
│   │   ├── 📂 models/                   # Data models (POJOs)
│   │   │   ├── Book.java
│   │   │   ├── Member.java
│   │   │   ├── IssuedBook.java
│   │   │   └── User.java
│   │   └── 📂 utils/                    # Utility classes
│   │       ├── DBUtil.java              # Database connection
│   │       ├── ValidationUtil.java      # Input validation
│   │       └── KeyboardShortcutUtil.java
│   └── 📂 resources/
│       ├── 📂 views/                    # FXML files
│       │   ├── Dashboard.fxml
│       │   ├── BookManagement.fxml
│       │   ├── MemberManagement.fxml
│       │   ├── IssueBook.fxml
│       │   └── ReturnBook.fxml
│       └── 📂 css/
│           └── style.css                # Application styling
│
├── 📂 database/
│   └── schema.sql                       # Database schema with sample data
│
├── 📂 lib/
│   ├── javafx-sdk-25.0.1/              # JavaFX SDK
│   ├── gson/                            # Gson library
│   └── itext/                           # iText library
│
├── 📂 target/                           # Compiled classes (Maven output)
│   ├── classes/
│   └── jpackage/                        # Windows EXE output
│
├── 📄 pom.xml                           # Maven configuration
├── 📄 launch.bat                        # Quick launch script (Windows)
├── 📄 BUILD_EXE.md                      # EXE packaging guide
└── 📄 README.md                         # This file
```

---

## 🗄️ Database Schema

### **Tables**

#### **books**

Store book information and inventory

| Column | Type | Description |
|--------|------|-------------|
| `book_id` | INT (PK, Auto) | Unique book identifier |
| `title` | VARCHAR(255) | Book title |
| `author` | VARCHAR(255) | Author name |
| `publisher` | VARCHAR(255) | Publisher name |
| `category` | VARCHAR(100) | Book category/genre |
| `quantity` | INT | Available copies |

#### **members**

Library member records

| Column | Type | Description |
|--------|------|-------------|
| `member_id` | INT (PK, Auto) | Unique member identifier |
| `name` | VARCHAR(255) | Member full name |
| `email` | VARCHAR(255) | Email address (unique) |
| `phone` | VARCHAR(20) | Contact number |
| `address` | TEXT | Physical address |

#### **issued_books**

Track borrowing transactions

| Column | Type | Description |
|--------|------|-------------|
| `issue_id` | INT (PK, Auto) | Unique issue identifier |
| `book_id` | INT (FK) | Reference to books table |
| `member_id` | INT (FK) | Reference to members table |
| `issue_date` | DATE | Date book was issued |
| `due_date` | DATE | Expected return date |
| `return_date` | DATE | Actual return date (NULL if not returned) |

#### **users**

System users (admin authentication)

| Column | Type | Description |
|--------|------|-------------|
| `user_id` | INT (PK, Auto) | Unique user identifier |
| `username` | VARCHAR(50) | Login username |
| `password` | VARCHAR(255) | Hashed password |
| `role` | VARCHAR(20) | User role (admin, librarian) |

---

## 🚀 Quick Start

### **Prerequisites**

- ✅ **Java JDK 21+** - [Download Adoptium Temurin](https://adoptium.net/temurin/releases/)
- ✅ **MySQL Server 8.0+** - [Download MySQL](https://dev.mysql.com/downloads/mysql/)
- ✅ **Maven** (Optional) - [Download Maven](https://maven.apache.org/download.cgi)

### **Installation**

```bash
# 1. Clone the repository
git clone https://github.com/shawkath646/java-final-project.git
cd java-final-project

# 2. Setup database
mysql -u root -p < database/schema.sql
```

Or open `database/schema.sql` in MySQL Workbench and execute it.

### **Configure Database Connection**

Edit `src/main/java/utils/DBUtil.java` (lines 10-11):

```java
private static final String DB_USER = "root";           // Your MySQL username
private static final String DB_PASSWORD = "your_pass";  // Your MySQL password
```

### **Run Application**

**Easy way (Recommended):**

```cmd
.\launch.bat
```

**Or compile first, then run separately:**

```powershell
# Compile
.\compile.ps1

# Run
java --module-path lib\javafx-sdk-25.0.1\lib --add-modules javafx.controls,javafx.fxml -cp "target\classes;lib\mysql-connector-j-8.0.33.jar" Main
```

**Or using Maven:**

```bash
mvn clean javafx:run
```

**Or using VS Code:**

- Press **F5** to run (launch configuration included)

---

## 📦 Windows EXE Packaging

Build a native Windows installer with bundled Java runtime.

### **Prerequisites**

- JDK 14+ with `jpackage` (recommended: Adoptium Temurin JDK 21/25)
- Maven installed and available in PATH

### **Build Steps**

```powershell
# From project root
mvn clean package

# The installer (.exe) will be created at:
# target/jpackage/Library Management System-1.0.0.exe
```

### **Optional**

- Add an app icon at `src/main/resources/icon.ico` before packaging
- The generated installer bundles a Java runtime; end users don't need Java installed

For detailed instructions, see [`BUILD_EXE.md`](BUILD_EXE.md).

---

## 🎮 Usage Guide

### **Dashboard**

View real-time statistics:

- Total books in library
- Total registered members
- Currently issued books
- Overdue books count

### **Manage Books**

1. Click **"Manage Books"** from dashboard
2. **Add Book**: Fill form and click "Add Book"
3. **Edit Book**: Select book, modify fields, click "Update"
4. **Delete Book**: Select book and click "Delete"
5. **Search**: Use search bar to filter by title, author, or category
6. **Export**: Generate PDF reports or JSON backup

### **Manage Members**

1. Click **"Manage Members"** from dashboard
2. **Add Member**: Enter details (email and phone validated)
3. **Edit Member**: Select member, update info, save
4. **Delete Member**: Remove member record
5. **Search**: Find members by name or email

### **Issue Book**

1. Click **"Issue Book"** from dashboard
2. Enter **Member ID** or search by name
3. Enter **Book ID** or search by title
4. Select **Due Date** (default: 14 days from today)
5. Click **"Issue Book"**
6. Quantity automatically decremented

### **Return Book**

1. Click **"Return Book"** from dashboard
2. Enter **Issue ID** or search by member/book
3. View issue details (member, book, dates)
4. Click **"Return Book"**
5. System checks for overdue and updates quantity

---

## 🔧 Troubleshooting

### **Database Connection Failed**

- Verify MySQL is running: `mysql -u root -p`
- Check username/password in `DBUtil.java`
- Ensure `library_app` database exists
- Verify MySQL port (default: 3306)

### **Compilation Errors**

```powershell
# Clean and recompile
Remove-Item target -Recurse -Force -ErrorAction SilentlyContinue
.\compile.ps1
```

### **Missing MySQL Connector**

The compile script auto-downloads it. If it fails, manually download from:
<https://dev.mysql.com/downloads/connector/j/>

Place `mysql-connector-j-8.0.33.jar` in `lib/` folder.

### **JavaFX Runtime Not Found**

Ensure JavaFX SDK is in `lib/javafx-sdk-25.0.1/` or download from:
<https://openjfx.io/>

### **Maven Build Issues**

```bash
# Update dependencies
mvn clean install -U

# Skip tests
mvn clean package -DskipTests
```

---

## 📅 Roadmap

### ✅ **Completed**

- [x] Book management (CRUD operations)
- [x] Member management with validation
- [x] Issue/return system with date tracking
- [x] Dashboard with real-time statistics
- [x] Search functionality across modules
- [x] Database schema with sample data
- [x] FXML-based UI design
- [x] Custom CSS styling
- [x] Keyboard shortcuts
- [x] Windows EXE packaging with jpackage
- [x] Maven build configuration

### 🚧 **In Progress**

- [ ] Fine calculation for overdue books
- [ ] User authentication system
- [ ] Data export (PDF reports)
- [ ] Backup and restore database

### 📋 **Planned**

- [ ] Advanced search filters
- [ ] Book reservation system
- [ ] Email notifications for due dates
- [ ] Barcode scanning support
- [ ] Multi-user role management
- [ ] Audit log for all transactions
- [ ] Dark mode support
- [ ] Internationalization (i18n)
- [ ] Unit and integration tests
- [ ] RESTful API for mobile app
- [ ] Cloud database support (MySQL on AWS/Azure)
- [ ] Automated backups
- [ ] Performance optimization
- [ ] Comprehensive documentation

---

## 👨‍💻 Author

**Shawkat Hossain Maruf**

- 🌐 Portfolio: [shawkath646.pro](https://shawkath646.pro)
- 💼 LinkedIn: [linkedin.com/in/shawkath645](https://linkedin.com/in/shawkath645)
- 📧 Email: <shawkath646@gmail.com>
- 🐙 GitHub: [@shawkath646](https://github.com/shawkath646)

**About Me**: Full-stack developer and Computer Science student at Sejong University, specializing in Java, React, Next.js, TypeScript, and modern web/desktop development. Passionate about creating clean, maintainable, and user-friendly applications.

---

## 🏢 Powered By

<div align="center">

<img src="https://cloudburstlab.vercel.app/api/branding/logo?variant=transparent" alt="Cloudburst Lab" width="200" />

<br />

**Cloudburst Lab** is a digital innovation studio focused on creating exceptional web and mobile applications. We specialize in modern JavaScript frameworks, Java desktop applications, cloud technologies, and user-centric design principles.

</div>


## 📄 License

This project is **proprietary** and © 2024-2025 Shawkat Hossain Maruf. All rights reserved.

The source code is available for viewing and learning purposes. For commercial use, collaboration, or inquiries, please contact the author.

---

## 🙏 Acknowledgments

- **Oracle & OpenJDK Community** - For the Java platform
- **JavaFX Team** - For the excellent GUI framework
- **MySQL Team** - For the reliable database system
- **Maven Community** - For build automation
- **Open Source Community** - For incredible tools and libraries

---

## 📊 Project Stats

![Java](https://img.shields.io/badge/Language-Java_25-007396?style=flat-square&logo=openjdk)
![JavaFX](https://img.shields.io/badge/Framework-JavaFX_25-FF6C37?style=flat-square&logo=java)
![MySQL](https://img.shields.io/badge/Database-MySQL_8.0-4479A1?style=flat-square&logo=mysql)
![Code Quality](https://img.shields.io/badge/Code_Quality-A+-success?style=flat-square)
![Architecture](https://img.shields.io/badge/Architecture-MVC-blue?style=flat-square)
![Status](https://img.shields.io/badge/Status-Production_Ready-brightgreen?style=flat-square)

---

<div align="center">

### ⭐ Star this repository if you find it helpful!

</div>
