<?php

// GitHub repository owner and name
$owner = 'Madness676';
$repo = 'HashHash';

// New file details
$file_path = 'new_file.txt';
$file_content = 'Content of the new file';

// GitHub API endpoint for creating or updating a file
$url = "https://api.github.com/repos/{$owner}/{$repo}/contents/{$file_path}";

// Data payload for the request
$data = array(
    'message' => 'Create new file via API',
    'content' => base64_encode($file_content), // Content encoded in base64
    // Optional: branch, committer, author, etc.
);

// Secret token stored in an environment variable named DEV_KEY
$token = getenv('DEV_KEY');

if ($token === false) {
    die('Error: GitHub access token not found.');
}

// Headers
$headers = array(
    'Authorization: token ' . $token,
    'User-Agent: PHP Script',
    'Content-Type: application/json',
);

// cURL options
$options = array(
    CURLOPT_URL => $url,
    CURLOPT_HTTPHEADER => $headers,
    CURLOPT_CUSTOMREQUEST => 'PUT', // Use PUT to create a new file
    CURLOPT_POSTFIELDS => json_encode($data),
    CURLOPT_RETURNTRANSFER => true,
);

// Initialize cURL session
$curl = curl_init();
curl_setopt_array($curl, $options);

// Execute cURL request
$response = curl_exec($curl);

// Check for errors
if ($response === false) {
    echo 'Error: ' . curl_error($curl);
} else {
    // Decode JSON response
    $responseData = json_decode($response, true);
    
    // Print response data
    print_r($responseData);
}

// Close cURL session
curl_close($curl);
?>
