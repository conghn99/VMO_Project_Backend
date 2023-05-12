Environment:
- Java 17
- Maven 3.9.1
- MySQL

NOTE:
- After running the project successfully the first time, call api POST localhost:8080/api/user by postman or Swagger (localhost:8080/swagger-ui/index.html) first and only call this API ONE TIME to init user (username: admin, password: admin)
- Login by the above account to access the other API
+ Postman: Authorization -> Bearer token -> paste the token provided after login
+ Swagger: Authorize -> paste Bearer + token provided after login
