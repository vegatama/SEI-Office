<?php 

class Cuti extends Sei_Controller {
	public function __construct()
	{
		parent::__construct();
	}

	public function tambah($page = 0){
		$this->data["page"] = "cuti";

		$url = API_BASE_URL."employee/list/".$page."/500";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$this->data["karyawan"] = $hasil->data;

		$this->load->view('master/tambah_cuti',$this->data);
	}

	public function add(){
		$this->data["page"] = "cuti";

		$jCuti = $this->input->post("jenis_cuti");
		$jPengajuan = $this->input->post("jenis_pengajuan");
		$cutCuti = $this->input->post("cut_cuti");
		$jApproval = intval($this->input->post("jumlah_approval"));
		$approval1 = $this->input->post("approval_1");
		$approval2 = $this->input->post("approval_2");
		$approval3 = $this->input->post("approval_3");

		$this->load->library('form_validation');
        $this->form_validation->set_rules('jenis_cuti', 'Jenis Cuti', 'required');

		// list approval
		$approval = [];
		// array pembantu
		$atasan = array("0","1","2");
	
		if($jApproval == 1){
			$this->form_validation->set_rules('approval_1', 'Approval 1', 'required');
			if(in_array($approval1, $atasan)){
				$approval[] = array("layerIndex" => intval($approval1));
			}else{
				$approval[] = array("empCode" => $approval1);
			}
		}else if ($jApproval == 2){
			$this->form_validation->set_rules('approval_1', 'Approval 1', 'required');
			$this->form_validation->set_rules('approval_2', 'Approval 2', 'required');

			if(in_array($approval1, $atasan)){
				$approval[] = array("layerIndex" => intval($approval1));
			}else{
				$approval[] = array("empCode" => $approval1);
			}
			if(in_array($approval2, $atasan)){
				$approval[] = array("layerIndex" => intval($approval2));
			}else{
				$approval[] = array("empCode" => $approval2);
			}
		}else if ($jApproval == 3){
			$this->form_validation->set_rules('approval_1', 'Approval 1', 'required');
			$this->form_validation->set_rules('approval_2', 'Approval 2', 'required');
			$this->form_validation->set_rules('approval_3', 'Approval 3', 'required');

			if(in_array($approval1, $atasan)){
				$approval[] = array("layerIndex" => intval($approval1));
			}else{
				$approval[] = array("empCode" => $approval1);
			}
			if(in_array($approval2, $atasan)){
				$approval[] = array("layerIndex" => intval($approval2));
			}else{
				$approval[] = array("empCode" => $approval2);
			}
			if(in_array($approval3, $atasan)){
				$approval[] = array("layerIndex" => intval($approval3));
			}else{
				$approval[] = array("empCode" => $approval3);
			}
		}

        if ($this->form_validation->run() == FALSE)
        {
            $this->session->set_flashdata('info', 'Data belum diisi.');
			redirect('cuti/tambah','refresh');
        }
        else{
        	$dtupload = json_encode(array(
				"izinCuti" => $jCuti,
				"cutCuti" => filter_var($cutCuti, FILTER_VALIDATE_BOOLEAN),
				"reviewer" => $approval,
				"pengajuan" => $jPengajuan,
			));

			$authorization = "Authorization: Bearer ".$this->session->userdata('token');
			$url = API_BASE_URL."master/izincuti";
			$result = curl_api($url,'POST',$dtupload,$authorization);
			
			if ($result->data->msg == "SUCCESS") {
				$this->session->set_flashdata('info', 'Data berhasil ditambahkan.');
				redirect('master/cuti','refresh');
			}else if($result->data->msg == "ERROR: DUPLICATE_LAYER_INDEX" || $result->data->msg == "ERROR: DUPLICATE_EMP_CODE"){
				$this->session->set_flashdata('info', 'Data approval duplikat.');
				redirect('cuti/tambah','refresh');
			}else{
				$this->session->set_flashdata('error', $result);
				redirect('info/error');
			}
        }
	}

	public function edit($id_cuti, $app1, $app2, $app3, $page = 0){
		$this->data["page"] = "cuti";

		$url = API_BASE_URL."employee/list/".$page."/500";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$this->data["karyawan"] = $hasil->data;

		$url = API_BASE_URL."master/izincuti/".$id_cuti;

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$this->data["data_cuti"] = $result->data;
		$this->data["app_array"] = array($app1,$app2,$app3);

		$this->load->view('master/edit_cuti',$this->data);
	}

	public function update($id_cuti){
		$this->data["page"] = "cuti";

		$jCuti = $this->input->post("jenis_cuti");
		$jPengajuan = $this->input->post("jenis_pengajuan");
		$cutCuti = $this->input->post("cut_cuti");
		$jApproval = intval($this->input->post("jumlah_approval"));
		$approval1 = $this->input->post("approval_1");
		$approval2 = $this->input->post("approval_2");
		$approval3 = $this->input->post("approval_3");

		$this->load->library('form_validation');
        $this->form_validation->set_rules('jenis_cuti', 'Jenis Cuti', 'required');

		// list approval
		$approval = [];
		// array pembantu
		$atasan = array("0","1","2");
	
		if($jApproval == 1){
			$this->form_validation->set_rules('approval_1', 'Approval 1', 'required');
			if(in_array($approval1, $atasan)){
				$approval[] = array("layerIndex" => intval($approval1));
			}else{
				$approval[] = array("empCode" => $approval1);
			}
		}else if ($jApproval == 2){
			$this->form_validation->set_rules('approval_1', 'Approval 1', 'required');
			$this->form_validation->set_rules('approval_2', 'Approval 2', 'required');

			if(in_array($approval1, $atasan)){
				$approval[] = array("layerIndex" => intval($approval1));
			}else{
				$approval[] = array("empCode" => $approval1);
			}
			if(in_array($approval2, $atasan)){
				$approval[] = array("layerIndex" => intval($approval2));
			}else{
				$approval[] = array("empCode" => $approval2);
			}
		}else if ($jApproval == 3){
			$this->form_validation->set_rules('approval_1', 'Approval 1', 'required');
			$this->form_validation->set_rules('approval_2', 'Approval 2', 'required');
			$this->form_validation->set_rules('approval_3', 'Approval 3', 'required');

			if(in_array($approval1, $atasan)){
				$approval[] = array("layerIndex" => intval($approval1));
			}else{
				$approval[] = array("empCode" => $approval1);
			}
			if(in_array($approval2, $atasan)){
				$approval[] = array("layerIndex" => intval($approval2));
			}else{
				$approval[] = array("empCode" => $approval2);
			}
			if(in_array($approval3, $atasan)){
				$approval[] = array("layerIndex" => intval($approval3));
			}else{
				$approval[] = array("empCode" => $approval3);
			}
		}

        if ($this->form_validation->run() == FALSE)
        {
            $this->session->set_flashdata('info', 'Data belum diisi.');
				redirect('cuti/edit/'.$id_cuti,'refresh');
        }
        else{
        	$dtupload = json_encode(array(
				"id" => $id_cuti,
				"izinCuti" => $jCuti,
				"cutCuti" => filter_var($cutCuti, FILTER_VALIDATE_BOOLEAN),
				"reviewer" => $approval,
				"pengajuan" => $jPengajuan,
			));

			$authorization = "Authorization: Bearer ".$this->session->userdata('token');
			$url = API_BASE_URL."master/izincuti";
			$result = curl_api($url,'PUT',$dtupload,$authorization);
			
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
			else{
				$this->session->set_flashdata('info', 'Data berhasil diupdate.');
				redirect('master/cuti','refresh');
			}
        }
	}

	public function delete($id_cuti){
		$this->data["page"] = "cuti";

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."master/izincuti/".$id_cuti;
		$result = curl_api($url,'DELETE','',$authorization);
		
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		else{
			$this->session->set_flashdata('info', 'Data berhasil dihapus.');
			redirect('master/cuti','refresh');
		}
	}
}