<?php
$step = 0;
$answer = 0;
$response = 0;

$firstAction = '1-1';
echo("{$firstAction}\r\n");
$row = 1;
$column = 1;
$column = $column + 1;

while (TRUE) {
    $answer = (integer)readline();
    if ($column > 10) {
        $row = $row + 1;
        $column = 1;
    }
    $response = "{$row}-{$column}";
    echo("{$response}\r\n");
    $column = $column + 1;
}
?>