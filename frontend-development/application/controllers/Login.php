<?php

use Google\Client;

class Login extends CI_Controller {
	public function __construct()
	{
		parent::__construct();
		#include_once FCPATH . "vendor/google/auth/autoload.php";
	}

	public function index()
	{
		//$this->output->enable_profiler(TRUE);
		$data = array();
		if($this->session->flashdata('error') != NULL)
			$data['error'] = $this->session->flashdata('error');
		if($this->session->flashdata('info') != NULL)
			$data['info'] = $this->session->flashdata('info');

		// init configuration
		$clientID = GOOGLE_CLIENT_ID;
		$clientSecret = GOOGLE_CLIENT_SECRET;
		$redirectUri = GOOGLE_REDIRECT_URI;

		// create Client Request to access Google API
		$client = new Google_Client();
		$client->setClientId($clientID);
		$client->setClientSecret($clientSecret);
		$client->setRedirectUri($redirectUri);
		$client->addScope("email");
		$client->addScope("profile");
		$data['login_google'] = $client->createAuthUrl();

		$this->load->view('login',$data);
	}

	public function proses()
	{
		$this->load->library('form_validation');
		$this->form_validation->set_rules('email', 'Email', 'trim|required|valid_email');
		$this->form_validation->set_rules('password', 'Password', 'trim|required');

        if ($this->form_validation->run() == FALSE)
        {
            $this->load->view('login');
        }
        else
        {
        	$email = $this->input->post("email");
        	$pass = $this->input->post("password");
        	//echo "email: ".$email;
        	$ch = curl_init(); 

		    $url = API_BASE_URL."employee/login";
			$data = json_encode(array("email"=>"$email","password"=>"$pass"));
			//echo $data;
			 
			$curl = curl_init();
			curl_setopt($curl, CURLOPT_URL, $url);
			curl_setopt($curl, CURLOPT_POST, 1);
			curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
			curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
			curl_setopt($curl, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));
			$result = curl_exec($curl);
			if (curl_errno($curl)) {
			    $this->session->set_flashdata('error', curl_error($curl));
				redirect('login');
			}
			curl_close($curl);  
			
			$hasil = json_decode($result);
			if($hasil->msg === "SUCCESS"){

				//tutup akses untuk sementara
				$emp = $hasil->employee_code;
				$isactive = $hasil->_active;
				$maintenance_mode = $hasil->maintenance_mode;
				if(!$isactive || ($maintenance_mode == "1" && $emp != "EMP458")){
					//$this->session->sess_destroy();
					$this->session->set_flashdata('info', 'Mohon Maaf, Saat ini kami sedang Maintenance, Silahkan kembali lagi nanti.');
					redirect('login');
				}

				$status = $hasil->status;
				$isthl = FALSE;
				if($status == "THL (Tenaga Harian Lepas)"){
					$isthl = TRUE;
				}

				if($hasil->nav_id == "" || $hasil->nav_id == NULL){
					$navid = "SEI531";
				}
				else{
					$navid = $hasil->nav_id;
				}

				$splitToken = explode(".", $hasil->token);
				$base64Encoded = $splitToken[1]; // JWT payload is always the second part
				$base64Decoded = base64_decode($base64Encoded);
				$parsedToken = json_decode($base64Decoded);
				$exp = $parsedToken->exp; //

				$ses = array(
			        'email' => $hasil->email,
			        'nama' => $hasil->name,
			        'token' => $hasil->token,
			        'empCode' => $hasil->employee_code,
			        'isLogin' => TRUE,
			        'create_password' => $hasil->create_password,
			        'access' => $hasil->access,
			        'isthl' => $isthl,
			        'employeeID' => $hasil->employee_id,
			        'navid' => $navid,
					'permission' => $hasil->roles->permission,
					'expireUntil' => $exp
				);

				$this->session->set_userdata($ses);

				$url = $this->session->userdata('loginRedirect');
				if(isset($url)){
					$this->session->unset_userdata('loginRedirect');
					redirect($url,"refresh");
				}else{
					redirect("dashboard","refresh");
				}
			}
			else{
				$this->session->set_flashdata('error', 'email atau password Anda tidak dikenali');
				redirect('login');
			}
        }
	}

	public function out()
	{
		$this->session->sess_destroy();
		redirect('login','refresh');
	}

	public function google()
	{
		// init configuration
		$clientID = GOOGLE_CLIENT_ID;
		$clientSecret = GOOGLE_CLIENT_SECRET;
		$redirectUri = GOOGLE_REDIRECT_URI;

		// create Client Request to access Google API
		$client = new Google_Client();
		$client->setClientId($clientID);
		$client->setClientSecret($clientSecret);
		$client->setRedirectUri($redirectUri);
		$client->addScope("email");
		$client->addScope("profile");
		
		// authenticate code from Google OAuth Flow
		if (isset($_GET['code'])) {
			$token = $client->fetchAccessTokenWithAuthCode($_GET['code']);
			$client->setAccessToken($token['access_token']);
			$tokenGoogle = $token['access_token'];

			//echo $tokenGoogle;
		
			$ch = curl_init(); 

		    $url = API_BASE_URL."employee/login/google";
			$data = json_encode(
				array(
					"token"=>"$tokenGoogle"
				));
			//echo $data;
			 
			$curl = curl_init();
			curl_setopt($curl, CURLOPT_URL, $url);
			curl_setopt($curl, CURLOPT_POST, 1);
			curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
			curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
			curl_setopt($curl, CURLOPT_HTTPHEADER, array('Content-Type:application/json'));
			$result = curl_exec($curl);
			if (curl_errno($curl)) {
				$this->session->set_flashdata('error', curl_error($curl));
				redirect('login');
			}
			curl_close($curl);  
			
			$hasil = json_decode($result);
			if($hasil->msg === "SUCCESS"){
				$status = $hasil->status;

				//tutup akses untuk sementara
				$emp = $hasil->employee_code;
				$isactive = $hasil->_active;
				$maintenance_mode = $hasil->maintenance_mode;
				if(!$isactive || ($maintenance_mode == "1" && $emp != "EMP458")){
					//$this->session->sess_destroy();
					$this->session->set_flashdata('info', 'Mohon Maaf, Saat ini kami sedang Maintenance, Silahkan kembali lagi nanti.');
					redirect('login');
				}

				$isthl = FALSE;
				if($status == "THL (Tenaga Harian Lepas)"){
					$isthl = TRUE;
				}

				if($hasil->nav_id == "" || $hasil->nav_id == NULL){
					$navid = "SEI531";
				}
				else{
					$navid = $hasil->nav_id;
				}

				$ses = array(
			        'email' => $hasil->email,
			        'nama' => $hasil->name,
			        'token' => $hasil->token,
			        'empCode' => $hasil->employee_code,
			        'isLogin' => TRUE,
			        'create_password' => $hasil->create_password,
			        'access' => $hasil->access,
			        'isthl' => $isthl,
			        'employeeID' => $hasil->employee_id,
			        'navid' => $navid,
					'permission' => $hasil->roles->permission
				);

				$this->session->set_userdata($ses);
				redirect("dashboard","refresh");
			}
			else{
				$this->session->set_flashdata('error', 'email atau password Anda tidak dikenali');
				redirect('login');
			}
		}
		else{
			redirect('login','refresh');
		}
	}

}
