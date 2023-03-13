<div id="MainTitle">

# Клиент-серверное приложение

</div>

<div id="SubTitle"> 

### Приложение для организации покупок сети кофеен

</div>

## Команда
1. Васильев Александр.
2. Лукьяненко Милана.
3. Деревяникин Павел.
4. Шабанова Ирина.

## Предметная область
Приложение для конкретной сети кофейн.

## Функциональные требования
1. Транзакционные
   - Добавить пункт меню.
   - Добавить новую кофейню.
   - Изменить время работы.
   - Удалить оборудование для конкретной кофейни.

2. Справочные
   - Получить данные всех кофейн в заданном городе.
   - Получить всех работников кофейни.
   - Получить количество работников на заданной должности.
   - Получить адрес заданной кофейни.
   - Получить количество кофеен в городе.
   
3. Аналитические
   - Средняя цена кофе из всего ассортимента.
   - Количество заказов в кофейне за конктретный день.
   - Самый дешевый завтрак.
   - Город наибольшим количеством кофейн.
   - Сколько видов кофе присутствует в меню

## Описание хранимых данных в БД.
  - Первая бд (MS Sql Server, MariaDB)
    
    - Предназначение - хранение информации о продуктах (информация о ингридиентах и пищевой ценности).
  
      ![db_products_v3](https://user-images.githubusercontent.com/66141673/113042580-09cc9a80-91a4-11eb-8a69-3b64789a16fc.png)
  
  - Вторая бд (PostgreSQL)
  
    - Предназначение - хранение информации о кофейнях и персонале
  
      ![WwDcj4l_edo (1)](https://user-images.githubusercontent.com/57771719/111890171-06911c00-89f8-11eb-8c67-cfc7abc93f91.jpg)
    
  - Третья бд (MongoDB)
  
    - Предназначение - хранение информации о пользователях и заказах.
  
      ![db_users_v2](https://user-images.githubusercontent.com/66141673/112721953-cd850a00-8f17-11eb-86e0-2c3db9cb4bc0.png)

## Перечень основных операций
![image](https://user-images.githubusercontent.com/57771719/111890448-786a6500-89fa-11eb-971e-5926f2a760cc.png)

## Trello
   https://trello.com/b/slfP0CKa


# Лабараторная №2
## Макеты приложения
   <img src="https://user-images.githubusercontent.com/66141673/115174398-db224f80-a0d1-11eb-8fc0-c1e2da710f10.png" width="50%"></img><img src="https://user-images.githubusercontent.com/66141673/115174425-e2e1f400-a0d1-11eb-86d1-bbca3bead42b.png" width="50%"></img><img src="https://user-images.githubusercontent.com/66141673/115174439-e8d7d500-a0d1-11eb-9fd4-8587371a0cdd.png" width="50%"></img><img src="https://user-images.githubusercontent.com/66141673/115174464-f42b0080-a0d1-11eb-8aca-3e2d01a6b8ed.png" width="50%"></img><img src="https://user-images.githubusercontent.com/66141673/115174479-fb520e80-a0d1-11eb-9640-ff5c9f3d23fc.png" width="50%"></img><img src="https://user-images.githubusercontent.com/66141673/115174488-00af5900-a0d2-11eb-82a7-db6206ce6896.png" width="50%"></img>

   






## Схемы и примеры заполнения бд
   - MariaDB
     
     - БД товаров меню
       
       ![image](https://user-images.githubusercontent.com/66141673/113521561-f1dd8800-95a2-11eb-921a-6a8bd35e4774.png)
       ![image](https://user-images.githubusercontent.com/66141673/113521568-f9049600-95a2-11eb-9791-c68234af4c26.png)
       
   - MS Sql Server

     - БД товаров меню
     
       ![image](https://user-images.githubusercontent.com/75484601/113525101-b69b8300-95bb-11eb-895f-32f3ec787e6e.png)
       ![image](https://user-images.githubusercontent.com/66141673/113524824-f3667a80-95b9-11eb-8b38-2e28c6ac6ce4.png)
  
   - PostgeSQL
  
     - БД кофеен
  
       ![image](https://user-images.githubusercontent.com/66141673/113521447-2bfa5a00-95a2-11eb-96f0-190a0a8b88fc.png)
       ![image](https://user-images.githubusercontent.com/66141673/113521215-b346ce00-95a0-11eb-839c-9eaaf8db21b7.png) 

   - MongoDB
   
     - БД с пользователями
     
       ![image](https://user-images.githubusercontent.com/66141673/113521696-d32bc100-95a3-11eb-8e69-e2049ef3c9c1.png)

## Диаграмма классов мобильного приложения

   ![Coffees (1)](https://user-images.githubusercontent.com/66141673/121871016-4c663380-cd0c-11eb-93f4-ecad0fb42587.png)

## ## Технологический стек:

<div id="TechStack">

* ASP.NET Core
* EF
* MongoDB
* PostgreSQL
* MariaDB
* MySQL

</div>
