<?php 
require('json.php');

$username = "";

if(isset($_POST['username']))
{
	$username = mysqli_real_escape_string($con, $_POST['username']);
}
$query = "Select * from `TaiKhoan` where `username`='$username'";

$data = mysqli_query($con,$query);

class User{
	function User($username,$password,$hoten,$usertype)
	{
		$this->Username = $username;
		$this->Password = $password;
		$this->Hoten = $hoten;
		$this->Usertype = $usertype;
	}
}

$mang = array();

while($row = mysqli_fetch_assoc($data)){
	array_push($mang, new User($row['username'], $row['password'], $row['hoten'], $row['usertype']));
}

echo json_encode($mang);



// $query = "Select * from `TaiKhoan` where `username`='$username'";

// $data = mysqli_query($con,$query);

// class User{
// 	function User($username,$password,$hoten,$usertype)
// 	{
// 		$this->Username = $username;
// 		$this->Password = $password;
// 		$this->Hoten = $hoten;
// 		$this->Usertype = $usertype;
// 	}
// }

// $mang = array();

// while($row = mysqli_fetch_assoc($data)){
// 	array_push($mang, new User($row['username'], $row['password'], $row['hoten'], $row['usertype']));
// }

// echo json_encode($mang);

 ?>