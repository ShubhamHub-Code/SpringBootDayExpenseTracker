# Day-to-Day Expense Tracker — Spring Boot + PostgreSQL

A production-ready Expense Tracking application built by **Shubham Mankar**, focused on
real-world architecture and backend engineering best practices using **Spring Boot (Monolith)**.

This project demonstrates how a 3+ year backend developer structures, secures, and scales
a financial application — from authentication to database migrations and clean layering.

---

## 👤 Author — Shubham Babaji Mankar

**Backend Java Developer (3+ years)**  
- Spring Boot | Java | Microservices  
- REST API design & system integration  
- JPA/Hibernate | PostgreSQL  
- CI/CD | Jenkins | GitHub | Tomcat  
- Performance optimization & clean architecture  

📧 Email: shubhammankar0401@gmail.com  
🔗 LinkedIn: www.linkedin.com/in/shubham-mankar-7a2b41201  
🐙 GitHub: https://github.com/ShubhamHub-Code

---

## ✨ Key Features

- User registration & JWT-based authentication
- Secure role-based access  
- Manage income & expenses
- 🧰 Clean layered architecture (Controller → Service → Repository)  
- ⚠ Centralized exception handling  
- Pagination & filtering APIs
- Logging & structured architecture
- 🔍 Logging & monitoring ready (Actuator)

---

## 🏗️ Tech Stack

| Category | Tools |
|----------|-------|
Backend | Spring Boot 4, Java 25  
Persistence | Spring Data JPA, Hibernate  
Database | PostgreSQL   
Security | Spring Security (JWT)  
Build | Maven  
Utilities | Lombok, Validation API  

---

## 📂 Architecture Overview

```
controller  → API layer
service     → business logic
repository  → persistence layer
dto         → request/response models
entity      → JPA entities
security    → authentication & authorization
exception   → global error handling
config      → shared configuration
```

Designed for **maintainability, testability, and scalability**.

---

## 🚀 Getting Started

### 1️⃣ Clone repository

```bash
git clonegit clone https://github.com/ShubhamHub-Code/SpringBootDayExpenseTracker.git
cd expense-tracker
```

### 2️⃣ Create PostgreSQL DB

```sql
CREATE DATABASE expense_db;
CREATE USER expense_user WITH ENCRYPTED PASSWORD 'expense_pass';
GRANT ALL PRIVILEGES ON DATABASE expense_db TO expense_user;
```

### 3️⃣ Configure application (if needed)

Edit:

```
src/main/resources/application.yml
```

### 4️⃣ Run the application

```bash
mvn spring-boot:run
```

### 5️⃣ Test health endpoint

Open:

```
http://localhost:8080/api/health
```

---

## 🧭 Roadmap (Upcoming Phases)

- ✔ JWT authentication + refresh tokens  
- ✔ Expense & Category CRUD APIs  
- ⏳ Monthly reports and charts   

---

## 🤝 Contributions

This project is actively evolving as part of my **Spring Boot industry practice journey**.  
Suggestions, issues, and PRs are welcome!

---

## 🙌 Acknowledgment

Inspired by real-world backend architecture patterns focused on **production-first design**.

---

> ⭐ If you find this useful, please **star the repository** — it helps support further development!
