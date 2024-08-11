<?php 

class Order extends Sei_Controller{
	public function __construct(){
		parent::__construct();
		if(!$this->access["carorder"])
			redirect('dashboard','refresh');
	}

	public function list(){
		if(!$this->permission->opsk_order)
			redirect('dashboard','refresh');

		$this->data["page"] = "carorder";

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$pid = $this->session->userdata('employeeID');
		$url = API_BASE_URL."order/pemesan/".$pid;
		$result = curl_api($url,'GET','',$authorization);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$return = $result->data;
		$this->data["orders"] = $return->orders;

		$this->load->view('vehicle/order_list',$this->data);
	}

	public function na(){
		if(!$this->permission->opsk_approval)
			redirect('dashboard','refresh');

		$this->data["page"] = "carorderna";

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$pid = $this->session->userdata('employeeID');
		$url = API_BASE_URL."order/na/".$pid;
		$result = curl_api($url,'GET','',$authorization);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$return = $result->data;
		$this->data["ordersna"] = $return->orders;

		$url = API_BASE_URL."order/app/".$pid;
		$result = curl_api($url,'GET','',$authorization);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$return = $result->data;
		$this->data["ordersapp"] = $return->orders;

		$this->load->view('vehicle/order_na_list',$this->data);
	}

	public function add(){
		if(!$this->permission->opsk_order)
			redirect('dashboard','refresh');

		$this->data["page"] = "carorder";

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."employee/list/0/9999999";
		$result = curl_api($url,'GET','',$authorization);
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$return = $result->data;
		$this->data["employee"] = $return->data;

		$url = API_BASE_URL."proyek/list/0/9999999";
		$result = curl_api($url,'GET','',$authorization);
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$return = $result->data;
		$this->data["proyeks"] = $return->proyeks;

		$this->load->view('vehicle/order_add',$this->data);
	}

	public function addp(){
		if(!$this->permission->opsk_order)
			redirect('dashboard','refresh');

		$this->data["page"] = "carorder";

		$pengguna = $this->input->post('pengguna');
		$tgl_berangkat = $this->input->post('tanggal_berangkat');
//		$jam_berangkat = $this->input->post('jam');
//		$menit_berangkat = $this->input->post('menit');
		$tgl_kembali = $this->input->post('tanggal_kembali');
		$tujuan = $this->input->post('tujuan');
		$keperluan = $this->input->post('keperluan');
		$kdproyek = $this->input->post('kdproyek');
		$keterangan = $this->input->post('keterangan');
		$waktu_berangkat = $this->input->post('waktu_berangkat');
		// $waktu_kembali = $this->input->post('waktu_kembali');

		$this->load->library('form_validation');
        $this->form_validation->set_rules('pengguna[]', 'Nama Pengguna', 'required');
        $this->form_validation->set_rules('tanggal_berangkat', 'Tanggal Berangkat', 'required');
        $this->form_validation->set_rules('tanggal_kembali', 'Tanggal Kembali', 'required');
        $this->form_validation->set_rules('tujuan', 'Tujuan', 'required');
        $this->form_validation->set_rules('keperluan', 'Keperluan', 'required');
        if ($this->form_validation->run() == FALSE)
        {
        	$authorization = "Authorization: Bearer ".$this->session->userdata('token');
			$url = API_BASE_URL."employee/list/0/9999999";
			$result = curl_api($url,'GET','',$authorization);
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}

			$return = $result->data;
			$this->data["employee"] = $return->data;

			$url = API_BASE_URL."proyek/list/0/9999999";
			$result = curl_api($url,'GET','',$authorization);
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}

			$return = $result->data;
			$this->data["proyeks"] = $return->proyeks;
            $this->load->view('vehicle/order_add',$this->data);
        }
        else{
        	$dtupload = json_encode(array(
				"pengguna" => $pengguna,
				//"waktu_berangkat" => $tgl_berangkat." ".$jam_berangkat.":".$menit_berangkat,
				"waktu_berangkat" => $tgl_berangkat." ".$waktu_berangkat,
				"tgl_kembali" => $tgl_kembali,
				"tujuan" => $tujuan,
				"keperluan" => $keperluan,
				"kode_proyek" => $kdproyek,
				"keterangan" => $keterangan,
				"pemesan" => $this->session->userdata('employeeID')
			));

			$authorization = "Authorization: Bearer ".$this->session->userdata('token');
			$url = API_BASE_URL."order";
			$result = curl_api($url,'POST',$dtupload,$authorization);
			
			if (trim($result->error) != "") {
			    $this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
			else{
				$this->session->set_flashdata('success', 'Data berhasil ditambahkan.');
				redirect('order/list','refresh');
			}
        }
	}

	public function update($oid, $page="carorder") {
		if(!$this->permission->opsk_order)
			redirect('dashboard','refresh');

		$this->data["page"] = $page;

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."employee/list/0/9999999";
		$result = curl_api($url,'GET','',$authorization);
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$return = $result->data;
		$this->data["employee"] = $return->data;

		$url = API_BASE_URL."proyek/list/0/9999999";
		$result = curl_api($url,'GET','',$authorization);
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$return = $result->data;
		$this->data["proyeks"] = $return->proyeks;

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."order/".$oid;
		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$return = $result->data;
		$this->data["employee_id"] = $this->session->userdata('employeeID');
		$this->data["order"] = $return;

		$this->load->view('vehicle/order_update',$this->data);
	}

	public function updatep()
	{
		if(!$this->permission->opsk_order)
			redirect('dashboard','refresh');

		$this->data["page"] = "carorder";

		$order_id = $this->input->post('order_id');
		$pengguna = $this->input->post('pengguna');
		$tgl_berangkat = $this->input->post('tanggal_berangkat');
//		$jam_berangkat = $this->input->post('jam');
//		$menit_berangkat = $this->input->post('menit');
		$tgl_kembali = $this->input->post('tanggal_kembali');
		$tujuan = $this->input->post('tujuan');
		$keperluan = $this->input->post('keperluan');
		$kdproyek = $this->input->post('kdproyek');
		$keterangan = $this->input->post('keterangan');
		$waktu_berangkat = $this->input->post('waktu_berangkat');

		$this->load->library('form_validation');
		$this->form_validation->set_rules('pengguna[]', 'Nama Pengguna', 'required');
		$this->form_validation->set_rules('tanggal_berangkat', 'Tanggal Berangkat', 'required');
		$this->form_validation->set_rules('tanggal_kembali', 'Tanggal Kembali', 'required');
		$this->form_validation->set_rules('tujuan', 'Tujuan', 'required');
		$this->form_validation->set_rules('keperluan', 'Keperluan', 'required');
		if ($this->form_validation->run() == FALSE) {
			$authorization = "Authorization: Bearer " . $this->session->userdata('token');
			$url = API_BASE_URL . "employee/list/0/9999999";
			$result = curl_api($url, 'GET', '', $authorization);
			if (trim($result->error) != "") {
				$this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}

			$return = $result->data;
			$this->data["employee"] = $return->data;

			$url = API_BASE_URL . "proyek/list/0/9999999";
			$result = curl_api($url, 'GET', '', $authorization);
			if (trim($result->error) != "") {
				$this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}

			$return = $result->data;
			$this->data["proyeks"] = $return->proyeks;
			$this->load->view('vehicle/order_add', $this->data);
		} else {
			$dtupload = json_encode(array(
				"id" => $order_id,
				"pengguna" => $pengguna,
				//"waktu_berangkat" => $tgl_berangkat." ".$jam_berangkat.":".$menit_berangkat,
				"waktu_berangkat" => $tgl_berangkat . " " . $waktu_berangkat,
				"tgl_kembali" => $tgl_kembali,
				"tujuan" => $tujuan,
				"keperluan" => $keperluan,
				"kode_proyek" => $kdproyek,
				"keterangan" => $keterangan,
				"pemesan" => $this->session->userdata('employeeID')
			));

			$authorization = "Authorization: Bearer " . $this->session->userdata('token');
			$url = API_BASE_URL . "order";
			$result = curl_api($url, 'PUT', $dtupload, $authorization);

			if (trim($result->error) != "") {
				$this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			} else {
				$this->session->set_flashdata('success', 'Data berhasil diubah.');
				redirect('order/list', 'refresh');
			}
		}
	}

	public function delete() {
		if(!$this->permission->opsk_order)
			redirect('dashboard','refresh');

		$id = $this->input->post('id');

		$authorization = "Authorization: Bearer " . $this->session->userdata('token');
		$url = API_BASE_URL . "order/" . $id;
		$result = curl_api($url, 'DELETE', '', $authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		} else {
			$this->session->set_flashdata('success', 'Data berhasil dihapus.');
			redirect('order/list', 'refresh');
		}
	}


	public function detail($oid, $page="carorder" ,$mode = "na"){
		$this->data["page"] = $page;
        $this->data["mode"] = $mode;

		$pid = $this->session->userdata('employeeID');

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."order/".$oid;
		$result = curl_api($url,'GET','',$authorization);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$return = $result->data;
		$this->data["employee_id"] = $this->session->userdata('employeeID');
		$this->data["order"] = $return;

		// normalize order->hp_driver dalam bentuk internasional tanpa "+"
		$hp_driver = $return->hp_driver;
		if ($hp_driver != null) {
			// cek apakah hp_driver dimulai dengan "+"
			if (substr($hp_driver, 0, 1) == "+") {
				// hapus "+" di depan
				$hp_driver = substr($hp_driver, 1);
			}
			// cek apakah hp_driver dimulai dengan "0"
			if (substr($hp_driver, 0, 1) == "0") {
				// hapus "0" di depan
				$hp_driver = substr($hp_driver, 1);
				// tambahkan "62" di depan
				$hp_driver = "62".$hp_driver;
			}
			$this->data["hp_driver"] = $hp_driver;
		}

		if ($return->need_approve_id == $pid) {
			// insert vehicles data
			$url = API_BASE_URL."master/vehicle/list";
			$result = curl_api($url,'GET','',$authorization);
			if (trim($result->error) != "") {
				$this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
			$return = $result->data;
			$this->data["vehicles"] = $return->vehicles;
		}

		$this->load->view('vehicle/order_detail',$this->data);
	}

	public function rekap(){
		$this->data["page"] = "carorderrekap";

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$pid = $this->session->userdata('employeeID');
		$url = API_BASE_URL."order/appga";
		$result = curl_api($url,'GET','',$authorization);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$return = $result->data;
		$this->data["rekap"] = $return->orders;

		$this->load->view('vehicle/rekap_list',$this->data);
	}

	

	public function updaterekap($order_id) {
		$this->data["page"] = "updateorder";
	
		$pid = $this->session->userdata('employeeID');

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');
		$url = API_BASE_URL."order/".$order_id;
		$result = curl_api($url,'GET','',$authorization);
		
		if (trim($result->error) != "") {
		    $this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$return = $result->data;
		$this->data["employee_id"] = $this->session->userdata('employeeID');
		$this->data["order"] = $return;

		// normalize order->hp_driver dalam bentuk internasional tanpa "+"
		$hp_driver = $return->hp_driver;
		if ($hp_driver != null) {
			// cek apakah hp_driver dimulai dengan "+"
			if (substr($hp_driver, 0, 1) == "+") {
				// hapus "+" di depan
				$hp_driver = substr($hp_driver, 1);
			}
			// cek apakah hp_driver dimulai dengan "0"
			if (substr($hp_driver, 0, 1) == "0") {
				// hapus "0" di depan
				$hp_driver = substr($hp_driver, 1);
				// tambahkan "62" di depan
				$hp_driver = "62".$hp_driver;
			}
			$this->data["hp_driver"] = $hp_driver;
		}
		$url = API_BASE_URL."master/vehicle/list";
		$result = curl_api($url,'GET','',$authorization);
		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}
		$return = $result->data;
		$this->data["vehicles"] = $return->vehicles;
		$this->load->view('vehicle/rekap_update', $this->data);
	}
	public function updatedriver() {
		$oid = $this->input->post("order_id");
		$driver = $this->input->post("driver");
		$no_hp_driver = $this->input->post("no_hp_driver");
		$vehicle_id = $this->input->post("kendaraan");
	
		// Log received data for debugging
		log_message('debug', 'Order ID: ' . $oid);
		log_message('debug', 'Driver: ' . $driver);
		log_message('debug', 'No HP Driver: ' . $no_hp_driver);
		log_message('debug', 'Vehicle ID: ' . $vehicle_id);
	
		if ($vehicle_id == '-1') {
			// If "Lainnya" is selected
			$dtupload = json_encode(array(
				"order_id" => $oid,
				"approval_id" => $this->session->userdata('employeeID'),
				"assignedDriverName" => $driver,
				"assignedDriverPhone" => $no_hp_driver,
				"assignedOtherPlatNumber" => $this->input->post("nopol"),
				"assignedOtherMerk" => $this->input->post("merk"),
				"assignedOtherTipe" => $this->input->post("tipe"),
				"assignedOtherTahun" => $this->input->post("tahun"),
				"assignedOtherBBM" => $this->input->post("bbm"),
				"assignedOtherPemilik" => $this->input->post("pemilik"),
				"assignedOtherPKB" => $this->input->post("pkb"),
				"assignedOtherKeterangan" => $this->input->post("ket"),
			));
		} else {
			$dtupload = json_encode(array(
				"order_id" => $oid,
				"approval_id" => $this->session->userdata('employeeID'),
				"assignedVehicleId" => $vehicle_id,
				"assignedDriverName" => $driver,
				"assignedDriverPhone" => $no_hp_driver,
			));
		}
	
		// Log data to be sent for debugging
		log_message('debug', 'Data to send to API: ' . $dtupload);
	
		$url = API_BASE_URL . "order/updatedriver";
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');
		
		// Use curl to send the API request
		$result = $this->curl_api($url, 'PUT', $dtupload, $authorization);
	
		// Log API response for debugging
		log_message('debug', 'API Response: ' . json_encode($result));
	
		if (isset($result->error) && trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->data->msg);
			redirect('info/error');
		} else {
			$this->session->set_flashdata('info', 'Document Order Kendaraan ' . $oid . ' sudah berhasil Update.');
			redirect('order/rekap');
		}
	}
	
	private function curl_api($url, $method, $data, $authorization) {
		$ch = curl_init();
	
		// Set cURL options
		curl_setopt($ch, CURLOPT_URL, $url);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
		curl_setopt($ch, CURLOPT_CUSTOMREQUEST, $method);
	
		// Add authorization header
		curl_setopt($ch, CURLOPT_HTTPHEADER, array(
			'Content-Type: application/json',
			$authorization
		));
	
		// Add payload
		curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
	
		// Execute the request
		$response = curl_exec($ch);
	
		// Check for errors
		if (curl_errno($ch)) {
			log_message('error', 'cURL error: ' . curl_error($ch));
			return null;
		}
	
		curl_close($ch);
	
		// Decode the response
		return json_decode($response);
	}

	public function download($order_id){
		$this->data["page"] = "carorderrekap";

		$url = API_BASE_URL."dow/word/".$order_id;
	
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
       
		header('Content-disposition: attachment; filename=Form_Order_Kendaraan.docx');
		header('Content-type: application/vnd.openxmlformats-officedocument.wordprocessingml.document');
		header('Content-Transfer-Encoding: binary');
		header('Cache-Control: must-revalidate');
		header('Pragma: public');
		//ob_clean();
		flush(); 

        echo $file;


	}

	public function done($oid) {
		if(!$this->permission->opsk_order)
			redirect('dashboard','refresh');

		$this->data["page"] = "carorderrekap";


		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$url = API_BASE_URL."order/".$oid;
		$result = curl_api($url,'GET','',$authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$return = $result->data;
		$this->data["employee_id"] = $this->session->userdata('employeeID');
		$this->data["order"] = $return;

		$this->load->view('vehicle/rekap_done',$this->data);
	}

	public function donep()
	{
		if(!$this->permission->opsk_order)
			redirect('dashboard','refresh');

		$this->data["page"] = "carorderrekap";

		$order_id = $this->input->post('order_id');
		$tgl_kembali = $this->input->post('tanggal_kembali');
		$jam_kembali = $this->input->post('jam_kembali');


		$this->load->library('form_validation');
		$this->form_validation->set_rules('tanggal_kembali', 'Tanggal Kembali', 'required');
		$this->form_validation->set_rules('jam_kembali', 'Jam Kembali', 'required');
		
			$dtupload = json_encode(array(
				"id" => $order_id,
				"tgl_kembali" => $tgl_kembali,
				"jam_kembali" => $jam_kembali
			));

			$authorization = "Authorization: Bearer " . $this->session->userdata('token');
			$url = API_BASE_URL . "order/done";
			$result = curl_api($url, 'PUT', $dtupload, $authorization);

			if (trim($result->error) != "") {
				$this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			} else {
				$this->session->set_flashdata('success', 'Pemesanan Selesai.');
				redirect('order/rekap', 'refresh');
			}
		}
	
		public function rekapDetail($oid, $page="carorderrekap" ,$mode = "na"){
			$this->data["page"] = $page;
			$this->data["mode"] = $mode;
	
			$pid = $this->session->userdata('employeeID');
	
			$authorization = "Authorization: Bearer ".$this->session->userdata('token');
			$url = API_BASE_URL."order/".$oid;
			$result = curl_api($url,'GET','',$authorization);
			
			if (trim($result->error) != "") {
				$this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}
	
			$return = $result->data;
			$this->data["employee_id"] = $this->session->userdata('employeeID');
			$this->data["order"] = $return;
	
			// normalize order->hp_driver dalam bentuk internasional tanpa "+"
			$hp_driver = $return->hp_driver;
			if ($hp_driver != null) {
				// cek apakah hp_driver dimulai dengan "+"
				if (substr($hp_driver, 0, 1) == "+") {
					// hapus "+" di depan
					$hp_driver = substr($hp_driver, 1);
				}
				// cek apakah hp_driver dimulai dengan "0"
				if (substr($hp_driver, 0, 1) == "0") {
					// hapus "0" di depan
					$hp_driver = substr($hp_driver, 1);
					// tambahkan "62" di depan
					$hp_driver = "62".$hp_driver;
				}
				$this->data["hp_driver"] = $hp_driver;
			}
	
			if ($return->need_approve_id == $pid) {
				// insert vehicles data
				$url = API_BASE_URL."master/vehicle/list";
				$result = curl_api($url,'GET','',$authorization);
				if (trim($result->error) != "") {
					$this->session->set_flashdata('error', $result->error);
					redirect('info/error');
				}
				$return = $result->data;
				$this->data["vehicles"] = $return->vehicles;
			}
	
			$this->load->view('vehicle/rekap_detail',$this->data);
		}
	
	


}


	

