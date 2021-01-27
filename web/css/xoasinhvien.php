<?php 
require('json.php');

$idlop = "";
$username  = "";

if(isset($_POST['idlophoc']))
{
	$idlop = mysqli_real_escape_string($con, $_POST['idlophoc']);
}
if(isset($_POST['username']))
{
	$username = mysqli_real_escape_string($con, $_POST['username']);
}

$query = "DELETE FROM `SVLH` WHERE idlophoc='$idlop' and username='$username'";
if(mysqli_query($con,$query))
{
	echo "true";
}
else
{
	echo "false";
}

 ?>