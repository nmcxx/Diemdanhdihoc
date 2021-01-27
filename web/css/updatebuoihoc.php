<?php 
require('json.php');

$idlophoc = "";
$key = "";
$sobuoi = "";

if(isset($_POST['idlophoc']))
{
	$idlophoc = mysqli_real_escape_string($con, $_POST['idlophoc']);
}
if(isset($_POST['key']))
{
	$key = mysqli_real_escape_string($con, $_POST['key']);
}
if(isset($_POST['sobuoi']))
{
	$sobuoi = mysqli_real_escape_string($con, $_POST['sobuoi']);
}
// $query = "insert into `TaiKhoan` values('$username','$password')";
// INSERT INTO `BuoiHoc` (`idbuoihoc`, `idlophoc`, `sobuoi`, `madiemdanh`, `hethan`) VALUES (NULL, '7', NULL, 'f3tLt5', '0');
// UPDATE `BuoiHoc` SET `hethan` = '1' WHERE `BuoiHoc`.`idbuoihoc` = 9;
$queryhethan = "UPDATE `BuoiHoc` SET `hethan` = '1' WHERE `BuoiHoc`.`sobuoi` = '$sobuoi'";
mysqli_query($con,$queryhethan);
$querycapnhatbuoihoc = "INSERT INTO `BuoiHoc` (`idbuoihoc`, `idlophoc`, `sobuoi`, `madiemdanh`, `hethan`) VALUES (NULL, '$idlophoc', NULL, '$key', '0')";
if(mysqli_query($con,$querycapnhatbuoihoc))
	echo 'true';
else
	echo 'false';


 ?>
