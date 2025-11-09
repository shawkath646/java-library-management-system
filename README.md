<div align="center"># Library Management System



# 📚 Library Management SystemA desktop-based Library Management System built with **JavaFX** and **MySQL**. This application provides an intuitive GUI for managing library operations including books, members, and book borrowing/return records.



### Desktop Application | Java 21 | JavaFX 21 | MySQL 8.0## 🎯 Features



[![Java](https://img.shields.io/badge/Java-21_LTS-007396?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)- **Book Management**: Add, update, delete, and search books

[![JavaFX](https://img.shields.io/badge/JavaFX-21-FF6C37?style=for-the-badge&logo=java&logoColor=white)](https://openjfx.io/)- **Member Management**: Manage library member records  

[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)- **Issue/Return System**: Track book borrowing and returns

[![License](https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge)](LICENSE)- **Database Integration**: MySQL database with JDBC

- **User-Friendly GUI**: Built with JavaFX

[🚀 Quick Start](#-quick-start) • [✨ Features](#-features) • [🛠️ Tech Stack](#️-technology-stack) • [📸 Screenshots](#-screenshots)- **Input Validation**: Email, phone, and data validation

- **Search Functionality**: Search books by title, author, or category

</div>

## 🛠️ Technologies

---

- **Java 21 LTS**

## 📋 Table of Contents- **JavaFX 21** (GUI Framework)

- **MySQL 8.0** (Database)

- [About](#-about)- **JDBC** (Database Connectivity)

- [Features](#-features)

- [Technology Stack](#️-technology-stack)## 📋 Prerequisites

- [Quick Start](#-quick-start)

- [Project Structure](#-project-structure)1. **Java JDK 21 or higher** installed

- [Database Schema](#-database-schema)2. **MySQL Server** installed and running

- [Author](#-author)3. **JavaFX SDK** (already in `lib/javafx-sdk-25.0.1/`)



---## ⚙️ Quick Setup (3 Steps!)



## 🎯 About### Step 1: Setup Database



A modern, feature-rich **Library Management System** built with Java 21 and JavaFX 21. This desktop application provides an intuitive interface for managing library operations including books, members, and borrowing/return transactions. Designed with clean architecture, proper separation of concerns, and best practices in mind.Run this in MySQL:

```bash

**Perfect for**: Small to medium libraries, educational institutions, personal book collectionsmysql -u root -p < database/schema.sql

```

---

Or open `database/schema.sql` in MySQL Workbench and execute it.

## ✨ Features

### Step 2: Configure Database

### 📚 **Book Management**

- ✅ Add, edit, and delete booksEdit `src/main/java/utils/DBUtil.java` (lines 10-11):

- ✅ Real-time search by title, author, or category```java

- ✅ Track book quantity and availabilityprivate static final String DB_USER = "root";           // Your MySQL username

- ✅ Category-based organizationprivate static final String DB_PASSWORD = "your_pass";  // Your MySQL password

- ✅ Publisher information```



### 👥 **Member Management**### Step 3: Run Application

- ✅ Complete member registration system

- ✅ Email and phone validation**Easy way (Recommended):**

- ✅ Member profile with full contact details```powershell

- ✅ Search members by name or email.\compile-and-run.ps1

```

### 📤 **Issue & Return System**

- ✅ Book borrowing with due date tracking**Or compile first, then run separately:**

- ✅ Return processing with overdue detection```powershell

- ✅ Automatic quantity management# Compile

- ✅ Visual overdue warnings.\compile.ps1



### 📊 **Dashboard Analytics**# Run

- ✅ Real-time statistics displayjava --module-path lib\javafx-sdk-25.0.1\lib --add-modules javafx.controls,javafx.fxml -cp "target\classes;lib\mysql-connector-j-8.0.33.jar" Main

- ✅ Total books count```

- ✅ Active members count

- ✅ Currently issued books tracking**Or using VS Code:**

- ✅ Overdue books monitoring- Press **F5** to run



### 🎨 **User Experience**## 📁 Project Structure

- ✅ Clean, modern UI with custom styling

- ✅ Intuitive navigation```

- ✅ Form validation with helpful error messages├── src/main/

- ✅ Confirmation dialogs for critical actions│   ├── java/

│   │   ├── Main.java              # Application entry

---│   │   ├── controllers/           # JavaFX controllers

│   │   ├── models/                # Data models

## 🛠️ Technology Stack│   │   ├── dao/                   # Database operations

│   │   └── utils/                 # Utilities

### **Core Technologies**│   └── resources/

<div align="center">│       ├── views/                 # FXML files

│       └── css/                   # Stylesheets

![Java](https://img.shields.io/badge/Java-21_LTS-007396?style=for-the-badge&logo=openjdk&logoColor=white)├── database/

![JavaFX](https://img.shields.io/badge/JavaFX-21-FF6C37?style=for-the-badge&logo=java&logoColor=white)│   └── schema.sql                 # Database schema

![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)├── lib/

![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)│   └── javafx-sdk-25.0.1/        # JavaFX libraries

├── compile-and-run.ps1            # Build & run script

</div>└── pom.xml                        # Maven config (optional)

```

- **Java 21 LTS** - Latest long-term support version

- **JavaFX 21** - Rich GUI framework## 🎮 Using the Application

- **MySQL 8.0** - Relational database

- **JDBC** - Pure database connectivity1. **Dashboard** - View statistics (books, members, issued, overdue)

- **Maven** - Build automation2. **Manage Books** - Add, edit, delete, search books

3. **Manage Members** - Handle member records

### **Architecture**4. **Issue/Return** - Track book transactions

- **MVC Pattern** - Separation of concerns

- **DAO Pattern** - Database abstraction## 🔧 Troubleshooting

- **FXML** - Declarative UI design

### Database Connection Failed

---- Verify MySQL is running

- Check username/password in `DBUtil.java`

## 🚀 Quick Start- Ensure `library_app` database exists



### Prerequisites### Compilation Errors

```powershell

- ✅ **Java JDK 21+** - [Download](https://adoptium.net/temurin/releases/)# Clean and recompile

- ✅ **MySQL Server 8.0+** - [Download](https://dev.mysql.com/downloads/mysql/)Remove-Item target -Recurse -Force -ErrorAction SilentlyContinue

.\compile.ps1

### Installation```



```bash### Missing MySQL Connector

# 1. Clone the repositoryThe compile script auto-downloads it. If it fails, manually download from:

git clone https://github.com/shawkath646/java-final-project.githttps://dev.mysql.com/downloads/connector/j/

cd java-final-project

## 📚 Sample Data

# 2. Setup database

mysql -u root -p < database/schema.sqlThe database includes sample data:

- 5 books (classics)

# 3. Create database user- 3 members

mysql -u root -p- 1 admin user (username: admin, password: admin123)

```

## 👨‍💻 Development

```sql

CREATE USER 'library_app'@'localhost' IDENTIFIED BY '11111111';Built without Spring Boot using pure JDBC for educational purposes.

GRANT ALL PRIVILEGES ON library_app.* TO 'library_app'@'localhost';

FLUSH PRIVILEGES;**Architecture:**

EXIT;- MVC pattern (Model-View-Controller)

```- DAO pattern for database access

- JavaFX with FXML for UI

```bash- PreparedStatement for SQL security

# 4. Launch application

launch.bat## 📝 Documentation

```

- `SETUP_GUIDE.md` - Detailed setup instructions

### Default Credentials- `COMPLETE.md` - Project overview

- `PROJECT_STATUS.md` - Implementation details

**Database:**

- Username: `library_app`---

- Password: `11111111`

- Database: `library_app`**Project Status:** ✅ Complete and ready to use!



---For detailed setup instructions, see `SETUP_GUIDE.md`


## 📁 Project Structure

```
library-management-system/
├── 📂 src/main/
│   ├── 📂 java/
│   │   ├── Main.java
│   │   ├── 📂 controllers/          # JavaFX controllers
│   │   ├── 📂 dao/                  # Data Access Objects
│   │   ├── 📂 models/               # Data models
│   │   └── 📂 utils/                # Utilities
│   └── 📂 resources/
│       ├── 📂 views/                # FXML files
│       └── 📂 css/                  # Stylesheets
├── 📂 database/
│   └── schema.sql                   # Database schema
├── 📂 lib/
│   ├── javafx-sdk-25.0.1/
│   └── mysql-connector-j-8.0.33.jar
├── launch.bat                       # Launch script
└── pom.xml                          # Maven config
```

---

## 🗄️ Database Schema

### Tables

**books** - Store book information
- `book_id`, `title`, `author`, `publisher`, `category`, `quantity`

**members** - Library member records
- `member_id`, `name`, `email`, `phone`, `address`

**issued_books** - Track borrowing
- `issue_id`, `book_id`, `member_id`, `issue_date`, `due_date`, `return_date`

**users** - System users (admin)
- `user_id`, `username`, `password`, `role`

---

## 👨‍💻 Author

**Shawkat Hossain Maruf**

- 🌐 Portfolio: [shawkath646.pro](https://shawkath646.pro)
- 💼 LinkedIn: [linkedin.com/in/shawkath645](https://linkedin.com/in/shawkath645)
- 📧 Email: shawkath646@gmail.com
- 🐙 GitHub: [@shawkath646](https://github.com/shawkath646)

---

<div align="center">

### ⭐ Star this repository if you find it helpful!

**Built with ❤️ using Java & JavaFX**

</div>
