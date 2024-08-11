<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Tambah Data Karyawan</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('master/karyawan'); ?>">Master Data Karyawan</a></li>
          <li class="breadcrumb-item active">Tambah Karyawan</li>
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
      <div class="col-12">

      <div class="card">

        <div class="card-header">
          <h3 class="card-title">Complete Form Below</h3>
        </div>
        <!-- /.card-header -->
        
        <?php echo form_open('karyawan/add'); ?>
        <div class="card-body">
          <div class="form-group">
            <label>Employee Code</label>
            <?php echo form_error('empcode','<div class="alert alert-warning">','</div>'); ?>
            <input type="text" name="empcode" class="form-control " required placeholder="Employee Code">
          </div>
          <div class="form-group">
            <label>Nama</label>
            <?php echo form_error('nama','<div class="alert alert-warning">','</div>'); ?>
            <input type="text" name="nama" class="form-control" required placeholder="Nama">
          </div>
        </div>
        <div class="card-footer">
          <button type="submit" class="btn btn-primary">Tambah Data</button>
        </div>
        <?php echo form_close(); ?>

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