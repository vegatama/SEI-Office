<?php

class View extends CI_Controller {
	public function __construct()
	{
		parent::__construct();
	}

	public function detailruang($id)
	{
		$data = array();

		if($this->session->flashdata('error') != NULL)
			$data['error'] = $this->session->flashdata('error');

		$url = API_BASE_URL."view/ruangmeeting?id=".$id;
		$result = curl_api($url,'GET','','');
		
		 if (trim($result->error) != "") {
		 	$this->session->set_flashdata('error', $result->error);
		 	redirect('view/error');
		 }

		$return = $result->data;
	$data["ruang"] = $return;
	 $this->load->view("viewruang",$data); 
	}

	public function absen($viewid)
	{
		$data = array();

		if($this->session->flashdata('error') != NULL)
			$data['error'] = $this->session->flashdata('error');

		$url = API_BASE_URL."view/absen/".$viewid;
		 
		$result = curl_api($url,'GET','','');
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;

		$jum = $hasil->jumlah_data;
		$dt = $hasil->data;
		$data["absensi"] = $dt;
		$data["total_lupa"] = $hasil->total_lupa_check;
		$data["total_terlambat"] = $hasil->total_terlambat;
		$data["total_izin_hari"] = $hasil->total_izin_hari;
		$data["total_izin"] = $hasil->total_izin_menit;
		$data["total_kurang_jam"] = $hasil->total_kurang_jam;
		$data["total_alpha"] = $hasil->total_tanpa_keterangan;
		$data["total_form_lupa"] = $hasil->total_form_lupa;
		$data["emp"] = $hasil->employee_code;
		$data["nama"] = $hasil->nama;
		$data["nmbulan"] = $this->_namaBulan($hasil->bulan);
		$data["tahun"] = $hasil->tahun;
		

		$this->load->view('viewabsen',$data);
	}

	public function event($hid){
		$data = array();
		date_default_timezone_set('Asia/Jakarta');

		if($this->session->flashdata('error') != NULL)
			$data['error'] = $this->session->flashdata('error');

		$url = API_BASE_URL."view/hadir/".$hid;
		$result = curl_api($url,'GET','','');
        
        if (trim($result->error) != "") {
            $this->session->set_flashdata('error', $result->error);
            redirect('view/error');
        }

		$return = $result->data;
		$data["hadir"] = $return;

		$authorization = "API-KEY: LENSEI442531";
		$url = API_BASE_URL."public/listemp";
		$result = curl_api($url,'GET','',$authorization);
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$data["employee"] = $hasil->data;

		$stanggal = $return->tanggal;
		$date = str_replace('/', '-', $stanggal);
		$tanggal = date('Y-m-d', strtotime($date));
		$stanggalskrg = date('Y-m-d');
		$stanggalskrg2 = date('d/m/Y');
		//echo $stanggal." - ".$stanggalskrg;

		if($stanggal != $stanggalskrg2){
			if($stanggalskrg < $tanggal){
				$data['info'] = "Kegiatan Belum Dimulai.";
            	$this->load->view('view_error',$data);
			}
			else{
				$data['info'] = "Kegiatan Sudah Berakhir.";
            	$this->load->view('view_error',$data);
			}
		}
		else{
			$waktu_mulai = $return->waktu_mulai;
			$waktu_selesai = $return->waktu_selesai;
			$waktu_skrg_plus = date('H:i',strtotime("+10 minutes"));
			$waktu_skrg_min = date('H:i',strtotime("-10 minutes"));
			//echo $waktu_skrg;
			
			if($waktu_skrg_plus < $waktu_mulai){
				$data['info'] = "Kegiatan Belum Dimulai.";
            	$this->load->view('view_error',$data);
			}
			else if($waktu_skrg_min > $waktu_selesai){
				$data['info'] = "Kegiatan Sudah Berakhir.";
            	$this->load->view('view_error',$data);
			}
			else{				
				$this->load->view("viewhadir",$data); 
			}
		}
		//$data["hadir"] = $return;
		//$this->load->view("viewhadir",$data); 
	}

	public function hadirs($hid){
		$data = array();

		if($this->session->flashdata('error') != NULL)
			$data['error'] = $this->session->flashdata('error');

		$url = API_BASE_URL."view/hadir/".$hid;
		$result = curl_api($url,'GET','','');
        
        if (trim($result->error) != "") {
            $this->session->set_flashdata('error', $result->error);
            redirect('view/error');
        }

		$return = $result->data;
        $data["hadir"] = $return;
		$this->load->view("viewhadirs",$data); 
	}

	public function hadirp(){
		$this->load->library('form_validation');
        $this->form_validation->set_rules('nama', 'Nama', 'required');
        $this->form_validation->set_rules('bagian', 'Departmen/Bagian', 'required');
        $this->form_validation->set_rules('email', 'Email/Phone', 'required');
		if ($this->form_validation->run() == FALSE)
        {
            $url = API_BASE_URL."view/hadir/".$hid;
			$result = curl_api($url,'GET','','');
			
			if (trim($result->error) != "") {
				$this->session->set_flashdata('error', $result->error);
				redirect('view/error');
			}

			$return = $result->data;
			$data["hadir"] = $return;
			$this->load->view("viewhadir",$data); 
        }
		else{
			$nama = $this->input->post('nama');
            $bagian = $this->input->post('bagian');
            $email = $this->input->post('email');
            $hid = $this->input->post('hid');
			if(trim($bagian) == ""){
				$bagian = "PT. Surya Energi Indotama";
			}
            
            $dtupload = json_encode(array(
				"nama" => $nama,
				"bagian" => $bagian,
                "email_phone" => $email,
                "daftar_id" => $hid
			));

			$url = API_BASE_URL."view/peserta";
			$result = curl_api($url,'POST',$dtupload,'');
			
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('view/error');
			}
			else{
				$this->session->set_flashdata('info', 'Data berhasil ditambahkan.');
				redirect('view/hadirs/'.$hid,'refresh');
			}
		}
	}

	public function error()
	{
		$data = array();
		$data["page"] = "error";
		if($this->session->flashdata('error') != NULL)
			$data['error'] = $this->session->flashdata('error');
		if($this->session->flashdata('info') != NULL)
			$data['info'] = $this->session->flashdata('info');

		$this->load->view('view_error',$data);
	}

	public function _namaBulan($bln){
		switch($bln){
			case 1: $nmbulan = "Januari";
				break;
			case 2: $nmbulan = "Februari";
				break;
			case 3: $nmbulan = "Maret";
				break;
			case 4: $nmbulan = "April";
				break;
			case 5: $nmbulan = "Mei";
				break;
			case 6: $nmbulan = "Juni";
				break;
			case 7: $nmbulan = "Juli";
				break;
			case 8: $nmbulan = "Agustus";
				break;
			case 9: $nmbulan = "September";
				break;
			case 10: $nmbulan = "Oktober";
				break;
			case 11: $nmbulan = "November";
				break;
			case 12: $nmbulan = "Desember";
				break;
		}

		return $nmbulan;
	}
}
