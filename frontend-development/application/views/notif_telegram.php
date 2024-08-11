<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">How to Activate Telegram Notification</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Telegram Notification</li>
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
    <div class="row">
      <div class="col-12" id="accordion">

      <div class="card card-success card-outline">
        <a class="d-block w-100" data-toggle="collapse" href="#collapseOne">
            <div class="card-header">
                <h4 class="card-title w-100">
                    1. Cari SEIOfficeBot
                </h4>
            </div>
        </a>
        <div id="collapseOne" class="collapse show" data-parent="#accordion">
            <div class="card-body">
                Pada fitur pencarian Telegram, lakukan pencarian <strong>SEIOfficeBot</strong>
            </div>
        </div>
      </div>
      <div class="card card-success card-outline">
        <a class="d-block w-100" data-toggle="collapse" href="#collapseTwo">
            <div class="card-header">
                <h4 class="card-title w-100">
                    2. Mulai Percakapan
                </h4>
            </div>
        </a>
        <div id="collapseTwo" class="collapse" data-parent="#accordion">
            <div class="card-body">
                Klik tombol <strong>START</strong> pada kolom percakapan dengan SEIOfficeBot
            </div>
        </div>
      </div>
      <div class="card card-success card-outline">
        <a class="d-block w-100" data-toggle="collapse" href="#collapseThree">
            <div class="card-header">
                <h4 class="card-title w-100">
                    3. Kirim Perintah Registrasi Notifikasi
                </h4>
            </div>
        </a>
        <div id="collapseThree" class="collapse" data-parent="#accordion">
            <div class="card-body">
                Kirimkan pesan <strong>NOTIF <?php echo $employeeID; ?></strong> ke SEIOfficeBot
            </div>
        </div>
      </div>
      <div class="card card-success card-outline">
        <a class="d-block w-100" data-toggle="collapse" href="#collapseFour">
            <div class="card-header">
                <h4 class="card-title w-100">
                    4. Tunggu Balasan
                </h4>
            </div>
        </a>
        <div id="collapseFour" class="collapse" data-parent="#accordion">
            <div class="card-body">
                Notifikasi berhasil didaftarkan jika sudah mendapat balasan dari SEIOfficeBot
            </div>
        </div>
      </div>

      </div>    
    </div>
    <!-- /.row -->
  </div><!-- /.container-fluid -->
</div>
</section>
<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>