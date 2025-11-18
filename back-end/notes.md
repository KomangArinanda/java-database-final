
mongoimport --uri="mongodb://root:s7PPDGrJe6AB2XM8x3qOIQiP@172.21.43.173/reviews?authSource=admin" --collection=reviews --file /home/project/java-database-final/reviews.json --jsonArray
mysql -h 172.21.94.63 -uroot -p0SZmlAsE0zEHQsMGAf0NCAw5 < /home/project/java-database-final/insert_data.sql

DELIMITER //

CREATE PROCEDURE GetMonthlySalesForEachStore(
IN year_param INT,
IN month_param INT
)
BEGIN
SELECT
od.store_id,
SUM(od.total_price) AS total_sales,
MONTH(od.date) AS sales_month,
YEAR(od.date) AS sales_year
FROM order_details od
WHERE MONTH(od.date) = month_param
AND YEAR(od.date) = year_param
GROUP BY od.store_id, MONTH(od.date), YEAR(od.date)
ORDER BY total_sales DESC;
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE GetAggregateSalesForCompany(
IN year_param INT,
IN month_param INT
)
BEGIN
SELECT
SUM(od.total_price) AS total_sales,
MONTH(od.date) AS sales_month,
YEAR(od.date) AS sales_year
FROM order_details od
WHERE MONTH(od.date) = month_param
AND YEAR(od.date) = year_param
GROUP BY MONTH(od.date), YEAR(od.date)
ORDER BY total_sales DESC;
END //

DELIMITER ;




DELIMITER //

CREATE PROCEDURE GetTopSellingProductsByCategory(
IN target_month INT,
IN target_year INT
)
BEGIN
SELECT
t.category,
t.product_name AS name,
t.total_quantity_sold,
t.total_sales
FROM (
-- Step 1: total sales per product per category
SELECT
p.category,
p.name AS product_name,
SUM(oi.quantity) AS total_quantity_sold,
SUM(oi.quantity * oi.price) AS total_sales
FROM order_item oi
JOIN order_details od ON oi.order_id = od.id
JOIN product p ON oi.product_id = p.id
WHERE MONTH(od.date) = target_month
AND YEAR(od.date) = target_year
GROUP BY p.category, p.id
) t
WHERE t.total_quantity_sold = (
-- Step 2: find max quantity per category
SELECT MAX(sub.total_quantity_sold)
FROM (
SELECT
SUM(oi2.quantity) AS total_quantity_sold
FROM order_item oi2
JOIN order_details od2 ON oi2.order_id = od2.id
JOIN product p2 ON oi2.product_id = p2.id
WHERE MONTH(od2.date) = target_month
AND YEAR(od2.date) = target_year
AND p2.category = t.category
GROUP BY p2.id
) sub
)
ORDER BY t.category;
END //

DELIMITER ;



DELIMITER $$

CREATE PROCEDURE GetTopSellingProductByStore(
IN target_month INT,
IN target_year INT
)
BEGIN
SELECT
x.store_id,
x.name,
x.total_quantity_sold,
x.total_sales
FROM (
SELECT
od.store_id,
p.name,
SUM(oi.quantity) AS total_quantity_sold,
SUM(oi.quantity * oi.price) AS total_sales,
ROW_NUMBER() OVER (
PARTITION BY od.store_id
ORDER BY SUM(oi.quantity) DESC
) AS rn
FROM order_item oi
JOIN order_details od ON oi.id = od.id
JOIN product p ON oi.id = p.id
WHERE MONTH(od.date) = target_month
AND YEAR(od.date) = target_year
GROUP BY od.store_id, p.name
) x
WHERE x.rn = 1;
END$$

DELIMITER ;


DROP PROCEDURE GetTopSellingProductByStore;


const apiURL = 'https://captainfedo1-8080.theiadockernext-0-labs-prod-theiak8s-4-tor01.proxy.cognitiveclass.ai';

https://labs.cognitiveclass.ai/v2/tools/cloud-ide-kubernetes?ulid=ulid-4aea8156297d8528e06635fac4bc06b8e4e07424