# AndroidAssassin

This app requires an Apache server, and a MySQL server. This can easily be achieved using XAMPP. Configure the MySQL server using the following commands:

> CREATE DATABASE assassins

> CREATE TABLE players (
id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
player VARCHAR(50) NOT NULL,
roomkey VARCHAR(4) NOT NULL,
tar VARCHAR(50)
)

> CREATE TABLE games (
roomkey VARCHAR(4) NOT NULL DEFAULT 'ERRK' PRIMARY KEY,
player_count INT(11) NOT NULL,
active TINYINT NULL
)

Place the contents of the htdocs folder into the htdocs folder of your server.
Configure htdocs/assassins/includes/Constants.php as necessary for your server.

In app/src/main/java/com.vakinc.testapp/AssassinsAPI.java, root_url must be set to "http://YOUR.IP.ADDRESS.HERE/assassins/v1.0/API.php?apicall="

This app is a work in progress, but currently is fully functional, if you have any questions please don't hesitate to contact me.
