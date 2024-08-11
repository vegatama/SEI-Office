<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Master Data</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Master Data Lokasi Absensi</li>
        </ol>
      </div><!-- /.col -->
    </div><!-- /.row -->
  </div><!-- /.container-fluid -->
</div>
<!-- /.content-header -->

<!-- Main content -->
<section class="content">
<div class="container-fluid">
  <div class="row">
    <div class="col-12">

      <div class="card">
        <div class="card-header">
          <h3 class="card-title">Lokasi Absensi</h3>
          <a class="btn btn-primary btn-sm float-sm-right" href="<?php echo site_url('lokasi/lihatlokasi'); ?>">
              <i class="far fa-calendar-alt"></i> Lihat Lokasi karyawan
          </a>
        </div>
        <!-- /.card-header -->
        <div class="card-body">
          <?php 
          if(isset($lokasi)):
          foreach($lokasi as $data):
          if($data->isDefault == 1):                  
          ?>
          <div class="card" style="background-color: rgba(228, 255, 223, 1);">
              <div class="card-body">
                <div class="row">
                  <div class="col">
                    <h5 class="card-title"><?php echo $data->lokasi_absen; ?></h5>
                    <p class="card-text"><small class="text-muted"><?php echo $data->latitude;?>, <?php echo $data->longitude; ?></small></p>
                  </div>
                    <div class="row p-2">
                      <a class="btn btn-primary w-30 py-1 px-3 mr-2" href="<?php echo site_url('lokasi/edit/'.$data->id); ?>">
                          Edit
                      </a>
                      <a class="btn btn-danger w-30 py-1 px-3" href="<?php echo site_url('lokasi/delete/'.$data->id); ?>">
                          Hapus
                      </a>
                  </div>
                </div>
            </div>
          </div>
          <?php 
          endif;
          endforeach; 
          ?>
          <?php 
          foreach($lokasi as $data):
          if($data->isDefault == 0): 
          ?>
          <div class="card">
          <div class="card-body">
                <div class="row">
                  <div class="col">
                    <h5 class="card-title"><?php echo $data->lokasi_absen; ?></h5>
                    <p class="card-text"><small class="text-muted"><?php echo $data->latitude;?>, <?php echo $data->longitude; ?></small></p>
                  </div>
                    <div class="row p-2">
                      <a class="btn btn-success w-30 py-1 px-3 mr-2" href="<?php echo site_url('lokasi/setDefault/'.$data->id); ?>">
                          Set Default
                      </a>
                      <a class="btn btn-primary w-30 py-1 px-3 mr-2" href="<?php echo site_url('lokasi/edit/'.$data->id); ?>">
                          Edit
                      </a>
                      <a class="btn btn-danger w-30 py-1 px-3" href="<?php echo site_url('lokasi/delete/'.$data->id); ?>">
                          Hapus
                      </a>
                  </div>
                </div>
            </div>
          </div>
          <?php 
            endif;
            endforeach; 
            endif;
            ?>
          <div class="col text-center p-3">
            <a class="btn btn-success btn-lg" href="<?php echo site_url('lokasi/tambah'); ?>">
              Tambah Lokasi
            </a>
          </div>
        </div>
      </div>
      <!-- /.row -->
    </div><!-- /.container-fluid -->
  </div>
  <!-- /.content -->
  </section>
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>
