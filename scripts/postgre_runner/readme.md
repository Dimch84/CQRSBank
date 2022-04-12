Команда запуска базы данных локально:
БД: cqrs
Пользователь cqrs
Пароль cqrs
Порт 5432

```
docker run --rm --name cqrs -e POSTGRES_PASSWORD=cqrs -e POSTGRES_USER=cqrs -e POSTGRES_DB=cqrs -d -p 5432:5432 postgres
```

Остановка:

```
docker stop cqrs
```
