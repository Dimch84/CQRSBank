Команда запуска базы данных локально:
БД: cqrs
Пользователь cqrs
Пароль cqrs
Порт 5432

```
docker run --rm --name cqrs -e POSTGRES_PASSWORD=cqrs -e POSTGRES_USER=cqrs -e POSTGRES_DB=cqrs -d -p 5432:5432 -v <Путь до текущей директории>/init:/docker-entrypoint-initdb.d postgres
```

Остановка:

```
sudo docker stop cqrs
```
