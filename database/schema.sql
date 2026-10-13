CREATE DATABASE IF NOT EXISTS restaurant_management;
USE restaurant_management;

CREATE TABLE IF NOT EXISTS menu_items (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(255),
  category VARCHAR(60) NOT NULL DEFAULT 'Main Course',
  price DECIMAL(10,2) NOT NULL,
  image_url VARCHAR(500),
  available BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS restaurant_orders (
  id INT PRIMARY KEY AUTO_INCREMENT,
  customer_name VARCHAR(100) NOT NULL,
  customer_email VARCHAR(150) NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  status ENUM('PLACED','PREPARING','READY','DELIVERED','CANCELLED') NOT NULL DEFAULT 'PLACED',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_items (
  id INT PRIMARY KEY AUTO_INCREMENT,
  order_id INT NOT NULL,
  menu_item_id INT NULL,
  item_name VARCHAR(100) NOT NULL,
  quantity INT NOT NULL,
  unit_price DECIMAL(10,2) NOT NULL,
  CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES restaurant_orders(id) ON DELETE CASCADE,
  CONSTRAINT fk_order_item_menu FOREIGN KEY (menu_item_id) REFERENCES menu_items(id) ON DELETE SET NULL
);

INSERT INTO menu_items (name, description, category, price, image_url) VALUES
('Paneer Butter Masala','Creamy tomato gravy with paneer','Main Course',220,'https://images.unsplash.com/photo-1631452180519-c014fe946bc7?auto=format&fit=crop&w=900&q=80'),
('Chicken Biryani','Fragrant basmati rice with spices','Biryani',260,'https://images.unsplash.com/photo-1563379091339-03246963d96c?auto=format&fit=crop&w=900&q=80'),
('Masala Dosa','Crispy dosa served with chutney','South Indian',100,'https://images.unsplash.com/photo-1589301760014-d929f3979dbc?auto=format&fit=crop&w=900&q=80'),
('Veg Fried Rice','Wok-tossed rice and vegetables','Rice',150,'https://images.unsplash.com/photo-1603133872878-684f208fb84b?auto=format&fit=crop&w=900&q=80'),
('Chocolate Brownie','Warm chocolate brownie','Dessert',90,'https://images.unsplash.com/photo-1606313564200-e75d5e30476c?auto=format&fit=crop&w=900&q=80');
