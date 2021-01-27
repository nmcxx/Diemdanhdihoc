<?php 
require('json.php');
session_start();
$idlop = "";

if(isset($_SESSION["checkLop"]))
{
	$idlop = $_SESSION["checkLop"];
}
// echo $idlop;

// $query = "insert into `TaiKhoan` values('$username','$password')";
    $query = "update `BuoiHoc` set hethan='1' where idlophoc='$idlop'";
	mysqli_query($con,$query);
	$query2 = "INSERT INTO `BuoiHoc` (`idbuoihoc`, `idlophoc`, `sobuoi`, `madiemdanh`, `hethan`) VALUES (NULL, '$idlop', NULL, NULL, '0')";
	mysqli_query($con,$query2);
	header("location: index.php");
	unset($_SESSION['checkLop']);

 ?>
