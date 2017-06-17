<?php
require_once '../library/config.php';

	$username = $_POST['username'] ;
	$password = $_POST['password'] ;

	// check the database and see if the username and password combo do match
		$sql = "SELECT cid
		        FROM tbl_customer 
				WHERE cname = '$username' AND cpass = '$password' ";

		$result = dbQuery($sql);

		if(dbNumRows($result) == 1)
		{
			$array = array("status" => "success" );
		}
		else
		{
			$array = array("status" => "failure", "message" => "Invalid User Name and/or Password " );
		}

		echo json_encode($array);
?>