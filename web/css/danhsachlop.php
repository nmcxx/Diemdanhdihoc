<?php
require('json.php');
$username = "";
$usertype = "";
$query = "";

if(isset($_POST['username']))
{
	$username = $_POST['username'];
}
if(isset($_POST['usertype']))
{
    $usertype = $_POST['usertype'];
}

if($usertype==0)
{
    $query = "select `LopHoc`.`idlophoc`,`LopHoc`.`malop`,`LopHoc`.`username`, `LopHoc`.`tenlop`, (Select `TaiKhoan`.`hoten` from `TaiKhoan` where `TaiKhoan`.`username`=`LopHoc`.`username`) as 'hoten', (SELECT COUNT(*) from `BuoiHoc` where `BuoiHoc`.`idlophoc`=`LopHoc`.`idlophoc`) as 'sobuoi' from `LopHoc` where `LopHoc`.`username`='$username'";
}
else if($usertype==1)
{
    $query = "select `LopHoc`.`idlophoc`,`LopHoc`.`malop`,`LopHoc`.`username`, `LopHoc`.`tenlop`, (Select `TaiKhoan`.`hoten` from `TaiKhoan` where `TaiKhoan`.`username`=`LopHoc`.`username`) as 'hoten', (SELECT COUNT(*) from `BuoiHoc` where `BuoiHoc`.`idlophoc`=`LopHoc`.`idlophoc`) as 'sobuoi' from `LopHoc` inner join `SVLH` on `SVLH`.`username`='$username' and `SVLH`.`idlophoc`=`LopHoc`.`idlophoc`";
}
// if($query!="")
// {
$data = mysqli_query($con,$query);



class LopHoc{
	function LopHoc($idlophoc, $malop, $username, $tenlop, $hoten, $sobuoi)
	{
		$this->Idlophoc = $idlophoc;
		$this->Malop = $malop;
		$this->Username = $username;
		$this->Tenlop = $tenlop;
		$this->Hoten = $hoten;
		$this->Sobuoi = $sobuoi;
	}
}

$mang = array();

while($row = mysqli_fetch_assoc($data)){
	//echo $row['tenlop'];
// 	array_push($mang, new LopHoc("a", "ad3de", "can", "Lập trình hướng đối tượng"));
	array_push($mang, new LopHoc($row['idlophoc'], $row['malop'], $row['username'], $row['tenlop'], $row['hoten'], $row['sobuoi']));
}

if($mang!=null){
echo json_encode($mang);
}
    
// }
// require('json.php');
// class User
// {
// 	public function isLoginExist($username, $password)
// 	{
// 		$query = "select * from `TaiKhoan` where username = `$username` and password = `$password` limit 1";
// 		$result = mysqli_query($con,$query);
// 		if(mysqli_num_rows($result) > 0)
// 		{
// 			return true;
// 		}
// 		return false;
// 	}
// 	public function loginUsers($username,$password)
// 	{
// 		$json = array();
// 		$canUserLogin = isLoginExist($username,$password);
// 		if($canUserLogin){
// // 			$json['success'] = 1;
// // 			$json['message'] = "Dang nhap thanh cong";
//             echo "A";
// 		}
// 		else
// 		{
// 		    echo "B";
// // 			$json['success'] = 0;
// // 			$json['message'] = "Dang nhap that bai";
// 		}
// 		return 0;
// 	}
// }
 ?>