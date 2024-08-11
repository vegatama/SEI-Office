<?php

class Info extends Sei_Controller{
	public function __construct()
	{
		parent::__construct();
	}

	public function error(){
		$this->data["page"] = "error";
		$this->load->view('info_error',$this->data);
	}

	public function show(){
		$this->data["page"] = "error";
		$this->load->view('info_show',$this->data);
	}
}