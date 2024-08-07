<?php

// Database connection parameters
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "referral";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
$response = array();

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
 
// Check if data parameter exists in the POST request
if (isset($_POST['data'])) { 
    $data = $_POST['data'];

    // Prepare update query to increment count based on referid
    $query = "UPDATE userdb SET count = count + 1 WHERE referid = '$data'";  

    $result = $conn->query($query); 
    if ($result) { 
        echo "Data inserted";
    } else {
        echo "Data not inserted";
    }

    // Prepare update query to set flag to 1 when count reaches 3 for all columns
    $query1 = "UPDATE userdb SET flag = 1 WHERE referid = '$data' AND count >= 3";

    $result1 = $conn->query($query1);      
} else {
    // Data parameter not found in the POST request  
    echo "Data parameter not found";
}
  
if (isset($_POST['code'])) {
    $refer = mysqli_real_escape_string($conn, $_POST['code']);
    $select = " SELECT * FROM userdb WHERE referid = '$refer' ";
    $result2 = mysqli_query($conn, $select);

   if(mysqli_num_rows($result2) > 0){  
    $error[] = 'user already exist!';
   
         
   }else{

    $insert = "INSERT INTO userdb(referid,count,flag) VALUES('$refer','0','0')";
mysqli_query($conn, $insert);
   }}




   if (isset($_POST['id'])) {
    $id = $_POST['id'];
    
    // Prepare query to select courseName based on id
    $stmt = $conn->prepare("SELECT count FROM userdb WHERE referid = ?");
    $stmt->bind_param("s", $id);
    $stmt->execute();
    $stmt->store_result();

    // Check if the query executed successfully
    if ($stmt->num_rows > 0) {
        // Fetch result        
        $stmt->bind_result($courseName);      
        $stmt->fetch();         
        
        // Prepare response
        $response['error'] = false;  
        $response['message'] = "Retrieval Successful!";
        $response['courseName'] = $courseName;
    } else {
        // No data found for the provided id
        $response['error'] = true; 
        $response['message'] = "No course found for the provided id";
    }
} else {
    // 'id' parameter not found in the POST request
    $response['error'] = true;
    $response['message'] = "Parameter 'id' not found in the request";
}  

// Output JSON response
echo json_decode($response);
           
// Close connection       

?>