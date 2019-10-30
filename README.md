# AndroidAssassin

This app requires a MySQL server. You can configure it using the follwowing commands:

> CREATE DATABASE assassins

> CREATE TABLE players

> CREATE TABLE games

> ALTER TABLE `players` ADD `id` INT NOT NULL AUTO_INCREMENT, ADD `player` VARCHAR(50) NOT NULL, ADD `roomkey` VARCHAR(4) NULL, ADD `tar` VARCHAR(50) NOT NULL, ADD PRIMARY KEY (`id`);

> ALTER TABLE `games` ADD `roomkey` VARCHAR(4) NOT NULL DEFAULT 'ERRK', ADD `player_count` INT(11) NOT NULL, ADD `active` TINYINT NULL, ADD PRIMARY KEY (`roomkey`);

Place the contents of the htdocs folder into the htdocs folder of your server.
Configure htdocs/assassins/includes/Constants.php as necessary for your server.

In app/src/main/java/com.vakinc.testapp/AssassinsAPI.java, root_url must be set to "http://YOUR.IP.ADDRESS.HERE/assassins/v1.0/API.php?apicall="

This app is a work in progress, but currently is fully functional, if you have any questions please don't hesitate to contact me.
