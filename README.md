# Users Rest Service

Users service is a service for aggregating users data from multiple databases. Application 
provides two rest endpoints for selecting data, selected from all databases.

GET /users - retrieve all users

POST /users - search users by criteria(an additional endpoint to cover optional requirement). Criteria represents a list of filters, each of them has name of the field and value.

![image](https://github.com/user-attachments/assets/817cbce7-1c2c-4ee4-afac-a08f883c6904)


## Setup

Building this project requires adding data sources configuration to application.yml file. Example:

```
ua:
 nure:
   fomin:
     data-sources:
     - name: data-base-1
       strategy: postgres
       url: jdbc:postgresql://localhost:5432/userdb
       table: users
       user: postgres
       password: postgres
       mapping:
        id: user_id
        username: login
        name: first_name
        surname: last_name
     - name: data-base-2
       strategy: h2
       url: jdbc:h2:~/users
       table: users_table
       user: sa
       password:
       mapping:
         id: ldap_login
         username: ldap_login
         name: name
         surname: surname
```
Then you need to build the project using command `./gradlew build` and execute the jar file using command `java -jar build/libs/users-0.0.1-SNAPSHOT.jar`
