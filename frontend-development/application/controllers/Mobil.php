<?php

class Mobil extends Sei_Controller{
	public function __construct()
	{
		parent::__construct();
	}

	public function unit(){
		if(!$this->permission->opsk_kendaraan)
			redirect('dashboard','refresh');

		$this->data["page"] = "carunit";

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."master/vehicle/list";
		$result = curl_api($url,'GET','',$authorization);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$return = $result->data;
		$this->data["vehicles"] = $return->vehicles;
		$this->load->view('vehicle/unit',$this->data);
	}

	public function add(){
		if(!$this->permission->opsk_kendaraan)
			redirect('dashboard','refresh');

		$this->data["page"] = "carunit";

		$this->load->view('vehicle/unit_add',$this->data);
	}

	public function addp(){
		$this->data["page"] = "carunit";

		$plat = $this->input->post("nopol");
		$merk = $this->input->post("merk");
		$tipe = $this->input->post("tipe");
		$tahun = $this->input->post("tahun");
		$bbm = $this->input->post("bbm");
		$pemilik = $this->input->post("pemilik");
		$pkb = $this->input->post("pkb");
		$ket = $this->input->post("ket");

		$this->load->library('form_validation');
        $this->form_validation->set_rules('nopol', 'No. Polisi', 'required');
        $this->form_validation->set_rules('merk', 'Merek', 'required');
        $this->form_validation->set_rules('tipe', 'Tipe', 'required');
        $this->form_validation->set_rules('tahun', 'Tahun', 'required');

        if ($this->form_validation->run() == FALSE)
        {
            $this->load->view('vehicle/unit_add',$this->data);
        }
        else{
        	$dtupload = json_encode(array(
				"plat_number" => $plat,
				"year" => $tahun,
				"type" => $tipe,
				"merk" => $merk,
				"ownership" => $pemilik,
				"certificate_expired" => $pkb,
				"tax_expired" => $pkb,
				"bbm" => $bbm,
				"keterangan" => $ket
			));

			$authorization = "Authorization: Bearer ".$this->session->userdata('token');
			$url = API_BASE_URL."master/vehicle";
			$result = curl_api($url,'POST',$dtupload,$authorization);
			
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
			else{
				$this->session->set_flashdata('info', 'Data berhasil ditambahkan.');
				redirect('mobil/unit','refresh');
			}
        }
	}

	public function detail($id){
		if(!$this->permission->opsk_kendaraan)
			redirect('dashboard','refresh');

		$this->data["page"] = "carunit";

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."master/vehicle/".$id;
		$result = curl_api($url,'GET','',$authorization);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		
		$this->data["vehicle"] = $result->data;
		$this->load->view('vehicle/unit_detail',$this->data);
	}

	public function update($id){
		$this->data["page"] = "carunit";

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."master/vehicle/".$id;
		$result = curl_api($url,'GET','',$authorization);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		
		$this->data["vehicle"] = $result->data;
		$this->load->view('vehicle/unit_update',$this->data);
	}

	public function updatep(){
		if(!$this->permission->opsk_kendaraan)
			redirect('dashboard','refresh');

		$this->data["page"] = "carunit";

		$plat = $this->input->post("nopol");
		$merk = $this->input->post("merk");
		$tipe = $this->input->post("tipe");
		$tahun = $this->input->post("tahun");
		$bbm = $this->input->post("bbm");
		$pemilik = $this->input->post("pemilik");
		$pkb = $this->input->post("pkb");
		$ket = $this->input->post("ket");
		$vid = $this->input->post("id");

		$this->load->library('form_validation');
        $this->form_validation->set_rules('nopol', 'No. Polisi', 'required');
        $this->form_validation->set_rules('merk', 'Merek', 'required');
        $this->form_validation->set_rules('tipe', 'Tipe', 'required');
        $this->form_validation->set_rules('tahun', 'Tahun', 'required');

        if ($this->form_validation->run() == FALSE)
        {
            $this->load->view('vehicle/unit_add',$this->data);
        }
        else{
        	$dtupload = json_encode(array(
				"plat_number" => $plat,
				"year" => $tahun,
				"type" => $tipe,
				"merk" => $merk,
				"ownership" => $pemilik,
				"certificate_expired" => $pkb,
				"tax_expired" => $pkb,
				"bbm" => $bbm,
				"keterangan" => $ket,
				"vehicle_id" => $vid
			));

			$authorization = "Authorization: Bearer ".$this->session->userdata('token');
			$url = API_BASE_URL."master/vehicle";
			$result = curl_api($url,'PUT',$dtupload,$authorization);
			
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
			else{
				$this->session->set_flashdata('info', 'Data berhasil diupdate.');
				redirect('mobil/unit','refresh');
			}
        }
	}

	public function delete(){
		if(!$this->permission->opsk_kendaraan)
			redirect('dashboard','refresh');
		
		$this->data["page"] = "carunit";
		$vid = $this->input->post("id");

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."master/vehicle/".$vid;
		$result = curl_api($url,'DELETE','',$authorization);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		else{
			$this->session->set_flashdata('info', 'Data berhasil dihapus.');
			redirect('mobil/unit','refresh');
		}
	}

	
}