<?php
$con = mysqli_connect("localhost","id10832997_nhumanhcan","RlZbvoL(e&-F6h{1","id10832997_diemdanh");
if (mysqli_connect_errno())
  {
  echo "Không thể kết nối đến MySQL: " . mysqli_connect_error();
  }
// $query = "SELECT * FROM `TaiKhoan`";
// $taikhoan = mysqli_query($con,$query);
// $mang = array();
// while($row = mysqli_fetch_array($taikhoan))
// {
// 	$username = $row["username"];
// 	$password = $row["password"];
// 	array_push($mang,new TaiKhoan($username,$password));
// }
// echo json_encode($mang);


// class TaiKhoan{
// 	var $username;
// 	var $password;
// 	function TaiKhoan($u, $p){
// 		$this->username = $u;
// 		$this->password = $p;
// 	}
// }
?>