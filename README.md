# Java-Client-Server-Quiz
This is simple client-server Quiz game. 

# Application setup
Your server.config file should look similar to this:

![image](https://user-images.githubusercontent.com/60007028/131218388-7397d806-69e0-41bd-b687-5b2954817402.png)

When you first run server app you have to create your database, download adn add JDBC Driver for PostgreSQL to your project. You can download it from this site: https://jdbc.postgresql.org/download.html . After you add JDBC driver you have to run createTable() and fillTable() methods from CreateDatabase class (uncomment this 3 lines). By doing this you create table and fill it with questions.

![image](https://user-images.githubusercontent.com/60007028/131220291-b2333658-31cc-4649-acf9-db4aa1bf3572.png)


# Run application

After that you need to run server and wait till 2 players connect. Server fetches questions from database and shuffle them. 
When first player connects window will pop up and he will have to wait for second player.

![image](https://user-images.githubusercontent.com/60007028/131218524-5897fa78-38a5-4cb4-9c1f-6f261feab4f6.png)

When second player connets the game starts, server sends qusetions to players and their windows change state and it looks like this:

![image](https://user-images.githubusercontent.com/60007028/131218542-feb6b3e0-ffe4-4951-80f9-37c2ceab891c.png)

After playes answer all questions game ends and display result.

Example window for first player will look like this:

![image](https://user-images.githubusercontent.com/60007028/131218597-dd7c3179-1455-4105-a662-650661e59326.png)

For second player it will look like this:

![image](https://user-images.githubusercontent.com/60007028/131218616-2488beec-2daf-4f22-8156-d52d7591b3b8.png)



