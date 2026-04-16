-- Top-Selling Products

SELECT p.name, SUM(oi.quantity) AS total_sold
FROM Order_Items oi
JOIN Products p ON oi.product_id = p.product_id
GROUP BY p.name
ORDER BY total_sold DESC;

-- Most Valuable Customers (by total spending)

SELECT c.name, SUM(p.price * oi.quantity) AS total_spent
FROM Customers c
JOIN Orders o ON c.customer_id = o.customer_id
JOIN Order_Items oi ON o.order_id = oi.order_id
JOIN Products p ON oi.product_id = p.product_id
GROUP BY c.name
ORDER BY total_spent DESC;

-- Monthly Revenue Calculation

SELECT 
    DATE_FORMAT(o.date, '%Y-%m') AS month,
    SUM(p.price * oi.quantity) AS revenue
FROM Orders o
JOIN Order_Items oi ON o.order_id = oi.order_id
JOIN Products p ON oi.product_id = p.product_id
GROUP BY month
ORDER BY month;

-- Category-wise Sales Analysis

SELECT 
    p.category,
    SUM(p.price * oi.quantity) AS total_sales
FROM Order_Items oi
JOIN Products p ON oi.product_id = p.product_id
GROUP BY p.category
ORDER BY total_sales DESC;

-- Inactive Customers (no orders)

SELECT c.name
FROM Customers c
LEFT JOIN Orders o ON c.customer_id = o.customer_id
WHERE o.order_id IS NULL;