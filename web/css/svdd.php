<?php 
require('json.php');

$idlophoc = "";
$sobuoi = "";
$madiemdanh = "";
$username = "";

if(isset($_POST['idlophoc']))
{
	$idlophoc = mysqli_real_escape_string($con, $_POST['idlophoc']);
}

if(isset($_POST['username']))
{
	$username = mysqli_real_escape_string($con, $_POST['username']);
}

if(isset($_POST['sobuoi']))
{
	$sobuoi = mysqli_real_escape_string($con, $_POST['sobuoi']);
}

if(isset($_POST['madiemdanh']))
{
	$madiemdanh = mysqli_real_escape_string($con, $_POST['madiemdanh']);
}


$queryKiemTra = "select * from `BuoiHoc` where `BuoiHoc`.`idlophoc`='$idlophoc' and `BuoiHoc`.`sobuoi`='$sobuoi' and `BuoiHoc`.`madiemdanh`='$madiemdanh' and `BuoiHoc`.`hethan`='0'";

$result = mysqli_query($con,$queryKiemTra);
$row = mysqli_num_rows($result);
$row1 = mysqli_fetch_object($result);

if($row==1)
{
	$queryKiemTraDD = "select * from `SVDD` where `SVDD`.`idbuoihoc`='$row1->idbuoihoc' and `SVDD`.`username`='$username'";
	$result1 = mysqli_query($con,$queryKiemTraDD);
	$row2 = mysqli_num_rows($result1);
	if($row2==0)
	{		
		$queryDiemDanh = "insert into `SVDD`(`idbuoihoc`,`username`) values ('$row1->idbuoihoc','$username')";
		if(mysqli_query($con,$queryDiemDanh)){
			echo "true";
		}
		else
		{
			echo "false";
		}
	}
	else
	{
		echo "isset";
	}
}
else
{
	echo "empty";
}


 ?>