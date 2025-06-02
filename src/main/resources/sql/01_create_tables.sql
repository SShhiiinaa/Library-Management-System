-- 图书表
CREATE TABLE books (
                       book_id NUMBER PRIMARY KEY,
                       title VARCHAR2(100) NOT NULL,
                       author VARCHAR2(50),
                       isbn VARCHAR2(20) UNIQUE,
                       category VARCHAR2(30),
                       publish_date DATE,
                       stock NUMBER DEFAULT 0
);

-- 用户表
CREATE TABLE users (
                       user_id NUMBER PRIMARY KEY,
                       username VARCHAR2(50) UNIQUE NOT NULL,
                       password VARCHAR2(64) NOT NULL, -- SHA-256加密
                       role VARCHAR2(10) CHECK (role IN ('ADMIN', 'USER'))
);

-- 借阅记录表
CREATE TABLE borrow_records (
                                record_id NUMBER PRIMARY KEY,
                                user_id NUMBER REFERENCES users(user_id),
                                book_id NUMBER REFERENCES books(book_id),
                                borrow_date DATE DEFAULT SYSDATE,
                                return_date DATE,
                                status VARCHAR2(10) CHECK (status IN ('BORROWED', 'RETURNED'))
);
CREATE TABLE users (
                       user_id NUMBER PRIMARY KEY,
                       username VARCHAR2(50) UNIQUE NOT NULL,
                       password VARCHAR2(64) NOT NULL, -- SHA-256加密存储
                       role VARCHAR2(10) CHECK(role IN ('ADMIN','USER')),
                       email VARCHAR2(100),
                       created_date DATE DEFAULT SYSDATE
);

CREATE SEQUENCE user_seq START WITH 1001 INCREMENT BY 1;