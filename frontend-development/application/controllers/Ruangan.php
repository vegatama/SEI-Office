<?php 

class Ruangan extends Sei_Controller{
	public function __construct()
	{
		parent::__construct();
	}

	public function tambah(){
		$this->data["page"] = "ruangan";
		$this->load->view('master/tambah_ruangan',$this->data);
	}

	public function add(){
		$this->data["page"] = "ruangan";
		$name = $this->input->post("name");
		$capaty = $this->input->post("capacity");
		$desc = $this->input->post("description");
		$this->load->library('form_validation');
        $this->form_validation->set_rules('name', 'Name', 'required');
		$this->form_validation->set_rules('capacity', 'Capacity', 'required');

		if ($this->form_validation->run() == FALSE){
            $this->load->view('master/tambah_ruangan',$this->data);
        }else{
			
			// $url = API_BASE_URL."master/ruangmeeting?name=".$name."&capacity=".$capaty."&description=".$desc;

			$curl = curl_init();

			curl_setopt_array($curl, array(
				CURLOPT_URL => API_BASE_URL."master/ruangmeeting?name=".urlencode($name)."&capacity=".urlencode($capaty)."&description=".urlencode($desc),
				CURLOPT_RETURNTRANSFER => true,
				CURLOPT_ENCODING => '',
				CURLOPT_MAXREDIRS => 10,
				CURLOPT_TIMEOUT => 0,
				CURLOPT_FOLLOWLOCATION => true,
				CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
				CURLOPT_CUSTOMREQUEST => 'POST',
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
            	$this->session->set_flashdata('info', 'Data berhasil ditambahkan.');
                redirect('master/ruangan','refresh');
			}
		}
	}

	public function delete($id){
		$this->data["page"] = "ruangan";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."master/ruangmeeting/".$id;
		$result = curl_api($url,'DELETE','',$authorization);
		
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		else{
			$this->session->set_flashdata('info', 'Data berhasil dihapus.');
			redirect('master/ruangan','refresh');
		}
	}

	public function edit($id){
		$this->data["page"] = "ruangan";

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."master/ruangmeeting/".$id;
		$result = curl_api($url,'GET','',$authorization);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		
		$this->data["ruangan"] = $result->data;
		$this->load->view('master/edit_ruangan',$this->data);
	}

	public function update($id){
		$this->data["page"] = "ruangan";
		$name = $this->input->post("name");
		$capaty = $this->input->post("capacity");
		$desc = $this->input->post("description");
		$this->load->library('form_validation');
        $this->form_validation->set_rules('name', 'Name', 'required');
		$this->form_validation->set_rules('capacity', 'Capacity', 'required');

		if ($this->form_validation->run() == FALSE){
            $this->load->view('master/tambah_ruangan',$this->data);
        }else{
			
			// $url = API_BASE_URL."master/ruangmeeting?name=".$name."&capacity=".$capaty."&description=".$desc;

			$curl = curl_init();

			curl_setopt_array($curl, array(
				CURLOPT_URL => API_BASE_URL."master/ruangmeeting?id=".urldecode($id)."&name=".urlencode($name)."&capacity=".urlencode($capaty)."&description=".urlencode($desc),
				CURLOPT_RETURNTRANSFER => true,
				CURLOPT_ENCODING => '',
				CURLOPT_MAXREDIRS => 10,
				CURLOPT_TIMEOUT => 0,
				CURLOPT_FOLLOWLOCATION => true,
				CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
				CURLOPT_CUSTOMREQUEST => 'PUT',
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
            	$this->session->set_flashdata('info', 'Data berhasil diedit.');
                redirect('master/ruangan','refresh');
			}
		}
	}
}