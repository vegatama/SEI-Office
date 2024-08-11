<?php

class Role extends Sei_Controller{
    public function __construct(){
        parent::__construct();
    }

    public function tambah(){
        $this->data["page"] = "roles";
        $this->load->view('master/role_add',$this->data);
    }

    public function addp(){
        $nama = $this->input->post('nama');
		$keterangan = $this->input->post('keterangan');
        $default = $this->input->post('default') !== null ? true : false ;

        $masterHariLibur = $this->input->post('masterHariLibur') !== null ? true : false ;
        $masterLokasiAbsensi = $this->input->post('masterLokasiAbsensi') !== null ? true : false ;
        $masterRoles = $this->input->post('masterRoles') !== null ? true : false ;
        $masterRuang = $this->input->post('masterRuang') !== null ? true : false ;
        $masterCuti = $this->input->post('masterCuti') !== null ? true : false ;
        $masterNotifikasi = $this->input->post('masterNotifikasi') !== null ? true : false ;
        $master = false;
        if($masterHariLibur == true || $masterLokasiAbsensi == true || $masterRoles == true || $masterRuang == true || $masterCuti == true || $masterNotifikasi == true)
            $master = true;

        $absensiDashboard = $this->input->post('absensiDashboard') !== null ? true : false ;
        $absensiDataMentah = $this->input->post('absensiDataMentah') !== null ? true : false ;
        $absensiRekap = $this->input->post('absensiRekap') !== null ? true : false ;
        $absensiSaya = $this->input->post('absensiSaya') !== null ? true : false ;
        $absensiApprove = $this->input->post('absensiApprove') !== null ? true : false ;
        $absensi = false;
        if($absensiDashboard == true || $absensiDataMentah == true || $absensiRekap == true || $absensiSaya == true || $absensiApprove == true)
            $absensi = true;

        $cutiJatah = $this->input->post('cutiJatah') !== null ? true : false ;
        $cutiSaya = $this->input->post('cutiSaya') !== null ? true : false ;
        $cutiApprove = $this->input->post('cutiApprove') !== null ? true : false ;
        $cuti = false;
        if($cutiJatah == true || $cutiSaya == true || $cutiApprove == true)
            $cuti = true;

        $karyawanDashboard = $this->input->post('karyawanDashboard') !== null ? true : false ;
        $karyawanData = $this->input->post('karyawanData') !== null ? true : false ;
        $karyawan = false;
        if($karyawanDashboard == true || $karyawanData == true)
            $karyawan = true;

        $pmoProject = $this->input->post('pmoProject') !== null ? true : false ;
        $pmoAnggaran = $this->input->post('pmoAnggaran') !== null ? true : false ;
        $pmoDpbj = $this->input->post('pmoDpbj') !== null ? true : false ;
        $pmoPo = $this->input->post('pmoPo') !== null ? true : false ;
        $pmo = false;
        if($pmoProject == true || $pmoAnggaran == true || $pmoDpbj == true || $pmoPo == true)
            $pmo = true;
        
        $navDashboard = $this->input->post('navDashboard') !== null ? true : false ;
        $navBudget = $this->input->post('navBudget') !== null ? true : false ;
        $navDpb = $this->input->post('navDpb') !== null ? true : false ;
        $navDpj = $this->input->post('navDpj') !== null ? true : false ;
        $navBapb = $this->input->post('navBapb') !== null ? true : false ;
        $navSppd = $this->input->post('navSppd') !== null ? true : false ;
        $navUm = $this->input->post('navUm') !== null ? true : false ;
        $navUmm = $this->input->post('navUmm') !== null ? true : false ;
        $navPjum = $this->input->post('navPjum') !== null ? true : false ;
        $nav = false;
        if($navDashboard == true || $navBudget == true || $navDpb == true || $navDpj == true || $navPjum == true || $navBapb == true || $navSppd == true || $navUm == true || $navUmm == true)
            $nav = true;

        $opskKendaraan = $this->input->post('opskKendaraan') !== null ? true : false ;
        $opskOrder = $this->input->post('opskOrder') !== null ? true : false ;
        $opskRekapOrder = $this->input->post('opskRekapOrder') !== null ? true : false ;
        $opskApproval = $this->input->post('opskApproval') !== null ? true : false ;
        $opsk = false;
        if($opskKendaraan == true || $opskOrder == true || $opskRekapOrder == true || $opskApproval == true)
            $opsk = true;

        $event = $this->input->post('event') !== null ? true : false ;
        $dokumen = $this->input->post('dokumen') !== null ? true : false ;

        $permission = array(
            "master" => $master,
            "master_ruang" => $masterRuang,
            "master_hari_libur" => $masterHariLibur,
            "master_lokasi_absensi" => $masterLokasiAbsensi,
            "master_roles" => $masterRoles,
            "master_cuti" => $masterCuti,
            "master_notifikasi" => $masterNotifikasi,
            "absensi" => $absensi,
            "absensi_dashboard" => $absensiDashboard,
            "absensi_data_mentah" => $absensiDataMentah,
            "absensi_rekap" => $absensiRekap,
            "absensi_saya" => $absensiSaya,
            "absensi_approve" => $absensiApprove,
            "cuti" => $cuti,
            "cuti_jatah" => $cutiJatah,
            "cuti_saya" => $cutiSaya,
            "cuti_approve" => $cutiApprove,
            "karyawan" => $karyawan,
            "karyawan_dashboard" => $karyawanDashboard,
            "karyawan_data" => $karyawanData,
            "pmo" => $pmo,
            "pmo_project" => $pmoProject,
            "pmo_anggaran" => $pmoAnggaran,
            "pmo_dpbj" => $pmoDpbj,
            "pmo_po" => $pmoPo,
            "nav" => $nav,
            "nav_dashboard" => $navDashboard,
            "nav_budget" => $navBudget,
            "nav_dpb" => $navDpb,
            "nav_dpj" => $navDpj,
            "nav_bapb" => $navBapb,
            "nav_sppd" => $navSppd,
            "nav_um" => $navUm,
            "nav_umm" => $navUmm,
            "nav_pjum" => $navPjum,
            "opsk" => $opsk,
            "opsk_kendaraan" => $opskKendaraan,
            "opsk_order" => $opskOrder,
            "opsk_rekap_order" => $opskRekapOrder,
            "opsk_approval" => $opskApproval,
            "event" => $event,
            "dokumen" => $dokumen
        );

		$this->load->library('form_validation');
        $this->form_validation->set_rules('nama', 'Nama', 'required');
        $this->form_validation->set_rules('keterangan', 'Keterangan', 'required');
        if ($this->form_validation->run() == FALSE)
        {
            $this->data["page"] = "roles";
            $this->load->view('master/role_add',$this->data);
        }
        else{
            $dtupload = json_encode(array(
                "role_name" => $nama,
                "role_description" => $keterangan,
                "role_default" => $default,
                "permission" => json_encode($permission)
			));

			$authorization = "Authorization: Bearer ".$this->session->userdata('token');
			$url = API_BASE_URL."master/role";
			$result = curl_api($url,'POST',$dtupload,$authorization);
			
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
			else{
				$this->session->set_flashdata('success', 'Role berhasil ditambahkan.');
				redirect('master/roles','refresh');
			}
        }
    }
}