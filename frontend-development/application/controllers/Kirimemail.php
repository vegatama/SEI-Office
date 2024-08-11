<?php

class Kirimemail extends Sei_Controller {
	public function __construct()
	{
		parent::__construct();
	}

	public function test()
	{
		$this->load->library('email');
		$config['protocol'] = 'smtp';
		$config['smtp_host'] = 'smtp.kirimemail.com';
		$config['smtp_user'] = 'sdmu@suryaenergi.my.id';
		$config['smtp_pass'] = 'D8MzUfRNS8';
		$config['smtp_port'] = 587;
		$config['smtp_crypto'] = 'tls';
		$config['charset'] = 'iso-8859-1';
		$config['wordwrap'] = TRUE;

		$this->email->initialize($config);

		$this->email->from('sdmu@suryaenergi.my.id', 'SDM dan Umum');
		$this->email->to('azmifauzan@gmail.com');

		$this->email->subject('subject email test');
		$this->email->message('Testing the email class3.');

		if ( !$this->email->send())
		{
		    $this->email->print_debugger(array('headers'));
		}
		echo "email send.";
	}
}