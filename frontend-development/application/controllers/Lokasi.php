<?php 

class Lokasi extends Sei_Controller{
	public function __construct()
	{
		parent::__construct();
	}

	// crud data lokasi
	public function tambah(){
		$this->data["page"] = "lokasi";
		$this->load->view('master/tambah_lokasi',$this->data);
	}

	public function edit($id_lokasi){
		$this->data["page"] = "lokasi";

		$url = API_BASE_URL."master/lokasi/".$id_lokasi;;
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$this->data["data_lokasi"] = $result->data;

		$this->load->view('master/edit_lokasi',$this->data);
	}

	public function add(){
		$this->data["page"] = "lokasi";

		$lat = $this->input->post("latitude");
		$lng = $this->input->post("longitude");
		$rad = $this->input->post("radius");
		$nama = $this->input->post("nama_lokasi");

		$this->load->library('form_validation');
        $this->form_validation->set_rules('latitude', 'Latitude', 'required');
        $this->form_validation->set_rules('longitude', 'Longitude', 'required');
        $this->form_validation->set_rules('nama_lokasi', 'Nama Lokasi', 'required');

        if ($this->form_validation->run() == FALSE)
        {
            $this->load->view('master/tambah_lokasi',$this->data);
        }
        else{
        	$dtupload = json_encode(array(
				"latitude" => floatval($lat),
				"lokasi_absen" => $nama,
				"longitude" => floatval($lng),
				"radius" => floatval($rad)*35,
				"isDefault" => 0,
			));

			$authorization = "Authorization: Bearer ".$this->session->userdata('token');
			$url = API_BASE_URL."master/lokasi";
			$result = curl_api($url,'POST',$dtupload,$authorization);
			
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
			else{
				$this->session->set_flashdata('info', 'Data berhasil ditambahkan.');
				redirect('master/lokasi','refresh');
			}
        }
	}

	public function update($id_lokasi){
		$this->data["page"] = "lokasi";

		$lat = $this->input->post("latitude");
		$lng = $this->input->post("longitude");
		$rad = $this->input->post("radius");
		$nama = $this->input->post("nama_lokasi");
		$default = $this->input->post("isDefault");

		$this->load->library('form_validation');
        $this->form_validation->set_rules('latitude', 'Latitude', 'required');
        $this->form_validation->set_rules('longitude', 'Longitude', 'required');
        $this->form_validation->set_rules('nama_lokasi', 'Nama Lokasi', 'required');

        if ($this->form_validation->run() == FALSE)
        {
			
            $this->load->view('master/edit_lokasi',$this->data);
        }
        else{
        	$dtupload = json_encode(array(
				"id" => $id_lokasi,
				"latitude" => floatval($lat),
				"lokasi_absen" => $nama,
				"longitude" => floatval($lng),
				"radius" => floatval($rad)*35,
				"isDefault" => intval($default),
			));

			$authorization = "Authorization: Bearer ".$this->session->userdata('token');
			$url = API_BASE_URL."master/lokasi";
			$result = curl_api($url,'PUT',$dtupload,$authorization);
			
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
			else{
				$this->session->set_flashdata('info', 'Data berhasil diupdate.');
				redirect('master/lokasi','refresh');
			}
        }
	}

	public function delete($id_lokasi){
		$this->data["page"] = "lokasi";

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."master/lokasi/".$id_lokasi;
		$result = curl_api($url,'DELETE','',$authorization);
		
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		else{
			$this->session->set_flashdata('info', 'Data berhasil dihapus.');
			redirect('master/lokasi','refresh');
		}
	}

	public function setDefault($id_lokasi){
		$this->data["page"] = "lokasi";

		$url = API_BASE_URL."master/lokasi";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$lokasi = $hasil->lokasiAbsen;

		foreach($lokasi as $data):
			if($data->id == $id_lokasi){
				$dtupload = json_encode(array(
					"id" => $data->id,
					"latitude" => $data->latitude,
					"lokasi_absen" => $data->lokasi_absen,
					"longitude" => $data->longitude,
					"radius" => $data->radius,
					"isDefault" => 1,
				));
			}else{
				$dtupload = json_encode(array(
					"id" => $data->id,
					"latitude" => $data->latitude,
					"lokasi_absen" => $data->lokasi_absen,
					"longitude" => $data->longitude,
					"radius" => $data->radius,
					"isDefault" => 0,
				));
			}
			$result = curl_api($url,'PUT',$dtupload,$authorization);
			if (trim($result->error) != "") {
				$this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
		endforeach; 

		$this->session->set_flashdata('info', 'Data berhasil diupdate.');
		redirect('master/lokasi','refresh');
	}

	//edit lokasi karyawan
	public function lihatlokasi($page = 0){
		$this->data["page"] = "lokasi";

		$url = API_BASE_URL."employee/list/".$page."/500";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$this->data["karyawan"] = $hasil->data;

		$this->load->view('master/lihat_lokasi_karyawan',$this->data);
	}

	public function editLokasiKaryawan($id_karyawan){
		$this->data["page"] = "lokasi";

		$url = API_BASE_URL."master/lokasi";;
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$this->data["data_lokasi"] = $hasil->lokasiAbsen;
		$this->data["EMPCODE"] = $id_karyawan;

		$this->load->view('master/edit_lokasiKaryawan',$this->data);
	}

	public function updateKaryawan($id_karyawan){
		$this->data["page"] = "lokasi";

		$idLok = $this->input->post("id_lokasi");

		$this->load->library('form_validation');
        $this->form_validation->set_rules('id_lokasi', 'ID Lokasi', 'required');

        if ($this->form_validation->run() == FALSE)
        {
			
            $this->load->view('master/edit_lokasiKaryawan',$this->data);
        }
        else{
        	$dtupload = json_encode(array(
				"employee_code" => $id_karyawan,
				"lokasi_absen_id" => intval($idLok),
			));

			$authorization = "Authorization: Bearer ".$this->session->userdata('token');
			$url = API_BASE_URL."employee/update/lokasi";
			$result = curl_api($url,'PUT',$dtupload,$authorization);
			
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
			else{
				$this->session->set_flashdata('info', 'Data berhasil diupdate.');
				redirect('lokasi/lihatlokasi','refresh');
			}
        }
	}
}