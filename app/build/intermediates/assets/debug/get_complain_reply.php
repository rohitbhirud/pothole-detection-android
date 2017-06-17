<?php
	require_once '../library/config.php';
	
	$sql = "SELECT * from tbl_complains cid = " . $cid;
	$result = dbQuery($sql);

	if( dbNumRows($result) == 0 )
	{
		$array = array("status" => "failure", "message" => "No Complain Found" );	
	}

	$array_success = array("status" => "success");

	$veh = array();
	while($row = mysql_fetch_array($result)) {

			$a1[] = array( "comp_title" => $row['comp_title'],
						"description" => $row['comp_desc'],
						"reply" => $row['eng_comment'],
					       );
	}
	
	$array = array("status" => "success", "details" => $a1);
	echo json_encode($array);
?>