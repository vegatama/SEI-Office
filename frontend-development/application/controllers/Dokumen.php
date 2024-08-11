<?php 

class Dokumen extends Sei_Controller{
	public function __construct()
	{
		parent::__construct();
	}

    public function index(){
		$this->data["page"] = "docnav";

		$emp = $this->session->userdata('empCode');
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."documents?page=0&size=50";
		$result = curl_api($url,'GET','',$authorization);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;

        if ($this->input->post('kategori')) {
			$kategori = $this->input->post('kategori');
            if($kategori === 'ALL'){
                $data = $hasil->data;
            }else{
            $data = array_filter($hasil->data, function($object) use($kategori){
                return $object->category == $kategori;
            });
        }
		} else {
			$kategori = 'ALL';
            $data = $hasil->data;
		}
		$this->data['ktgr'] = $kategori;

		$this->data["documents"] = $data;
		$this->data["user_empCode"] = $emp;
		$this->load->view('dokumen_perusahaan/dokumen',$this->data);
	}

    public function view($id){
		$this->data["page"] = "docnav";

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."documents/view/".$id;
		$result = curl_api($url,'GET','',$authorization);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;

        redirect($hasil->url);
	}

	// crud dokumen perusahaan
	public function tambah(){
		if(!$this->permission->dokumen)
            redirect('dokumen','refresh');

		$this->data["page"] = "docnav";
		$this->load->view('dokumen_perusahaan/dokumen_tambah',$this->data);
	}

	public function add(){
		if(!$this->permission->dokumen)
            redirect('dokumen','refresh');

		$this->data["page"] = "docnav";

		$noDokumen = $this->input->post("noDokumen");
		$kategori = $this->input->post("kategori");
		$namaDokumen = $this->input->post("namaDokumen");
		$tanggalPengesahan = $this->input->post("datesingle");
        $revisi = $this->input->post("revisi");

		if($_FILES['inputFile']['tmp_name'] === "" || $_FILES['inputFile']['tmp_name'] == null){
            $this->session->set_flashdata('info', 'Form File Kosong.');
			redirect('dokumen/tambah','refresh');
        }else{
			$document = new CURLFile($_FILES['inputFile']['tmp_name'], $_FILES['inputFile']['type'],$_FILES['inputFile']['name']);
		}

		$this->load->library('form_validation');
        $this->form_validation->set_rules('noDokumen', 'No. Dokumen', 'required');
        $this->form_validation->set_rules('kategori', 'Kategori', 'required');
        $this->form_validation->set_rules('namaDokumen', 'Nama Dokumen', 'required');
        $this->form_validation->set_rules('datesingle', 'Tanggal Pengesahan', 'required');
        $this->form_validation->set_rules('revisi', 'Revisi', 'required');

        if ($this->form_validation->run() == FALSE)
        {
            $this->session->set_flashdata('info', 'Form Data Kosong.');
			redirect('dokumen/tambah','refresh');
        }
        else{
            $emp = $this->session->userdata('empCode');
			$curl = curl_init();

            curl_setopt_array($curl, array(
            CURLOPT_URL => API_BASE_URL.'documents',
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_ENCODING => '',
            CURLOPT_MAXREDIRS => 10,
            CURLOPT_TIMEOUT => 0,
            CURLOPT_FOLLOWLOCATION => true,
            CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
            CURLOPT_CUSTOMREQUEST => 'POST',
            CURLOPT_POSTFIELDS => array(
				'documentNumber' => $noDokumen,
				'category' => $kategori,
				'documentName' => $namaDokumen,
				'approvalDate' => $tanggalPengesahan,
				'revision' => $revisi,
				'file' => $document,
                'uploaderEmployeeCode' => $emp
			),
            CURLOPT_HTTPHEADER => array(
                'Authorization: Bearer '.$this->session->userdata('token'),
                'Content-Type: multipart/form-data'
            ),
            ));

            $response = curl_exec($curl);
            if(curl_errno($curl)){
                $error_msg = curl_error($curl);
            }
            curl_close($curl);

            $message = json_decode($response);
		
			if(isset($error_msg)){
			    $this->session->set_flashdata('error', $error_msg);
				redirect('info/error');
            }else{
                if($message->message === "SUCCESS"){
                    $this->session->set_flashdata('info', 'Data berhasil ditambah.');
                    redirect('dokumen','refresh');
                }else{
                    $this->session->set_flashdata('info', $message->message);
                    redirect('dokumen','refresh');
                }
			}
        }
	}

    public function edit($id){
		if(!$this->permission->dokumen)
            redirect('dokumen','refresh');

		$this->data["page"] = "docnav";

		$url = API_BASE_URL."documents/".$id;
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		$this->data["dataDokumen"] = $result->data;

		$this->load->view('dokumen_perusahaan/dokumen_edit',$this->data);
	}

    public function update($id){
		if(!$this->permission->dokumen)
            redirect('dokumen','refresh');

		$this->data["page"] = "docnav";

		$noDokumen = $this->input->post("noDokumen");
		$kategori = $this->input->post("kategori");
		$namaDokumen = $this->input->post("namaDokumen");
		$tanggalPengesahan = $this->input->post("datesingle");
        $revisi = $this->input->post("revisi");

		$this->load->library('form_validation');
        $this->form_validation->set_rules('noDokumen', 'No. Dokumen', 'required');
        $this->form_validation->set_rules('kategori', 'Kategori', 'required');
        $this->form_validation->set_rules('namaDokumen', 'Nama Dokumen', 'required');
        $this->form_validation->set_rules('datesingle', 'Tanggal Pengesahan', 'required');
        $this->form_validation->set_rules('revisi', 'Revisi', 'required');

        if ($this->form_validation->run() == FALSE)
        {
            $this->session->set_flashdata('info', 'Form Data Kosong.');
			redirect('dokumen/edit/'.$id,'refresh');
        }
        else{
            $emp = $this->session->userdata('empCode');
			$curl = curl_init();

            curl_setopt_array($curl, array(
            CURLOPT_URL => API_BASE_URL.'documents/'.$id,
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_ENCODING => '',
            CURLOPT_MAXREDIRS => 10,
            CURLOPT_TIMEOUT => 0,
            CURLOPT_FOLLOWLOCATION => true,
            CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
            CURLOPT_CUSTOMREQUEST => 'PUT',
            CURLOPT_POSTFIELDS => array(
				'documentNumber' => $noDokumen,
				'category' => $kategori,
				'documentName' => $namaDokumen,
				'approvalDate' => $tanggalPengesahan,
                'employeeCode' => $emp,
				'revision' => $revisi
			),
            CURLOPT_HTTPHEADER => array(
                'Authorization: Bearer '.$this->session->userdata('token')
            ),
            ));

            $response = curl_exec($curl);
            if(curl_errno($curl)){
                $error_msg = curl_error($curl);
            }
            curl_close($curl);

            $message = json_decode($response);
		
			if(isset($error_msg)){
			    $this->session->set_flashdata('error', $error_msg);
				redirect('info/error');
            }else{
                if($message->msg === "SUCCESS"){
                    $this->session->set_flashdata('info', 'Data berhasil diupdate.');
                    redirect('dokumen','refresh');
                }else{
                    $this->session->set_flashdata('info', $message->msg);
                    redirect('dokumen','refresh');
                }
			}
        }
	}

    public function delete($id){
		if(!$this->permission->dokumen)
            redirect('dokumen','refresh');

		$this->data["page"] = "docnav";

        $emp = $this->session->userdata('empCode');
		$url = API_BASE_URL."documents/".$id."?employeeCode=".$emp;
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$result = curl_api($url,'DELETE','',$authorization);
		
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error); 
			redirect('info/error');
		}
		else{
			$this->session->set_flashdata('info', 'Data berhasil dihapus.');
			redirect('dokumen','refresh');
		}
    }
}