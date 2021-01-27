<?php
require('json.php');
$username = "";

if(isset($_POST['username']))
{
	$username = $_POST['username'];
}
if($username!="")
{
$query = "select `a`.`idlophoc`,`a`.`malop`,`a`.`username`,`a`.`tenlop` from `LopHoc` `a` inner join `SVLH` `b` on `b`.`username`='$username' and `b`.`idlophoc`=`a`.`idlophoc`";

$data = mysqli_query($con,$query);



class LopHoc{
	function LopHoc($idlophoc, $malop, $username, $tenlop)
	{
		$this->Idlophoc = $idlophoc;
		$this->Malop = $malop;
		$this->Username = $username;
		$this->Tenlop = $tenlop;
	}
}

$mang = array();

while($row = mysqli_fetch_assoc($data)){
	//echo $row['tenlop'];
// 	array_push($mang, new LopHoc("a", "ad3de", "can", "Lập trình hướng đối tượng"));
	array_push($mang, new LopHoc($row['idlophoc'], $row['malop'], $row['username'], $row['tenlop']));
}

echo json_encode($mang);
}
 ?>