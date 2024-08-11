<?php

class Dashboard extends Sei_Controller {
	public function __construct()
	{
		parent::__construct();
	}

	public function index()
	{
		$this->data['page'] = "dashboard";

		//ambil data summary absen
		$emp = $this->session->userdata('empCode');
		$url = API_BASE_URL."absen/dashboard/".$emp;
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);
        
        if (trim($result->error) != "") {
            $this->session->set_flashdata('error', $result->error);
            redirect('info/error');
        }

		$hasil = $result->data;
		$cekin_today = $hasil->absen_masuk_today;
		$cekout_today = $hasil->absen_keluar_today;
		if($cekout_today == $cekin_today)
			$cekout = "N/A";
		else
			$cekout = $cekout_today;
		$this->data['cekin_today'] = $hasil->absen_masuk_today;
		$this->data['cekout_today'] = $cekout;
		$this->data['cekin_yesterday'] = $hasil->absen_masuk_yesterday;
		$this->data['cekout_yesterday'] = $hasil->absen_keluar_yesterday;
		$this->data['total_terlambat'] = $hasil->total_terlambat;
		$this->data['total_kurang_jam'] = $hasil->total_kurang_jam;
		$this->data['total_lct'] = $hasil->total_lupa_check_time;		

		//dapetin data ulang tahun
		$url = API_BASE_URL."employee/birthday";
		$result = curl_api($url,'GET','',$authorization);
        
        if (trim($result->error) != "") {
            $this->session->set_flashdata('error', $result->error);
            redirect('info/error');
        }
    
        $hasil = $result->data;
		$this->data['birthday'] = $hasil->data;

		//dapetin data kegiatan
		$url = API_BASE_URL."hadir/list";
		$result = curl_api($url,'GET','',$authorization);
        
        if (trim($result->error) != "") {
            $this->session->set_flashdata('error', $result->error);
            redirect('info/error');
        }
    
        $hasil = $result->data;
		$this->data['kegiatan'] = $hasil->hadirs;

        $url = API_BASE_URL."notification/list?empCode=".$emp;
        $result = curl_api($url,'GET','',$authorization);
    
        if (trim($result->error) != "") {
            $this->session->set_flashdata('error', $result->error);
            redirect('info/error');
        }

    
        $this->data["notifications"] = $result->data->notifications;

		$this->load->view('dashboard',$this->data);
	}


}