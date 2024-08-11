<?php

class Profil extends Sei_Controller{
	public function __construct()
	{
		parent::__construct();
	}

	public function newpassword()
	{
		$this->data["page"] = "newpassword";
		$this->load->view('newpass',$this->data);
	}

	public function newpass()
	{
		$this->data["page"] = "newpassword";

		$this->load->library('form_validation');
		$this->form_validation->set_rules('newpass', 'New Password', 'required|min_length[6]');
		$this->form_validation->set_rules('confirmnewpass', 'Confirm New Password', 'required|matches[newpass]');

        if ($this->form_validation->run() == FALSE)
        {
        	$this->data['error'] = "Please correct ERROR below!";
        	$this->data["akses"] = $this->access;
			$this->load->view('newpass',$this->data);
        }
        else{
        	$pass = $this->input->post('newpass');

		    $url = API_BASE_URL."employee/password/create";
		    $authorization = "Authorization: Bearer ".$this->session->userdata('token');
		    $empcode = $this->session->userdata('empCode');
			$data = json_encode(array("empcode"=>"$empcode","password"=>"$pass"));
			$result = curl_api($url,'POST',$data,$authorization);
        
			if (trim($result->error) != "") {
				$this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
		
			$hasil = $result->data;
			$this->session->set_flashdata('success', "Password berhasil dibuat.");
			$this->session->set_userdata('create_password',FALSE);
			redirect('dashboard');
        }
	}

	public function changepassword()
	{
		if($this->session->userdata('create_password'))
			redirect('profil/newpassword','refresh');

		$this->data["page"] = "changepassword";

		if($this->session->userdata('create_password')){
			$this->load->view('newpass',$this->data);
		}
		else{
			$this->load->view('changepass',$this->data);
		}
	}

	public function chpass()
	{
		$this->data["page"] = "changepassword";

		$this->load->library('form_validation');
		$this->form_validation->set_rules('oldpass', 'New Password', 'required|min_length[6]');
		$this->form_validation->set_rules('newpass', 'New Password', 'required|min_length[6]');
		$this->form_validation->set_rules('confirmnewpass', 'Confirm New Password', 'required|matches[newpass]');

        if ($this->form_validation->run() == FALSE)
        {
        	$this->data['error'] = "Please correct ERROR below!";
			$this->load->view('changepass',$this->data);
        }
        else{
        	$oldpass = $this->input->post('oldpass');
        	$pass = $this->input->post('newpass');

		    $url = API_BASE_URL."employee/password/change";
		    $authorization = "Authorization: Bearer ".$this->session->userdata('token');
		    $empcode = $this->session->userdata('empCode');
			$data = json_encode(array("empcode"=>"$empcode","new_password"=>"$pass","old_password"=>$oldpass));
			$result = curl_api($url,'POST',$data,$authorization);
        
			if (trim($result->error) != "") {
				$this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
		
			$hasil = $result->data;
			$this->session->set_flashdata('success', "Password berhasil dirubah.");
			redirect('dashboard');
        }
	}

	public function update(){
		$this->data["page"] = "editprofil";

		$empcode = $this->session->userdata('empCode');

		$url = API_BASE_URL."employee/profile/".$empcode;
	    $authorization = "Authorization: Bearer ".$this->session->userdata('token');
		 
		$result = curl_api($url,'GET','',$authorization);
        
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
	
		$hasil = $result->data;
		$this->data['profil'] = $hasil;
		$dob = $hasil->birthday;
		$dobarr = explode("/", $dob);
		$dateOfBirth = $dobarr[0]."-".$dobarr[1]."-".$dobarr[2];
		$today = date("Y-m-d");
		$diff = date_diff(date_create($dateOfBirth), date_create($today));
		$this->data['age'] = $diff->format('%y');
		$this->data['tglmpp'] = "";
		$this->data['tglpensiun'] = "";

		$this->load->view('profil_update',$this->data);
	}

	public function telegram(){
		$this->data["page"] = "telegramnotif";
		$this->data["employeeID"] = $this->session->userdata('employeeID');
		$this->load->view('notif_telegram',$this->data);
	}
}