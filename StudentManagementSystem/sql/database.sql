-- ============================================================
--  Smart Student Record & Attendance Management System
--  VIT Bhopal University | CSE4019 Advanced Java Programming
--
--  Group:  Rishi Raj (24BCE10149), Abhilash Singh (24BCE10706),
--          Md Afzal Khan (24BCE11247), Ayush Mishra (24BCE10019),
--          Pranav Kumar (24BCE10080)
--  Faculty: Vandana Shakya  |  Slot: B14+D21
-- ============================================================

-- Step 1: Create and select database
CREATE DATABASE IF NOT EXISTS vit_sms_db;
USE vit_sms_db;

-- Step 2: Users (login accounts)
CREATE TABLE IF NOT EXISTS users (
  id        INT AUTO_INCREMENT PRIMARY KEY,
  username  VARCHAR(50)  UNIQUE NOT NULL,
  password  VARCHAR(100) NOT NULL,
  role      VARCHAR(20)  NOT NULL,          -- ADMIN | FACULTY | STUDENT
  full_name VARCHAR(100),
  email     VARCHAR(100),
  active    BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Step 3: Students
CREATE TABLE IF NOT EXISTS students (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  reg_no     VARCHAR(20)  UNIQUE NOT NULL,
  name       VARCHAR(100) NOT NULL,
  email      VARCHAR(100),
  phone      VARCHAR(15),
  branch     VARCHAR(50),
  semester   INT DEFAULT 1,
  cgpa       DECIMAL(4,2) DEFAULT 0.00,
  gender     VARCHAR(10),
  status     VARCHAR(20) DEFAULT 'ACTIVE',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Step 4: Attendance
CREATE TABLE IF NOT EXISTS attendance (
  id              INT AUTO_INCREMENT PRIMARY KEY,
  student_id      INT NOT NULL,
  subject         VARCHAR(150),
  attendance_date DATE NOT NULL,
  status          VARCHAR(10) NOT NULL,     -- PRESENT | ABSENT | LATE
  remarks         VARCHAR(255),
  FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

-- ============================================================
-- Seed default users
-- ============================================================
INSERT IGNORE INTO users (username, password, role, full_name, email) VALUES
  ('admin',   'admin123',   'ADMIN',   'System Administrator',  'admin@vitbhopal.ac.in'),
  ('vandana', 'faculty123', 'FACULTY', 'Vandana Shakya',        'vandana.shakya@vitbhopal.ac.in'),
  ('rishi',   'rishi123',   'STUDENT', 'Rishi Raj',             'rishi.raj@vitbhopal.ac.in');

-- ============================================================
-- Seed student data (23BCE & 24BCE batches)
-- Java application will also auto-seed on first run
-- ============================================================
INSERT IGNORE INTO students (reg_no, name, email, branch, semester, cgpa, gender) VALUES
-- 23BCE Batch
('23BCE10014','Muskan Srivastav',   '23bce10014@vitbhopal.ac.in','CSE',4,8.7,'Female'),
('23BCE10108','Shikhar Sharma',     '23bce10108@vitbhopal.ac.in','CSE',4,8.2,'Male'),
('23BCE10189','Naman Gupta',        '23bce10189@vitbhopal.ac.in','CSE',4,7.9,'Male'),
('23BCE10194','Shorya Pathak',      '23bce10194@vitbhopal.ac.in','CSE',4,8.5,'Male'),
('23BCE10259','Dhruv',              '23bce10259@vitbhopal.ac.in','CSE',4,7.6,'Male'),
('23BCE10271','Pragati Tripathi',   '23bce10271@vitbhopal.ac.in','CSE',4,9.1,'Female'),
('23BCE10306','Khushbu',            '23bce10306@vitbhopal.ac.in','CSE',4,8.8,'Female'),
('23BCE10339','Krishna',            '23bce10339@vitbhopal.ac.in','CSE',4,8.0,'Male'),
('23BCE10343','Himanshu Rathore',   '23bce10343@vitbhopal.ac.in','CSE',4,7.4,'Male'),
('23BCE10355','Kumar Arya',         '23bce10355@vitbhopal.ac.in','CSE',4,8.3,'Male'),
('23BCE10385','Ritik Mangawa',      '23bce10385@vitbhopal.ac.in','CSE',4,7.8,'Male'),
('23BCE10436','Shriya Dhawan',      '23bce10436@vitbhopal.ac.in','CSE',4,9.0,'Female'),
('23BCE10447','Amit Yadav',         '23bce10447@vitbhopal.ac.in','CSE',4,8.1,'Male'),
('23BCE10546','Divyanshu Mittal',   '23bce10546@vitbhopal.ac.in','CSE',4,8.6,'Male'),
('23BCE10596','Advay Singh',        '23bce10596@vitbhopal.ac.in','CSE',4,7.7,'Male'),
('23BCE10626','Radha Agarwal',      '23bce10626@vitbhopal.ac.in','CSE',4,9.2,'Female'),
('23BCE10628','Devesh Murjani',     '23bce10628@vitbhopal.ac.in','CSE',4,8.4,'Male'),
('23BCE10700','Tanishk Vamne',      '23bce10700@vitbhopal.ac.in','CSE',4,7.3,'Male'),
('23BCE10717','Nitya Agarwal',      '23bce10717@vitbhopal.ac.in','CSE',4,8.9,'Female'),
('23BCE10746','Yashvi Kohli',       '23bce10746@vitbhopal.ac.in','CSE',4,9.3,'Female'),
('23BCE10752','Rushil Walia',       '23bce10752@vitbhopal.ac.in','CSE',4,7.1,'Male'),
('23BCE10760','Shashank Pandey',    '23bce10760@vitbhopal.ac.in','CSE',4,8.0,'Male'),
('23BCE10777','Ansh Tyagi',         '23bce10777@vitbhopal.ac.in','CSE',4,7.9,'Male'),
('23BCE10815','Kabir Roy',          '23bce10815@vitbhopal.ac.in','CSE',4,8.5,'Male'),
('23BCE10831','Snehal Chakrabarti', '23bce10831@vitbhopal.ac.in','CSE',4,9.0,'Female'),
('23BCE10838','Shreeved Kiran Dhanait','23bce10838@vitbhopal.ac.in','CSE',4,8.2,'Male'),
('23BCE10849','Yashvardhan Singh Sarangdevot','23bce10849@vitbhopal.ac.in','CSE',4,7.8,'Male'),
('23BCE10861','Mannat Sachdeva',    '23bce10861@vitbhopal.ac.in','CSE',4,9.1,'Female'),
('23BCE10874','Yash Rawalkar',      '23bce10874@vitbhopal.ac.in','CSE',4,8.3,'Male'),
('23BCE10889','Nikhil Tiwari',      '23bce10889@vitbhopal.ac.in','CSE',4,7.6,'Male'),
('23BCE10899','Chinmay Bhardwaj',   '23bce10899@vitbhopal.ac.in','CSE',4,8.7,'Male'),
('23BCE10928','Sainyam Bansal',     '23bce10928@vitbhopal.ac.in','CSE',4,8.9,'Male'),
('23BCE10937','Revanta Choudhary',  '23bce10937@vitbhopal.ac.in','CSE',4,7.5,'Male'),
('23BCE10983','Taufiq Ahamed',      '23bce10983@vitbhopal.ac.in','CSE',4,8.1,'Male'),
('23BCE11125','Anuj Tiwari',        '23bce11125@vitbhopal.ac.in','CSE',4,7.4,'Male'),
('23BCE11130','Jahnvi Verma',       '23bce11130@vitbhopal.ac.in','CSE',4,8.6,'Female'),
('23BCE11169','Devraj Bijpuria',    '23bce11169@vitbhopal.ac.in','CSE',4,7.2,'Male'),
('23BCE11176','Swastik Das',        '23bce11176@vitbhopal.ac.in','CSE',4,8.0,'Male'),
('23BCE11215','Pingaksh Sharma',    '23bce11215@vitbhopal.ac.in','CSE',4,7.8,'Male'),
('23BCE11226','Samyukta Sandeep Menon','23bce11226@vitbhopal.ac.in','CSE',4,9.2,'Female'),
('23BCE11247','Alok Kumar Gupta',   '23bce11247@vitbhopal.ac.in','CSE',4,8.4,'Male'),
('23BCE11304','Ayush Kumar',        '23bce11304@vitbhopal.ac.in','CSE',4,7.7,'Male'),
('23BCE11313','Harshika Sharma',    '23bce11313@vitbhopal.ac.in','CSE',4,9.0,'Female'),
('23BCE11315','Yashwardhan Sandilya','23bce11315@vitbhopal.ac.in','CSE',4,8.3,'Male'),
('23BCE11356','Yash Kumar Singh',   '23bce11356@vitbhopal.ac.in','CSE',4,7.9,'Male'),
('23BCE11374','Utkarsh Arya',       '23bce11374@vitbhopal.ac.in','CSE',4,8.5,'Male'),
('23BCE11377','Abhinav Verma',      '23bce11377@vitbhopal.ac.in','CSE',4,7.6,'Male'),
('23BCE11384','Pratyush Srivastava','23bce11384@vitbhopal.ac.in','CSE',4,8.8,'Male'),
('23BCE11387','Aryan Gupta',        '23bce11387@vitbhopal.ac.in','CSE',4,7.3,'Male'),
('23BCE11419','Parv Khandelwal',    '23bce11419@vitbhopal.ac.in','CSE',4,8.1,'Male'),
('23BCE11449','Gargee Singh',       '23bce11449@vitbhopal.ac.in','CSE',4,9.1,'Female'),
('23BCE11558','Yash Aman',          '23bce11558@vitbhopal.ac.in','CSE',4,7.7,'Male'),
('23BCE11586','Shriyam Rastogi',    '23bce11586@vitbhopal.ac.in','CSE',4,8.0,'Male'),
('23BCE11596','Ayush Kumar B',      '23bce11596@vitbhopal.ac.in','CSE',4,8.4,'Male'),
('23BCE11614','Yash Saini',         '23bce11614@vitbhopal.ac.in','CSE',4,7.5,'Male'),
('23BCE11631','Sarthak Vyas',       '23bce11631@vitbhopal.ac.in','CSE',4,8.2,'Male'),
('23BCE11634','Suryanshi Ranawat',  '23bce11634@vitbhopal.ac.in','CSE',4,9.0,'Female'),
('23BCE11635','Manvi Nayak',        '23bce11635@vitbhopal.ac.in','CSE',4,8.7,'Female'),
('23BCE11675','Harsh Raj Singh',    '23bce11675@vitbhopal.ac.in','CSE',4,7.8,'Male'),
('23BCE11691','Anand Singh',        '23bce11691@vitbhopal.ac.in','CSE',4,8.3,'Male'),
('23BCE11700','Vaibhav Verma',      '23bce11700@vitbhopal.ac.in','CSE',4,7.6,'Male'),
('23BCE11720','Hiya Dosi',          '23bce11720@vitbhopal.ac.in','CSE',4,9.2,'Female'),
('23BCE11728','Aaditya Pathak',     '23bce11728@vitbhopal.ac.in','CSE',4,8.5,'Male'),
('23BCE11740','Pranhavee Tyagi',    '23bce11740@vitbhopal.ac.in','CSE',4,8.9,'Female'),
('23BCE11752','Chirayu Zalke',      '23bce11752@vitbhopal.ac.in','CSE',4,7.4,'Male'),
('23BCE11761','Suryansh Singh',     '23bce11761@vitbhopal.ac.in','CSE',4,8.1,'Male'),
('23BCE11783','Prince Saini',       '23bce11783@vitbhopal.ac.in','CSE',4,7.9,'Male'),
('23BCE11793','Kumar Vaibhav',      '23bce11793@vitbhopal.ac.in','CSE',4,8.6,'Male'),
('23BCE11825','Ansh Pandey',        '23bce11825@vitbhopal.ac.in','CSE',4,7.7,'Male'),
('23BCE11828','Parvathi M',         '23bce11828@vitbhopal.ac.in','CSE',4,9.0,'Female'),
('23BCG10082','Mohd Bashar',        '23bcg10082@vitbhopal.ac.in','CSE',4,8.2,'Male'),
-- 24BCE Batch
('24BCE10019','Ayush Mishra',       '24bce10019@vitbhopal.ac.in','CSE',2,8.4,'Male'),
('24BCE10058','Anuj Parashar',      '24bce10058@vitbhopal.ac.in','CSE',2,7.8,'Male'),
('24BCE10080','Pranav Kumar',       '24bce10080@vitbhopal.ac.in','CSE',2,8.6,'Male'),
('24BCE10149','Rishi Raj',          '24bce10149@vitbhopal.ac.in','CSE',2,9.1,'Male'),
('24BCE10176','Klemens Nile Danny', '24bce10176@vitbhopal.ac.in','CSE',2,7.5,'Male'),
('24BCE10288','Shivam Sikka',       '24bce10288@vitbhopal.ac.in','CSE',2,8.0,'Male'),
('24BCE10293','Hardik Pichholiya',  '24bce10293@vitbhopal.ac.in','CSE',2,8.3,'Male'),
('24BCE10306','Arunima Mohan Rai',  '24bce10306@vitbhopal.ac.in','CSE',2,9.0,'Female'),
('24BCE10348','Saurav Choudhary',   '24bce10348@vitbhopal.ac.in','CSE',2,7.6,'Male'),
('24BCE10366','Yadvendra Raj Yadav','24bce10366@vitbhopal.ac.in','CSE',2,8.1,'Male'),
('24BCE10401','Parth Sarthi',       '24bce10401@vitbhopal.ac.in','CSE',2,7.9,'Male'),
('24BCE10459','Jashwin Sharma',     '24bce10459@vitbhopal.ac.in','CSE',2,8.7,'Male'),
('24BCE10475','Vivek Lohar',        '24bce10475@vitbhopal.ac.in','CSE',2,7.4,'Male'),
('24BCE10478','Kinshuk Sharma',     '24bce10478@vitbhopal.ac.in','CSE',2,8.5,'Male'),
('24BCE10519','Deepak Sharma',      '24bce10519@vitbhopal.ac.in','CSE',2,7.7,'Male'),
('24BCE10525','Priyanshi Gupta',    '24bce10525@vitbhopal.ac.in','CSE',2,9.2,'Female'),
('24BCE10541','Shreyanshi Tiwari',  '24bce10541@vitbhopal.ac.in','CSE',2,8.8,'Female'),
('24BCE10646','Krish',              '24bce10646@vitbhopal.ac.in','CSE',2,7.3,'Male'),
('24BCE10677','Atharva Dixit',      '24bce10677@vitbhopal.ac.in','CSE',2,8.2,'Male'),
('24BCE10685','Ayush Ranjan',       '24bce10685@vitbhopal.ac.in','CSE',2,8.0,'Male'),
('24BCE10706','Abhilash Singh',     '24bce10706@vitbhopal.ac.in','CSE',2,8.9,'Male'),
('24BCE10743','Sankalp Kabra',      '24bce10743@vitbhopal.ac.in','CSE',2,7.8,'Male'),
('24BCE10765','Saksham Pandey',     '24bce10765@vitbhopal.ac.in','CSE',2,8.4,'Male'),
('24BCE10801','Khengar Chaun',      '24bce10801@vitbhopal.ac.in','CSE',2,7.1,'Male'),
('24BCE10803','Anirudh Sharma',     '24bce10803@vitbhopal.ac.in','CSE',2,8.3,'Male'),
('24BCE10820','Suraj Kumar Chaudhary','24bce10820@vitbhopal.ac.in','CSE',2,7.6,'Male'),
('24BCE10867','Sakshi Vibhash Chandra','24bce10867@vitbhopal.ac.in','CSE',2,8.7,'Female'),
('24BCE10935','Shivam Kumar',       '24bce10935@vitbhopal.ac.in','CSE',2,7.9,'Male'),
('24BCE10942','Krishanu Das',       '24bce10942@vitbhopal.ac.in','CSE',2,8.1,'Male'),
('24BCE10979','Aayushmaan Kumar Pandey','24bce10979@vitbhopal.ac.in','CSE',2,7.5,'Male'),
('24BCE10993','Saurish Verma',      '24bce10993@vitbhopal.ac.in','CSE',2,8.6,'Male'),
('24BCE11096','Aditya Chandra',     '24bce11096@vitbhopal.ac.in','CSE',2,7.8,'Male'),
('24BCE11194','Kalpesh Parashar',   '24bce11194@vitbhopal.ac.in','CSE',2,8.0,'Male'),
('24BCE11197','Divyansh Sharma',    '24bce11197@vitbhopal.ac.in','CSE',2,8.5,'Male'),
('24BCE11234','Diya Mittal',        '24bce11234@vitbhopal.ac.in','CSE',2,9.3,'Female'),
('24BCE11247','Md Afzal Khan',      '24bce11247@vitbhopal.ac.in','CSE',2,8.8,'Male'),
('24BCE11260','Arav Sharma',        '24bce11260@vitbhopal.ac.in','CSE',2,7.7,'Male'),
('24BCE11307','Shreshth Gupta',     '24bce11307@vitbhopal.ac.in','CSE',2,8.2,'Male'),
('24BCE11360','Shishir Kant Upadhyay','24bce11360@vitbhopal.ac.in','CSE',2,7.4,'Male'),
('24BCE11384','Sarthak Solanki',    '24bce11384@vitbhopal.ac.in','CSE',2,8.9,'Male'),
('24BCE11391','Athar Abbas Zaidi',  '24bce11391@vitbhopal.ac.in','CSE',2,7.6,'Male'),
('24BCE11393','Aarshi Jaiswal',     '24bce11393@vitbhopal.ac.in','CSE',2,9.0,'Female'),
('24BCE11418','Tejas Singh',        '24bce11418@vitbhopal.ac.in','CSE',2,8.3,'Male'),
('24BCE11434','Sanskar Shrivastava','24bce11434@vitbhopal.ac.in','CSE',2,7.8,'Male'),
('24BCE11444','Aditya Kumar Singh', '24bce11444@vitbhopal.ac.in','CSE',2,8.1,'Male'),
('24BCE11460','Daksh Taneja',       '24bce11460@vitbhopal.ac.in','CSE',2,7.5,'Male'),
('24BCE11505','Smridhi Narwal',     '24bce11505@vitbhopal.ac.in','CSE',2,8.7,'Female'),
('24BCE11514','Daksh Maru',         '24bce11514@vitbhopal.ac.in','CSE',2,7.9,'Male');

-- Sample attendance records
INSERT IGNORE INTO attendance (student_id, subject, attendance_date, status) VALUES
  (1,'Advanced Java Programming (CSE4019)','2025-01-15','PRESENT'),
  (2,'Advanced Java Programming (CSE4019)','2025-01-15','PRESENT'),
  (3,'Advanced Java Programming (CSE4019)','2025-01-15','ABSENT'),
  (1,'Advanced Java Programming (CSE4019)','2025-01-17','PRESENT'),
  (2,'Advanced Java Programming (CSE4019)','2025-01-17','LATE'),
  (3,'Advanced Java Programming (CSE4019)','2025-01-17','PRESENT');

SELECT 'Database setup complete!' AS message;
