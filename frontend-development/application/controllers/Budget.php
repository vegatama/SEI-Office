<?php 

class Budget extends Sei_Controller{
	public function __construct()
	{
		parent::__construct();
	}

	public function list(){
		if(!$this->access["budget"])
			redirect('dashboard','refresh');

		$this->data["page"] = "budget";

		if($this->input->post('tahun'))
			$tahun = $this->input->post('tahun');
		else
			$tahun = date('Y');
		if($this->input->post('tipe'))
			$tipe = $this->input->post('tipe');
		else
			$tipe = "Proyek";
		
		$this->data["tahun"] = $tahun;
		$this->data["tipe"] = $tipe;

		$url = API_BASE_URL."proyek/list2/".$tipe."/".$tahun;
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data; 
		$dt = $hasil->proyeks;
		$this->data["proyeks"] = $dt;

		if($this->input->post('kdproyek')){			
			$kdproyek = $this->input->post('kdproyek');
		}
		else{
			$kdproyek = $dt[count($dt)-1]->project_code;
		}

		//dapetin dpb
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
		
		$this->load->view("pmo/budget_list",$this->data);
	}
}