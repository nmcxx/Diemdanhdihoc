<?php 
require ('json.php');
session_start();
if(isset($_SESSION["username"]))
{
	header("Location: index.php");
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Document</title>
	<link rel="stylesheet" href="css/style.css" />
	<style>
		*{
			margin: 0;
			padding: 0;
			box-sizing: border-box;
		}
		body{
			min-height: 100vh;
			background: #eee;
			display: flex;
			font-family: sans-serif;
		}
		.container{
			margin: auto;
			width: 500px;
			max-width: 90%;
		}
		.container form{
			width: 100%;
			height: 100%;
			padding: 20px;
			background: white;
			border-radius: 4px;
			box-shadow: 0px 8px 16px rgba(0,0,0,.3);
		}
		.container h1{
			text-align: center;
			margin-bottom: 24px;
			color: #222;
		}
		.container form .form-control{
			width: 100%;
			height: 40px;
			background: white;
			border-radius: 4px;
			border: 1px solid silver;
			margin: 10px 0 18px 0;
			padding: 0 10px;
		}
		.container form .btn{
			margin-left: 50%;
			transform:  translateX(-50%);
			width: 120px;
			height: 34px;
			border: none;
			outline: none;
			background: #27a327;
			cursor: pointer;
			font-size: 16px;
			text-transform: uppercase;
			color: white;
			border-radius: 4px;
			transition: .3s;
		}
		.container form .btn:hover{
			opacity: .7;
		}
	</style>
</head>
<body>
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

                <!-- <div class="navigation__menu col-sm-6 col-5 d-flex justify-content-end align-items-center">
                    <ul class="navigation__menu-list align-items-center">
                                                
                                                    <li>
                                <a class="btn btn__rounded btn__rounded--blue" href="https://app.qr-code-generator.com/en/">Đăng nhập</a>
                            </li>
                                            </ul>
                    <a href="#" class="btn toggle menu-toggle">
                        <i class="icon icon--menu-toggle icon-business-fab" data-js="mobile-nav-toggle"></i>
                    </a>
                </div> -->

            </div>
        </div>

    </div>

</div>
<div class="container">
		
		<form action="" method="post" name="login">
			<h1>Đăng Nhập</h1>
			<div class="form-group">
				<label for="">Username</label>
				<input type="text" class="form-control" name="username" placeholder="Tài khoản" required />
			</div>
			<div class="form-group">
				<label for="">Password</label>
				<input type="password" class="form-control" name="password" placeholder="Mật khẩu" required />
			</div>
			<input type="submit" class="btn" value="Đăng nhập">
			<?php 
			if(isset($_POST['username']) && isset($_POST['password']))
{
	$username = stripslashes($_REQUEST['username']);
	$username = mysqli_real_escape_string($con,$username);
	$password = stripslashes($_REQUEST['password']);
	$password = mysqli_real_escape_string($con,$password);
	$query = "SELECT * FROM `TaiKhoan` WHERE `username`='$username' and `password`='$password' and `usertype`='1'";
	$result = mysqli_query($con,$query);
		$rows = mysqli_num_rows($result);
        if($rows==1){
			$_SESSION['username'] = $username;
			echo("<script>location.href = 'index.php';</script>");
// 			header("Location: indexclgt.php");
            }else{
				echo "<div class='login_error'><h6>Tên đăng nhập hoặc mật khẩu không đúng!</h6></div>";
				}
    }
    else{}?>
		</form>
	</div>
</body>
</html>