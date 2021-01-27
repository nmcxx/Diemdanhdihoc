<?php 
require('json.php');
$username = "";
$password = "";

if(isset($_POST['username']))
{
	$username = $_POST['username'];
}

if(isset($_POST['password']))
{
	$password = $_POST['password'];
}
// $query = "insert into `TaiKhoan` values('$username','$password')";
    $query = "select * from `TaiKhoan` where username='$username' and password='$password' limit 1";
	$result = mysqli_query($con,$query);
	$row = mysqli_num_rows($result);
	$row1 = mysqli_fetch_object($result);
// 	echo $row;
    if($row==1)
	{
	    if($row1->usertype==1)
	    {
		    echo "gv";
	    }
	    else
	    {
	        echo "hs";
	    }
	}
	else
	{
	    echo "false";
	}

// if(!empty($username) && !empty($password)){
        
//         // $hashed_password = md5($password);
        
//         $json_array = $userObject->loginUsers($username, $password);
        
//         echo json_encode($json_array);
//     }

 ?>
