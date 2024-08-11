<?php 

class Reject extends Sei_Controller{
	public function __construct()
	{
		parent::__construct();
	}

	public function dpbj(){
		$nodpb = $this->input->post('nodpb');
		$alasan = $this->input->post('alasan');

		if(trim($alasan) == ""){
			$this->session->set_flashdata('error', "Alasan Tidak Boleh Kosong");
			redirect('info/error');
		}

		$this->data["page"] = "dashnav";

		$navid = $this->session->userdata('navid');
		$url = API_NAV_URL."apiDpbHead?\$filter=No%20eq%20'".$nodpb."'%20and%20Need_Approve_User_Id%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$dpbheadcek = $result->data;
			$count = count($dpbheadcek->value);
			if($count != 1)
				redirect('nav/dash');

			$dpbhead = $result->data->value[0];
			$urutanapprove = $dpbhead->UrutanApproveKe;
		}

		switch($urutanapprove){
			case 1: $this->rejectDPBSatu($dpbhead,$alasan);
				break;
		}
	}

	public function order() {
		$orderid = $this->input->post('orderid');
		$alasan = $this->input->post('alasan');
		$pid = $this->session->userdata('employeeID');

		$dtupload = json_encode(array(
			"order_id" => $orderid,
			"alasan" => $alasan,
			"reject_id" => $pid
		));

		$url = API_BASE_URL . "order/reject";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$result = curl_api($url,'PUT',$dtupload,$authorization);
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		else{
			$this->session->set_flashdata('info', 'Document Order Kendaraan '.$orderid.' ditolak.');
			redirect('order/na');
		}
	}

	public function budget(){
		$nodoc = $this->input->post('nodoc');
		
		$this->data["page"] = "dashnav";

		$alasan = $this->input->post('alasan');

		$navid = $this->session->userdata('navid');
		$url = API_NAV_URL."apiBudgetPerubahan?\$filter=No%20eq%20'".$nodoc."'%20and%20Need_Approve_User_Id%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$dpbheadcek = $result->data;
			$count = count($dpbheadcek->value);
			if($count != 1)
				redirect('nav/dash');

			$dochead = $result->data->value[0];
			$urutanapprove = $dochead->UrutanApproveKe;
		}

		switch($urutanapprove){
			case 0: $this->rejectBudgetNol($dochead,$alasan);
				break;
		}
	}

	private function rejectBudgetNol($dochead,$alasan){
		$navid = $this->session->userdata('navid');
		
		//get last approve log number
		$lastaplog = $this->getLastNoApproveLog();
		$lastaplog ++;

		//get last no urut approve
		$nourutapprove = $this->getLastUrutanApproveLog($dochead->No);
		$nourutapprove++;
		//echo $nourutapprove;

		date_default_timezone_set("UTC");
		$datalog = array(
			"Entry_No" => $lastaplog,
			"Process_Document" => "BGT",
			"Document_No" => $dochead->No,
			"Type_Process" => "Reject",
			"From_ID" => $navid,
			"Notes" => "Office Apps -> reject from:".$navid.", urutan approve ke:0",
			"Urutan_No" => $nourutapprove,
			"Entry_Date_Time" => date('Y-m-d')."T".date('H:i:s.v')."Z",
			"Job_Task" => "Budget Entry",
			"ApproveReject_Id" => "FIN-04",
			"Alasan_Reject" => $alasan
		);

		$databud = array(
			"Status" => "Open",
			"Approve_User_Pemohon" => "",
			"Approve_User_Anggaran" => "",
			"Approve_User_Dirut" => "",
			"Need_Approve_User_Id" => "",
			"Approved" => false
		);

		$url = API_NAV_URL."Approve_Log";
		$result = curl_api_nav($url,'POST',json_encode($datalog),NAV_USERNAME,NAV_PASSWORD);
		//print_r($datadpb);

		if($result->httpcode == 201){
			//echo "test";
			$urlapprove = API_NAV_URL."apiBudgetPerubahan(Type_Table='Header',Line_No=0,No='".$dochead->No."')";
			//echo $urlapprove;
			$resultbud = curl_api_nav($urlapprove,'PUT',json_encode($databud),NAV_USERNAME,NAV_PASSWORD);
			if($resultbud->httpcode == 200){
				$this->session->set_flashdata('info', "Document No: ".$dochead->No." sudah direject.");
				redirect('info/show');
			}
			else{
				if (trim($resultbud->error) != "") {
				    $this->session->set_flashdata('error', $resultbud->error);
					redirect('info/error');
				}
			}
		}
		else{
			//echo $result->httpcode;
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
		}
	}

	private function rejectDPBSatu($dpbhead,$alasan){
		$pemohonPimpro = $dpbhead->Pemohon_PimPro;
		$admin = $dpbhead->Assigned_User_ID;
		$navid = $this->session->userdata('navid');

		//get last approve log number
		$lastaplog = $this->getLastNoApproveLog();
		$lastaplog ++;

		//get last no urut approve
		$nourutapprove = $this->getLastUrutanApproveLog($dpbhead->No);
		$nourutapprove++;
		//echo $nourutapprove;

		date_default_timezone_set("UTC");
		$datalog = array(
			"Entry_No" => $lastaplog,
			"Process_Document" => "DPB",
			"Document_No" => $dpbhead->No,
			"Type_Process" => "Reject",
			"From_ID" => $navid,
			"Notes" => "Office Apps -> reject from:".$navid.", urutan approve ke:1",
			"Urutan_No" => $nourutapprove,
			"ApproveReject_Id" => $admin,
			"Entry_Date_Time" => date('Y-m-d')."T".date('H:i:s.v')."Z",
			"Job_Task" => "Daftar Permintaan Barang Dan Jasa",
			"Alasan_Reject" => $alasan
		);

		$datadpb = array(
			"Released" => false,
			"Approved" => false,
			"UrutanApproveKe" => 0,
			"Status" => "Open",
			"Need_Approve_User_Id" => "",
			"Approve_User_Pemohon" => "",
			"Approve_User_Anggaran" => "",
			"Approve_User_Kabag_Keuangan" => "",
			"Approve_User_Kabag_Logistik" => "",
			"Approve_User_Kadept_Logistik" => "",
			"Approve_User_SEGM_1" => "",
			"Approve_User_DirOps" => "",
			"Approve_User_Dirut" => ""
		);

		$url = API_NAV_URL."Approve_Log";
		$result = curl_api_nav($url,'POST',json_encode($datalog),NAV_USERNAME,NAV_PASSWORD);
		//print_r($datadpb);

		if($result->httpcode == 201){
			//echo "test";
			$urlapprove = API_NAV_URL."apiDpbHead(Document_Type='Quote',No='".$dpbhead->No."')";
			//echo $urlapprove;
			$resultdpb = curl_api_nav($urlapprove,'PUT',json_encode($datadpb),NAV_USERNAME,NAV_PASSWORD);
			if($resultdpb->httpcode == 200){
				$this->session->set_flashdata('info', "Document No: ".$dpbhead->No." sudah direject.");
				redirect('info/show');
			}
			else{
				if (trim($resultdpb->error) != "") {
				    $this->session->set_flashdata('error', $resultdpb->error);
					redirect('info/error');
				}
			}
		}
		else{
			//echo $result->httpcode;
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
		}
	}

	public function bapb(){
		$nodoc = $this->input->post('nodoc');
		
		$this->data["page"] = "dashnav";

		$alasan = $this->input->post('alasan');
		$navid = $this->session->userdata('navid');
		$url = API_NAV_URL."apiBAPBHead?\$filter=No%20eq%20'".$nodoc."'%20and%20Need_Approve_User_Id%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$dpbheadcek = $result->data;
			$count = count($dpbheadcek->value);
			if($count != 1)
				redirect('nav/dash');

			$dochead = $result->data->value[0];
			$urutanapprove = $dochead->UrutanApproveKe;
		}

		switch($urutanapprove){
			case 2: $this->rejectBapbDua($dochead,$alasan);
				break;
		}
	}

	private function rejectBapbDua($dochead,$alasan){
		$navid = $this->session->userdata('navid');

		//get last approve log number
		$lastaplog = $this->getLastNoApproveLog();
		$lastaplog ++;

		//get last no urut approve
		$nourutapprove = $this->getLastUrutanApproveLog($dochead->No);
		$nourutapprove++;
		//echo $nourutapprove;

		date_default_timezone_set("UTC");
		$datalog = array(
			"Entry_No" => $lastaplog,
			"Process_Document" => "BAPB",
			"Document_No" => $dochead->No,
			"Type_Process" => "Reject",
			"From_ID" => $navid,
			"Notes" => "Office Apps -> reject from:".$navid.", urutan approve ke:2",
			"Urutan_No" => $nourutapprove,
			"Entry_Date_Time" => date('Y-m-d')."T".date('H:i:s.v')."Z",
			"Job_Task" => "Penerimaan Barang dan Jasa",
			"Alasan_Reject" => $alasan
		);

		$databud = array(
			"Status" => "Open",
			"Need_Approve_User_Id" => "",
			"Approved" => false,
			"UrutanApproveKe" => 0,
			"Approve_Kabag_Logistik" => "",
			"Approve_Menerima" => ""
		);

		$url = API_NAV_URL."Approve_Log";
		$result = curl_api_nav($url,'POST',json_encode($datalog),NAV_USERNAME,NAV_PASSWORD);
		//print_r($datadpb);

		if($result->httpcode == 201){
			//echo "test";
			$urlapprove = API_NAV_URL."apiBAPBHead(Type_Table='Header',Line_No=0,No='".$dochead->No."')";
			//echo $urlapprove;
			$resultbud = curl_api_nav($urlapprove,'PUT',json_encode($databud),NAV_USERNAME,NAV_PASSWORD);
			if($resultbud->httpcode == 200){
				$this->session->set_flashdata('info', "Document No: ".$dochead->No." sudah direject.");
				redirect('info/show');
			}
			else{
				if (trim($resultbud->error) != "") {
				    $this->session->set_flashdata('error', $resultbud->error);
					redirect('info/error');
				}
			}
		}
		else{
			//echo $result->httpcode;
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
		}
	}

	public function sppd(){
		$nodoc = $this->input->post('nodoc');
		
		$this->data["page"] = "dashnav";

		$navid = $this->session->userdata('navid');
		$alasan = $this->input->post('alasan');
		$url = API_NAV_URL."api_sppd_list?\$filter=No%20eq%20'".$nodoc."'%20and%20Need_Approve_User_Id%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$dpbheadcek = $result->data;
			$count = count($dpbheadcek->value);
			if($count != 1)
				redirect('nav/dash');

			$dochead = $result->data->value[0];
			$urutanapprove = $dochead->UrutanApproveKe;
		}

		switch($urutanapprove){
			case 1: $this->rejectSppdSatu($dochead,$alasan);
				break;
		}
	}

	private function rejectSppdSatu($dochead,$alasan){
		$navid = $this->session->userdata('navid');
		$useridfilter = $dochead->User_Id_Filter;

		//get last approve log number
		$lastaplog = $this->getLastNoApproveLog();
		$lastaplog ++;

		//get last no urut approve
		$nourutapprove = $this->getLastUrutanApproveLog($dochead->No);
		$nourutapprove++;
		//echo $nourutapprove;

		date_default_timezone_set("UTC");
		$datalog = array(
			"Entry_No" => $lastaplog,
			"Process_Document" => "SPJ",
			"Document_No" => $dochead->No,
			"Type_Process" => "Reject",
			"From_ID" => $navid,
			"Notes" => "Office Apps -> reject from:".$navid.", urutan approve ke:1",
			"Urutan_No" => $nourutapprove,
			"Entry_Date_Time" => date('Y-m-d')."T".date('H:i:s.v')."Z",
			"Job_Task" => "Pengajuan Perjalanan Dinas (K-32)",
			"ApproveReject_Id" => $useridfilter,
			"Alasan_Reject" => $alasan
		);

		$databud = array(
			"Status" => "Open",
			"Need_Approve_User_Id" => "",
			"UrutanApproveKe" => 0,
			"Approve_Pemohon_K_32" => "",
			"Approve_Menyetujui_K_32" => "",
			"Approve_Menyiapkan_Staff_SDM" => "",
			"Approve_KaBag_SDM_K_33" => "",
			"Approve_DirOps_K_34" => "",
			"Approve_User_Anggaran" => "",
			"Approve_User_Kabag_Keuangan" => "",
			"Approve_User_Kabag_Accounting" => "",
			"Approve_User_Kadept_Keuangan" => "",
			"Approve_User_SEGM_1" => "",
			"Approve_User_DirOps" => "",
			"Approve_User_Dirut" => "",
			"Approved_Anggaran" => false
		);

		$url = API_NAV_URL."Approve_Log";
		$result = curl_api_nav($url,'POST',json_encode($datalog),NAV_USERNAME,NAV_PASSWORD);
		//print_r($datadpb);

		if($result->httpcode == 201){
			//echo "test";
			$urlapprove = API_NAV_URL."api_sppd_list(No='".$dochead->No."')";
			//echo $urlapprove;
			$resultbud = curl_api_nav($urlapprove,'PUT',json_encode($databud),NAV_USERNAME,NAV_PASSWORD);
			if($resultbud->httpcode == 200){
				$this->session->set_flashdata('info', "Document No: ".$dochead->No." sudah direject.");
				redirect('info/show');
			}
			else{
				if (trim($resultbud->error) != "") {
				    $this->session->set_flashdata('error', $resultbud->error);
					redirect('info/error');
				}
			}
		}
		else{
			//echo $result->httpcode;
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
		}
	}

	public function um(){
		$nodoc = $this->input->post('nodoc');
		
		$this->data["page"] = "dashnav";

		$navid = $this->session->userdata('navid');
		$alasan = $this->input->post('alasan');
		$url = API_NAV_URL."apiUM?\$filter=Nomor%20eq%20'".$nodoc."'%20and%20Need_Approve_User_Id%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$docheadcek = $result->data;
			$count = count($docheadcek->value);
			if($count != 1)
				redirect('nav/dash');

			$dochead = $result->data->value[0];
			$urutanapprove = $dochead->UrutanApproveKe;
		}

		switch($urutanapprove){
			case 1: $this->rejectUmSatu($dochead,$alasan);
				break;
		}
	}

	private function rejectUmSatu($dochead,$alasan){
		$navid = $this->session->userdata('navid');
		$useridfilter = $dochead->User_Id_Filter;

		//get last approve log number
		$lastaplog = $this->getLastNoApproveLog();
		$lastaplog ++;

		//get last no urut approve
		$nourutapprove = $this->getLastUrutanApproveLog($dochead->Nomor);
		$nourutapprove++;
		//echo $nourutapprove;

		date_default_timezone_set("UTC");
		$datalog = array(
			"Entry_No" => $lastaplog,
			"Process_Document" => "UMPG",
			"Document_No" => $dochead->Nomor,
			"Type_Process" => "Reject",
			"From_ID" => $navid,
			"Notes" => "Office Apps -> reject from:".$navid.", urutan approve ke:1",
			"Urutan_No" => $nourutapprove,
			"Entry_Date_Time" => date('Y-m-d')."T".date('H:i:s.v')."Z",
			"Job_Task" => "Permohonan Uang Muka (K-4)",
			"ApproveReject_Id" => $useridfilter,
			"Alasan_Reject" => $alasan
		);

		$databud = array(
			"Status" => "Open",
			"Need_Approve_User_Id" => "",
			"UrutanApproveKe" => 0,
			"Approve_User_Pemohon" => "",
			"Approve_User_Menyetujui" => "",
			"Approve_User_Mengetahui" => "",
			"Approve_User_Anggaran" => "",
			"Approve_User_Kabag_Keuangan" => "",
			"Approve_User_Kabag_Accounting" => "",
			"Approve_User_Kadept_Keuangan" => "",
			"Approve_User_SEGM_1" => "",
			"Approve_User_DirOps" => "",
			"Approve_User_Dirut" => "",
			"Approved_Anggaran" => false
		);

		$url = API_NAV_URL."Approve_Log";
		$result = curl_api_nav($url,'POST',json_encode($datalog),NAV_USERNAME,NAV_PASSWORD);
		//print_r($datadpb);

		if($result->httpcode == 201){
			//echo "test";
			$urlapprove = API_NAV_URL."apiUM(Type_Table='Header',Line_No=0,Nomor='".$dochead->Nomor."')";
			//echo $urlapprove;
			$resultbud = curl_api_nav($urlapprove,'PUT',json_encode($databud),NAV_USERNAME,NAV_PASSWORD);
			if($resultbud->httpcode == 200){
				$this->session->set_flashdata('info', "Document No: ".$dochead->Nomor." sudah direject.");
				redirect('info/show');
			}
			else{
				if (trim($resultbud->error) != "") {
				    $this->session->set_flashdata('error', $resultbud->error);
					redirect('info/error');
				}
			}
		}
		else{
			//echo $result->httpcode;
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
		}
	}

	public function pjum(){
		$nodoc = $this->input->post('nodoc');
		
		$this->data["page"] = "dashnav";

		$navid = $this->session->userdata('navid');
		$alasan = $this->input->post('alasan');
		$url = API_NAV_URL."apiPJUM?\$filter=No%20eq%20'".$nodoc."'%20and%20Need_Approve_User_Id%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$docheadcek = $result->data;
			$count = count($docheadcek->value);
			if($count != 1)
				redirect('nav/dash');

			$dochead = $result->data->value[0];
			$urutanapprove = $dochead->UrutanApproveKe;
		}

		switch($urutanapprove){
			case 0: $this->rejectUmNol($dochead,$alasan);
				break;
		}
	}

	private function rejectUmNol($dochead,$alasan){
		$navid = $this->session->userdata('navid');
		$useridfilter = $dochead->User_ID_Filter;

		//get last approve log number
		$lastaplog = $this->getLastNoApproveLog();
		$lastaplog ++;

		//get last no urut approve
		$nourutapprove = $this->getLastUrutanApproveLog($dochead->No);
		$nourutapprove++;
		//echo $nourutapprove;

		date_default_timezone_set("UTC");
		$datalog = array(
			"Entry_No" => $lastaplog,
			"Process_Document" => "PJUM",
			"Document_No" => $dochead->No,
			"Type_Process" => "Reject",
			"From_ID" => $navid,
			"Notes" => "Office Apps -> reject from:".$navid.", urutan approve ke:0",
			"Urutan_No" => $nourutapprove,
			"Entry_Date_Time" => date('Y-m-d')."T".date('H:i:s.v')."Z",
			"ApproveReject_Id" => $useridfilter,
			"Alasan_Reject" => $alasan
		);

		$databud = array(
			"Status" => "Open",
			"Need_Approve_User_Id" => "",
			"UrutanApproveKe" => 0,
			"Approve_User_Pemohon" => "",
			"Disetujui_Oleh" => "",
			"Approve_User_Anggaran" => "",
			"Approve_User_Kabag_Keuangan" => "",
			"Approve_User_Kabag_Accounting" => "",
			"Approve_User_Kadept_Keuangan" => "",
			"Approve_User_SEGM_1" => "",
			"Approved_Anggaran" => false
		);

		$url = API_NAV_URL."Approve_Log";
		$result = curl_api_nav($url,'POST',json_encode($datalog),NAV_USERNAME,NAV_PASSWORD);
		//print_r($datadpb);

		if($result->httpcode == 201){
			//echo "test";
			$urlapprove = API_NAV_URL."apiPJUM(Type_Table='Header',Line_No=0,No='".$dochead->No."')";
			//echo $urlapprove;
			$resultbud = curl_api_nav($urlapprove,'PUT',json_encode($databud),NAV_USERNAME,NAV_PASSWORD);
			if($resultbud->httpcode == 200){
				$this->session->set_flashdata('info', "Document No: ".$dochead->No." sudah direject.");
				redirect('info/show');
			}
			else{
				if (trim($resultbud->error) != "") {
				    $this->session->set_flashdata('error', $resultbud->error);
					redirect('info/error');
				}
			}
		}
		else{
			//echo $result->httpcode;
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
		}
	}

	private function getLastUrutanApproveLog($nodoc){
		$url = API_NAV_URL."Approve_Log?\$filter=Document_No%20eq%20'".urlencode($nodoc)."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200 && isset($result->data->value)){
			$nomor = 0;
			foreach ($result->data->value as $dt) {
				$nourut = $dt->Urutan_No;
				if($nourut > $nomor)
					$nomor = $nourut;
			}
			return $nomor;
		}
		else{
			return 0;
		}
	}

	private function getLastNoApproveLog(){
		$url = API_NAV_URL."Approve_Log?\$top=1";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200 && isset($result->data->value[0])){
			return $result->data->value[0]->Entry_No;
		}
		else
			return 0;
	}
}
