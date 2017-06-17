<?php
require_once '../library/config.php';
		
		 $compType = $_POST['compType'];
		$compTitle = $_POST['compTitle'];
		$compDesc = $_POST['compDesc'];
		$compImg = $_POST['compImg'];
		$cust_id = (int)$_SESSION['user_id'];
		$cust_name = $_SESSION['user_name'];
		
		$sql = "INSERT INTO tbl_complains (cust_id, cust_name, comp_type, comp_title, comp_desc, status, eng_id, eng_name, eng_comment, create_date, close_date, compImg)
			VALUES ($cust_id, '$cust_name', '$compType', '$compTitle', '$compDesc', 'open', 0, '' , '', NOW(), '', '$compImg')";
				
	$result = dbQuery($sql);
			if( dbNumRows($result) > 0 )
				{
		 			$array = array("status" => "Complain send.");
				}
			else
		{
				$array = array("status" => "failure","details" => "Unable to Send Query");
		}

		echo json_encode($array);	
?>
