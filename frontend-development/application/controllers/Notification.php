<?php

class Notification extends Sei_Controller {
	public function history() {
		$emp = $this->session->userdata('empCode');
		$url = API_BASE_URL . "notification/history?empCode=" . $emp;

		$authorization = "Authorization: Bearer ".$this->session->userdata('token');

		$result = curl_api($url, 'GET', '', $authorization);

		echo json_encode($result->data);
	}

	public function list()
	{
		$after = $this->input->get('after');
		$empCode = $this->session->userdata('empCode');
		$url = API_BASE_URL . "notification/list?empCode=" . $empCode . "&after=" . $after;

		$authorization = "Authorization: Bearer " . $this->session->userdata('token');

		$result = curl_api($url, 'GET', '', $authorization);

		echo json_encode($result->data);
	}
	public function markAsRead()
	{
		$id = $this->input->get('id');
		$empCode = $this->session->userdata('empCode');
		$url = API_BASE_URL . "notification/read?empCode=" . $empCode . "&id=" . $id;
		$authorization = "Authorization: Bearer " . $this->session->userdata('token');

		$result = curl_api($url, 'GET', '', $authorization);
		echo json_encode($result->data);
	}

}
