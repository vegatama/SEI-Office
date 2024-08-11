<?php 

class Sei_Controller extends CI_Controller{
	public $access = array();
    public $test;
    public $data;
    public $permission;

	public function __construct()
	{
		parent::__construct();
        date_default_timezone_set('Asia/Jakarta');
		if($this->session->userdata('isLogin') != TRUE){
            $this->session->set_userdata(array(
                "loginRedirect" => $this->uri->uri_string()
            ));
            redirect('login','refresh');
        }
        
        /*if($this->session->userdata('create_password'))
        	redirect('profil/newpassword');*/
        
        $this->data = array();
        if($this->session->flashdata('error') != NULL)
			$this->data['error'] = $this->session->flashdata('error');
		if($this->session->flashdata('info') != NULL)
			$this->data['info'] = $this->session->flashdata('info');
        if($this->session->flashdata('success') != NULL)
			$this->data['success'] = $this->session->flashdata('success');

		// PATCH, flashdata sometimes not being persistent
		// to patch this, use userdata instead of flashdata
		if ($this->session->userdata('error') != NULL) {
			$this->data['error'] = $this->session->userdata('error');
			$this->session->unset_userdata('error');
		} else if ($this->session->userdata('info') != NULL) {
			$this->data['info'] = $this->session->userdata('info');
			$this->session->unset_userdata('info');
		} else if ($this->session->userdata('success') != NULL) {
			$this->data['success'] = $this->session->userdata('success');
			$this->session->unset_userdata('success');
		}
		// END PATCH

        $this->access = array(
        	"master" => false,
        	"karyawan" => false,
        	"libur" => false,
            "lokasi" => false,
            "cuti" => false,
        	"absensi" => false,
        	"rekapabsen" => false,
        	"absensaya" => false,
            "employee" => false,
            "kontraklist" => false,
            "kontrakeval" => false,
            "empabsen" => false,
            "dashkaryawan" => false,
            "absenmentah" => false,
            "izincuti" => false,
            "jatahcuti" => false,
            "ketidakhadiran" => false,
            "proyek" => false,
            "dashproyek" => false,
            "budget" => false,
            "dpb" => false,
            "purchase" => false,
            "nav" => false,
            "mobil" => false,
            "dashcar" => false,
            "carorder" => false,
            "carorderrekap" => false,
            "carunit" => false,
            "daftarhadir" => false,
            "roles" => false
        );

        $dtaccess = $this->session->userdata("access");
        if($dtaccess != NULL){
        	//echo "masuk sini";
        	$dt = json_decode($dtaccess);
        	if(isset($dt->master))
        		$this->access["master"] = $dt->master;
        	if(isset($dt->karyawan))
        		$this->access["karyawan"] = $dt->karyawan;
        	if(isset($dt->libur))
        		$this->access["libur"] = $dt->libur;
            if(isset($dt->lokasi))
        		$this->access["lokasi"] = $dt->lokasi;
            if(isset($dt->lokasi))
        		$this->access["cuti"] = $dt->cuti;
        	if(isset($dt->absensi))
        		$this->access["absensi"] = $dt->absensi;
        	if(isset($dt->rekapabsen))
        		$this->access["rekapabsen"] = $dt->rekapabsen;
        	if(isset($dt->absensaya))
        		$this->access["absensaya"] = $dt->absensaya;
            if(isset($dt->employee))
                $this->access["employee"] = $dt->employee;
            if(isset($dt->kontraklist))
                $this->access["kontraklist"] = $dt->kontraklist;
            if(isset($dt->kontrakeval))
                $this->access["kontrakeval"] = $dt->kontrakeval;
            if(isset($dt->empabsen))
                $this->access["empabsen"] = $dt->empabsen;
            if(isset($dt->dashkaryawan))
                $this->access["dashkaryawan"] = $dt->dashkaryawan;
            if(isset($dt->absenmentah))
                $this->access["absenmentah"] = $dt->absenmentah;
            if(isset($dt->izincuti))
                $this->access["izincuti"] = $dt->izincuti;
            if(isset($dt->jatahcuti))
                $this->access["jatahcuti"] = $dt->jatahcuti;
            if(isset($dt->ketidakhadiran))
                $this->access["ketidakhadiran"] = $dt->ketidakhadiran;
            if(isset($dt->proyek))
                $this->access["proyek"] = $dt->proyek;
            if(isset($dt->dashproyek))
                $this->access["dashproyek"] = $dt->dashproyek;
            if(isset($dt->budget))
                $this->access["budget"] = $dt->budget;
            if(isset($dt->dpb))
                $this->access["dpb"] = $dt->dpb;
            if(isset($dt->purchase))
                $this->access["purchase"] = $dt->purchase;
            if(isset($dt->nav))
                $this->access["nav"] = $dt->nav;
            if(isset($dt->mobil))
                $this->access["mobil"] = $dt->mobil;
            if(isset($dt->dashcar))
                $this->access["dashcar"] = $dt->dashcar;
            if(isset($dt->carorder))
                $this->access["carorder"] = $dt->carorder;
            if(isset($dt->carorderrekap))
                $this->access["carorderrekap"] = $dt->carorderrekap;
            if(isset($dt->carunit))
                $this->access["carunit"] = $dt->carunit;
            if(isset($dt->daftarhadir))
                $this->access["daftarhadir"] = $dt->daftarhadir;
            if(isset($dt->roles))
                $this->access["roles"] = $dt->roles;
        }

        $this->data["akses"] = $this->access;
        $this->data["permission"] = json_decode($this->session->userdata("permission"));
        $this->permission = $this->data["permission"];
	}
}
