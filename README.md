# java-filmorate

Template repository for Filmorate project.

База данных программы содержит ER-диаграмму:
![Схема базы данных](https://github.com/Rass00032/java-filmorate/assets/115939388/b1daff00-1bf4-4c1c-b5de-fd485cf4fc80)

Примеры SQL-запросов к базе данных:

 Получение списка друзей пользователя по '*userId из запроса*':
 ```SQL
SELECT friend_id
FROM users 
WHERE user_id = 1
```
