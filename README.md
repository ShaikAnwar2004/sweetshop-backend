# 🍬 Sweet Shop Management System – Backend

This is the backend API for the Sweet Shop Management System, built using **Spring Boot**, **MySQL**, and **JWT Authentication**. It provides secure endpoints for managing sweets, inventory, and user roles (Admin/User).

---

## 🚀 Technologies Used

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- JWT (JSON Web Token)
- Maven

---

## 📦 Features

- ✅ User Authentication (Login/Register)
- ✅ Role-Based Access Control (Admin/User)
- ✅ CRUD operations for sweets
- ✅ Inventory management (purchase, restock)
- ✅ Search sweets by name, category, price range
- ✅ Inventory report (total stock, value, top sweet)
- ✅ Secure endpoints with JWT and role checks

---

## 🧑‍💻 API Endpoints

| Method | Endpoint                     | Description                          | Access   |
|--------|------------------------------|--------------------------------------|----------|
| POST   | `/api/auth/register`         | Register new user                    | Public   |
| POST   | `/api/auth/login`            | Login and receive JWT                | Public   |
| POST   | `/api/sweets/add`            | Add new sweet                        | Admin    |
| GET    | `/api/sweets/inventory`      | View all sweets                      | Public   |
| GET    | `/api/sweets/search`         | Search sweets                        | Public   |
| PUT    | `/api/sweets/{id}`           | Update sweet                         | Admin    |
| DELETE | `/api/sweets/{id}`           | Delete sweet                         | Admin    |
| POST   | `/api/sweets/{id}/purchase`  | Purchase sweet                       | User/Admin |
| POST   | `/api/sweets/{id}/restock`   | Restock sweet                        | Admin    |
| GET    | `/api/sweets/report`         | View inventory report                | Admin    |

---

## 🛠️ Setup Instructions

1. **Clone the repo**
   ```bash
   git clone https://github.com/ShaikAnwar2004/sweetshop-backend.git
   cd sweetshop-backend
