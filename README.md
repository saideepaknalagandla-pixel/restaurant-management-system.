# Restaurant Management System

A Java web application for browsing a restaurant menu, building a session-based cart, placing orders, and managing order statuses from a simple admin dashboard.

## Features
- Responsive restaurant landing page and menu
- Session-based shopping cart with quantity updates/removal
- Checkout with basic input validation
- MySQL order persistence using JDBC transactions and prepared statements
- Admin dashboard to view recent orders, update order status, add menu items, and hide menu items
- Maven WAR build for Apache Tomcat 10.1+

## Tech stack
Java 17+ (tested target), Jakarta Servlets 6, JSP, JDBC, MySQL 8, Maven, Apache Tomcat 10.1, HTML, CSS.

## Prerequisites
- JDK 17 or newer (JDK 25 is fine)
- MySQL Server 8+
- Maven 3.9+
- Apache Tomcat 10.1+

## Run locally
1. Start MySQL.
2. Open MySQL Workbench or a MySQL terminal and run `database/schema.sql`.
3. Edit `src/main/java/com/restaurant/util/DBConnection.java`. Set `USER` and `PASSWORD` for your local MySQL account. Do not commit real credentials.
4. From this project folder, run:
   ```bash
   mvn clean package
   ```
5. Copy `target/RestaurantManagementSystem.war` to Tomcat's `webapps` directory.
6. Start Tomcat using `bin/startup.bat` on Windows.
7. Open `http://localhost:8080/RestaurantManagementSystem/`.

## Main routes
- `/` - landing page
- `/menu` - menu loaded from MySQL
- `/cart` - cart
- `/checkout` - checkout
- `/admin` - order/menu management

## Important security note
This is a learning portfolio project. The admin route is intentionally simple and **does not include authentication** yet. Do not deploy it publicly or use real customer data until login, role-based access control, CSRF protection, output escaping, and secure configuration have been added. For a public deployment, store DB credentials in environment variables or a secret manager and never commit passwords.

## Portfolio improvements to add next
- Admin login with password hashing and role checks
- Search/filter menu and category filters
- Order detail page and customer order history
- JUnit tests and screenshots
- Deploy to a server with managed MySQL and HTTPS
