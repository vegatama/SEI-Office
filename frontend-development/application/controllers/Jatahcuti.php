<?php 

use PhpOffice\PhpSpreadsheet\Reader\Xls;
use PhpOffice\PhpSpreadsheet\Reader\Xlsx;

class Jatahcuti extends Sei_Controller {
    public function __construct()
    {
        parent::__construct();
    }

	public function downloadTemplate(){
		$url = API_BASE_URL."master/jatahcuti/template_download";
	
			$options = array(
			  'http'=>array(
				'method'=>"GET",
				'header'=> array(
					'Authorization: Bearer '.$this->session->userdata('token'),
				),
			  )
			);
			
			$context = stream_context_create($options);
			$file = file_get_contents($url, false, $context);
       
		header('Content-disposition: attachment; filename=TemplateExcel.xlsx');
		header('Content-type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
		header('Content-Transfer-Encoding: binary');
		header('Cache-Control: must-revalidate');
		header('Pragma: public');
		//ob_clean();
		flush(); 

        echo $file;
	}

	public function importfile(){
		$this->data["page"] = "jatahcuti";

		$this->load->view('izindancuti/import_jatah_cuti',$this->data);
	}

	public function tambahdata($page = 0){
		$this->data["page"] = "jatahcuti";

		$url = API_BASE_URL."employee/list/".$page."/500";
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$hasil = $result->data;
		$this->data["karyawan"] = $hasil->data;
		$this->data['tahun'] = date('Y');

		$this->load->view('izindancuti/tambah_jatah_cuti',$this->data);
	}

	public function add(){
		$this->data["page"] = "jatahcuti";

		$empcode = $this->input->post("nama");
		$tahun = $this->input->post("tahun_cuti");
		$jatahCuti = $this->input->post("jumlah_cuti");

		$this->load->library('form_validation');
		$this->form_validation->set_rules('nama', 'Nama', 'required');
		$this->form_validation->set_rules('tahun_cuti', 'Tahun Cuti', 'required');
		$this->form_validation->set_rules('jumlah_cuti', 'Jumlah Cuti', 'required');

		if ($this->form_validation->run() == FALSE)
		{
			$this->session->set_flashdata('info', 'Data Kosong.');
				redirect('jatahcuti/tambahdata','refresh');
		}
		else{
			$authorization = "Authorization: Bearer ".$this->session->userdata('token');
			$url = API_BASE_URL."cuti/jatah/group/".$empcode."/".$tahun;
			  $result = curl_api($url,'GET','',$authorization);
			  if (trim($result->error) != "") {
				$this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			  }
			  $jatahAwal = $result->data->jumlahHari;
			  $diff = $jatahCuti - $jatahAwal;

			$url = API_BASE_URL."cuti/jatah?empcode=".$empcode."&tahun=".$tahun."&jumlahHari=".$diff;
			$result = curl_api($url,'POST','',$authorization);
			
			if (trim($result->error) != "") {
				$this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
			else{
				$this->session->set_flashdata('info', 'Data berhasil ditambah.');
				redirect('Izincuti/jatahCuti','refresh');
			}
		}
	}

	public function import(){
		$file = $_FILES['inputFile'];
		$file_name = $file['name'];

		if(isset($file_name)){
		  $ext = pathinfo($file_name, PATHINFO_EXTENSION);
		  if($ext == "xls"){
			$reader = new Xls();
		  } else {
			$reader = new Xlsx();
		  }

		  $path = $file['tmp_name'];
		  $spreadsheet = $reader->load($path);
		  $sheet = $spreadsheet->getActiveSheet()->toArray();
		  $data = [];

		  foreach($sheet as $row => $col){
			if(in_array($row, array(0,1,2,3,4,5,6,7))) continue;

			$empcode = $col[2];
			$tahun = $col[4];
			$jatahCuti = $col[5];

			if($empcode != "" && $tahun != "" && $jatahCuti != "" && strlen($tahun) == 4){
				$data[] = array($empcode, $tahun, $jatahCuti);
			}else{
				$this->session->set_flashdata('info', 'Data kosong atau salah format.');
				redirect('jatahcuti/importfile','refresh');
			}
		  }

		  $authorization = "Authorization: Bearer ".$this->session->userdata('token');

		  foreach($data as $upload){
			  $url = API_BASE_URL."cuti/jatah/group/".$upload[0]."/".$upload[1];
			  $result = curl_api($url,'GET','',$authorization);
			  if (trim($result->error) != "") {
				$this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			  }
			  $jatahAwal = $result->data->jumlahHari;
			  $diff = $upload[2] - $jatahAwal;

			  $url = API_BASE_URL."cuti/jatah?empcode=".$upload[0]."&tahun=".$upload[1]."&jumlahHari=".$diff;
			  $result = curl_api($url,'POST','',$authorization);
			  
			  if (trim($result->error) != "") {
				  $this->session->set_flashdata('error', $result->error);
				  redirect('info/error');
			  }
			}
			$this->session->set_flashdata('info', 'Data berhasil ditambahkan.');
			redirect('izincuti/jatahCuti','refresh');
		}else{
			$this->session->set_flashdata('info', 'Data kosong.');
			redirect('jatahcuti/importfile','refresh');
		}
	}

    public function edit($empcode, $tahun){
		$this->data["page"] = "jatahcuti";

		$url = API_BASE_URL."cuti/jatah/group/".$empcode."/".$tahun;
		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		$hasil = $result->data;
		$hasil->empcode = $empcode;
		$this->data["datajatah"] = $hasil;

		$this->load->view('izindancuti/edit_jatah_cuti',$this->data);
	}

	public function update($empcode, $tahun, $jumlahAwal){
    $this->data["page"] = "jatahcuti";

    $jumlahHari = $this->input->post("jumlah_cuti");

    $this->load->library('form_validation');
    $this->form_validation->set_rules('jumlah_cuti', 'Jumlah Cuti', 'required');

    if ($this->form_validation->run() == FALSE)
    {
        $this->session->set_flashdata('info', 'Data Kosong.');
			redirect('jatahcuti/edit/'.$empcode.'/'.$tahun,'refresh');
    }
    else{
		$diff = $jumlahHari - $jumlahAwal;

        $authorization = "Authorization: Bearer ".$this->session->userdata('token');
        $url = API_BASE_URL."cuti/jatah?empcode=".$empcode."&tahun=".$tahun."&jumlahHari=".$diff;
        $result = curl_api($url,'POST','',$authorization);
		
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		else{
			$this->session->set_flashdata('info', 'Data berhasil diupdate.');
			redirect('Izincuti/jatahCuti','refresh');
		}
    }
	}

	public function delete($id_jatahcuti){
		$this->data["page"] = "jatahcuti";

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."jatah/cuti/".$id_jatahcuti;
		$result = curl_api($url,'DELETE','',$authorization);
		
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		else{
			$this->session->set_flashdata('info', 'Data berhasil dihapus.');
			redirect('izincuti/jatahCuti','refresh');
		}
	}
}

