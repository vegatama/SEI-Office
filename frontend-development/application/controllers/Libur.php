<?php 

class Libur extends Sei_Controller{
	public function __construct()
	{
		parent::__construct();
	}

	public function tambah(){
		$this->data["page"] = "libur";
		$this->load->view('master/tambah_libur',$this->data);
	}

	public function add(){
		$this->data["page"] = "libur";

		$tgl = $this->input->post("tanggal");
		$ket = $this->input->post("keterangan");

		$this->load->library('form_validation');
        $this->form_validation->set_rules('tanggal', 'Tanggal', 'required');

		if ($this->form_validation->run() == FALSE){
            $this->load->view('master/tambah_libur',$this->data);
        }else{
        	$dtupload = json_encode(array(
				"tgl" => $tgl,
				"keterangan" => $ket
			));

			$authorization = "Authorization: Bearer ".$this->session->userdata('token');
			$url = API_BASE_URL."master/harilibur";
			$result = curl_api($url,'POST',$dtupload,$authorization);
			
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
			else{
				$this->session->set_flashdata('info', 'Data berhasil ditambahkan.');
				redirect('master/libur','refresh');
			}
        }
	}

	public function delete($id){
		$this->data["page"] = "libur";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."master/harilibur?id=".$id;
		$result = curl_api($url,'DELETE','',$authorization);
		
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		else{
			$this->session->set_flashdata('info', 'Data berhasil dihapus.');
			redirect('master/libur','refresh');
		}
	}

	public function edit($id){
		$this->data["page"] = "libur";

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."master/harilibur?id=".$id;
		$result = curl_api($url,'GET','',$authorization);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		
		$this->data["libur"] = $result->data;
		$this->load->view('master/edit_libur',$this->data);
	}

	public function update($id){
		$this->data["page"] = "libur";

		$tgl = $this->input->post("tanggal");
		$ket = $this->input->post("keterangan");

		$this->load->library('form_validation');
        $this->form_validation->set_rules('tanggal', 'Tanggal', 'required');

		if ($this->form_validation->run() == FALSE){
            $this->load->view('master/edit_libur',$this->data);
        }else{
        	$dtupload = json_encode(array(
				"id" => $id,
				"tgl" => $tgl,
				"keterangan" => $ket
			));

			$authorization = "Authorization: Bearer ".$this->session->userdata('token');
			$url = API_BASE_URL."master/harilibur";
			$result = curl_api($url,'PUT',$dtupload,$authorization);
			
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
			else{
				$this->session->set_flashdata('info', 'Data berhasil diupdate.');
				redirect('master/libur','refresh');
			}
        }
	}

}