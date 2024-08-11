<?php

class Proyek extends Sei_Controller{
	public function __construct()
	{
		parent::__construct();
	}

	public function dashboard(){
		if(!$this->permission->pmo_project)
			redirect('dashboard','refresh');

		$this->data["page"] = "dashproyek";

		if($this->input->post('tahun'))
			$tahun = $this->input->post('tahun');
		else
			$tahun = date('Y');
		if($this->input->post('tipe'))
			$tipe = $this->input->post('tipe');
		else
			$tipe = "Proyek";
		$url = API_BASE_URL."proyek/list2/".$tipe."/".$tahun;
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$datakirim = json_encode(
			array(
				"status" => "Open"
			));

		$result = curl_api($url,'GET',$datakirim,$authorization);
	
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
	
		$hasil = $result->data;
		$jum = $hasil->count;
		$dt = $hasil->proyeks;
		$this->data["jumlah"] = $jum;
		$this->data["proyeks"] = $dt;
		$this->data["tahun"] = $tahun;
		$this->data["tipe"] = $tipe;
		$this->load->view("pmo/proyek_dash",$this->data);
	}

	public function detail($kdproyek){
		if(!$this->permission->pmo_project)
			redirect('dashboard','refresh');

		$this->data["page"] = "dashproyek";

		$url = API_BASE_URL."proyek/detail/".$kdproyek;
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$result = curl_api($url,'GET','',$authorization);
        
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
	
		$hasil = $result->data;
		$data["proyek"] = $hasil->proyek;
		$data["budgets"] = $hasil->budgets;
		$data["dpbs"] = $hasil->dpbs;

		$this->load->view("pmo/proyek_detail",$this->data);
	}

	public function budget($kdproyek){
		if(!$this->permission->pmo_anggaran)
			redirect('dashboard','refresh');

		$this->data["page"] = "budget";

		$url = API_BASE_URL."proyek/detail/".$kdproyek;
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$result = curl_api($url,'GET','',$authorization);
        
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
	
		$hasil = $result->data;
		$this->data["proyek"] = $hasil->proyek;
		$this->data["budgets"] = $hasil->budgets;
		$this->data["dpbs"] = $hasil->dpbs;

		$this->load->view("pmo/proyek_budget",$this->data);
	}

	public function dpb($kdproyek){
		if(!$this->permission->pmo_dpbj)
			redirect('dashboard','refresh');

		$this->data["page"] = "dpb";

		$url = API_BASE_URL."proyek/detail/".$kdproyek;
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$result = curl_api($url,'GET','',$authorization);
        
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
	
		$hasil = $result->data;
		$this->data["proyek"] = $hasil->proyek;
		$this->data["budgets"] = $hasil->budgets;
		$this->data["dpbs"] = $hasil->dpbs;
		$this->load->view("pmo/proyek_dpb",$this->data);
	}
}