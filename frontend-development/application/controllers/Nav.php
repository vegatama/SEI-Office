<?php

class Nav extends Sei_Controller{
	public function __construct()
	{
		parent::__construct();
	}

	public function dash(){
		if(!$this->permission->nav_dashboard)
			redirect('dashboard','refresh');

		$this->data["page"] = "dashnav";

		$navid = $this->session->userdata('navid');

		//get atasan
		$url = API_NAV_URL."apiEmployee?\$filter=User_Id%20eq%20'".$navid."'";
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
		
		//get data dpb
		$url = API_NAV_URL."apiDpbHead?\$filter=Need_Approve_User_Id%20eq%20'".$navid."'%20and%20Additional_Type%20eq%20'Barang'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$hasil = $result->data;
			$this->data["dpb"] = count($hasil->value);
		}

		$url = API_NAV_URL."apiDpbHead?\$filter=Pemohon_PimPro%20eq%20'".$navid."'%20and%20Additional_Type%20eq%20'Barang'%20and%20(Approve_User_Pemohon%20eq%20'".$navid."'%20or%20Approve_User_Pemohon%20eq%20'".$atasan."')%20and%20(Status%20eq%20'Released'%20or%20Status%20eq%20'Pending%20Approval')";
		//echo $url;
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$hasil = $result->data;
			$this->data["dpba"] = count($hasil->value);
		}

		//get data dpj
		$url = API_NAV_URL."apiDpbHead?\$filter=Need_Approve_User_Id%20eq%20'".$navid."'%20and%20Additional_Type%20eq%20'Jasa'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$hasil = $result->data;
			$this->data["dpj"] = count($hasil->value);
		}

		$url = API_NAV_URL."apiDpbHead?\$filter=Pemohon_PimPro%20eq%20'".$navid."'%20and%20Additional_Type%20eq%20'Jasa'%20and%20(Approve_User_Pemohon%20eq%20'".$navid."'%20or%20Approve_User_Pemohon%20eq%20'".$atasan."')%20and%20(Status%20eq%20'Released'%20or%20Status%20eq%20'Pending%20Approval')";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$hasil = $result->data;
			$this->data["dpja"] = count($hasil->value);
		}

		//get data budget perubahan
		$url = API_NAV_URL."apiBudgetPerubahan?\$filter=Need_Approve_User_Id%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);

		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$hasil = $result->data;
			$this->data["budget"] = count($hasil->value);
		}
		
		$url = API_NAV_URL."apiBudgetPerubahan?\$filter=Approve_User_Pemohon%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);

		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$hasil = $result->data;
			$this->data["budgeta"] = count($hasil->value);
		}

		//get data bapb
		$url = API_NAV_URL."apiBAPBHead?\$filter=Need_Approve_User_Id%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);

		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$hasil = $result->data;
			$this->data["bapb"] = count($hasil->value);
		}

		$url = API_NAV_URL."apiBAPBHead?\$filter=Approve_Menerima%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);

		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$hasil = $result->data;
			$this->data["bapba"] = count($hasil->value);
		}

		//get data sppd
		$url = API_NAV_URL."api_sppd_list?\$filter=Need_Approve_User_Id%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);

		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$hasil = $result->data;
			$this->data["sppd"] = count($hasil->value);
		}

		$url = API_NAV_URL."api_sppd_list?\$filter=Approve_Pemohon_K_32%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);

		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$hasil = $result->data;
			$this->data["sppda"] = count($hasil->value);
		}

		//get data UM
		$url = API_NAV_URL."apiUM?\$filter=Need_Approve_User_Id%20eq%20'".$navid."'%20and%20From_Logistik%20eq%20false";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);

		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$hasil = $result->data;
			$this->data["uangmuka"] = count($hasil->value);
		}

		$url = API_NAV_URL."apiUM?\$filter=Approve_User_Pemohon%20eq%20'".$navid."'%20and%20From_Logistik%20eq%20false";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);

		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$hasil = $result->data;
			$this->data["uangmukap"] = count($hasil->value);
		}

		//get data UM Multiple
		$url = API_NAV_URL."apiUM?\$filter=Need_Approve_User_Id%20eq%20'".$navid."'%20and%20From_Logistik%20eq%20true";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);

		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$hasil = $result->data;
			$this->data["ummulti"] = count($hasil->value);
		}

		$url = API_NAV_URL."apiUM?\$filter=Approve_User_Pemohon%20eq%20'".$navid."'%20and%20From_Logistik%20eq%20true";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);

		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$hasil = $result->data;
			$this->data["ummultip"] = count($hasil->value);
		}

		//get data pjum
		$url = API_NAV_URL."apiPJUM?\$filter=Need_Approve_User_Id%20eq%20'".$navid."'";

		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);

		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$hasil = $result->data;
			$this->data["pjum"] = count($hasil->value);
		}

		$url = API_NAV_URL."apiPJUM?\$filter=Approve_User_Pemohon%20eq%20'".$navid."'";

		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);

		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$hasil = $result->data;
			$this->data["pjuma"] = count($hasil->value);
		}

		$this->load->view('nav/nav_dashboard',$this->data);
	}

	public function bapb(){
		if(!$this->permission->nav_bapb)
			redirect('dashboard','refresh');

		$this->data["page"] = "navbapb";

		$navid = $this->session->userdata('navid');
		
		$url = API_NAV_URL."apiBAPBHead?\$filter=Need_Approve_User_Id%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["bapb"] = $result->data->value;
		}

		$url = API_NAV_URL."apiBAPBHead?\$filter=Approve_Menerima%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["bapba"] = $result->data->value;
		}

		$this->load->view('nav/nav_bapb',$this->data);
	}

	public function dpb(){
		if(!$this->permission->nav_dpb)
			redirect('dashboard','refresh');

		$this->data["page"] = "navdpb";

		$navid = $this->session->userdata('navid');
		//get atasan
		$url = API_NAV_URL."apiEmployee?\$filter=User_Id%20eq%20'".$navid."'";
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
		
		$url = API_NAV_URL."apiDpbHead?\$filter=Need_Approve_User_Id%20eq%20'".$navid."'%20and%20Additional_Type%20eq%20'Barang'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["dpb"] = $result->data->value;
		}

		$url = API_NAV_URL."apiDpbHead?\$filter=Pemohon_PimPro%20eq%20'".$navid."'%20and%20Additional_Type%20eq%20'Barang'%20and%20(Approve_User_Pemohon%20eq%20'".$navid."'%20or%20Approve_User_Pemohon%20eq%20'".$atasan."')%20and%20(Status%20eq%20'Released'%20or%20Status%20eq%20'Pending%20Approval')";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["dpba"] = $result->data->value;
		}

		$this->load->view('nav/nav_dpb',$this->data);
	}

	public function dpj(){
		if(!$this->permission->nav_dpj)
			redirect('dashboard','refresh');

		$this->data["page"] = "navdpj";

		$navid = $this->session->userdata('navid');
		//get atasan
		$url = API_NAV_URL."apiEmployee?\$filter=User_Id%20eq%20'".$navid."'";
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
		
		$url = API_NAV_URL."apiDpbHead?\$filter=Need_Approve_User_Id%20eq%20'".$navid."'%20and%20Additional_Type%20eq%20'Jasa'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["dpb"] = $result->data->value;
		}

		$url = API_NAV_URL."apiDpbHead?\$filter=Pemohon_PimPro%20eq%20'".$navid."'%20and%20Additional_Type%20eq%20'Jasa'%20and%20(Approve_User_Pemohon%20eq%20'".$navid."'%20or%20Approve_User_Pemohon%20eq%20'".$atasan."')%20and%20(Status%20eq%20'Released'%20or%20Status%20eq%20'Pending%20Approval')";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["dpba"] = $result->data->value;
		}

		$this->load->view('nav/nav_dpj',$this->data);
	}

	public function dpbdetail($nodpb,$app="no"){
		if(!$this->permission->nav_dpb)
			redirect('dashboard','refresh');

		$this->data["page"] = "navdpb";

		$navid = $this->session->userdata('navid');
		$url = API_NAV_URL."apiDpbHead?\$filter=No%20eq%20'".$nodpb."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["dpbhead"] = $result->data->value[0];
		}

		$url = API_NAV_URL."ApiDPB?\$filter=Document_No%20eq%20'".$nodpb."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["dpbline"] = $result->data->value;
		}

		$url = API_NAV_URL."Approve_Log?\$filter=Document_No%20eq%20'".$nodpb."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$rsl = $result->data->value;
			usort($rsl,function($first,$second){
			    return $first->Urutan_No <=> $second->Urutan_No;
			});
			$this->data["dpblog"] = $rsl;
		}

		if($app == "app")
			$this->load->view('nav/nav_dpba_detail',$this->data);
		else
			$this->load->view('nav/nav_dpb_detail',$this->data);
	}

	public function dpjdetail($nodpb,$app="no"){
		if(!$this->permission->nav_dpj)
			redirect('dashboard','refresh');

		$this->data["page"] = "navdpj";

		$navid = $this->session->userdata('navid');
		$url = API_NAV_URL."apiDpbHead?\$filter=No%20eq%20'".$nodpb."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["dpbhead"] = $result->data->value[0];
		}

		$url = API_NAV_URL."ApiDPB?\$filter=Document_No%20eq%20'".$nodpb."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["dpbline"] = $result->data->value;
		}

		$url = API_NAV_URL."Approve_Log?\$filter=Document_No%20eq%20'".$nodpb."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$rsl = $result->data->value;
			usort($rsl,function($first,$second){
			    return $first->Urutan_No <=> $second->Urutan_No;
			});
			$this->data["dpblog"] = $rsl;
		}

		if($app == "app")
			$this->load->view('nav/nav_dpja_detail',$this->data);
		else
			$this->load->view('nav/nav_dpj_detail',$this->data);
	}

	public function budget(){
		if(!$this->permission->nav_budget)
			redirect('dashboard','refresh');

		$this->data["page"] = "navbudget";

		$navid = $this->session->userdata('navid');
		
		$url = API_NAV_URL."apiBudgetPerubahan?\$filter=Need_Approve_User_Id%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["budget"] = $result->data->value;
		}

		$url = API_NAV_URL."apiBudgetPerubahan?\$filter=Approve_User_Pemohon%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["budgeta"] = $result->data->value;
		}

		$this->load->view('nav/nav_budget',$this->data);
	}

	public function budgetdetail($nob,$app="no"){
		if(!$this->permission->nav_budget)
			redirect('dashboard','refresh');

		$this->data["page"] = "navbudget";

		$navid = $this->session->userdata('navid');
		$url = API_NAV_URL."apiBudgetPerubahan?\$filter=No%20eq%20'".$nob."'%20and%20Type_Table%20eq%20'Header'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["budhead"] = $result->data->value[0];
		}

		$url = API_NAV_URL."apiBudgetPerubahan?\$filter=No%20eq%20'".$nob."'%20and%20Type_Table%20eq%20'Line'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["budline"] = $result->data->value;
		}

		$url = API_NAV_URL."Approve_Log?\$filter=Document_No%20eq%20'".$nob."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$rsl = $result->data->value;
			usort($rsl,function($first,$second){
			    return $first->Urutan_No <=> $second->Urutan_No;
			});
			$this->data["logs"] = $rsl;
		}

		if($app == "app")
			$this->load->view('nav/nav_budgeta_detail',$this->data);
		else
			$this->load->view('nav/nav_budget_detail',$this->data);
	}

	public function bapbdetail($nob,$app="no"){
		if(!$this->permission->nav_bapb)
			redirect('dashboard','refresh');

		$this->data["page"] = "navbapb";

		$navid = $this->session->userdata('navid');
		$url = API_NAV_URL."apiBAPBHead?\$filter=No%20eq%20'".$nob."'%20and%20Type_Table%20eq%20'Header'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["bapbhead"] = $result->data->value[0];
		}

		$url = API_NAV_URL."ApiBAPB?\$filter=No%20eq%20'".$nob."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["bapbline"] = $result->data->value;
		}

		$url = API_NAV_URL."Approve_Log?\$filter=Document_No%20eq%20'".$nob."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$rsl = $result->data->value;
			usort($rsl,function($first,$second){
			    return $first->Urutan_No <=> $second->Urutan_No;
			});
			$this->data["logs"] = $rsl;
		}

		if($app == "app")
			$this->load->view('nav/nav_bapba_detail',$this->data);
		else
			$this->load->view('nav/nav_bapb_detail',$this->data);
	}

	public function sppd(){
		if(!$this->permission->nav_sppd)
			redirect('dashboard','refresh');

		$this->data["page"] = "navsppd";

		$navid = $this->session->userdata('navid');
		
		$url = API_NAV_URL."api_sppd_list?\$filter=Need_Approve_User_Id%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["sppd"] = $result->data->value;
		}

		$url = API_NAV_URL."api_sppd_list?\$filter=Approve_Pemohon_K_32%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["sppda"] = $result->data->value;
		}

		$this->load->view('nav/nav_sppd',$this->data);
	}

	public function sppddetail($nob,$app="no"){
		if(!$this->permission->nav_sppd)
			redirect('dashboard','refresh');

		$this->data["page"] = "navsppd";

		$navid = $this->session->userdata('navid');
		$url = API_NAV_URL."api_sppd_list?\$filter=No%20eq%20'".$nob."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["sppdhead"] = $result->data->value[0];
		}

		$url = API_NAV_URL."api_data_SPPD?\$filter=Document_No%20eq%20'".$nob."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["sppdline"] = $result->data->value;
		}

		$url = API_NAV_URL."Approve_Log?\$filter=Document_No%20eq%20'".$nob."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$rsl = $result->data->value;
			usort($rsl,function($first,$second){
			    return $first->Urutan_No <=> $second->Urutan_No;
			});
			$this->data["logs"] = $rsl;
		}

		if($app == "app")
			$this->load->view('nav/nav_sppda_detail',$this->data);
		else
			$this->load->view('nav/nav_sppd_detail',$this->data);
	}

	public function um(){
		if(!$this->permission->nav_um)
			redirect('dashboard','refresh');

		$this->data["page"] = "navum";

		$navid = $this->session->userdata('navid');
		
		$url = API_NAV_URL."apiUM?\$filter=Need_Approve_User_Id%20eq%20'".$navid."'%20and%20From_Logistik%20eq%20false";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["um"] = $result->data->value;
		}

		$url = API_NAV_URL."apiUM?\$filter=Approve_User_Pemohon%20eq%20'".$navid."'%20and%20From_Logistik%20eq%20false";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["uma"] = $result->data->value;
		}

		$this->load->view('nav/nav_um',$this->data);
	}

	public function umdetail($nob,$app="no",$multiple="no"){
		if(!$this->permission->nav_um)
			redirect('dashboard','refresh');
		
		if($multiple == "multi")
			$this->data["page"] = "navumm";
		else
			$this->data["page"] = "navum";

		$navid = $this->session->userdata('navid');
		$url = API_NAV_URL."apiUM?\$filter=Nomor%20eq%20'".$nob."'%20and%20Type_Table%20eq%20'Header'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["umhead"] = $result->data->value[0];
		}

		$url = API_NAV_URL."apiUM?\$filter=Nomor%20eq%20'".$nob."'%20and%20Type_Table%20ne%20'Header'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["umline"] = $result->data->value;
		}

		$url = API_NAV_URL."Approve_Log?\$filter=Document_No%20eq%20'".$nob."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$rsl = $result->data->value;
			usort($rsl,function($first,$second){
			    return $first->Urutan_No <=> $second->Urutan_No;
			});
			$this->data["logs"] = $rsl;
		}

		if($app == "app"){
			if($multiple == "multi")
				$this->load->view('nav/nav_umma_detail',$this->data);
			else	
				$this->load->view('nav/nav_uma_detail',$this->data);
		}
		else{
			if($multiple == "multi")
				$this->load->view('nav/nav_umm_detail',$this->data);
			else	
				$this->load->view('nav/nav_um_detail',$this->data);
		}
	}

	public function ummulti(){
		if(!$this->permission->nav_umm)
			redirect('dashboard','refresh');

		$this->data["page"] = "navumm";

		$navid = $this->session->userdata('navid');
		
		$url = API_NAV_URL."apiUM?\$filter=Need_Approve_User_Id%20eq%20'".$navid."'%20and%20From_Logistik%20eq%20true";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["um"] = $result->data->value;
		}

		$url = API_NAV_URL."apiUM?\$filter=Approve_User_Pemohon%20eq%20'".$navid."'%20and%20From_Logistik%20eq%20true";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["uma"] = $result->data->value;
		}

		$this->load->view('nav/nav_umm',$this->data);
	}

	public function pjum(){
		if(!$this->permission->nav_pjum)
			redirect('dashboard','refresh');

		$this->data["page"] = "navpjum";

		$navid = $this->session->userdata('navid');
		
		$url = API_NAV_URL."apiPJUM?\$filter=Need_Approve_User_Id%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["pjum"] = $result->data->value;
		}

		$url = API_NAV_URL."apiPJUM?\$filter=Approve_User_Pemohon%20eq%20'".$navid."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["pjuma"] = $result->data->value;
		}

		$this->load->view('nav/nav_pjum',$this->data);
	}

	public function pjumdetail($nob,$app="no",$multiple="no"){
		if(!$this->permission->nav_pjum)
			redirect('dashboard','refresh');
		
		$this->data["page"] = "navpjum";

		$navid = $this->session->userdata('navid');
		$url = API_NAV_URL."apiPJUM?\$filter=No%20eq%20'".$nob."'%20and%20Type_Table%20eq%20'Header'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["pjumhead"] = $result->data->value[0];
		}

		$url = API_NAV_URL."apiPJUM?\$filter=No%20eq%20'".$nob."'%20and%20Type_Table%20eq%20'Line'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$this->data["pjumline"] = $result->data->value;
		}

		$url = API_NAV_URL."Approve_Log?\$filter=Document_No%20eq%20'".$nob."'";
		$result = curl_api_nav($url,'GET','',NAV_USERNAME,NAV_PASSWORD);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if($result->httpcode == 200){
			$rsl = $result->data->value;
			usort($rsl,function($first,$second){
			    return $first->Urutan_No <=> $second->Urutan_No;
			});
			$this->data["logs"] = $rsl;
		}
			
		$this->load->view('nav/nav_pjum_detail',$this->data);		
	}
}