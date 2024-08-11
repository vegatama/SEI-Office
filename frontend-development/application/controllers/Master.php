<?php 

class Master extends Sei_Controller {
	public function __construct()
	{
		parent::__construct();
	}

	public function karyawan($page = 0){

		$this->data["page"] = "karyawan";

		$url = API_BASE_URL."employee/list/".$page."/500";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$this->data["karyawan"] = $hasil->data;
		
		$this->load->view('master/master_karyawan',$this->data);
	}

	public function libur($page = 0){
		if (!$this->permission->master_hari_libur)
			redirect('dashboard', 'refresh');

		$this->data["page"] = "libur";

		if($this->input->post('tahun')){
			$year = $this->input->post('tahun');
		}else{
			$year = date('Y');
		}
		$this->data['tahun'] = $year;

		$url = API_BASE_URL."master/harilibur/".$year;
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$this->data["libur"] = $hasil->data;

		$this->load->view('master/master_libur',$this->data);
	}

	public function ruangan(){
		if (!$this->permission->master_ruang)
			redirect('dashboard', 'refresh');

		$this->data["page"] = "ruangan";

		$url = API_BASE_URL."master/ruangmeeting/list";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$this->data["ruangan"] = $hasil->data;
		$this->load->view('master/master_ruangan',$this->data);
	}

	public function lokasi($page = 0){
		if (!$this->permission->master_lokasi_absensi)
			redirect('dashboard', 'refresh');

		$this->data["page"] = "lokasi";

		$url = API_BASE_URL."master/lokasi";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$this->data["lokasi"] = $hasil->lokasiAbsen;

		$this->load->view('master/master_lokasi',$this->data);
	}

	public function cuti($page = 0){
		if (!$this->permission->master_cuti)
			redirect('dashboard', 'refresh');

		$this->data["page"] = "cuti";

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."master/izincuti?empCode=".$this->session->userdata('empCode');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		$hasil = $result->data;
		$this->data["izincuti"] = $hasil->izinCuti;

		$this->load->view('master/master_cuti',$this->data);
	}

	public function roles(){
		if (!$this->permission->master_roles)
			redirect('dashboard', 'refresh');

		$this->data["page"] = "roles";
		$url = API_BASE_URL."master/role/list";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$this->data["roles"] = $hasil->roles;
		
		$this->load->view('master/role_list',$this->data);
	}

	public function notifikasi(){
		$this->data["page"] = "notif";
		$url = API_BASE_URL."notification/settings/all";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$this->data["notifikasi"] = $hasil->data;
		
		$this->load->view('master/master_notifikasi',$this->data);
	}
}