<?php
$step = 0;
$answer = 0;
$response = 0;

$step = 1;
echo("{$step}\r\n");
$step = $step + 1;

while (TRUE) {
    $answer = (integer)readline();
    $response = $step;
    echo("{$response}\r\n");
    $step = $step + 1;
}
?>