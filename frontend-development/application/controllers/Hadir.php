<?php

class Hadir extends Sei_Controller{
    public function __construct()
    {
        parent::__construct();
    }

    public function index()
    {
        date_default_timezone_set('Asia/Jakarta');
		$this->data["page"] = "daftarhadir";

        $authorization = "Authorization: Bearer ".$this->session->userdata('token');
        $pid = $this->session->userdata('employeeID');
        $url = API_BASE_URL."hadir/list";
        $result = curl_api($url,'GET','',$authorization);
        
        if (trim($result->error) != "") {
            $this->session->set_flashdata('error', $result->error);
            redirect('info/error');
        }
    
        $return = $result->data;
        $this->data["hadirs"] = $return->hadirs;    
        $this->data["employee_id"] = $pid;

		// get room list
		$url = API_BASE_URL."master/ruangmeeting/list";
		$result = curl_api($url,'GET','',$authorization);
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$return = $result->data;
		$this->data["ruang_meeting"] = $return->data;

        $this->load->view('hadir/list',$this->data);
    }

    public function add(){
        if(!$this->permission->event)
			redirect('hadir','refresh');

		$this->data["page"] = "daftarhadir";
        
        $authorization = "Authorization: Bearer ".$this->session->userdata('token');
        $url = API_BASE_URL."employee/list/0/9999999";
        $result = curl_api($url,'GET','',$authorization);
        if (trim($result->error) != "") {
            $this->session->set_flashdata('error', $result->error);
            redirect('info/error');
        }

        $return = $result->data;
        $this->data["employee"] = $return->data;

		$url = API_BASE_URL."master/ruangmeeting/list";
		$result = curl_api($url,'GET','',$authorization);
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hadir_repopulate = $this->session->userdata('hadir_repopulate');
		$this->session->unset_userdata('hadir_repopulate');

		$return = $result->data;
		$this->data["ruang_meeting"] = $return->data;
		$this->data["hadir_repopulate"] = $hadir_repopulate;


        $this->load->view('hadir/add',$this->data);
    }

    public function addp(){
        if(!$this->permission->event)
            redirect('hadir','refresh');

		$this->data["page"] = "daftarhadir";

		// unset repopulate form
		$this->session->unset_userdata('hadir_repopulate');

        $this->load->library('form_validation');
        $this->form_validation->set_rules('tanggal', 'Tanggal', 'required');
        $this->form_validation->set_rules('kegiatan', 'Kegiatan', 'required');
        $this->form_validation->set_rules('pimpinan', 'Pimpinan', 'required');
        $this->form_validation->set_rules('subyek', 'Subyek', 'required');
        if ($this->form_validation->run() == FALSE)
        {
            $authorization = "Authorization: Bearer ".$this->session->userdata('token');
			$url = API_BASE_URL."employee/list/0/9999999";
			$result = curl_api($url,'GET','',$authorization);
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}

			$return = $result->data;
			$this->data["employee"] = $return->data;
            $this->load->view('hadir/add',$this->data);
        }
        else{
            $kegiatan = $this->input->post('kegiatan');
            $pimpinan = $this->input->post('pimpinan');
            $tanggal = $this->input->post('tanggal');
            $subyek = $this->input->post('subyek');
            /*$jam_mulai = $this->input->post('jam_mulai');
            $menit_mulai = $this->input->post('menit_mulai');
            $jam_selesai = $this->input->post('jam_selesai');
            $menit_selesai = $this->input->post('menit_selesai');*/
            $waktu_mulai = $this->input->post('waktu_mulai');
            $waktu_selesai = $this->input->post('waktu_selesai');
            $tempat = $this->input->post('tempat');
			$ruang_meeting = $this->input->post('ruang_meeting');
            $notulis = $this->input->post('notulis');
            $keterangan = $this->input->post('keterangan');
            $undangan_internal = $this->input->post('undangan_internal');
            $undangan_external = $this->input->post('undangan_external');
            $isi_undangan = $this->input->post('isi_undangan');
            $undangan = array();
            //print_r($undangan_external);
            if($undangan_internal != "" && count($undangan_internal) > 0){
                foreach($undangan_internal as $email){
                    $undangan[] = $email;
                }
            }
            if($undangan_external != ""){
                $arrundangan = explode(",",$undangan_external);
                foreach($arrundangan as $email2){
                    $undangan[] = trim($email2);
                } 
            }
            
            $dtupload = json_encode(array(
				"kegiatan" => $kegiatan,
				"pimpinan" => $pimpinan,
                "subyek" => $subyek,
                "tanggal" => $tanggal,
                "waktu_mulai" => $waktu_mulai,
                "waktu_selesai" => $waktu_selesai,
                "tempat" => $tempat,
				"pembuat" => $this->session->userdata('employeeID'),
                "notulis" => $notulis,
                "undangan" => $undangan,
                "keterangan" => $keterangan,
                "isi_undangan" => $isi_undangan,
				"ruang_meeting" => $ruang_meeting
			));

            //print_r($dtupload);

			$authorization = "Authorization: Bearer ".$this->session->userdata('token');
			$url = API_BASE_URL."hadir";
			$result = curl_api($url,'POST',$dtupload,$authorization);

            //print_r($result);
			
//			if (trim($result->error) != "") {
//			    $this->session->set_flashdata('error', $result->error);
//				redirect('info/error');
//			}
//			else{
//				$this->session->set_flashdata('success', 'Data kegiatan berhasil ditambahkan.');
//				redirect('hadir','refresh');
//			}
			if ($result->data->msg != "SUCCESS") {
				if ($result->data->msg == "ERROR: RUANG_MEETING_ALREADY_BOOKED") {
					$result->data->msg = "Ruang meeting sudah terpakai pada waktu tersebut.";
				} else if ($result->data->msg == "ERROR: RUANG_MEETING_NOT_FOUND") {
					$result->data->msg = "Ruang meeting tidak ditemukan.";
				}
//				$this->session->set_flashdata('error', "Gagal menambah data kegiatan. ".$result->data->msg);
				$this->session->set_flashdata('info', "Gagal mengupdate data kegiatan. Silahkan coba lagi. ".$result->data->msg);

				// repopulate form
				$hadir_repopulate = array(
					'tanggal' => $tanggal,
					'waktu_mulai' => $waktu_mulai,
					'waktu_selesai' => $waktu_selesai,
					'kegiatan' => $kegiatan,
					'subyek' => $subyek,
					'pimpinan' => $pimpinan,
					'notulis' => $notulis,
					'ruang_meeting' => $ruang_meeting,
                    "keterangan" => $keterangan,
					'tempat' => $tempat
				);
				$this->session->set_userdata('hadir_repopulate', $hadir_repopulate);
				redirect('hadir/add','refresh');
			} else
				if (trim($result->error) != "") {
					$this->session->set_flashdata('error', $result->error);
					redirect('info/error');
				}
				else{
					$this->session->set_flashdata('success', 'Data kegiatan berhasil ditambahkan.');
					redirect('hadir','refresh');
				}

        }
    }

    public function download($dhid){
		$this->data["page"] = "daftarhadir";

        $authorization = "Authorization: Bearer ".$this->session->userdata('token');
        $url = API_BASE_URL."hadir/".$dhid;
        $result = curl_api($url,'GET','',$authorization);
        if (trim($result->error) != "") {
            $this->session->set_flashdata('error', $result->error);
            redirect('info/error');
        }
    
        $daftarhadir = $result->data;
        $this->data["hadir"] = $daftarhadir;
        $this->load->view("hadir/download2",$this->data);
    }

    public function detail($dhid){
		$this->data["page"] = "daftarhadir";

        $authorization = "Authorization: Bearer ".$this->session->userdata('token');
        $url = API_BASE_URL."hadir/".$dhid;
        $result = curl_api($url,'GET','',$authorization);
        if (trim($result->error) != "") {
            $this->session->set_flashdata('error', $result->error);
            redirect('info/error');
        }

		$employee_id = $this->session->userdata('employeeID');

		$this->data["employee_id"] = $employee_id;
    
        $daftarhadir = $result->data;
        $this->data["hadir"] = $daftarhadir;
        $this->load->view("hadir/detail",$this->data);
    }

    public function update($dhid){
        if(!$this->permission->event)
            redirect('hadir','refresh');

		$this->data["page"] = "daftarhadir";

        $authorization = "Authorization: Bearer ".$this->session->userdata('token');
        $url = API_BASE_URL."hadir/".$dhid;
        $result = curl_api($url,'GET','',$authorization);
        if (trim($result->error) != "") {
            $this->session->set_flashdata('error', $result->error);
            redirect('info/error');
        }
    
        $daftarhadir = $result->data;
        $this->data["hadir"] = $daftarhadir;

		$valueRuangMeeting = $daftarhadir->ruang_meeting;
		$this->data["tempat_meeting"] = null;
		$this->data["ruang_meeting_id"] = -1;

		if ($valueRuangMeeting == null) {
			$this->data["tempat_meeting"] = $daftarhadir->tempat;
		} else {
			$this->data["ruang_meeting_id"] = $valueRuangMeeting->id;
		}

        //echo $this->data["ruang_meeting_id"];

        $authorization = "Authorization: Bearer ".$this->session->userdata('token');
        $url = API_BASE_URL."employee/list/0/9999999";
        $result = curl_api($url,'GET','',$authorization);
        if (trim($result->error) != "") {
            $this->session->set_flashdata('error', $result->error);
            redirect('info/error');
        }

        $return = $result->data;
        $this->data["employee"] = $return->data;

		$url = API_BASE_URL."master/ruangmeeting/list";
		$result = curl_api($url,'GET','',$authorization);
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hadir_repopulate = $this->session->userdata('hadir_repopulate');
		$this->session->unset_userdata('hadir_repopulate');

		$return = $result->data;
		$this->data["ruang_meeting"] = $return->data;
		$this->data["hadir_repopulate"] = $hadir_repopulate;

        //echo $this->data["ruang_meeting_id"];

        $this->load->view("hadir/update",$this->data);
    }

    public function updatep(){
        if(!$this->permission->event)
            redirect('hadir','refresh');

		$this->data["page"] = "daftarhadir";

		// unset repopulate form from being kept
		$this->session->unset_userdata('hadir_repopulate');

        $this->load->library('form_validation');
        $this->form_validation->set_rules('tanggal', 'Tanggal', 'required');
        $this->form_validation->set_rules('kegiatan', 'Kegiatan', 'required');
        $this->form_validation->set_rules('pimpinan', 'Pimpinan', 'required');
        $this->form_validation->set_rules('subyek', 'Subyek', 'required');
		$this->form_validation->set_rules('ruang_meeting', 'Ruang Meeting', 'required');
        if ($this->form_validation->run() == FALSE)
        {
            $authorization = "Authorization: Bearer ".$this->session->userdata('token');
            $url = API_BASE_URL."hadir/".$dhid;
            $result = curl_api($url,'GET','',$authorization);
            if (trim($result->error) != "") {
                $this->session->set_flashdata('error', $result->error);
                redirect('info/error');
            }
        
            $daftarhadir = $result->data;
            $this->data["hadir"] = $daftarhadir;

            $authorization = "Authorization: Bearer ".$this->session->userdata('token');
            $url = API_BASE_URL."employee/list/0/9999999";
            $result = curl_api($url,'GET','',$authorization);
            if (trim($result->error) != "") {
                $this->session->set_flashdata('error', $result->error);
                redirect('info/error');
            }

            $return = $result->data;
            $this->data["employee"] = $return->data;

            $this->load->view('hadir/update',$this->data);
        }
        else{
            $dhid = $this->input->post('dhid');
            $kegiatan = $this->input->post('kegiatan');
            $pimpinan = $this->input->post('pimpinan');
            $tanggal = $this->input->post('tanggal');
            $subyek = $this->input->post('subyek');
            //$jam_mulai = $this->input->post('jam_mulai');
            //$menit_mulai = $this->input->post('menit_mulai');
            //$jam_selesai = $this->input->post('jam_selesai');
            //$menit_selesai = $this->input->post('menit_selesai');
            $tempat = $this->input->post('tempat');
            $keterangan = $this->input->post('keterangan');
            $notulis = $this->input->post('notulis');
            $waktu_mulai = $this->input->post('waktu_mulai');
            $waktu_selesai = $this->input->post('waktu_selesai');
			$ruang_meeting = $this->input->post('ruang_meeting');

			if ($ruang_meeting == "-1") {
				$ruang_meeting = null;
			} else if ($ruang_meeting != null) {
				$tempat = null;
			}
            
            $dtupload = json_encode(array(
				"kegiatan" => $kegiatan,
				"pimpinan" => $pimpinan,
                "subyek" => $subyek,
                "tanggal" => $tanggal,
                "waktu_mulai" => $waktu_mulai,
                "waktu_selesai" => $waktu_selesai,
                "daftar_id" => $dhid,
                "tempat" => $tempat,
                "notulis" => $notulis,
                "keterangan" => $keterangan,
				"ruang_meeting_id" => $ruang_meeting
			));

            //print_r($dtupload);

			$authorization = "Authorization: Bearer ".$this->session->userdata('token');
			$url = API_BASE_URL."hadir";
			$result = curl_api($url,'PUT',$dtupload,$authorization);

            //print_r($result);
			if ($result->data->msg != "SUCCESS") {
				if ($result->data->msg == "ERROR: RUANG_MEETING_ALREADY_BOOKED") {
					$result->data->msg = "Ruang meeting sudah terpakai pada waktu tersebut.";
				} else if ($result->data->msg == "ERROR: RUANG_MEETING_NOT_FOUND") {
					$result->data->msg = "Ruang meeting tidak ditemukan.";
				}
				$this->session->set_userdata('info', "Gagal mengupdate data kegiatan. Silahkan coba lagi. ".$result->data->msg);

				// repopulate form
				$hadir_repopulate = array(
					'tanggal' => $tanggal,
					'waktu_mulai' => $waktu_mulai,
					'waktu_selesai' => $waktu_selesai,
					'kegiatan' => $kegiatan,
					'subyek' => $subyek,
					'pimpinan' => $pimpinan,
					'notulis' => $notulis,
					'ruang_meeting' => $ruang_meeting,
                    "keterangan" => $keterangan,
					'tempat' => $tempat
				);
				$this->session->set_userdata('hadir_repopulate', $hadir_repopulate);

				redirect('hadir/update/'.$dhid,'refresh');
			} else if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
			else{
				$this->session->set_flashdata('success', 'Data kegiatan berhasil diupdate.');
				redirect('hadir','refresh');
			}
        }
    }

    public function delete(){
        if(!$this->permission->event)
            redirect('hadir','refresh');

		$this->data["page"] = "daftarhadir";
        if($this->session->flashdata('error') != NULL)
			$data['error'] = $this->session->flashdata('error');
		if($this->session->flashdata('info') != NULL)
			$data['info'] = $this->session->flashdata('info');

        $dhid = $this->input->post('dhid');
        $authorization = "Authorization: Bearer ".$this->session->userdata('token');
        $url = API_BASE_URL."hadir/".$dhid;
        $result = curl_api($url,'DELETE','',$authorization);
        
        if (trim($result->error) != "") {
            $this->session->set_flashdata('error', $result->error);
            redirect('info/error');
        }
        else{
            $this->session->set_flashdata('success', 'Data kegiatan berhasil dihapus.');
            redirect('hadir','refresh');
        }
    }

    public function risalah($dhid){
		$this->data["page"] = "daftarhadir";

        $authorization = "Authorization: Bearer ".$this->session->userdata('token');
        $url = API_BASE_URL."hadir/".$dhid;
        $result = curl_api($url,'GET','',$authorization);
        if (trim($result->error) != "") {
            $this->session->set_flashdata('error', $result->error);
            redirect('info/error');
        }
    
        $daftarhadir = $result->data;
        $this->data["hadir"] = $daftarhadir;

        $this->load->view("hadir/risalah",$this->data);
    }

    public function risalahp(){
		$this->data["page"] = "daftarhadir";

        $this->load->library('form_validation');
        $this->form_validation->set_rules('risalah', 'Risalah', 'required');
        if ($this->form_validation->run() == FALSE)
        {
            $authorization = "Authorization: Bearer ".$this->session->userdata('token');
            $url = API_BASE_URL."hadir/".$dhid;
            $result = curl_api($url,'GET','',$authorization);
            if (trim($result->error) != "") {
                $this->session->set_flashdata('error', $result->error);
                redirect('info/error');
            }
        
            $daftarhadir = $result->data;
            $this->data["hadir"] = $daftarhadir;

            $this->load->view("hadir/risalah",$this->data);
        }
        else{
            $dhid = $this->input->post('dhid');
            $risalah = $this->input->post('risalah');

            $dtupload = json_encode(array(
                "daftar_hadir_id" => $dhid,
                "risalah" => $risalah
            ));

            $authorization = "Authorization: Bearer ".$this->session->userdata('token');
            $url = API_BASE_URL."hadir/risalah";
            $result = curl_api($url,'PUT',$dtupload,$authorization);
            if (trim($result->error) != "") {
                $this->session->set_flashdata('error', $result->error);
                redirect('info/error');
            }
            else{
				$this->session->set_flashdata('success', 'Risalah berhasil disimpan.');
				redirect('hadir/risalah/'.$dhid,'refresh');
			}

        }
    }

    public function downloadr($dhid){
        $this->data["page"] = "daftarhadir";

        $authorization = "Authorization: Bearer ".$this->session->userdata('token');
        $url = API_BASE_URL."hadir/".$dhid;
        $result = curl_api($url,'GET','',$authorization);
        if (trim($result->error) != "") {
            $this->session->set_flashdata('error', $result->error);
            redirect('info/error');
        }
    
        $daftarhadir = $result->data;
        $this->data["hadir"] = $daftarhadir;
        $this->load->view("hadir/downloadr",$this->data);
    }

    public function finish($dhid){
        $authorization = "Authorization: Bearer ".$this->session->userdata('token');
        $dtupload = json_encode(array(
            "daftar_hadir_id" => $dhid,
            "waktu_selesai" => date('H:i')
        ));
        $url = API_BASE_URL."hadir/finish";
        $result = curl_api($url,'PUT',$dtupload,$authorization);
        
        if (trim($result->error) != "") {
            $this->session->set_flashdata('error', $result->error);
            redirect('info/error');
        }
        else{
            $this->session->set_flashdata('success', 'Kegiatan berhasil diselesaikan.');
            redirect('hadir','refresh');
        }
    }
}
