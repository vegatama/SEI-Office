<?php

class Dpb extends Sei_Controller{
	public function __construct()
	{
		parent::__construct();
	}

	public function list(){
		if(!$this->access["dpb"])
			redirect('dashboard','refresh');

		$this->data["page"] = "dpb";

		$url = API_BASE_URL."proyek/list/0/999999";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data; 
		$dt = $hasil->proyeks;
		krsort($dt);
		$this->data["proyeks"] = $dt;

		if($this->input->post('kdproyek')){			
			$kdproyek = $this->input->post('kdproyek');
		}
		else{
			$kdproyek = $dt[count($dt)-1]->project_code;
		}

		//dapetin dpb
		$url = API_BASE_URL."proyek/detail/".$kdproyek;
		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data; 
		$this->data["proyek"] = $hasil->proyek;
		$this->data["budgets"] = $hasil->budgets;
		$this->data["dpbs"] = $hasil->dpbs;

		$this->load->view("pmo/dpb_list",$this->data);
	}

	public function detail($nodpb){
		if(!$this->access["dpb"])
			redirect('dashboard','refresh');

		$this->data["page"] = "dpb";

		$url = API_BASE_URL."proyek/dpb/detail/".$nodpb;
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data; 
		$this->data["dpb"] = $hasil->dpb_detail;
		$this->data["lines"] = $hasil->dpb_lines;

		$this->load->view("pmo/dpb_detail",$this->data);
	}
}