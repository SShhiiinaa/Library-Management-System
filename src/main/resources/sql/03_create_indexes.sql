CREATE TABLE users (
                       user_id NUMBER PRIMARY KEY,
                       username VARCHAR2(50) UNIQUE NOT NULL,
                       password VARCHAR2(64) NOT NULL,
                       role VARCHAR2(10) CHECK (role IN ('ADMIN', 'USER')),
                       created_date DATE DEFAULT SYSDATE
);

-- 序列（用于自增ID）
CREATE SEQUENCE user_seq START WITH 1001 INCREMENT BY 1;