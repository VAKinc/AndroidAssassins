<?php
    error_reporting(E_ALL);
    ini_set('display_errors', 1);

    class DBOperations
    {
        private $con;

        function __construct(){
            require_once dirname(__FILE__) . '/DBConnect.php';
    
            $db = new DBConnect();
    
            $this->con = $db->connect();
        }

        
        function updateRooms($roomkey){
            $statement = $this->con->prepare("INSERT INTO games (roomkey) VALUES (?)");
            $statement->bind_param("s", $roomkey);
            $statement->execute();
            $statement = $this->con->prepare("UPDATE games SET player_count=player_count+1 WHERE roomkey= ?");
            $statement->bind_param("s", $roomkey);
            if($statement->execute())
                return true;
            return false;
        }
        
        function addPlayer($player, $roomkey, $target){
            $statement = $this->con->prepare("INSERT INTO players (player, roomkey, tar) VALUES (?, ?, ?)");
            $statement->bind_param("sss", $player, $roomkey, $target);
            if($statement->execute())
                $this->updateRooms($roomkey);
                return true;
            return false;
        }
    
        function getPlayers(){
            $statement = $this->con->prepare("SELECT id, player, roomkey, tar FROM players");
            $statement->execute();
            $statement->bind_result($id, $player, $roomkey, $target);
            
            $players = array(); 
            
            while($statement->fetch()){
                $parr  = array();
                $parr['id'] = $id; 
                $parr['player'] = $player; 
                $parr['roomkey'] = $roomkey; 
                $parr['tar'] = $target; 
                
                array_push($players, $parr); 
            }
            
            return $players; 
        }

        function getPlayersInGame($roomkey)
        {
            $statement = $this->con->prepare("SELECT id, player, roomkey, tar FROM players WHERE roomkey = ?");
            $statement->bind_param("s", $roomkey);
            $statement->execute();
            $statement->bind_result($id, $player, $roomkey, $target);
            
            $players = array(); 
            
            while($statement->fetch()){
                $parr  = array();
                $parr['id'] = $id; 
                $parr['player'] = $player; 
                $parr['roomkey'] = $roomkey; 
                $parr['tar'] = $target; 
                
                array_push($players, $parr); 
            }
            
            return $players; 
        }

        function getGameInfo($roomkey)
        {
            $statement = $this->con->prepare("SELECT roomkey, active, player_count FROM games");
            $statement->execute();
            $statement->bind_result($roomkey, $active, $player_count);
            
            $game = array(); 
            
            while($statement->fetch()){
                $garr  = array();
                $garr['roomkey'] = $roomkey; 
                $garr['active'] = $active; 
                $garr['player_count'] = $player_count; 
                
                array_push($game, $garr); 
            }
            
            return $game; 
        }

        function getAllGames()
        {
            $statement = $this->con->prepare("SELECT roomkey, active, player_count FROM games WHERE roomkey = ?");
            $statement->bind_param("s", $roomkey);
            $statement->execute();
            $statement->bind_result($roomkey, $active, $player_count);
            
            $game = array(); 
            
            while($statement->fetch()){
                $garr  = array();
                $garr['roomkey'] = $roomkey; 
                $garr['active'] = $active; 
                $garr['player_count'] = $player_count; 
                
                array_push($game, $garr); 
            }
            
            return $game; 
        }
        
        function updatePlayer($id, $player, $roomkey, $target){
            $statement = $this->con->prepare("UPDATE players SET player = ?, roomkey = ?, tar = ? WHERE id = ?");
            $statement->bind_param("sssi", $player, $roomkey, $target, $id);
            if($statement->execute())
                return true; 
            return false; 
        }
        
        function deletePlayer($player, $roomkey){
            $statement = $this->con->prepare("DELETE FROM players WHERE player = ? AND roomkey = ?");
            $statement->bind_param("ss", $player, $roomkey);
            $statement->execute();
            $statement = $this->con->prepare("UPDATE games SET player_count=player_count-1 WHERE roomkey= ?");
            $statement->bind_param("s", $roomkey);
            if($statement->execute())
                return true; 
            return false; 
        }

        function startGame($roomkey){
            if($this->getPlayersInGame($roomkey) && $this->checkGameActive($roomkey) != true)
            {
                $statement = $this->con->prepare("SELECT id, player FROM players WHERE roomkey = ?");
                $statement->bind_param("s", $roomkey);
                $statement->execute();
                $statement->bind_result($id, $player);

                $players = array(); 
                $ids = array(); 
                while($statement->fetch()){
                    $players[] = $player; 
                    $ids[] = $id; 
                }
                
                shuffle($players);
                $targets = $players;
                $shift = rand(1, count($targets)-1);
                for($i = 0; $i < $shift; $i++)
                {
                    $temp = $targets[0];
                    for($x = 0; $x < count($targets) - 1; $x++)
                    {
                        $targets[$x] = $targets[$x+1];
                    }

                    $targets[count($targets)-1] = $temp;
                }

                foreach($players as $key => $value) {
                    $statement = $this->con->prepare("UPDATE players SET player = ? WHERE id = ? AND roomkey = ?");
                    $statement->bind_param("sss", $value, $ids[$key], $roomkey);
                    $statement->execute();
                }
    
                foreach($targets as $key => $value) {
                    $statement = $this->con->prepare("UPDATE players SET tar = ? WHERE player = ? AND roomkey = ?");
                    $statement->bind_param("sss", $value, $players[$key], $roomkey);
                    $statement->execute();
                }
    
                $statement = $this->con->prepare("UPDATE games SET active = 1 WHERE roomkey = ?");
                $statement->bind_param("s", $roomkey);
                if($statement->execute())
                    return true; 
            }
            return false; 
        }

        function checkGameActive($roomkey)
        {
            $statement = $this->con->prepare("SELECT active FROM games WHERE roomkey = ?");
            $statement->bind_param("s", $roomkey);
            $statement->execute();
            $statement->bind_result($active);
            $act = false;
            
            while($statement->fetch()){
                $act = $active; 
            }

            if($act == true)
                return true;
            return false; 
        }

        function getTargetFor($player, $roomkey){
            $statement = $this->con->prepare("SELECT tar FROM players WHERE player = ? AND roomkey = ?");
            $statement->bind_param("ss", $player, $roomkey);
            $statement->execute();
            $statement->bind_result($target);
            $tar = "";
            
            while($statement->fetch()){
                $tar = $target; 
            }

            return $tar;
        }

        function assassinateTarget($player, $roomkey)
        {
            $statement = $this->con->prepare("SELECT tar FROM players WHERE player = ? AND roomkey = ?");
            $statement->bind_param("ss", $player, $roomkey);
            $statement->execute();
            $statement->bind_result($target);
            $tar = "";
            
            while($statement->fetch()){
                $tar = $target; 
            }

            $statement = $this->con->prepare("SELECT tar FROM players WHERE player = ? AND roomkey = ?");
            $statement->bind_param("ss", $tar, $roomkey);
            $statement->execute();
            $statement->bind_result($target);
            $newtar = "";

            while($statement->fetch()){
                $newtar = $target; 
            }

            if($newtar == $player)
            {
                return $this->endGame($roomkey);
            }

            $statement = $this->con->prepare("UPDATE players SET tar = ? WHERE player = ? AND roomkey = ?");
            $statement->bind_param("sss", $newtar, $player, $roomkey);
            $statement->execute();

            $statement = $this->con->prepare("DELETE FROM players WHERE player = ? AND roomkey = ?");
            $statement->bind_param("ss", $tar, $roomkey);
            $statement->execute();

            $statement = $this->con->prepare("UPDATE games SET player_count=player_count-1 WHERE roomkey= ?");
            $statement->bind_param("s", $roomkey);
            if($statement->execute())
                return true; 
            return false; 
        }

        function endGame($roomkey)
        {
            $statement = $this->con->prepare("DELETE FROM players WHERE roomkey = ?");
            $statement->bind_param("s", $roomkey);
            $statement->execute();

            $statement = $this->con->prepare("DELETE FROM games WHERE roomkey = ?");
            $statement->bind_param("s", $roomkey);
            if($statement->execute())
                return true;
            return false;
        }
    }
?>