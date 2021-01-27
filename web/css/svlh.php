<?php 
require('json.php');
$idlophoc = "";

if(isset($_POST['idlophoc']))
{
	$idlophoc = $_POST['idlophoc'];
}

$query = "select a.username,b.hoten from `SVLH` a, `TaiKhoan` b where a.idlophoc='$idlophoc' and a.username=b.username";

$data = mysqli_query($con,$query);



class SVLH{
	function SVLH($username, $hoten)
	{
		$this->Username = $username;
		$this->Hoten = $hoten;
	}
}

$mang = array();

while($row = mysqli_fetch_assoc($data)){
	//echo $row['tenlop'];
// 	array_push($mang, new LopHoc("a", "ad3de", "can", "Lập trình hướng đối tượng"));
	array_push($mang, new SVLH($row['username'], $row['hoten']));
}

echo json_encode($mang);


 ?>
