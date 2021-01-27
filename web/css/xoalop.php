<?php 
require('json.php');

$idlop = "";

if(isset($_POST['idlophoc']))
{
	$idlop = mysqli_real_escape_string($con, $_POST['idlophoc']);
}

$query = "Delete from `LopHoc` where idlophoc='$idlop'";
if(mysqli_query($con,$query))
{
	echo "true";
}
else
{
	echo "false";
}

 ?>