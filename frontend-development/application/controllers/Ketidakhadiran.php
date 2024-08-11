<?php

class Ketidakhadiran extends Sei_Controller {
	public function __construct()
	{
		parent::__construct();
	}

	public function rekap()
	{
		$this->data["page"] = "ketidakhadiran";		
	}
}