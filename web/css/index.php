<?php 
require ('json.php');
session_start();
if(!isset($_SESSION["username"]))
{
	header("Location: login.php");
}
$query = "select LopHoc.idlophoc,LopHoc.tenlop, BuoiHoc.madiemdanh from LopHoc inner join BuoiHoc on LopHoc.idlophoc=BuoiHoc.idlophoc and BuoiHoc.hethan='0' and LopHoc.username='".$_SESSION['username']."'";
$result = mysqli_query($con,$query);
 ?>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Document</title>
	<link rel="stylesheet" href="css/style.css" />
	<script language ="javascript" >
		//test.style.display='none';
		//showtime.style.display='block';
		var tim;
		var sec = 60;
		function countdown() {
			if (parseInt(sec) > 0) {
				sec = parseInt(sec) - 1;
				document.getElementById("showtime").innerHTML = sec;
				tim = setTimeout("countdown()", 1000);
			}
			else if(parseInt(sec)==0)
			{
				clearTimeout(tim);
				document.getElementById("myImg").style.display = 'none'; 
				document.getElementById("showtime").innerHTML = 'Mã đã hết hạn, buổi học sẽ được cập nhật trong giây lát';
                // location.reload();
                <?php unset($checkLop) ?>
                setTimeout("location.href = 'hrefupdate.php'", 3000);
            	//alert("Thời gian làm bài đã kết thúc");
            	//window.location="https://bit.ly/2Egksr2"
            }
        }
    </script>
    <style>
    	/* Dropdown Button */
    	.dropbtn {
    		margin-right: 130px;
    		color: black;
    		padding: 16px;
    		font-size: 16px;
    		border: none;
    		background-color: Transparent;
    	}

    	/* The container <div> - needed to position the dropdown content */
    	.dropdown {
    		position: relative;
    		display: inline-block;
    	}

    	/* Dropdown Content (Hidden by Default) */
    	.dropdown-content {
    		display: none;
    		position: absolute;
    		background-color: #f1f1f1;
    		min-width: 160px;
    		box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
    		z-index: 1;
    	}

    	/* Links inside the dropdown */
    	.dropdown-content a {
    		color: black;
    		padding: 12px 16px;
    		text-decoration: none;
    		display: block;
    	}

    	/* Change color of dropdown links on hover */
    	.dropdown-content a:hover {background-color: #ddd;}

    	/* Show the dropdown menu on hover */
    	.dropdown:hover .dropdown-content {display: block;}

    	/* Change the background color of the dropdown button when the dropdown content is shown */
    	.dropdown:hover .dropbtn {}

    </style>
</head>
<body>
    <form action="" method="post">
	<div class="header header-nav-start" data-js="header-container">

		<div class="navigation navigation--startpage">

			<div class="container-fluid">

				<div class="row">

					<div class="navigation__logo col-sm-6 col-7 d-flex align-items-center">
						<a href="#" class="egoditor-logo">
							<img class="egoditor-logo__mark" src="https://cdn-web.qr-code-generator.com/wp-content/themes/qr/new_structure/assets/media/images/logos/egoditor/logo-icon-blue.svg">
							<span class="egoditor-logo__text">QR Code</span>
							<h1 class="egoditor-logo__text egoditor-logo__text--sub">Điểm danh đi học</h1>
						</a>
					</div>

					<div class="navigation__menu col-sm-6 col-5 d-flex justify-content-end align-items-center">
						<div class="dropdown">
							<button class="dropbtn"><?php echo $_SESSION['username']?></button>
							<div class="dropdown-content">
								<a href="logout.php">Đăng xuất</a>
							</div>
						</div>
						<a href="#" class="btn toggle menu-toggle">
							<i class="icon icon--menu-toggle icon-business-fab" data-js="mobile-nav-toggle"></i>
						</a>
					</div>

				</div>
			</div>

		</div>

	</div>
	<div class="container">
		
		<!-- <form action="" method="post" name="qrcode"> -->
			
			<div class="divshow">
				<center>
					<select name="danhsachlop" id="danhsachlop">
						<?php 
						while($row = mysqli_fetch_object($result)){
							echo "<option value='".$row->idlophoc."'>".$row->tenlop."</option>";
						} ?>
					</select>
                    <input type="submit" id="btnshow" value="Hiển thị lớp"/>
					<!--<button id="btnshow" class="btnshow">Hiện mã QR</button>-->
				</center>
				<center>

				</center>
			</div>
			<div>
				
			</div>
			<div id="main-time">
				<center><h1 id="showtime"></h1></center>
			</div>
			<!-- </form> -->
			<?php 
// 			$checkLop = "";
            $url="";
			if(isset($_POST['danhsachlop']))
			{
			 //   $_SESSION['idlophoc'] = $_POST['danhsachlop'];
				$selectLop = $_POST['danhsachlop'];
				// echo $row->madiemdanh;
				// echo "1";
				$query1 = "select LopHoc.idlophoc,LopHoc.tenlop, BuoiHoc.madiemdanh from LopHoc inner join BuoiHoc on LopHoc.idlophoc=BuoiHoc.idlophoc and BuoiHoc.hethan='0' and LopHoc.username='".$_SESSION['username']."'";
                $result1 = mysqli_query($con,$query1);
				while($row1 = mysqli_fetch_object($result1))
				{
				    // echo "1";
					if($row1->idlophoc==$selectLop)
					{
					    $checkLop=$row1->madiemdanh;
					    $_SESSION["checkLop"] = $row1->idlophoc;
				// 		$_SESSION["checkLop"]=$row->madiemdanh;
					}
				}
				// echo $_SESSION["checkLop"];
			$url =  "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=".$checkLop;
			}
			?>
			<!--<center><img id="myImg" src="<?php echo $url ?>" style="display: none" width="600" height="400" ></center>-->
			<!--<h1> <?php echo $selectLop ?> </h1>-->
			 </form> 
		</div>
</form>
	</body>
	</html>
	<?php
	if(isset($checkLop) && $checkLop!="")
	{
	   // echo "ac";
	    echo '<center><img id="myImg" src="'.$url.'" style="display: none" width="600" height="400" ></center>';
	    echo '<script>countdown();</script>';
	   echo '<script>document.getElementById("myImg").style.display = "block"; 
	   document.getElementById("btnshow").disabled = true;</script>';
	   
	}
	else
	{
	}
	?>

