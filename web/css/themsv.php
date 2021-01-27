<?php 
require('json.php');
$username = "";
$hoten = "";
$idlophoc = "";

if(isset($_POST['username']))
{
	$username = mysqli_real_escape_string($con, $_POST['username']);
}

if(isset($_POST['hoten']))
{
	$hoten = mysqli_real_escape_string($con, $_POST['hoten']);
}

if(isset($_POST['idlophoc']))
{
	$idlophoc = mysqli_real_escape_string($con, $_POST['idlophoc']);
}
// $query = "insert into `TaiKhoan` values('$username','$password')";

$querykiemtra = "select * from `TaiKhoan` where `username`='$username'";
$result = mysqli_query($con,$querykiemtra);
$row = mysqli_num_rows($result);
if($row==0)
{
	$querythemtaikhoan = "insert into `TaiKhoan` values('$username','$username',0,'$hoten')";
	mysqli_query($con,$querythemtaikhoan);
	// 	echo 'true'
	// else
	// 	echo 'false'
	// $querythemsv = "insert into `SVLH`(`idlophoc`,`username`) values('$idlophoc','$username')"
}
$querykiemtrasv = "select * from `SVLH` where `username`='$username' and `idlophoc`='$idlophoc'";
$result1 = mysqli_query($con,$querykiemtrasv);
$row1 = mysqli_num_rows($result1);
if($row1==0)
{
$querythemsv = "insert into `SVLH`(`idlophoc`,`username`) values('$idlophoc','$username')";
if(mysqli_query($con,$querythemsv))
	echo 'true';
else
	echo 'false';
}

 ?>
