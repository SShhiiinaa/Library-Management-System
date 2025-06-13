## 1. 代码结构

- **dao/**  
  数据访问层，负责与数据库交互  
  - AdminDao, BookDao, BorrowDao, BorrowRecordDao, CategoryDao, OperationLogDao, ReaderDAO, RoleDao, UserRoleDao

- **exception/**  
  自定义异常类  
  - AuthenticationException  
  - DuplicateUsernameException  
  - ServiceException

- **filter/**  
  过滤器  
  - AuthFilter （登录校验、权限控制）

- **model/**  
  实体类（JavaBean）  
  - Admin, Book, BorrowRecord, Category, OperationLog, Reader, Role, User, UserRole

- **service/**  
  业务逻辑层  
  - BookService, BorrowService, ReaderService, UserService.java

- **servlet/**  
  控制器（Servlet），处理各类请求  
  - BookManageServlet, BookSearchServlet, BorrowRecordServlet, BorrowServlet, LoginServlet, LogoutServlet, ReaderManageServlet, RegisterServlet, ReturnBooksServlet

- **util/**  
  工具类  
  - DatabaseUtil, InputSanitizer, OperationLogger

## 2. 前端与资源结构

- **resources/**  
  资源文件目录（用于配置，暂未展开）

- **webapp/**  
  Web资源目录
  - **js/**  
    前端JavaScript文件目录
  - **WEB-INF/**  
    JSP页面及配置文件存放处（外部无法直接访问）
    - admin-home.jsp, bookEdit.jsp, bookManage.jsp, borrow.jsp, borrowRecord.jsp, ddd.html, error.jsp, index.jsp, login.jsp, readerEdit.jsp, readerManage.jsp, register.jsp, returnBooks.jsp, search.jsp

---
