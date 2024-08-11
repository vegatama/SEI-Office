<?php 

class Notifikasi extends Sei_Controller {
    public function __construct()
    {
        parent::__construct();

    }
    public function tambah($page = 0){
        $this->data["page"] = "notif";

        $url = API_BASE_URL."employee/list/".$page."/500";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$this->data["karyawan"] = $hasil->data;

        $this->load->view('master/tambah_notifikasi',$this->data);
    }

    public function add(){
        $this->data["page"] = "notif";

        $name = $this->input->post("name");
        //$notifkendaraan = $this->input->post("notif_kendaraan");

		$this->load->library('form_validation');
        $this->form_validation->set_rules('name', 'Name', 'required');

        if ($this->form_validation->run() == FALSE)
        {
            redirect('notifikasi/tambah','refresh');
        }
        else{
        	$dtupload = json_encode(array(
				"employeeCode" => $name,
                "receiveEventNotification" => True //filter_var($notifkendaraan, FILTER_VALIDATE_BOOLEAN)
			));

			$authorization = "Authorization: Bearer ".$this->session->userdata('token');
			$url = API_BASE_URL."notification/settings/update";
			$result = curl_api($url,'POST',$dtupload,$authorization);
			
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
			else{
				$this->session->set_flashdata('info', 'Data berhasil ditambahkan.');
				redirect('master/notifikasi','refresh');
			}
        }
    }

    public function delete($id){
		$this->data["page"] = "notif";

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."notification/settings/".$id;
		$result = curl_api($url,'DELETE','',$authorization);
		
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		else{
			$this->session->set_flashdata('info', 'Data berhasil dihapus.');
			redirect('master/notifikasi','refresh');
		}
	}
}

    ?>