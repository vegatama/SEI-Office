<?php 

class Approve extends Sei_Controller{
	public function __construct()
	{
		parent::__construct();
	}

	public function dpbj(){
		$nodpb = $this->input->post('nodpb');
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
			case 1: $this->approveDPBSatu($dpbhead);
				break;
		}
	}

	public function budget(){
		$nodoc = $this->input->post('nodoc');
		$this->data["page"] = "dashnav";

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
			case 0: $this->approveBudgetNol($dochead);
				break;
		}
	}

	public function bapb(){
		$nodoc = $this->input->post('nodoc');
		$this->data["page"] = "dashnav";

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
			case 2: $this->approveBapbDua($dochead);
				break;
		}
	}

	public function sppd(){
		$nodoc = $this->input->post('nodoc');
		$this->data["page"] = "dashnav";

		$navid = $this->session->userdata('navid');
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
			case 1: $this->approveSppdSatu($dochead);
				break;
		}
	}

	public function um(){
		$nodoc = $this->input->post('nodoc');		
		$this->data["page"] = "dashnav";

		$navid = $this->session->userdata('navid');
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
			case 1: $this->approveUmSatu($dochead);
				break;
		}
	}

	private function approveUmSatu($dochead){
		$navid = $this->session->userdata('navid');
		$disetujui = $dochead->Disetujui_Oleh;

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
			"Type_Process" => "Approved",
			"From_ID" => $navid,
			"Notes" => "Office Apps -> approval from:".$navid.", urutan approve ke:1",
			"Urutan_No" => $nourutapprove,
			"Entry_Date_Time" => date('Y-m-d')."T".date('H:i:s.v')."Z",
			"Job_Task" => "Permohonan Uang Muka (K-4)",
			"ApproveReject_Id" => $disetujui
		);

		$databud = array(
			"Approve_User_Pemohon" => $navid,
			"Need_Approve_User_Id" => $disetujui,
			"UrutanApproveKe" => 2
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
				$this->session->set_flashdata('info', "Document No: ".$dochead->Nomor." sudah direquest Approval kepada ".$disetujui);
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
			case 0: $this->approvePjumNol($dochead);
				break;
		}
	}

	private function approvePjumNol($dochead){
		$navid = $this->session->userdata('navid');
		$disetujui = $dochead->Disetujui_Oleh;

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
			"Type_Process" => "Approved",
			"From_ID" => $navid,
			"Notes" => "Office Apps -> approval from:".$navid.", urutan approve ke:0",
			"Urutan_No" => $nourutapprove,
			"Entry_Date_Time" => date('Y-m-d')."T".date('H:i:s.v')."Z",
			"ApproveReject_Id" => $disetujui
		);

		$databud = array(
			"Approve_User_Pemohon" => $navid,
			"Need_Approve_User_Id" => $disetujui,
			"UrutanApproveKe" => 1
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
				$this->session->set_flashdata('info', "Document No: ".$dochead->No." sudah direquest Approval kepada ".$disetujui);
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

	private function approveSppdSatu($dochead){
		$navid = $this->session->userdata('navid');
		$disetujui = $dochead->Disetujui_Oleh;

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
			"Type_Process" => "Approved",
			"From_ID" => $navid,
			"Notes" => "Office Apps -> approval from:".$navid.", urutan approve ke:1",
			"Urutan_No" => $nourutapprove,
			"Entry_Date_Time" => date('Y-m-d')."T".date('H:i:s.v')."Z",
			"Job_Task" => "Pengajuan Perjalanan Dinas (K-32)",
			"ApproveReject_Id" => $disetujui
		);

		$databud = array(
			"Approve_Pemohon_K_32" => $navid,
			"Need_Approve_User_Id" => $disetujui,
			"Approved_Date_Time_1" => date('Y-m-d')."T".date('H:i:s.v')."Z",
			"UrutanApproveKe" => 2
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
				$this->session->set_flashdata('info', "Document No: ".$dochead->No." sudah direquest Approval kepada ".$disetujui);
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

	private function approveBapbDua($dochead){
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
			"Type_Process" => "Released",
			"From_ID" => $navid,
			"Notes" => "Office Apps -> approval from:".$navid.", urutan approve ke:2",
			"Urutan_No" => $nourutapprove,
			"Entry_Date_Time" => date('Y-m-d')."T".date('H:i:s.v')."Z",
			"Job_Task" => "Penerimaan Barang dan Jasa"
		);

		$databud = array(
			"Status" => "Released",
			"Approve_Menerima" => $navid,
			"Need_Approve_User_Id" => "",
			"Approve_Menerima_Date" => date('Y-m-d')."T".date('H:i:s.v')."Z",
			"Approved" => true
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
				$this->session->set_flashdata('info', "Document No: ".$dochead->No." sudah diapprove.");
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

	private function approveBudgetNol($dochead){
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
			"Type_Process" => "Approved",
			"From_ID" => $navid,
			"Notes" => "Office Apps -> approval from:".$navid.", urutan approve ke:0",
			"Urutan_No" => $nourutapprove,
			"Entry_Date_Time" => date('Y-m-d')."T".date('H:i:s.v')."Z",
			"Job_Task" => "Budget Entry",
			"ApproveReject_Id" => "CSP-01"
		);

		$databud = array(
			"Approve_User_Pemohon" => $navid,
			"UrutanApproveKe" => 1,
			"Need_Approve_User_Id" => "CSP-01"
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
				$this->session->set_flashdata('info', "Document No: ".$dochead->No." sudah direquest Approval kepada: ".$databud["Need_Approve_User_Id"]);
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

	private function approveDPBSatu($dpbhead){
		$pemohonPimpro = $dpbhead->Pemohon_PimPro;

		//get jabatan in user setup
		$navid = $this->session->userdata('navid');
		$url = API_NAV_URL."UserSetup?\$filter=User_ID%20eq%20'".$pemohonPimpro."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$datausersetup = $result->data->value[0];
			$jabatan = $datausersetup->User_Jabatan;
			$dept = $datausersetup->User_Department;
		}

		//get kadept
		$url = API_NAV_URL."UserSetup?\$filter=User_Jabatan%20eq%20'Ka.Dept'%20and%20User_Department%20eq%20'".urlencode($dept)."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200 && isset($result->data->value[0])){
			$datausersetupkadept = $result->data->value[0];
			$kadept = $datausersetupkadept->User_ID;
		}
		else{
			$kadept = "";
		}

		//get atasan
		$url = API_NAV_URL."apiEmployee?\$filter=User_Id%20eq%20'".$pemohonPimpro."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200 && isset($result->data->value[0])){
			$datausersetupatasan = $result->data->value[0];
			$atasan = $datausersetupatasan->Atasan_User_Id;
		}
		else{
			$atasan = "";
		}

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
			"Type_Process" => "Approved",
			"From_ID" => $navid,
			"Notes" => "Office Apps -> approval from:".$navid.", urutan approve ke:1",
			"Urutan_No" => $nourutapprove,
			"Entry_Date_Time" => date('Y-m-d')."T".date('H:i:s.v')."Z",
			"Job_Task" => "Daftar Permintaan Barang Dan Jasa"
		);

		$datadpb = array(
			"Approve_User_Pemohon" => $navid,
			"UrutanApproveKe" => 2
		);

		if(trim($jabatan) == "" || $jabatan == "Staff" || $jabatan == "Ka.Bag"){			
			if(trim($kadept) != ""){
				$datalog["ApproveReject_Id"] = $kadept;
				$datadpb["Need_Approve_User_Id"] = $kadept;
			}
			else{
				$datalog["ApproveReject_Id"] = $atasan;
				$datadpb["Need_Approve_User_Id"] = $atasan;
			}
		}
		else{			
			$datalog["ApproveReject_Id"] = $atasan;
			$datadpb["Need_Approve_User_Id"] = $atasan;
		}

		$url = API_NAV_URL."Approve_Log";
		$result = curl_api_nav($url,'POST',json_encode($datalog),NAV_USERNAME,NAV_PASSWORD);
		//print_r($datadpb);

		if($result->httpcode == 201){
			//echo "test";
			$urlapprove = API_NAV_URL."apiDpbHead(Document_Type='Quote',No='".$dpbhead->No."')";
			//echo $urlapprove;
			$resultdpb = curl_api_nav($urlapprove,'PUT',json_encode($datadpb),NAV_USERNAME,NAV_PASSWORD);
			if($resultdpb->httpcode == 200){
				$this->session->set_flashdata('info', "Document No: ".$dpbhead->No." sudah direquest Approval kepada: ".$datadpb["Need_Approve_User_Id"]);
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

	public function order(){
		$oid = $this->input->post("order_id");

		if ($this->input->post("approval") != null) {
			$vehicle_id = $this->input->post("kendaraan");
			if ($vehicle_id == -1 || $vehicle_id == '-1') {
				$dtupload = json_encode(array(
					"order_id" => $oid,
					"approval_id" => $this->session->userdata('employeeID'),
					"assignedDriverName" => $this->input->post("driver"),
					"assignedDriverPhone" => $this->input->post("no_hp_driver"),
					"assignedOtherPlatNumber" => $this->input->post("other_plat_number"),
					"assignedOtherMerk" => $this->input->post("other_merk"),
					"assignedOtherTipe" => $this->input->post("other_tipe"),
					"assignedOtherTahun" => $this->input->post("other_tahun"),
					"assignedOtherBBM" => $this->input->post("other_bbm"),
					"assignedOtherPemilik" => $this->input->post("other_pemilik"),
					"assignedOtherPKB" => $this->input->post("other_pkb"),
					"assignedOtherKeterangan" => $this->input->post("other_keterangan"),
				));
			} else {
				$dtupload = json_encode(array(
					"order_id" => $oid,
					"approval_id" => $this->session->userdata('employeeID'),
					"assignedVehicleId" => $vehicle_id,
					"assignedDriverName" => $this->input->post("driver"),
					"assignedDriverPhone" => $this->input->post("no_hp_driver")
				));
			}
		} else {
			$dtupload = json_encode(array(
				"order_id" => $oid,
				"approval_id" => $this->session->userdata('employeeID')
			));
		}

		$url = API_BASE_URL."order/approve";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		//print_r($dtupload);
		$result = curl_api($url,'PUT',$dtupload,$authorization);
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->data->msg);
			redirect('info/error');
		}
		else{
			$this->session->set_flashdata('info', 'Document Order Kendaraan '.$oid.' sudah berhasil diapprove.');
			redirect('order/na');
		}
	}
}
