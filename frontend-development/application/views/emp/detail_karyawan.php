<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Data Karyawan</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item">Karyawan</li>
          <li class="breadcrumb-item active">Detail</li>
        </ol>
      </div><!-- /.col -->
    </div><!-- /.row -->
  </div><!-- /.container-fluid -->
</div>
<!-- /.content-header -->

<!-- Main content -->
<section class="content">
<div class="content">
  <div class="container-fluid">    

    <?php echo form_open('profil/pupdate'); ?>
    <div class="row">
      <div class="col-md-6">
        <?php if($profil->avatar != ""): ?>
        <!-- Profile Image -->
        <div class="card card-primary card-outline">

            <div class="card-body box-profile">
            <div class="text-center">
                <img class="profile-user-img img-fluid img-circle"
                    src="<?php echo $profil->avatar; ?>"
                    alt="User profile picture">
            </div>

            <h3 class="profile-username text-center"><?php echo $profil->name; ?></h3>
            </div>
            <!-- /.card-body -->
        </div>
        <?php endif; ?>
        <div class="card card-primary">

          <div class="card-header">
            <h3 class="card-title">PERSONAL INFO</h3>
          </div>
          <!-- /.card-header -->
          
          
          <div class="card-body">

            <div class="form-group">
              <label for="exampleInputEmail1">NIK</label>
              <?php echo form_error('nik','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="nik" class="form-control" required placeholder="NIK" value="<?php echo $profil->nik; ?>" readonly>
            </div>
            <div class="form-group">
              <label for="exampleInputEmail1">First Name</label>
              <?php echo form_error('firstname','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="firstname" class="form-control" required placeholder="First Name" value="<?php echo $profil->first_name; ?>">
            </div>
            <div class="form-group">
              <label for="exampleInputPassword1">Middle Name</label>
              <?php echo form_error('middlename','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="middlename" class="form-control" required placeholder="Middle Name" value="<?php echo $profil->middle_name; ?>">
            </div>
            <div class="form-group">
              <label for="exampleInputPassword1">Last Name</label>
              <?php echo form_error('lastname','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="lastname" class="form-control" required placeholder="Last Name" value="<?php echo $profil->last_name; ?>">
            </div>
            <div class="form-group">
              <label for="exampleInputPassword1">Gender</label>
              <?php echo form_error('gender','<div class="alert alert-warning">','</div>'); ?>
              <select name="gender" class="form-control">
                <option value="MALE" <?php if($profil->gender == "Male") echo "selected" ?>>MALE</option>
                <option value="FEMALE" <?php if($profil->gender == "Female") echo "selected" ?>>FEMALE</option>
              </select>
            </div>
            <div class="form-group">
              <label for="exampleInputPassword1">Religion</label>
              <?php echo form_error('religion','<div class="alert alert-warning">','</div>'); ?>
              <select name="gender" class="form-control">
                <option value="Muslim" <?php if($profil->religion == "Muslim") echo "selected" ?>>Muslim</option>
                <option value="Kristen" <?php if($profil->religion == "Kristen") echo "selected" ?>>Kristen</option>
                <option value="Katolik" <?php if($profil->religion == "Katolik") echo "selected" ?>>Katolik</option>
                <option value="Hindu" <?php if($profil->religion == "Hindu") echo "selected" ?>>Hindu</option>
                <option value="Buddha" <?php if($profil->religion == "Buddha") echo "selected" ?>>Buddha</option>
                <option value="Konghucu" <?php if($profil->religion == "Konghucu") echo "selected" ?>>Konghucu</option>
              </select>
            </div>
            <div class="form-group">
              <label for="exampleInputPassword1">Place Birth Date</label>
              <?php echo form_error('placebirthdate','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="placebirthdate" class="form-control" required placeholder="Place Birth Date" value="<?php echo $profil->place_birthdate; ?>">
            </div>
            <div class="row">              
              <div class="col-sm-6">
                <div class="form-group">
                  <label for="exampleInputPassword1">Birth Date</label>
                  <?php echo form_error('birthdate','<div class="alert alert-warning">','</div>'); ?>
                  <input type="text" name="birthdate" class="form-control" id="datepicker" readonly required placeholder="Birth Date" value="<?php echo $profil->birthday; ?>">
                </div>
              </div>
              <div class="col-sm-6">
                <div class="form-group">
                  <label for="exampleInputPassword1">Age</label>
                  <input type="text" name="placebirthdate" class="form-control" disabled placeholder="Age" id="age" value="<?php echo $age; ?>">
                </div>
              </div>
            </div>

            <div class="form-group">
              <label for="exampleInputPassword1">Address</label>
              <?php echo form_error('address','<div class="alert alert-warning">','</div>'); ?>
              <textarea class="form-control" rows="3" name="Address"><?php echo $profil->address; ?></textarea>
            </div>
            <div class="form-group">
              <label for="exampleInputPassword1">City</label>
              <?php echo form_error('city','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="city" class="form-control" required placeholder="City" value="<?php echo $profil->city; ?>">
            </div>
            <div class="form-group">
              <label for="exampleInputPassword1">Post Code</label>
              <?php echo form_error('postcode','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="postcode" class="form-control" required placeholder="Post Code" value="<?php echo $profil->post_code; ?>">
            </div>

            <div class="form-group">
              <label for="exampleInputPassword1">Phone</label>
              <?php echo form_error('phone','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="phone" class="form-control" required placeholder="Phone Number" value="<?php echo $profil->mobile_phone_no; ?>">
            </div>
            <div class="form-group">
              <label for="exampleInputPassword1">Email</label>
              <?php echo form_error('email','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="email" class="form-control" required placeholder="Email" value="<?php echo $profil->email; ?>">
            </div>
            <div class="form-group">
              <label for="exampleInputPassword1">Golongan Darah</label>
              <?php echo form_error('goldarah','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="goldarah" class="form-control" required placeholder="Golongan Darah" value="<?php echo $profil->gol_darah; ?>">
            </div>

            <div class="form-group">
              <label for="exampleInputPassword1">Status Pernikahan</label>
              <?php echo form_error('statusnikah','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="statusnikah" class="form-control" required placeholder="Status Pernikahan" value="<?php echo $profil->status_pernikahan; ?>">
            </div>
            <div class="form-group">
              <label for="exampleInputPassword1">NPWP</label>
              <?php echo form_error('npwp','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="npwp" class="form-control" required placeholder="NPWP" value="<?php echo $profil->npwp; ?>">
            </div>

          </div>         

        </div>  

        <div class="card card-info">

          <div class="card-header">
            <h3 class="card-title">KONTAK DARURAT</h3>
          </div>
          <!-- /.card-header -->
          <div class="card-body">
            <div class="form-group">
              <label for="exampleInputEmail1">No. Telp</label>
              <?php echo form_error('notelpdarurat','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="notelpdarurat" class="form-control" required placeholder="No. TELP DARURAT" value="<?php echo $profil->no_telp_darurat; ?>">
            </div>
            <div class="form-group">
              <label for="exampleInputEmail1">Status</label>
              <?php echo form_error('statusdarurat','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="statusdarurat" class="form-control" required placeholder="STATUS TELP DARURAT" value="<?php echo $profil->status_darurat; ?>">
            </div>
          </div>
        </div>

        <div class="card card-success">

          <div class="card-header">
            <h3 class="card-title">PENDIDIKAN</h3>
          </div>
          <!-- /.card-header -->
          <div class="card-body">
            <div class="form-group">
              <label for="exampleInputEmail1">Pendidikan Terakhir</label>
              <?php echo form_error('notelpdarurat','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="notelpdarurat" class="form-control" required placeholder="Pendidikan Terkahir" value="<?php echo $profil->pendidikan_terakhir; ?>">
            </div>
            <div class="form-group">
              <label for="exampleInputEmail1">Jurusan Pendidikan</label>
              <?php echo form_error('jurusanpendidikan','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="jurusanpendidikan" class="form-control" required placeholder="Jurusan Pendidikan" value="<?php echo $profil->jurusan_pendidikan; ?>">
            </div>
            <div class="form-group">
              <label for="exampleInputEmail1">Spesialis Pendidikan</label>
              <?php echo form_error('spesialispendidikan','<div class="alert alert-warning">','</div>'); ?>
              <select name="spesialispendidikan" class="form-control">
                <option value="Teknik" <?php if($profil->spesialis_pendidikan == "Teknik") echo "selected"; ?>>Teknik</option>
                <option value="Non Teknik" <?php if($profil->spesialis_pendidikan == "Non Teknik") echo "selected"; ?>>Non Teknik</option>
              </select>
            </div>
            <div class="form-group">
              <label for="exampleInputEmail1">Institusi Pendidikan</label>
              <?php echo form_error('institusipendidikan','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="institusipendidikan" class="form-control" required placeholder="Institusi Pendidikan" value="<?php echo $profil->institusi_pendidikan; ?>">
            </div>
          </div>
        </div>

        <div class="card card-warning">

          <div class="card-header">
            <h3 class="card-title">SOCIAL MEDIA</h3>
          </div>
          <!-- /.card-header -->
          <div class="card-body">
            <div class="form-group">
              <label for="exampleInputEmail1">Facebook</label>
              <?php echo form_error('facebook','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="facebook" class="form-control" required placeholder="Facebook" value="<?php echo $profil->facebook; ?>">
            </div>
            <div class="form-group">
              <label for="exampleInputEmail1">Twitter</label>
              <?php echo form_error('twitter','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="twitter" class="form-control" required placeholder="Twitter" value="<?php echo $profil->twitter; ?>">
            </div>
            <div class="form-group">
              <label for="exampleInputEmail1">Instagram</label>
              <?php echo form_error('instagram','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="insagram" class="form-control" required placeholder="Instagram" value="<?php echo $profil->instagram; ?>">
            </div>
          </div>
        </div>

      </div>

      <div class="col-md-6">
        <div class="card card-secondary">

          <div class="card-header">
            <h3 class="card-title">JOB ADMINISTRATION</h3>
          </div>
          <!-- /.card-header -->

          <div class="card-body">
            <div class="form-group">
              <label for="exampleInputEmail1">Company Code</label>
              <?php echo form_error('companycode','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="comapanycode" class="form-control" disabled placeholder="Company Code" value="<?php echo $profil->company_code; ?>">
            </div>
          
            <div class="form-group">
              <label for="exampleInputEmail1">Job Title</label>
              <?php echo form_error('jobtitle','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="jobtitle" class="form-control" disabled placeholder="Job Title" value="<?php echo $profil->job_title; ?>">
            </div>
          
            <div class="form-group">
              <label for="exampleInputEmail1">Grade</label>
              <?php echo form_error('grade','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="grade" class="form-control" disabled placeholder="Grade" value="<?php echo $profil->grade; ?>">
            </div>
         
            <div class="form-group">
              <label for="exampleInputEmail1">Golongan</label>
              <?php echo form_error('golongan','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="grade" class="form-control" disabled placeholder="Grade" value="<?php echo $profil->golongan; ?>">
            </div>
          
            <div class="form-group">
              <label for="exampleInputEmail1">Bagian/Fungsi</label>
              <?php echo form_error('bagianfungsi','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="bagianfungsi" class="form-control" disabled placeholder="Bagian/Fungsi" value="<?php echo $profil->bagian_fungsi; ?>">
            </div>
          
            <div class="form-group">
              <label for="exampleInputEmail1">Departemen</label>
              <?php echo form_error('departemen','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="departemen" class="form-control" disabled placeholder="Departemen" value="<?php echo $profil->unit_kerja; ?>">
            </div>
          
            <div class="form-group">
              <label for="exampleInputEmail1">Direktorat</label>
              <?php echo form_error('direktorat','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="direktorat" class="form-control" disabled placeholder="Direktorat" value="<?php echo $profil->direktorat; ?>">
            </div>
          
            <div class="form-group">
              <label for="exampleInputEmail1">Jabatan</label>
              <?php echo form_error('jabatan','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="jabatan" class="form-control" disabled placeholder="Jabatan" value="<?php echo $profil->jabatan; ?>">
            </div>
          
            <div class="form-group">
              <label for="exampleInputEmail1">Klasifikasi Level Jabatan</label>
              <?php echo form_error('klasifikasilvljabatan','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="klasifikasilvljabatan" class="form-control" disabled placeholder="Klasifikasi Level Jabatan" value="<?php echo $profil->klasifikasi_level_jabatan; ?>">
            </div>
          
            <div class="form-group">
              <label for="exampleInputEmail1">Job Stream</label>
              <?php echo form_error('jobstream','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="jobstream" class="form-control" disabled placeholder="Job Stream" value="<?php echo $profil->job_stream; ?>">
            </div>
          
            <div class="form-group">
              <label for="exampleInputEmail1">Klasifikasi Job</label>
              <?php echo form_error('klasifikasijob','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="klasifikasijob" class="form-control" disabled placeholder="Klasifikasi Job" value="<?php echo $profil->klasifikasi_job; ?>">
            </div>
          
            <div class="form-group">
              <label for="exampleInputEmail1">Rumpun Jabatan</label>
              <?php echo form_error('rumpunjabatan','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="rumpunjabatan" class="form-control" disabled placeholder="Rumpun Jabatan" value="<?php echo $profil->rumpun_jabatan; ?>">
            </div>
          
            <div class="form-group">
              <label for="exampleInputEmail1">Lokasi Kerja</label>
              <?php echo form_error('lokasikerja','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="lokasikerja" class="form-control" disabled placeholder="Lokasi Kerja" value="<?php echo $profil->lokasi_kerja; ?>">
            </div>
          </div>

        </div>

        <div class="card card-success">

          <div class="card-header">
            <h3 class="card-title">EMPLOYEE</h3>
          </div>
          <!-- /.card-header -->

          <div class="card-body">
            <div class="form-group">
              <label for="exampleInputEmail1">Status</label>
              <?php echo form_error('status','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="status" class="form-control" disabled placeholder="Status" value="<?php echo $profil->status_kerja; ?>">
            </div>          
            <div class="form-group">
              <label for="exampleInputEmail1">Tanggal Mulai Kerja</label>
              <?php echo form_error('tglmulaikerja','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="tglmulaikerja" class="form-control" disabled placeholder="Tanggal Mulai Kerja" value="<?php echo $profil->mulai_kerja; ?>">
            </div>
            <div class="form-group">
              <label for="exampleInputEmail1">Tanggal Akhir Kerja</label>
              <?php echo form_error('tglakhirkerja','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="tglakhirkerja" class="form-control" disabled placeholder="Tanggal Akhir Kerja" value="<?php echo $profil->akhir_kerja; ?>">
            </div>
            <div class="form-group">
              <label for="exampleInputEmail1">Tanggal MPP</label>
              <?php echo form_error('tglmpp','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="tglmpp" class="form-control" disabled placeholder="Tanggal MPP" value="<?php echo $tglmpp; ?>">
            </div>
            <div class="form-group">
              <label for="exampleInputEmail1">Tanggal Pensiun</label>
              <?php echo form_error('tglpensiun','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="tglpensiun" class="form-control" disabled placeholder="Tanggal Pensiun" value="<?php echo $tglpensiun; ?>">
            </div>
          </div>

        </div>

        <div class="card card-secondary">

          <div class="card-header">
            <h3 class="card-title">DATA ANGGOTA KELUARGA</h3>
          </div>
          <!-- /.card-header -->
          <div class="card-body">
            <div class="form-group">
              <label for="exampleInputEmail1">Jumlah Anggota Keluarga</label>
              <?php echo form_error('jumlahkeluarga','<div class="alert alert-warning">','</div>'); ?>
              <input type="text" name="jumlahkeluarga" class="form-control" required placeholder="Jumlah Anggota Keluarga" value="<?php echo $profil->jumlah_keluarga; ?>">
            </div>

            <div class="row">              
              <div class="col-sm-6">
                <div class="form-group">
                  <label for="exampleInputEmail1">Nama Istri/Suami</label>
                  <?php echo form_error('namaistrisuami','<div class="alert alert-warning">','</div>'); ?>
                  <input type="text" name="namaistrisuami" class="form-control" required placeholder="Nama Istri/Suami" value="<?php echo $profil->nama_istri_suami; ?>">
                </div>
              </div>
              <div class="col-sm-6">
                <div class="form-group">
                  <label for="exampleInputPassword1">Tanggal Lahir Istri/Suami</label>
                  <?php echo form_error('dobistrisuami','<div class="alert alert-warning">','</div>'); ?>
                  <input type="text" name="dobistrisuami" class="form-control" id="datepicker2" readonly placeholder="Tanggal Lahir Istri/Suami" value="<?php echo $profil->dob_istri_suami; ?>">
                </div>
              </div>
            </div>

            <div class="row">              
              <div class="col-sm-6">
                <div class="form-group">
                  <label for="exampleInputEmail1">Nama Anak 1</label>
                  <?php echo form_error('namaanak1','<div class="alert alert-warning">','</div>'); ?>
                  <input type="text" name="namaanak1" class="form-control" required placeholder="Nama Anak 1" value="<?php echo $profil->nama_anak_1; ?>">
                </div>
              </div>
              <div class="col-sm-6">
                <div class="form-group">
                  <label for="exampleInputPassword1">Tanggal Lahir Anak 1</label>
                  <?php echo form_error('dobanak1','<div class="alert alert-warning">','</div>'); ?>
                  <input type="text" name="dobanak1" class="form-control" id="datepicker3" readonly placeholder="Tanggal Lahir Anak 1" value="<?php echo $profil->dob_anak_1; ?>">
                </div>
              </div>
            </div>
            <div class="row">              
              <div class="col-sm-6">
                <div class="form-group">
                  <label for="exampleInputEmail1">Nama Anak 2</label>
                  <?php echo form_error('namaanak2','<div class="alert alert-warning">','</div>'); ?>
                  <input type="text" name="namaanak2" class="form-control" required placeholder="Nama Anak 1" value="<?php echo $profil->nama_anak_2; ?>">
                </div>
              </div>
              <div class="col-sm-6">
                <div class="form-group">
                  <label for="exampleInputPassword1">Tanggal Lahir Anak 2</label>
                  <?php echo form_error('dobanak2','<div class="alert alert-warning">','</div>'); ?>
                  <input type="text" name="dobanak2" class="form-control" id="datepicker4" readonly placeholder="Tanggal Lahir Anak 2" value="<?php echo $profil->dob_anak_2; ?>">
                </div>
              </div>
            </div>
            <div class="row">              
              <div class="col-sm-6">
                <div class="form-group">
                  <label for="exampleInputEmail1">Nama Anak 3</label>
                  <?php echo form_error('namaanak3','<div class="alert alert-warning">','</div>'); ?>
                  <input type="text" name="namaanak3" class="form-control" required placeholder="Nama Anak 3" value="<?php echo $profil->nama_anak_3; ?>">
                </div>
              </div>
              <div class="col-sm-6">
                <div class="form-group">
                  <label for="exampleInputPassword1">Tanggal Lahir Anak 3</label>
                  <?php echo form_error('dobanak3','<div class="alert alert-warning">','</div>'); ?>
                  <input type="text" name="dobanak3" class="form-control" id="datepicker5" readonly placeholder="Tanggal Lahir Anak 3" value="<?php echo $profil->dob_anak_3; ?>">
                </div>
              </div>
            </div>
            <div class="row">              
              <div class="col-sm-6">
                <div class="form-group">
                  <label for="exampleInputEmail1">Nama Anak 4</label>
                  <?php echo form_error('namaanak4','<div class="alert alert-warning">','</div>'); ?>
                  <input type="text" name="namaanak4" class="form-control" required placeholder="Nama Anak 4" value="<?php echo $profil->nama_anak_4; ?>">
                </div>
              </div>
              <div class="col-sm-6">
                <div class="form-group">
                  <label for="exampleInputPassword1">Tanggal Lahir Anak 4</label>
                  <?php echo form_error('dobanak4','<div class="alert alert-warning">','</div>'); ?>
                  <input type="text" name="dobanak4" class="form-control" id="datepicker6" readonly placeholder="Tanggal Lahir Anak 4" value="<?php echo $profil->dob_anak_4; ?>">
                </div>
              </div>
            </div>
            <div class="row">              
              <div class="col-sm-6">
                <div class="form-group">
                  <label for="exampleInputEmail1">Nama Anak 5</label>
                  <?php echo form_error('namaanak5','<div class="alert alert-warning">','</div>'); ?>
                  <input type="text" name="namaanak5" class="form-control" required placeholder="Nama Anak 5" value="<?php echo $profil->nama_anak_5; ?>">
                </div>
              </div>
              <div class="col-sm-6">
                <div class="form-group">
                  <label for="exampleInputPassword1">Tanggal Lahir Anak 5</label>
                  <?php echo form_error('dobanak5','<div class="alert alert-warning">','</div>'); ?>
                  <input type="text" name="dobanak5" class="form-control" id="datepicker7" readonly placeholder="Tanggal Lahir Anak 5" value="<?php echo $profil->dob_anak_5; ?>">
                </div>
              </div>
            </div>

          </div>
        </div>

      </div>
   
    </div>
    <!-- /.row -->
    <?php echo form_close(); ?>

  </div><!-- /.container-fluid -->
</div>
</section>
<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>