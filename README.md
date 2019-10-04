# AndroidAssassin

This app requires a MySQL server configured as follows:

Database: assassins
Table: players
id - INT PRIMARY AUTO-INCREMENT, player - VARCHAR 50, roomkey - VARCHAR 4, tar - VARCHAR 50
Table: games
roomkey - VARCHAR 50 PRIMARY, player_count - INT DEFAULT: 0, active - BOOLEAN DEFAULT: 0

Place the contents of the htdocs folder into the htdocs folder of your server.
Configure htdocs/assassins/includes/Constants.php as necessary for your server.

In app/src/main/java/com.vakinc.testapp/AssassinsAPI.java, root_url must be set to "http://YOUR.IP.ADDRESS.HERE/assassins/v1.0/API.php?apicall="

This app is a work in progress, but currently is fully functional, if you have any questions please don't hesitate to contact me.
