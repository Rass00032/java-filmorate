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
Получение списка пользователей, которым понравился фильм:
 ```SQL
SELECT user_id
FROM films AS f
INNER JOIN likes AS l ON f.film_id = l.film_id
WHERE f.film_id = 1
```
Получение списка жанров для конкретного фильма:
 ```SQL
SELECT g.id, g.name
FROM films AS f
JOIN films_genres AS fg ON fg.film_id = f.id
JOIN genres AS g ON fg.genre_id = g.id
WHERE f.id = ?
ORDER BY g.id
```
