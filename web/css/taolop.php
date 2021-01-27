<?php 
require('json.php');
$username = "";
$tenlop = "";
$key = "";

if(isset($_POST['username']))
{
	$username = mysqli_real_escape_string($con, $_POST['username']);
}

if(isset($_POST['tenlop']))
{
	$tenlop = mysqli_real_escape_string($con, $_POST['tenlop']);
}

if(isset($_POST['key']))
{
	$key = mysqli_real_escape_string($con, $_POST['key']);
}
// $query = "insert into `TaiKhoan` values('$username','$password')";
    $query = "insert into `LopHoc` values (NULL,'$key','$username','$tenlop')";
	$result = mysqli_query($con,$query);
	// $row = mysqli_num_rows($result);
	// echo $row;
    if($result)
	{
		echo "true";
	}
	else
	{
	    echo "false";
	}


// require('json.php');
// $username = "";
// $password = "";

// if(isset($_POST['username']))
// {
// 	$username = mysqli_real_escape_string($con, $_POST['username']);
// }

// if(isset($_POST['password']))
// {
// 	$password = mysqli_real_escape_string($con, $_POST['password']);
// }
// // $query = "insert into `TaiKhoan` values('$username','$password')";
//     $query = "update `TaiKhoan` set `password`='$password' where `username`='$username'";
// 	$result = mysqli_query($con,$query);
// 	// $row = mysqli_num_rows($result);
// 	// echo $row;
//     if($result)
// 	{
// 		echo "true";
// 	}
// 	else
// 	{
// 	    echo "false";
// 	}


 

 ?>
