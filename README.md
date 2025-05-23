**How to set up the project**
<br><br>
We will need to create an user called "prueba" with the password "12345" on our MySQL instance and grant it permissions to read, create, update and delete registries using the commands found below, alternatively we can modify the "application.properties" file to configure our own username and password combo:
<br><br><br>
*CREATE USER 'prueba'@'localhost' IDENTIFIED BY '12345';*
<br><br>
*GRANT CREATE, ALTER, DROP, INSERT, UPDATE, DELETE, SELECT, REFERENCES, RELOAD on \*.\* TO 'prueba'@'localhost' WITH GRANT OPTION;*
<br><br><br>
A database named "SpringBootCapstoneProject" is also required, we can create it using the following command:
<br><br><br>
*CREATE DATABASE SpringBootCapstoneProject;*
<br><br><br>
It's not necessary to create the databases tables manually, as the application will do it automatically, however it's required that we insert some values in certain tables, to begin we should insert the following values in the "status" table:
<br><br><br>
INSERT INTO status(name) VALUES('Created');
<br>
INSERT INTO status(name) VALUES('In progress');
<br>
INSERT INTO status(name) VALUES('Finished');
<br><br><br>
Then we should insert the following values in the "priority" table:
<br><br><br>
INSERT INTO priority(name) VALUES('Low');
<br>
INSERT INTO priority(name) VALUES('Medium');
<br>
INSERT INTO priority(name) VALUES('High');
<br><br><br>
Finally we should insert these values in the "roles" table before using the application as otherwise it would not work correctly:
<br><br><br>
*INSERT INTO roles(name) VALUES('ROLE_USER');*
<br>
*INSERT INTO roles(name) VALUES('ROLE_MODERATOR');*
<br>
*INSERT INTO roles(name) VALUES('ROLE_ADMIN');*
<br><br><br>
**How to run the application**
<br><br>
The application runs on port 8080, it will require authentication to access almost all the endpoints, therefore first it will be necessary that we register an user and then we login with said user.
<br><br>
It implements JWT tokens that will be returned in a cookie when a login request is succesful, reducing the need to consistently authenticate ourselves for future requests, at least until the token expires.
<br><br>
Some endpoints are only available to the specific users who created the pertinent task, there also are endpoints that will only work for users with an "admin" role.
<br><br>
The applications has a logger that will record all the relevant events in that occur in it, they will be saved on an external file in the route "/logs/app.log" of the application.
<br><br><br>
**API endpoints and sample requests for testing**
<br><br><br>
**Auth API**
<br><br><br>
*POST /api/auth/signup*
<br><br><br>
We can use it to register new users by sending a request body with the properties of an uesr object, as presented below:
<br><br><br>
{
"username": "BartSimpson25",
"email": "futurama@gmail.com",
"password": "aycaramba",
"role": ["user"]
}
<br><br><br>
We can't have two users with the same username or email, note that we are sending a roles array therefore an user can have multiple roles
<br><br><br>
*POST /api/auth/signin*
<br><br><br>
We can use it to login with a registered user by sending a request body its credentials, as presented below:
<br><br><br>
*{*
*"username": "BartSimpson25",*
*"password": "aycaramba"*
*}*
<br><br><br>
This will return us a cookie with a JWT token to authenticate ourselves in further requests
<br><br><br>
*POST /api/auth/signout*
<br><br><br>
We can use it to logout an already authenticated user, this will invalidate the cookie with its JWT token as it will expire automatically
<br><br><br>
**Tasks API**
<br><br><br>
*GET /tasks
<br><br><br>
This will return a list with all the tasks registered in the "tasks" table (Authentication with an "admin" role required)
<br><br><br>
*GET /tasks/{id}*
<br><br><br>
We can replace {id} with the number of the id of an specific task in the "tasks" table, it will return us the object that represents it (Authentication with an "user" or "admin" role required)
<br><br><br>
If the user has an "user" role, it will evaluate if the user was the one who created the task, if this is true, the task will be returned to him, otherwise it will not.
<br><br><br>
If the user has an "admin" role, it will always return the task to him, notwithstanding if he was the one who created it or not.
<br><br><br>
*POST /tasks
<br><br><br>
We can send a request body with the properties of a task object to create and insert it in the "tasks" table as presented below (Authentication with an "user" or "admin" role required):
<br><br><br>
*{*
*"title": "Make dinner",*
*"description": "Order something from Uber Eats",*
*"status": {*
	*"id": 1*
*},*
*"priority": {*
	*"id": 3*
*},*
*"dueDate": "2025-05-23",*
*"user": {*
	*"id": 1*
*}*
*}*
<br><br><br>
*PUT /tasks/{id}*
<br><br><br>
We can replace {id} with the number of the id of an specific task in the "tasks" table and send a request body with the properties of a book object to update that specific task details, as presented below (Authentication with an "user" or "admin" role required):
:
<br><br><br>
*{*
*"title": "Make dinner",*
*"description": "Order something from Uber Eats",*
*"status": {*
	*"id": 3*
*},*
*"priority": {*
	*"id": 3*
*},*
*"dueDate": "2025-05-23",*
*"user": {*
	*"id": 1*
*}*
*}*
<br><br><br>
If the user has an "user" or "admin" role, it will evaluate if the user was the one who created the task, if this is true, the task will be updated, otherwise it will not.
<br><br><br>
*DELETE /tasks/{id}*
<br><br><br>
We can replace {id} with the number of the id of an specific task in the "tasks" table, it will delete its registry (Authentication with an "user" or "admin" role required)
<br><br><br>
If the user has an "user" role, it will evaluate if the user was the one who created the task, if this is true, the task will be deleted, otherwise it will not.
<br><br><br>
If the user has an "admin" role, the task will always be deleted, notwithstanding if he was the one who created it or not.
<br><br><br>
**Users API**
<br><br><br>
*GET /users
<br><br><br>
This will return a list with all the users registered in the "users" table (Authentication with an "admin" role required)
<br><br><br>
*GET /users/{id}*

We can replace {id} with the number of the id of an specific user in the "users" table, it will return us the number of tasks which that user has created (Authentication with an "admin" role required)
