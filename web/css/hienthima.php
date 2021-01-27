<?php 
require ('json.php');
session_start();
if(!isset($_SESSION["username"]))
{
	header("Location: login.php");
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Document</title>
</head>
<body>
	<?php 
	$query1 = "select LopHoc.idlophoc,LopHoc.tenlop, BuoiHoc.madiemdanh from LopHoc inner join BuoiHoc on LopHoc.idlophoc=BuoiHoc.idlophoc and BuoiHoc.hethan='0' and LopHoc.username='".$_SESSION['username']."' and BuoiHoc.idlophoc='".$_POST['danhsachlop']."'";
                $result1 = mysqli_query($con,$query1);
				while($row1 = mysqli_fetch_object($result1))
				{
					if($row1->idlophoc==$selectLop)
					{
					    $url ="https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=".$row1->madiemdanh;
					   // echo $row1->madiemdanh;
				// 		$_SESSION["checkLop"]=$row->madiemdanh;
					}
				}
	 ?>
	 <center><img id="myImg" src="<?php echo $url ?>" style="display: none" width="600" height="400" ></center>
</body>
</html>