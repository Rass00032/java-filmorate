# java-filmorate

Template repository for Filmorate project.

База данных программы содержит ER-диаграмму:
![Схема базы данных](https://github.com/Rass00032/java-filmorate/assets/115939388/b1daff00-1bf4-4c1c-b5de-fd485cf4fc80)

Примеры SQL-запросов к базе данных:

 Получение списка друзей пользователя по '*userId из запроса*':
 ```SQL
SELECT *
FROM users 
WHERE user_id IN (SELECT friend_id 
                  FROM friendship 
                  WHERE user_id = <userId from request> AND
                        confirmed = true
                  UNION
                  SELECT user_id
                  FROM friendship
                  WHERE friend_id = <userId from request> AND
                        confirmed = true);
```
