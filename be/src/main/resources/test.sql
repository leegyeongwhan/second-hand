SELECT p.*
FROM product p
         LEFT JOIN interested i ON p.member_id = i.product_id
         LEFT JOIN town t ON p.town_id = t.town_id
         LEFT JOIN category c ON p.category_id = c.category_id
         LEFT JOIN member m ON p.member_id = m.member_id
WHERE p.status = 'SOLD'
  AND p.member_id = 4
ORDER BY product_id DESC;


SELECT p.*
FROM product p
         JOIN interested i ON p.product_id = i.product_id
WHERE p.category_id = 1
  and p.member_id = 4;

select *
from product
where product_id = 2;


SELECT p.*
FROM product p
         LEFT JOIN town t ON p.town_id = t.town_id
         LEFT JOIN category c ON p.category_id = c.category_id
         LEFT JOIN member m ON p.member_id = m.member_id
WHERE m.member_id = 4
GROUP BY p.status, p.product_id
HAVING (:status = 0 AND (p.status = 'SELLING' OR p.status = 'RESERVING'))
    OR (:status = 2 AND p.status = 'SOLD')
ORDER BY p.product_id DESC
LIMIT :offset, :limit

    DELIMITER //

