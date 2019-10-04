<?php 
	require_once '../includes/DBOperations.php';
 
	function validateParams($params){
		$available = true; 
		$missingparams = ""; 
		
		foreach($params as $param){
			if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
				$available = false; 
				$missingparams = $missingparams . ", " . $param; 
			}
		}
		
		if(!$available){
			$response = array(); 
			$response['error'] = true; 
			$response['message'] = 'Parameters ' . substr($missingparams, 1, strlen($missingparams)) . ' missing';
			
			echo json_encode($response);
			
			die();
		}
	}
	
	$response = array();
	
	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
			case 'addplayer':
				validateParams(array('player','roomkey','target'));
				
				$db = new DBOperations();
				
				$result = $db->addPlayer(
					$_POST['player'],
					$_POST['roomkey'],
					$_POST['target']
				);
				
 
				if($result){
					$name = $_POST['player'];
					$rk = $_POST['roomkey'];
					$response['responsecode'] = 0; 
					$response['error'] = false; 
					$response['message'] = "Player added to room $rk with name $name";
					$response['players'] = $db->getPlayers();
                }
                else{
					$response['responsecode'] = 0;
					$response['error'] = true; 
					$response['message'] = 'An error occured while adding player, please try again.';
				}
            break; 
            
			case 'getplayers':
				$db = new DBOperations();
				$response['responsecode'] = 1; 
				$response['error'] = false; 
				$response['message'] = 'Request successfully completed';
				$response['players'] = $db->getPlayers();
            break; 
            
            case 'getplayersingame':
                if(isset($_POST['roomkey'])){
					$db = new DBOperations();
					$rk = $_POST['roomkey'];
                    if($db->getPlayersInGame($_POST['roomkey'])){
						$response['responsecode'] = 2; 
						$response['error'] = false; 
						$response['message'] = "Getting players in room:  $rk";
                        $response['players'] = $db->getPlayersInGame($_POST['roomkey']);
                    }else{
						$response['responsecode'] = 2; 
						$response['error'] = true; 
                        $response['message'] = "An error occured while getting players in game: $rk, please try again.";
                    }
                }else{
					$response['responsecode'] = 2; 
                    $response['error'] = true; 
                    $response['message'] = 'No room key provided, please provide a room key.';
                }
			break; 

			case 'getgameinfo':
				if(isset($_POST['roomkey'])){
					$db = new DBOperations();
					$rk = $_POST['roomkey'];
					if($db->getGameInfo($_POST['roomkey'])){
						$response['responsecode'] = 3; 
						$response['error'] = false; 
						$response['message'] = "Getting game info for room:  $rk";
						$response['players'] = $db->getPlayersInGame($_POST['roomkey']);
						$response['info'] = $db->getGameInfo($_POST['roomkey']);
					}else{
						$response['responsecode'] = 3; 
						$response['error'] = true;
						$response['message'] = "An error occured while getting info for: $rk, please try again.";
					}
				}else{
					$response['responsecode'] = 3;
					$response['error'] = true; 
					$response['message'] = 'No room key provided, please provide a room key.';
				}
			break;

			case 'getallgames':
				$db = new DBOperations();
				$response['responsecode'] = 4;
				$response['error'] = false; 
				$response['message'] = 'Returning info for all games.';
				$response['info'] = $db->getAllGames();
			break; 
			
			case 'updateplayer':
				validateParams(array('player','roomkey','target'));
				$db = new DBOperations();
				$result = $db->updatePlayer(
                    $_POST['id'],
					$_POST['player'],
					$_POST['roomkey'],
                    $_POST['target']
				);
				
				if($result){
					$response['responsecode'] = 5;
					$response['error'] = false; 
					$response['message'] = 'Player updated.';
					$response['players'] = $db->getPlayers();
				}else{
					$response['responsecode'] = 5;
					$response['error'] = true; 
					$response['message'] = 'An error occured while updating player, please try again.';
				}
			break; 
			
			case 'deleteplayer':
				if(isset($_POST['player'], $_POST['roomkey'])){
					$db = new DBOperations();
					if($db->deletePlayer($_POST['player'], $_POST['roomkey'])){
						$response['responsecode'] = 6;
						$response['error'] = false; 
						$response['message'] = 'Player deleted successfully';
						$response['players'] = $db->getPlayers();
					}else{
						$response['responsecode'] = 6;
						$response['error'] = true; 
						$response['message'] = 'An error occured, please try again.';
					}
				}else{
					$response['responsecode'] = 6;
					$response['error'] = true; 
					$response['message'] = 'No ID provided, please provide an ID.';
				}
			break; 
			
			case 'startgame':
			if(isset($_POST['roomkey'])){
				$db = new DBOperations();
				$rk = $_POST['roomkey'];
				if($db->startGame($_POST['roomkey'])){
					$response['responsecode'] = 7; 
					$response['error'] = false; 
					$response['message'] = "Starting game in room:  $rk";
					$response['players'] = $db->getPlayersInGame($_POST['roomkey']);
				}else{
					$response['responsecode'] = 7; 
					$response['error'] = true; 
					$response['message'] = "An error occured while starting game in room: $rk, please try again.";
				}
			}else{
				$response['responsecode'] = 7; 
				$response['error'] = true; 
				$response['message'] = 'No room key provided, please provide a room key.';
			}
			break; 

			case 'gettargetfor':
			if(isset($_POST['player'], $_POST['roomkey'])){
				$db = new DBOperations();
				$p = $_POST['player'];
				if($db->getTargetFor($_POST['player'], $_POST['roomkey'])){
					$response['responsecode'] = 8; 
					$response['error'] = false; 
					$response['message'] = "Recieved target for:  $p";
					$response['target'] = $db->getTargetFor($_POST['player'], $_POST['roomkey']);
				}else{
					$response['responsecode'] = 8; 
					$response['error'] = true; 
					$response['message'] = "An error occured while receiving target for: $p, please try again.";
				}
			}else{
				$response['responsecode'] = 8; 
				$response['error'] = true; 
				$response['message'] = 'No player name provided.';
			}
			break; 

			case 'checkgameactive':
				if(isset($_POST['roomkey'])){
					$db = new DBOperations();
					$rk = $_POST['roomkey'];
					if($db->checkGameActive($_POST['roomkey'])){
						$response['responsecode'] = 9; 
						$response['error'] = false; 
						$response['message'] = "Checking if Room $rk is active.";
						$response['active'] = $db->checkGameActive($_POST['roomkey']);
					}else{
						$response['responsecode'] = 9; 
						$response['error'] = false; 
						$response['message'] = "Checking if Room $rk is active.";
						$response['active'] = $db->checkGameActive($_POST['roomkey']);
					}
				}else{
					$response['responsecode'] = 9; 
					$response['error'] = true; 
					$response['message'] = 'No room key provided, please provide a room key.';
				}
				break; 

				case 'assassinatetarget':
					if(isset($_POST['player'], $_POST['roomkey'])){
						$db = new DBOperations();
						$p = $_POST['player'];
						if($db->assassinateTarget($_POST['player'], $_POST['roomkey'])){
							$response['responsecode'] = 10; 
							$response['error'] = false; 
							$response['message'] = "$p has assassinated their target!";
						}else{
							$response['responsecode'] = 10; 
							$response['error'] = true; 
							$response['message'] = "An error occured while assassinating target for: $p, please try again.";
						}
					}else{
						$response['responsecode'] = 10; 
						$response['error'] = true; 
						$response['message'] = 'No player name provided.';
					}
				break; 
            
            default:
                $response['error'] = true; 
                $response['message'] = 'Invalid API Call';
		}
		
    }
    else{
		$response['error'] = true; 
		$response['message'] = 'Invalid API Call';
	}
	
    echo json_encode($response);
?>