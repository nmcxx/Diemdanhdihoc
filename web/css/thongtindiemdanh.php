<?php 
require('json.php');

$idlophoc = "";
$username = "";
$usertype = "";

if(isset($_POST['idlophoc']))
{
	$idlophoc = mysqli_real_escape_string($con, $_POST['idlophoc']);
}

if(isset($_POST['username']))
{
	$username = mysqli_real_escape_string($con, $_POST['username']);
}

if(isset($_POST['usertype']))
{
    $usertype = mysqli_real_escape_string($con, $_POST['usertype']);
}

if($usertype == 0)
{
    $query = "select DISTINCT `SVLH`.`username`,(SELECT `TaiKhoan`.`hoten` from `TaiKhoan` where `TaiKhoan`.`username`=`SVLH`.`username`) as 'hoten',`sobuoi`,(Select COUNT(*) from `SVDD` where `username`=`SVLH`.`username` and `idbuoihoc`=`BuoiHoc`.`idbuoihoc`) as 'diemdanh' from `BuoiHoc` inner join `SVDD` on `idlophoc`='$idlophoc' inner join `LopHoc` on `LopHoc`.`idlophoc`='$idlophoc' INNER join `SVLH`on `SVLH`.`idlophoc`='$idlophoc'";
}
else if($usertype == 1)
{
$query = "select DISTINCT `SVLH`.`username`,(SELECT `TaiKhoan`.`hoten` from `TaiKhoan` where `TaiKhoan`.`username`=`SVLH`.`username`) as 'hoten',`sobuoi`,(Select COUNT(*) from `SVDD` where `username`=`SVLH`.`username` and `idbuoihoc`=`BuoiHoc`.`idbuoihoc`) as 'diemdanh' from `BuoiHoc` inner join `SVDD` on `idlophoc`='$idlophoc' inner join `LopHoc` on `LopHoc`.`idlophoc`='$idlophoc' INNER join `SVLH`on `SVLH`.`idlophoc`='$idlophoc' and `SVLH`.`username`='$username'";
}

$result = mysqli_query($con,$query);

class DiemDanh{
	function DiemDanh($username,$hoten,$sobuoi,$diemdanh)
	{
		$this->Username = $username;
		$this->Hoten = $hoten;
		$this->Sobuoi = $sobuoi;
		$this->Diemdanh = $diemdanh;
	}
}

$mang = array();

while($row = mysqli_fetch_assoc($result)){
	array_push($mang, new DiemDanh($row['username'],$row['hoten'],$row['sobuoi'],$row['diemdanh']));
}

if($mang!=null)
echo json_encode($mang);
 ?>