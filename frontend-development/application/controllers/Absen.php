<?php

class Absen extends Sei_Controller
{
	public function __construct()
	{
		parent::__construct();
	}

	public function saya()
	{
		$this->data["page"] = "absensaya";

		$isthl = $this->session->userdata('isthl');

		if ($isthl)
			$url = API_BASE_URL . "absen/sayathl";
		else
			$url = API_BASE_URL . "absen/saya";

		$emp = $this->session->userdata('empCode');

		if ($this->input->post('tahun')) {
			$year = $this->input->post('tahun');
			$month = $this->input->post('bulan');
		} else {
			$year = date('Y');
			$month = date('n');
		}

		$this->data['bulan'] = $month;
		$this->data['tahun'] = $year;
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');
		$datakirim = json_encode(
			array(
				"employee_code" => "$emp",
				"year" => "$year",
				"month" => "$month"
			)
		);

		$result = curl_api($url, 'GET', $datakirim, $authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$hasil = $result->data;
		$jum = $hasil->jumlah_data;
		$dt = $hasil->data;
		$this->data["absensi"] = $dt;

		if (!$isthl) {
			$this->data["total_lupa"] = $hasil->total_lupa_check;
			$this->data["total_terlambat"] = $hasil->total_terlambat;
			$this->data["total_izin_hari"] = $hasil->total_izin_hari;
			$this->data["total_izin"] = $hasil->total_izin_menit;
			$this->data["total_kurang_jam"] = $hasil->total_kurang_jam;
			$this->data["total_alpha"] = $hasil->total_tanpa_keterangan;
			$this->data["total_form_lupa"] = $hasil->total_form_lupa;
		}

		if ($isthl)
			$this->load->view('absen/absensayathl', $this->data);
		else
			$this->load->view('absen/absensaya', $this->data);
	}

	public function sayanew()
	{
		$this->data["page"] = "absensaya";

		$isthl = $this->session->userdata('isthl');

		if ($isthl)
			$url = API_BASE_URL . "absen/sayathl";
		else
			$url = API_BASE_URL . "absen/saya";

		$emp = $this->session->userdata('empCode');

		if ($this->input->get('tahun')) {
			$year = $this->input->get('tahun');
			$month = $this->input->get('bulan');
		} else {
			$year = date('Y');
			$month = date('n');
		}

		$url = API_BASE_URL . "mobileapp/attendance/historybymonth?employee_code=".$emp."&year=".$year."&month=".$month;

		$this->data['bulan'] = $month;
		$this->data['tahun'] = $year;
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');

		$result = curl_api($url, 'GET', '', $authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$this->data["absensi"] = $hasil;

		if (!$isthl) {
			$this->data["total_lupa"] = $hasil->total_lupa_check;
			$this->data["total_terlambat"] = $hasil->total_terlambat;
			$this->data["total_izin_hari"] = $hasil->total_izin_hari;
			$this->data["total_izin"] = $hasil->total_izin_menit;
			$this->data["total_kurang_jam"] = $hasil->total_kurang_jam;
			$this->data["total_alpha"] = $hasil->total_tanpa_keterangan;
			$this->data["total_form_lupa"] = $hasil->total_form_lupa;
		}

		//$this->load->view('absen/absensaya', $this->data);

		if ($isthl)
			$this->load->view('absen/absensayathl', $this->data);
		else
			$this->load->view('absen/absensayanew', $this->data);
	}

	public function detil($emp, $year, $month)
	{
		if (!$this->permission->absensi_rekap)
			redirect('absen/saya', 'refresh');

		$this->data["page"] = "absenrekap";

		$url = API_BASE_URL . "absen/saya";

		$this->data['bulan'] = $month;
		$this->data['nmbulan'] = $this->_namaBulan($month);
		$this->data['tahun'] = $year;
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');

		$datakirim = json_encode(
			array(
				"employee_code" => "$emp",
				"year" => "$year",
				"month" => "$month"
			)
		);

		$result = curl_api($url, 'GET', $datakirim, $authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$jum = $hasil->jumlah_data;
		$dt = $hasil->data;
		$this->data["absensi"] = $dt;
		$this->data["total_lupa"] = $hasil->total_lupa_check;
		$this->data["total_terlambat"] = $hasil->total_terlambat;
		$this->data["total_izin_hari"] = $hasil->total_izin_hari;
		$this->data["total_izin"] = $hasil->total_izin_menit;
		$this->data["total_kurang_jam"] = $hasil->total_kurang_jam;
		$this->data["total_alpha"] = $hasil->total_tanpa_keterangan;
		$this->data["total_form_lupa"] = $hasil->total_form_lupa;
		$this->data["nama"] = $hasil->nama;

		$this->data["emp"] = $emp;
		$this->load->view('absen/absendetil', $this->data);
	}

	public function rekap()
	{
		if (!$this->permission->absensi_rekap)
			redirect('absen/saya', 'refresh');

		$this->data["page"] = "absenrekap";

		if ($this->input->post('tahun')) {
			$year = $this->input->post('tahun');
			$month = $this->input->post('bulan');
		} else {
			$year = date('Y');
			$month = date('n') - 1;
			if ($month == 0) {
				$year--;
				$month = 12;
			}
		}
		$url = API_BASE_URL . "absen/rekap/" . $year . "/" . $month;
		$this->data['bulan'] = $month;
		$this->data['tahun'] = $year;
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');
		$result = curl_api($url, 'GET', '', $authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		$hasil = $result->data;
		$jum = $hasil->count;
		$dt = $hasil->data;
		$this->data["rekap"] = $dt;

		$this->load->view('absen/rekapabsen', $this->data);

	}

	public function emailrekap()
	{
		if (!$this->permission->absensi_rekap)
			redirect('absen/saya', 'refresh');

		$this->data["page"] = "emailrekap";

		if ($this->input->post('tahun')) {
			$year = $this->input->post('tahun');
			$month = $this->input->post('bulan');
		} else {
			$year = date('Y');
			$month = date('n') - 1;
			if ($month == 0) {
				$year--;
				$month = 12;
			}
		}
		$url = API_BASE_URL . "absen/rekap/" . $year . "/" . $month;
		$this->data['bulan'] = $month;
		$this->data['tahun'] = $year;
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');
		$result = curl_api($url, 'GET', '', $authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		$hasil = $result->data;
		$jum = $hasil->count;
		$dt = $hasil->data;
		$this->data["rekap"] = $dt;

		$this->load->view('absen/emailrekap', $this->data);
	}

	public function kirimemail()
	{
		if (!$this->permission->absensi_rekap)
			redirect('absen/saya', 'refresh');

		$kirim = $this->input->post('kirim');
		$tahun = $this->input->post('tahun');
		$bulan = $this->input->post('bulan');
		$exp = $this->input->post('tanggal_akhir');

		$url = API_BASE_URL . "absen/kirimemail";
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');

		$data = json_encode(array("year" => "$tahun", "month" => "$bulan", "tanggal_akhir" => "$exp", "employee_code" => $kirim));

		$result = curl_api($url, 'POST', $data, $authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		$hasil = $result->data;

		$this->session->set_flashdata('success', "EMAIL BERHASIL DITAMBAHKAN KEDALAM ANTRIAN");
		redirect('absen/rekap');
	}

	public function rekapthl()
	{
		if (!$this->permission->absensi_rekap)
			redirect('absen/saya', 'refresh');

		$this->data["page"] = "absenrekapthl";

		if ($this->input->post('tahun')) {
			$year = $this->input->post('tahun');
			$month = $this->input->post('bulan');
		} else {
			$year = date('Y');
			$month = date('n') - 1;
			if ($month == 0) {
				$year--;
				$month = 12;
			}
		}

		$this->data['bulan'] = $month;
		$this->data['tahun'] = $year;

		$url = API_BASE_URL . "employee/list/thl";
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');

		$result = curl_api($url, 'GET', '', $authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		$hasil = $result->data;
		$this->data["karyawan"] = $hasil->data;

		$this->load->view('absen/rekap_thl', $this->data);
	}

	public function detilthl($emp, $year, $month)
	{
		if (!$this->permission->absensi_rekap)
			redirect('absen/saya', 'refresh');

		$this->data["page"] = "absenrekapthl";

		$url = API_BASE_URL . "absen/sayathl";

		$this->data['bulan'] = $month;
		$this->data['nmbulan'] = $this->_namaBulan($month);
		$this->data['tahun'] = $year;
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');

		//echo $authorization;

		$datakirim = json_encode(
			array(
				"employee_code" => "$emp",
				"year" => "$year",
				"month" => "$month"
			)
		);

		$result = curl_api($url, 'GET', $datakirim, $authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		$hasil = $result->data;
		$jum = $hasil->jumlah_data;
		$dt = $hasil->data;
		$this->data["absensi"] = $dt;
		$this->data["emp"] = $emp;
		$this->data["nama"] = $hasil->nama;
		$this->load->view('absen/absendetilthl',$this->data);
	}

	public function rekapcuti()
	{
		if (!$this->permission->absensi_rekap)
			redirect('absen/saya', 'refresh');

		$this->data["page"] = "absenrekapcuti";

		if ($this->input->post('tahun')) {
			$year = $this->input->post('tahun');
			$month = $this->input->post('bulan');
		} else {
			$year = date('Y');
			$month = date('n');
		}

		$this->data['bulan'] = $month;
		$this->data['tahun'] = $year;

		$url = API_BASE_URL . "employee/list/cuti/" . $year . "/" . $month;
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');

		$result = curl_api($url, 'GET', '', $authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		$hasil = $result->data;
		$this->data["cuti"] = $hasil->data;
		$this->load->view('absen/rekap_cuti', $this->data);
	}

	public function detilcuti($emp, $year, $month)
	{
		if (!$this->permission->absensi_rekap)
			redirect('absen/saya', 'refresh');

		$this->data["page"] = "absenrekapcuti";

		$url = API_BASE_URL . "employee/cutidetil";

		$this->data['bulan'] = $month;
		$this->data['tahun'] = $year;
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');
		$datakirim = json_encode(
			array(
				"employee_code" => "$emp",
				"year" => "$year",
				"month" => "$month"
			)
		);

		$result = curl_api($url, 'GET', $datakirim, $authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		$hasil = $result->data;
		$jum = $hasil->jumlah_data;
		$dt = $hasil->data;
		$nama = $hasil->name;
		$this->data["employee_code"] = $emp;
		$this->data["cuti"] = $dt;
		$this->data["nama"] = $nama;

		$this->data["emp"] = $emp;
		$this->load->view('absen/cutidetil', $this->data);
	}

	public function emp($year = 0, $month = 0, $day = 0, $dpt = "all")
	{
		if (!$this->permission->absensi_dashboard)
			redirect('absen/saya', 'refresh');

		$this->data["page"] = "empabsen";

		if ($this->input->get('tahun')) {
			$year = $this->input->get('tahun');
			$month = $this->input->get('bulan');
			$day = $this->input->get('tanggal');
			$dpt = $this->input->get('dept');
		} else if ($year == 0) {
			$year = date('Y');
			$month = date('n');
			if ($month == 0) {
				$year--;
				$month = 12;
			}
			$day = date('d');
			$dpt = "all";
		} else {
			$dpt = rawurldecode($dpt);
		}

		$this->data['bulan'] = $month;
		$this->data['tahun'] = $year;
		$this->data['hari'] = $day;
		$this->data['departemen'] = $dpt;
		$this->data['nama_bulan'] = $this->_namaBulan($month);

		/*if($this->input->post('tahun')){
				  $year = $this->input->post('tahun');
				  $month = $this->input->post('bulan');
				  $day = $this->input->post('day');
			  }
			  else{
				  $year = date('Y');
				  $month = date('n');
				  $day = date('d');
			  }

			  $data['bulan'] = $month;
			  $data['tahun'] = $year;
			  $data['hari'] = $day;*/

		$authorization = "Authorization: Bearer " . $this->session->userdata('token');
		$url = API_BASE_URL . "absen/empdash/" . $year . "/" . $month . "/" . $day;

		$result = curl_api($url, 'GET', '', $authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		$hasil = $result->data;

		$this->data["tanggal"] = $hasil->tgl;
		$this->data["namahari"] = $hasil->hari;
		$this->data["sum_all"] = $hasil->sum_check_time;
		$this->data["sum_sekper"] = $hasil->sum_check_time_sekper;
		$this->data["sum_eng"] = $hasil->sum_check_time_enginer;
		$this->data["sum_mppp"] = $hasil->sum_check_time_mppp;
		$this->data["sum_keu"] = $hasil->sum_check_time_keuangan;
		$this->data["sum_sdmu"] = $hasil->sum_check_time_sdmu;
		$this->data["sum_log"] = $hasil->sum_check_time_logistik;
		$this->data["sum_pem"] = $hasil->sum_check_time_pemasaran;
		$this->data["sum_bis"] = $hasil->sum_check_time_bisnis;


		/*$url2 = API_BASE_URL."master/dept/list/0/50";
			  $curl2 = curl_init();
			  curl_setopt($curl2, CURLOPT_URL, $url2);
			  curl_setopt($curl2, CURLOPT_CUSTOMREQUEST, 'GET' );
			  curl_setopt($curl2, CURLOPT_RETURNTRANSFER, 1);
			  curl_setopt($curl2, CURLOPT_HTTPHEADER, array('Content-Type:application/json',$authorization));
			  $result2 = curl_exec($curl2);
			  if (curl_errno($curl2)) {
				  echo 'Error:' . curl_error($curl2);
			  }
			  curl_close($curl2); 

			  $hasil2 = json_decode($result2);
			  if($hasil2->msg === "SUCCESS"){
				  $data["dept"] = $hasil2->data;
			  }*/

		$this->load->view('absen/absen_hari_dash', $this->data);
	}

	public function empdetail()
	{
		if (!$this->permission->absensi_dashboard)
			redirect('absen/saya', 'refresh');

		$this->data["page"] = "empabsen";

		if ($this->input->post('tahun')) {
			$year = $this->input->post('tahun');
			$month = $this->input->post('bulan');
			$day = $this->input->post('day');
		} else {
			$year = date('Y');
			$month = date('n');
			$day = date('d');
		}

		$this->data['bulan'] = $month;
		$this->data['tahun'] = $year;
		$this->data['hari'] = $day;
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');
		$url = API_BASE_URL . "absen/emp/" . $year . "/" . $month . "/" . $day;

		$result = curl_api($url, 'GET', '', $authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		$hasil = $result->data;
		$this->data["tanggal"] = $hasil->tgl;
		$this->data["namahari"] = $hasil->hari;
		$this->data["absensi"] = $hasil->data;

		$this->load->view('absen/absen_hari', $this->data);
	}
	public function approved()
	{
		if (!$this->permission->absensi_approve)
			redirect('absen/saya', 'refresh');

		$this->data["page"] = "absenapproved";

		$day = null;

		if ($this->input->get('tahun') != null) {
			$year = $this->input->get('tahun');
			$month = $this->input->get('bulan');
			$day = $this->input->get('day');
			if ($day == 0) {
				$day = null;
			}
		} else {
			$year = date('Y');
			$month = date('n');
		}

		$this->data['bulan'] = $month;
		$this->data['tahun'] = $year;
		$this->data['hari'] = $day;
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');


		// API 
		$empcode = $this->session->userdata('empCode');
		$url = API_BASE_URL . "mobileapp/attendance/needapproval?employee_code=".$empcode."&month=".$month."&year".$year;
		if($day!=null){
			$url = $url."&day=".$day;
		}
		
		$result = curl_api($url,'GET','',$authorization);
		// API Approved
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		$hasil = $result->data;
		$this->data["absensi"] = $hasil;

		$this->load->view('absen/absen_approved', $this->data);
                
	}
	
	public function acceptapproval(){
		if (!$this->permission->absensi_approve)
			redirect('absen/saya', 'refresh');

		$id = $this->input->post('attendanceId');
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');
		$empcode = $this->session->userdata('empCode');
		$url = API_BASE_URL . "mobileapp/attendance/confirm?empCode=".$empcode."&id=".$id."&approve=true";
		
		$result = curl_api($url,'GET','',$authorization);
		// API Approved
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error . " " . $url);
			redirect('info/error');	
		}

		$this->session->set_flashdata('info', 'Approved');
                redirect('absen/approved','refresh');
	}

	public function rejectapproval(){
		if (!$this->permission->absensi_approve)
			redirect('absen/saya', 'refresh');

		$attendanceId = $this->input->post('attendanceId');
		$reason = $this->input->post('rejectReason');
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');
		$empcode = $this->session->userdata('empCode');
		$params = array(
			'empCode' => $empcode,
			'id' => $attendanceId,
			'approve' => false,
			'reason' => $reason
		);
		$url = API_BASE_URL . "mobileapp/attendance/confirm?".http_build_query($params);
		
		$result = curl_api($url,'GET','',$authorization);
		// API Approved
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');	
		}

		$this->session->set_flashdata('info', 'Rejected');
                redirect('absen/approved','refresh');
	}

	public function view_attendance($id) {
        $authorization = "Authorization: Bearer " . $this->session->userdata('token');
		$this->data["page"] = "empabsen";
        $empcode = $this->session->userdata('empCode');
        $url = API_BASE_URL . "mobileapp/attendance/requestdetail?id=" . $id;

        $result = curl_api($url, 'GET', '', $authorization);
        if (trim($result->error) != "") {
            $this->session->set_flashdata('error', $result->error);
            redirect('info/error');
        }

        $this->data['attendance'] = $result->data->data;
		$this->load->view('absen/view_attendance', $this->data);
    }



	public function approveddata()
{
    // Check if the user has access to data approval
    if (!$this->permission->absensi_approve)
		redirect('absen/saya', 'refresh');

    $this->data["page"] = "absenapprovedcheck";

    // Get the year, month, and day from the POST data, or use current date as default
    $year = $this->input->post('tahun') ?: date('Y');
    $month = $this->input->post('bulan') ?: date('n');
    $day = $this->input->post('day') ?: date('d');

    $this->data['bulan'] = $month;
    $this->data['tahun'] = $year;
    $this->data['hari'] = $day;

    $authorization = "Authorization: Bearer " . $this->session->userdata('token');

    // API call to get data that needs approval
    $url = API_BASE_URL . "absen/need_approval/" . $year . "/" . $month . "/" . $day;
    // $result = curl_api($url, 'GET', '', $authorization);

    // if (trim($result->error) != "") {
    //     // If there's an error, redirect to error page
    //     $this->session->set_flashdata('error', $result->error);
    //     redirect('info/error');
    // }

    // $this->data['hasil'] = $result->data;

    // Load the view for displaying approved data details
    $this->load->view('absen/absen_approved_detail', $this->data);
}

	public function mentah($year = 0, $month = 0, $tgl = 0, $dpt = "all")
	{
		if (!$this->permission->absensi_data_mentah)
			redirect('absen/saya', 'refresh');

		$this->data["page"] = "absenmentah";

		if ($this->input->post('tahun')) {
			$year = $this->input->post('tahun');
			$month = $this->input->post('bulan');
			$tgl = $this->input->post('tanggal');
			$dpt = $this->input->post('dept');
		} else if ($year == 0) {
			$year = date('Y');
			$month = date('n');
			if ($month == 0) {
				$year--;
				$month = 12;
			}
			$tgl = date('d');
			$dpt = "all";
		} else {
			$dpt = rawurldecode($dpt);
		}

		$this->data['bulan'] = $month;
		$this->data['tahun'] = $year;
		$this->data['tanggal'] = $tgl;
		$this->data['departemen'] = $dpt;
		$this->data['nama_bulan'] = $this->_namaBulan($month);

		$url = API_BASE_URL . "absen/mentah/" . $year . "/" . $month . "/" . $tgl . "/" . $dpt;
		//echo "url = ".$url;
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');

		$result = curl_api($url, 'GET', '', $authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$this->data["absensi"] = $hasil->data;

		$url2 = API_BASE_URL . "master/dept/list/0/50";
		$curl2 = curl_init();
		$result2 = curl_api($url2, 'GET', '', $authorization);

		if (trim($result2->error) != "") {
			$this->session->set_flashdata('error', $result2->error);
			redirect('info/error');
		}
		$hasil2 = $result2->data;
		$this->data["dept"] = $hasil2->data;

		$this->load->view('absen/absen_mentah', $this->data);
	}

	public function _namaBulan($bln)
	{
		switch ($bln) {
			case 1:
				$nmbulan = "Januari";
				break;
			case 2:
				$nmbulan = "Februari";
				break;
			case 3:
				$nmbulan = "Maret";
				break;
			case 4:
				$nmbulan = "April";
				break;
			case 5:
				$nmbulan = "Mei";
				break;
			case 6:
				$nmbulan = "Juni";
				break;
			case 7:
				$nmbulan = "Juli";
				break;
			case 8:
				$nmbulan = "Agustus";
				break;
			case 9:
				$nmbulan = "September";
				break;
			case 10:
				$nmbulan = "Oktober";
				break;
			case 11:
				$nmbulan = "November";
				break;
			case 12:
				$nmbulan = "Desember";
				break;
		}

		return $nmbulan;
	}
}
