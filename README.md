# StudyOrganizer
![](https://forthebadge.com/images/badges/made-with-java.svg)
![](http://ForTheBadge.com/images/badges/built-with-love.svg)

Android application works as a calendar and scheduler for classes/exams/subjects

[<img src="https://github.com/atefhares/StudyOrganizer/blob/master/4.jpg" width="200"/>](https://github.com/atefhares/StudyOrganizer/blob/master/4.jpg)
[<img src="https://github.com/atefhares/StudyOrganizer/blob/master/5.jpg" width="200"/>](https://github.com/atefhares/StudyOrganizer/blob/master/5.jpg)
[<img src="https://github.com/atefhares/StudyOrganizer/blob/master/6.jpg" width="200"/>](https://github.com/atefhares/StudyOrganizer/blob/master/6.jpg)
[<img src="https://github.com/atefhares/StudyOrganizer/blob/master/7.jpg" width="200"/>](https://github.com/atefhares/StudyOrganizer/blob/master/7.jpg)

# Contents

- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Database Schema](#database-schema)
- [Features](#features)

## Getting Started

how to run the server:

- open project in netbeans or intellij idea
- open File -> Project Properties -> Libraries and make sure that all libraries from libs folder are included
- open ClientController.java and configure the attribute SERVER_ADDRESS to the server ip address
- clean, build and run

how to run the client:
- open project in netbeans or intellij idea
- open File -> Project Properties -> Libraries and make sure that all libraries from libs folder are included
- clean, build and run
- before starting server make sure there is a mysql server at the "localhost" on port 3306
   also add a user named "test" with password "!Pass12345678" plus please allow this user's access on database names "tictactoe"

### Prerequisites

- java 8 or higher recommended
- firewall configured for socket communications.

## Database Schema

![hg](https://github.com/atefhares/XO-Game-Pro-Server/blob/master/database_erd.png)

## Features

Client Side Features:</br>
- login
- SignUp
- play with pc
- play with online friends
- chat while playing
- see who has the highest score in the game
- see who is online offline or busy playing with someone else

Server side Features:</br>
- see a list of all users</br>
- see players status and score</br>
- close and reopen the server</br>


## Built With

* [MySQL](https://dev.mysql.com/downloads/connector/j/) - JDBC Type 4 driver for MySQL
* [JSON-Simple](https://code.google.com/archive/p/json-simple/) -  A simple Java toolkit for JSON


## License

This project is licensed under the GPL 3.0 License - see the [LICENSE](LICENSE) file for details
