# Smart Student Record & Attendance Management System
## VIT Bhopal University | CSE4019 — Advanced Java Programming

---

## Project Group
| Name | Registration No | Role |
|------|----------------|------|
| Rishi Raj | 24BCE10149 | Team Lead |
| Abhilash Singh | 24BCE10706 | Backend Development |
| Md Afzal Khan | 24BCE11247 | Database & JDBC |
| Ayush Mishra | 24BCE10019 | UI / Frontend |
| Pranav Kumar | 24BCE10080 | Testing & Integration |

**Faculty:** Vandana Shakya | **Slot:** B14+D21

---

## Prerequisites
- Java JDK 8 or higher
- MySQL Server 5.7 or higher
- MySQL JDBC Connector (`mysql-connector-j-8.x.x.jar`)

---

## Setup Instructions

### Step 1 — Configure MySQL
```sql
-- Run in MySQL Workbench or terminal:
mysql -u root -p < sql/database.sql
```

### Step 2 — Add JDBC Driver
Place `mysql-connector-j-8.x.x.jar` inside the `lib/` folder.

### Step 3 — Update Credentials (if needed)
Edit `src/com/vit/sms/util/DatabaseConnection.java`:
```java
private static final String DB_URL      = "jdbc:mysql://localhost:3306/vit_sms_db?...";
private static final String DB_USER     = "root";       // your MySQL username
private static final String DB_PASSWORD = "root";       // your MySQL password
```

### Step 4 — Compile
```bash
# From project root
javac -cp "lib/mysql-connector-j-8.x.x.jar" -d out \
  src/com/vit/sms/model/*.java \
  src/com/vit/sms/util/*.java \
  src/com/vit/sms/dao/*.java \
  src/com/vit/sms/ui/*.java \
  src/com/vit/sms/Main.java
```

### Step 5 — Run
```bash
java -cp "out:lib/mysql-connector-j-8.x.x.jar" com.vit.sms.Main
```

**On Windows use `;` instead of `:` as classpath separator.**

---

## Default Login Credentials
| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | Admin |
| vandana | faculty123 | Faculty |

---

## Features
- **Student CRUD** — Add, Edit, Delete, Search students
- **Attendance Management** — Mark Present/Absent/Late per subject
- **Reports & Analytics** — CGPA analysis, branch summary, progress bar charts
- **Threaded DB calls** — Non-blocking UI using Java threads
- **Serialization** — Java Bean models implement Serializable
- **JDBC Transactions** — Batch insert with rollback support

---

## Java Concepts Demonstrated (per syllabus unit)
| Unit | Topic | Where Used |
|------|-------|-----------|
| 1 | OOP, Serialization, Threading | All model classes, Main.java |
| 2 | Java Beans, Event Model | Student.java, Attendance.java |
| 3 | JDBC, Transactions, SQL API | All DAO classes |
| 4 | Swing, MVC, Layout Managers | All UI classes |
| 5 | Networking concepts | DatabaseConnection (URL/connection) |
