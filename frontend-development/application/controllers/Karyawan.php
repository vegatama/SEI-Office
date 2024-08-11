<?php 

class Karyawan extends Sei_Controller{
	public function __construct()
	{
		parent::__construct();
	}

	public function tambah(){
		$this->data["page"] = "karyawan";
		$this->load->view('emp/tambah_karyawan',$this->data);
	}

	public function dashboard(){
		if(!$this->permission->karyawan_dashboard)
			redirect('dashboard','refresh');

		$this->data["page"] = "dashkaryawan";

		$url = API_BASE_URL."employee/dashboard";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		 
		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data; 
		$dt = $hasil;
		$this->data["emp_total"] = $dt->emp_total;
		$this->data["emp_tetap"] = $dt->emp_tetap;
		$this->data["emp_kwt"] = $dt->emp_kwt;
		$this->data["emp_thl"] = $dt->emp_thl;
		
		$this->load->view('emp/dashboard_karyawan',$this->data);
	}

	public function list(){
		if(!$this->permission->karyawan_data)
			redirect('dashboard','refresh');

		$this->data["page"] = "karyawan";

		$url = API_BASE_URL."employee/list/0/10000";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		 
		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$this->data["employees"] = $hasil->data;

		$this->load->view('emp/list_karyawan',$this->data);
	}

	public function detail($kid){
		if(!$this->permission->karyawan_data)
			redirect('dashboard','refresh');

		$this->data["page"] = "karyawan";

		$url = API_BASE_URL."employee/".$kid;
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		 
		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$this->data["profil"] = $hasil;
		$dob = $hasil->birthday;
		$dobarr = explode("/", $dob);
		$dateOfBirth = $dobarr[0]."-".$dobarr[1]."-".$dobarr[2];
		$today = date("Y-m-d");
		$diff = date_diff(date_create($dateOfBirth), date_create($today));
		$this->data['age'] = $diff->format('%y');
		$this->data['tglmpp'] = "";
		$this->data['tglpensiun'] = "";

		$this->load->view('emp/detail_karyawan',$this->data);
	}

	public function slipgaji(){
		$this->data["page"] = "slipgaji";

		$url = API_BASE_URL."slipgaji";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		 
		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$this->data["slipgaji"] = $hasil->templates;


		$this->load->view('emp/list_slipgaji',$this->data);
	}
}