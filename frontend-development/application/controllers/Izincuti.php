<?php 

class Izincuti extends Sei_Controller {
    public function __construct()
    {
        parent::__construct();

    }
    public function jatahCuti(){
        if(!$this->permission->cuti_jatah)
            redirect('dashboard','refresh');

        $this->data["page"] = "jatahcuti";

        if ($this->input->post('tahun')) {
			$year = $this->input->post('tahun');
		} else {
			$year = date('Y');
		}
		$this->data['tahun'] = $year;

        // get data jatah cuti
        $url = API_BASE_URL."cuti/jatah/group/all/".$year;
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data->jatahCuti;
//        if(isset($hasil)){
//            $idx = 0;
//            foreach($hasil as $datajatah){
//                $url = API_BASE_URL."employee/".$datajatah->employeeId;
//                $result = curl_api($url,'GET','',$authorization);
//
//                if (trim($result->error) != "") {
//                    $this->session->set_flashdata('error', $result->error);
//                    redirect('info/error');
//                }
//                $hasil[$idx]->empcode = $result->data->employee_code;
//                $idx = $idx + 1;
//            }
//        }

		$this->data["listjatahcuti"] = $hasil;
        
        $this->load->view('izindancuti/input_jatah_cuti',$this->data);
	}

    public function pengajuan($page = 0){

        $this->data["page"] = "statuspengajuanizincuti";

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
        $emp = $this->session->userdata('empCode');

        // get data sisa cuti (dashboard)
        $url = API_BASE_URL."cuti/dashboard?empcode=".$emp;
		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

        $this->data["dashboard"] = $result->data;

        // get data cuti
		$url = API_BASE_URL."master/izincuti?empCode=".$emp;
		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

        $hasil = $result->data;
		$this->data["data_cuti"] = $hasil->izinCuti;

        $this->load->view('izindancuti/pengajuan_izin_cuti',$this->data);
    }

    public function ajukan(){
        $jenis = $this->input->post("jenis_cuti");
		$reason = $this->input->post("keterangan");
        $waktu = $this->input->post("jenis_pengajuan");

        $documents = [];
        if($_FILES['customFile']['tmp_name'][0] === "" || $_FILES['customFile']['tmp_name'][0] == null){
            $file_upload = array('documents'=> null);
        }else{
            $num_files = count($_FILES['customFile']['tmp_name']);
            for($i=0;$i<$num_files;$i++){
                if(is_uploaded_file($_FILES['customFile']['tmp_name'][$i])){
                    $documents[] = new CURLFile($_FILES['customFile']['tmp_name'][$i], $_FILES['customFile']['type'][$i],$_FILES['customFile']['name'][$i]);
                }
            }
            if($num_files == 1){
                $file_upload = array('documents'=> $documents[0]);
            }else if($num_files == 2){
                $file_upload = array('documents'=> $documents[0], 'documents'=> $documents[1]);
            }else if($num_files == 3){
                $file_upload = array('documents'=> $documents[0], 'documents'=> $documents[1], 'documents'=> $documents[2]);
            }else{
                $this->session->set_flashdata('info', 'File lebih dari 3. ');
			    redirect('Izincuti/pengajuan','refresh');
            }
        }

        if($waktu == "HARIAN"){
            $daterange = $this->input->post("daterange");

            $start = substr($daterange,0,10)."T00:00:00.00";
            $end = substr($daterange,13,23)."T23:00:00.00";
        }else{
            $datesingle = $this->input->post("datesingle");
            $timerange = $this->input->post("timerange");

            $start = $datesingle."T".substr($timerange,0,5).":00.00";
            $end = $datesingle."T".substr($timerange,8,13).":00.00";
        }

        $this->load->library('form_validation');
        $this->form_validation->set_rules('jenis_cuti', 'Jenis Cuti', 'required');
        $this->form_validation->set_rules('keterangan', 'Keterangan', 'required');

        if ($this->form_validation->run() == FALSE)
        {
            $this->session->set_flashdata('info', 'Data Kosong.');
			redirect('Izincuti/pengajuan','refresh');
        }
        else{
            $emp = $this->session->userdata('empCode');

            $curl = curl_init();

            curl_setopt_array($curl, array(
            CURLOPT_URL => API_BASE_URL.'cuti/request?empcode='.$emp.'&jenis='.$jenis.'&start='.$start.'&end='.$end.'&reason='.urlencode($reason),
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_ENCODING => '',
            CURLOPT_MAXREDIRS => 10,
            CURLOPT_TIMEOUT => 0,
            CURLOPT_FOLLOWLOCATION => true,
            CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
            CURLOPT_CUSTOMREQUEST => 'POST',
            CURLOPT_POSTFIELDS => $file_upload,
            CURLOPT_HTTPHEADER => array(
                'Authorization: Bearer '.$this->session->userdata('token'),
                'Content-Type: multipart/form-data'
            ),
            ));

            $response = curl_exec($curl);
            if(curl_errno($curl)){
                $error_msg = curl_error($curl);
            }
            curl_close($curl);

            $message = json_decode($response);
		
			if(isset($error_msg)){
			    $this->session->set_flashdata('error', $error_msg);
				redirect('info/error');
            }else{
                if($message->msg === "SUCCESS"){
                    $this->session->set_flashdata('info', 'Data berhasil diajukan.');
                    redirect('Izincuti/statuspengajuanizincuti','refresh');
                }else if($message->msg === "CUTI_OVERLAP"){
                    $this->session->set_flashdata('jenis_cuti', $jenis);
                    $this->session->set_flashdata('keterangan', $reason);
                    $this->session->set_flashdata('jenis_pengajuan', $waktu);
                    if ($waktu == "HARIAN") {
                        $this->session->set_flashdata('daterange', $daterange);
                    } else {
                        $this->session->set_flashdata('datesingle', $datesingle);
                        $this->session->set_flashdata('timerange', $timerange);
                    }
                    $this->session->set_flashdata('info', 'Gagal, tanggal cuti bentrok.');
                    redirect('Izincuti/pengajuan','refresh');
                }else{
                    $this->session->set_flashdata('jenis_cuti', $jenis);
                    $this->session->set_flashdata('keterangan', $reason);
                    $this->session->set_flashdata('jenis_pengajuan', $waktu);
                    if ($waktu == "HARIAN") {
                        $this->session->set_flashdata('daterange', $daterange);
                    } else {
                        $this->session->set_flashdata('datesingle', $datesingle);
                        $this->session->set_flashdata('timerange', $timerange);
                    }
                    $this->session->set_flashdata('info', $message->msg);
                    redirect('Izincuti/pengajuan','refresh');
                }
			}
        }
	}


    public function statuspengajuanizincuti(){

		$this->data["page"] = "statuspengajuanizincuti";

        $emp = $this->session->userdata('empCode');
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."cuti/list?empcode=".$emp;
		$result = curl_api($url,'GET','',$authorization);
        $url1 = API_BASE_URL."cuti/dashboard?empcode=".$emp;
		$result1 = curl_api($url1,'GET','',$authorization);

		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$return = $result->data;   

		$this->data["data"] = $return->data;
        $this->data["sisaCuti"]= $result1->data;
        $this->data["akumulasiIzin"]= $result1->data;
       
    
		$this->load->view('izindancuti/status_pengajuan_izin_cuti',$this->data);
	}

    public function detailstatuspengajuanizincuti($id_cuti){
        $this->data["page"] = "statuspengajuanizincuti";

        $authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."cuti/detail?izinCutiId=".$id_cuti;
		$result = curl_api($url,'GET','',$authorization);

        if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

        $this->data["status"] = $result->data;
        $this->data["startDate"] = $result->data;
        $this->data["endDate"] = $result->data;
        $this->data["reason"] = $result->data;
        $this->data["files"] = $result->data->files;
        $this->data["approvals"]= $result->data->approvals;
        $this->data["jenis"]= $result->data->jenis;
        $this->data["id"] = $result->data;
        // $this->data["reason"] = $result->reason;

        $this->load->view('izindancuti/detail_status_pengajuan_izin_cuti',$this->data);

    }

    public function cancel($id_cuti){
        $this->data["page"] = "statuspengajuanizincuti";

        $authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."cuti/cancel?izinCutiId=".$id_cuti;
		$result = curl_api($url,'GET','',$authorization);

        if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}else{
            $this->session->set_flashdata('info', 'Approval di cancel.');
            redirect('izincuti/statuspengajuanizincuti','refresh');
        }

    }

    public function approvalpengajuanizincuti($page = 0){
        if(!$this->permission->cuti_approve)
			redirect('dashboard','refresh');

        $this->data["page"] = "approvalpengajuanizincuti";

        $emp = $this->session->userdata('empCode');
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."cuti/requests?empcode=".$emp;
		$result = curl_api($url,'GET','',$authorization);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$return = $result->data;   

		$this->data["data"] = $return->data;

        $this->load->view('izindancuti/approval_pengajuan_izin_cuti',$this->data);

    }

    public function detailapprovalpengajuanizincuti($id_cuti){
        if(!$this->permission->cuti_approve)
			redirect('dashboard','refresh');

        $this->data["page"] = "approvalpengajuanizincuti";

        $authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."cuti/detail?izinCutiId=".$id_cuti;
		$result = curl_api($url,'GET','',$authorization);

        if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

        $this->data["id"] = $result->data;
        $this->data["employeeName"] = $result->data;
        $this->data["employeeCode"] = $result->data;
        $this->data["startDate"] = $result->data;
        $this->data["reason"] = $result->data;
        $this->data["status"] = $result->data;
        $this->data["files"] = $result->data->files;
        $this->data["approvals"]= $result->data->approvals;
        $this->data["jenis"]= $result->data->jenis;
        $this->load->view('izindancuti/detail_approval_pengajuan_izin_cuti',$this->data);
    }

    public function approve($idCuti, $reviewerCode){
        if(!$this->permission->cuti_approve)
			redirect('dashboard','refresh');

        $this->data["page"] = "approvalpengajuanizincuti";

        $authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."cuti/approve?izinCutiId=".$idCuti."&reviewerEmpCode=".$reviewerCode;
		$result = curl_api($url,'GET','',$authorization);

        if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}else{
            $this->session->set_flashdata('info', 'Approval di terima.');
            redirect('izincuti/approvalpengajuanizincuti','refresh');
        }
        
    }

    public function reject($idCuti, $reviewerCode) {
        if(!$this->permission->cuti_approve)
			redirect('dashboard','refresh');
        
        $alasan = $this->input->post('reason');
        $this->data["page"] = "approvalpengajuanizincuti";
    
        $authorization = "Authorization: Bearer ".$this->session->userdata('token');
        $url = API_BASE_URL."cuti/reject?izinCutiId=".$idCuti."&reviewerEmpCode=".$reviewerCode."&reason=".urlencode($alasan);
        $result = curl_api($url,'GET','',$authorization);
    
        if (trim($result->error) != "") {
            $this->session->set_flashdata('error', $result->error);
            redirect('info/error');
        } else {
            $this->session->set_flashdata('info', 'Approval di tolak.');
            redirect('izincuti/approvalpengajuanizincuti','refresh');
        }
    }
    
    
    
}

?>
