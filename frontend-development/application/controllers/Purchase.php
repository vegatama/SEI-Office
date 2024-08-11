<?php

class Purchase extends Sei_Controller{
	public function __construct()
	{
		parent::__construct();
	}

	public function list(){
		if(!$this->access["purchase"])
			redirect('dashboard','refresh');

		$this->data["page"] = "purchase";

		$url = API_BASE_URL."proyek/po/all";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);
        
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
	
		$hasil = $result->data;
		$this->data["purchases"] = $hasil->purchases;

		$this->load->view("pmo/po_list",$this->data);
	}
}