<?php

function curl_api_nav($url,$method,$data,$username="",$password=""){
	$curl = curl_init();
	curl_setopt($curl, CURLOPT_URL, $url);
	curl_setopt($curl, CURLOPT_CUSTOMREQUEST, $method );
	if($method == "POST"){
		curl_setopt( $curl, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));
	}
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);

	if(trim($data) != ""){
		curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
	}
	if(trim($username) != "" && trim($password) != ""){
		curl_setopt($curl, CURLOPT_USERPWD, $username . ":" . $password);
	}

	if($method == "PUT"){
		$headers = [
			'Content-Type:application/json',
			'if-Match: *'
		];
		curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
	}

	$result = curl_exec($curl);

	//print_r($result);

	$info = curl_getinfo($curl);
	$httpcode = $info["http_code"];
	$error = "";

	if($httpcode < 200 || $httpcode > 299){
		$js = json_decode($result);
		$error = "HTTP_CODE return: ".$httpcode.", message: ".$js->error->message;
		//print_r($result);
	}

	if (curl_errno($curl)) {
		$error = curl_error($curl)->message;
	}
	curl_close($curl);

	$obj = new stdClass();
	$obj->httpcode = $httpcode;
	$obj->error = $error;
	$obj->data = json_decode($result);

	return $obj;
}

function curl_api($url,$method,$data,$auth){
	$curl = curl_init();
	curl_setopt($curl, CURLOPT_URL, $url);
	curl_setopt($curl, CURLOPT_CUSTOMREQUEST, $method );
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
	curl_setopt($curl, CURLOPT_HTTPHEADER, array('Content-Type:application/json',$auth));

	if(trim($data) != ""){
		curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
		curl_setopt($curl, CURLOPT_POST, 1);
	}

	$result = curl_exec($curl);

	//print_r($result);

	$info = curl_getinfo($curl);
	$httpcode = $info["http_code"];
	$error = "";

	if($httpcode < 200 || $httpcode > 299){
		$js = json_decode($result);
		$error = "HTTP_CODE return: ".$httpcode;
	}

	if (curl_errno($curl)) {
		// $error = curl_error($curl)->message;
		$err = curl_error($curl);
		if (isset($err->message)){
			$error = $err->message;
		}
	}
	curl_close($curl);

	$obj = new stdClass();
	$obj->httpcode = $httpcode;
	$obj->error = $error;
	$obj->data = json_decode($result);

	return $obj;
}