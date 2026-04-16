-- Customers
INSERT INTO Customers VALUES
(1, 'Vijay', 'Chennai'),
(2, 'Arun', 'Bangalore'),
(3, 'Priya', 'Hyderabad'),
(4, 'Kiran', 'Mumbai');

-- Products
INSERT INTO Products VALUES
(101, 'Laptop', 'Electronics', 50000),
(102, 'Phone', 'Electronics', 20000),
(103, 'Shoes', 'Fashion', 3000),
(104, 'Watch', 'Accessories', 5000);

-- Orders
INSERT INTO Orders VALUES
(1001, 1, '2025-01-10'),
(1002, 2, '2025-01-15'),
(1003, 1, '2025-02-05'),
(1004, 3, '2025-02-20');

-- Order Items
INSERT INTO Order_Items VALUES
(1001, 101, 1),
(1001, 103, 2),
(1002, 102, 1),
(1003, 104, 3),
(1004, 101, 1),
(1004, 102, 2);