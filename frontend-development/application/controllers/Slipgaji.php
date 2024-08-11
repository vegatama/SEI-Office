<?php
class Slipgaji extends Sei_Controller
{
	public function __construct()
	{
		parent::__construct();
	}

	public function pdf($empcode, $id)
	{
		$url = API_BASE_URL . "slipgaji/pdf/" . $id . "/" . $empcode;

		$options = array(
			'http' => array(
				'method' => "GET",
				'header' => array(
					'Authorization: Bearer ' . $this->session->userdata('token'),
				),
				'ignore_errors' => true,
			)
		);

		$context = stream_context_create($options);
		$file = file_get_contents($url, false, $context);

		header('Content-type: application/pdf');
		header('Content-Transfer-Encoding: binary');
		header('Cache-Control: must-revalidate');
		header('Pragma: public');
		//ob_clean();
		flush();

		echo $file;
	}

	public function downloadTemplate($id)
	{
		$url = API_BASE_URL . "slipgaji/download/" . $id;

		$options = array(
			'http' => array(
				'method' => "GET",
				'header' => array(
					'Authorization: Bearer ' . $this->session->userdata('token'),
				),
				'ignore_errors' => true,
			)
		);

		$context = stream_context_create($options);
		$file = file_get_contents($url, false, $context);

		header('Content-disposition: attachment; filename=TemplateSlipgaji_' . $id . '.xlsx');
		header('Content-type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
		header('Content-Transfer-Encoding: binary');
		header('Cache-Control: must-revalidate');
		header('Pragma: public');
		//ob_clean();
		flush();

		echo $file;
	}

	public function updateTemplate($id = null, $mode = null)
	{
		$this->data["page"] = "slipgaji";
		if (isset($id)) {
			$this->data["id"] = $id;
		}

		$repopulate_mode = $this->session->userdata('repopulate_mode');
		$repopulate_name = $this->session->userdata('repopulate_name');
		$repopulate_tahun = $this->session->userdata('repopulate_tahun');
		$repopulate_bulan = $this->session->userdata('repopulate_bulan');
		$repopulate_fields = $this->session->userdata('repopulate_fields');

		if (isset($id)) {
			$url = API_BASE_URL . "slipgaji/detail/" . $id;
			$authorization = "Authorization: Bearer " . $this->session->userdata('token');

			$result = curl_api($url, 'GET', '', $authorization);

			if (trim($result->error) != "") {
				$this->session->set_flashdata('error', $result->error);
				redirect('info/error');
			}

			$this->data["data"] = $result->data;

			if ($mode == "duplicate") {
				$this->data["mode"] = "duplicate";
			} else {
				$this->session->unset_userdata('repopulate_mode');
				$this->session->unset_userdata('repopulate_name');
				$this->session->unset_userdata('repopulate_tahun');
				$this->session->unset_userdata('repopulate_bulan');
				$this->session->unset_userdata('repopulate_fields');
				$this->data["mode"] = "edit";
			}
		} else if (isset($repopulate_tahun) && isset($repopulate_bulan) && isset($repopulate_fields)) {
			$parsed_fields = [];
			foreach ($repopulate_fields as $field) {
				$parsed = json_decode($field);
				if (isset($parsed->id)) {
					$parsed_fields[] = array(
						"id" => $parsed->id,
						"name" => $parsed->name,
						"order" => $parsed->order,
						"category" => $parsed->category
					);
				} else {
					$parsed_fields[] = array(
						"name" => $parsed->name,
						"order" => $parsed->order,
						"category" => $parsed->category
					);
				}
			}
			$this->data["data"] = (object) array(
				"name" => $repopulate_name,
				"tahun" => $repopulate_tahun,
				"bulan" => $repopulate_bulan,
				"fields" => $parsed_fields
			);
			$this->data["mode"] = $repopulate_mode;
		} else {
			$this->data["mode"] = "add";
		}

		$url = API_BASE_URL . "slipgaji";
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');

		$result = curl_api($url, 'GET', '', $authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		if (isset($result->data->templates)) {
			$existingDates = array();
			foreach ($result->data->templates as $data) {
				$existingDates[] = (object) array(
					"tahun" => $data->tahun,
					"bulan" => $data->bulan,
					"name" => $data->name,
					"id" => $data->id
				);
			}
			$this->data["existingDates"] = $existingDates;
		}

		$this->load->view('emp/edit_slipgaji', $this->data);
	}

	public function detail($id)
	{
		$this->data["page"] = "slipgaji";

		$url = API_BASE_URL . "slipgaji/data/" . $id;
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');

		$result = curl_api($url, 'GET', '', $authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$this->data["dataslip"] = $result->data;

		$this->load->view('emp/detail_slipgaji', $this->data);
	}

	public function update($mode = "edit")
	{
		if (!$this->permission->dokumen)
			redirect('dokumen', 'refresh');
		$this->session->unset_userdata('repopulate_mode');
		$this->session->unset_userdata('repopulate_name');
		$this->session->unset_userdata('repopulate_tahun');
		$this->session->unset_userdata('repopulate_bulan');
		$this->session->unset_userdata('repopulate_fields');

		$id = $this->input->post("id");
		$name = $this->input->post("name");
		$bulan = $this->input->post("bulan");
		$tahun = $this->input->post("tahun");
		$fields = $this->input->post("fields"); // array of json (in string)

		$parsed_fields = array();
		if (isset($fields)) {
			foreach ($fields as $field) {
				$parsed = json_decode($field);
				if (isset($parsed->id)) {
					$parsed_fields[] = array(
						"id" => $parsed->id,
						"name" => $parsed->name,
						"order" => $parsed->order,
						"category" => $parsed->category
					);
				} else {
					$parsed_fields[] = array(
						"name" => $parsed->name,
						"order" => $parsed->order,
						"category" => $parsed->category
					);
				}
			}
		}
		$data = array(
			"id" => $id,
			"name" => $name,
			"tahun" => $tahun,
			"bulan" => $bulan,
			"fields" => $parsed_fields
		);
		$data = json_encode($data);
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');
		$url = API_BASE_URL . "slipgaji/update";
		$result = curl_api($url, 'POST', $data, $authorization);
		if (isset($result->data) && $result->data->msg == "SUCCESS") {
			if ($mode == "add") {
				$this->session->set_userdata('info', 'Data berhasil ditambahkan.');
			} else {
				$this->session->set_userdata('info', 'Data berhasil diupdate.');
			}
			redirect('karyawan/slipgaji', 'refresh');
		} else {
			if (isset($result->data)) {
				if ($result->data->msg == "ERROR: ALREADY EXISTS") {
					$result->data->msg = "Data sudah ada.";
				}
				if ($result->data->msg == "ERROR: NO CHANGES") {
					if ($mode == "add") {
						$this->session->set_userdata('info', 'Data kosong. Tidak ada data yang ditambahkan.');
					} else {
						$this->session->set_userdata('info', 'Tidak ada perubahan.');
					}
					redirect('karyawan/slipgaji', 'refresh');
					return;
				}
				$this->session->set_userdata('error', $result->data->msg);
			}
			if (trim($result->error) != "") {
				$this->session->set_userdata('error', $result->error);
			}
			$this->session->set_userdata('repopulate_mode', $mode);
			$this->session->set_userdata('repopulate_name', $name);
			$this->session->set_userdata('repopulate_tahun', $tahun);
			$this->session->set_userdata('repopulate_bulan', $bulan);
			$this->session->set_userdata('repopulate_fields', $fields);
			if ($mode == "add") {
				redirect('slipgaji/updateTemplate/', 'refresh');
			} else if ($mode == "duplicate") {
				redirect('slipgaji/updateTemplate/' . $id . '/duplicate', 'refresh');
			} else {
				redirect('slipgaji/updateTemplate/' . $id, 'refresh');
			}
		}
	}

	public function upload($id)
	{
		if (!$this->permission->dokumen)
			redirect('dokumen', 'refresh');

		$this->data["page"] = "slipgaji";

		$url = API_BASE_URL . "slipgaji/detail/" . $id;
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');

		$result = curl_api($url, 'GET', '', $authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		}

		$this->data["id"] = $id;
		$this->data["tahun"] = $result->data->tahun;
		$this->data["bulan"] = $result->data->bulan;
		$this->data["name"] = $result->data->name;

		$this->load->view('emp/upload_slipgaji', $this->data);
	}

	public function import($id)
	{

		$this->data["page"] = "slipgaji";

		if ($_FILES['inputFile']['tmp_name'] === "" || $_FILES['inputFile']['tmp_name'] == null) {
			$this->session->set_flashdata('info', 'Form File Kosong.');
			redirect('slipgaji/upload/' . $id, 'refresh');
		} else {
			$document = new CURLFile($_FILES['inputFile']['tmp_name'], $_FILES['inputFile']['type'], $_FILES['inputFile']['name']);

			$curl = curl_init();

			curl_setopt_array($curl, array(
				CURLOPT_URL => API_BASE_URL . "slipgaji/upload/" . $id,
				CURLOPT_RETURNTRANSFER => true,
				CURLOPT_ENCODING => '',
				CURLOPT_MAXREDIRS => 10,
				CURLOPT_TIMEOUT => 0,
				CURLOPT_FOLLOWLOCATION => true,
				CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
				CURLOPT_CUSTOMREQUEST => 'POST',
				CURLOPT_POSTFIELDS => array(
					'file' => $document
				),
				CURLOPT_HTTPHEADER => array(
					'Authorization: Bearer ' . $this->session->userdata('token'),
					'Content-Type: multipart/form-data'
				),
			)
			);

			$response = curl_exec($curl);
			if (curl_errno($curl)) {
				$error_msg = curl_error($curl);
			}
			curl_close($curl);

			$message = json_decode($response);

			if (isset($error_msg)) {
				$this->session->set_flashdata('error', $error_msg);
				redirect('info/error');
			} else {
				if ($message->msg === "SUCCESS") {
					$this->session->set_flashdata('info', 'Data berhasil dimport.');
					redirect('karyawan/slipgaji', 'refresh');
				} else {
					if ($message->msg == "ERROR: TEMPLATE VERSION MISMATCH") {
						$message->msg = "Versi template tidak sesuai.";
					}
					if ($message->msg == "ERROR: TEMPLATE NOT FOUND") {
						$message->msg = "Template tidak ditemukan.";
					}
					$this->session->set_flashdata('info', $message->msg);
					redirect('karyawan/slipgaji', 'refresh');
				}
			}
		}
	}

	public function kirim($id)
	{
		$this->data["page"] = "slipgaji";

		$url = API_BASE_URL . "slipgaji/send/" . $id;
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');

		$result = curl_api($url, 'GET', '', $authorization);

		if (trim($result->error) != "") {
			$this->session->set_flashdata('error', $result->error);
			redirect('info/error');
		} else if ($result->data->message != "SUCCESS") {
			$this->session->set_flashdata('error', $result->data->message);
			redirect('info/error');
		} else {
			$employeeSent = $result->data->employeeSent;
			if ($employeeSent == 0) {
				$this->session->set_flashdata('info', 'Semua karyawan sudah mendapatkan slip gaji, tidak ada slip gaji yang dikirim.');
				redirect('karyawan/slipgaji', 'refresh');
			}
			$this->session->set_flashdata('info', 'Slip Gaji Berhasil Dikirim ke ' . $employeeSent . ' Karyawan. ');
			redirect('karyawan/slipgaji', 'refresh');
		}
	}
}
