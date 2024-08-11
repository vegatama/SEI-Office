<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Edit Data Jatah Izin Cuti</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('Izincuti/jatahCuti'); ?>">Jatah Izin Cuti</a></li>
          <li class="breadcrumb-item active">Edit jatah Izin Cuti</li>
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
        
        <?php echo form_open('Jatahcuti/update/'.$datajatah->empcode.'/'.$datajatah->tahun.'/'.$datajatah->jumlahHari); ?>
        <div class="card-body">
          <div class="form-group px-3">
            <label>Nama</label>
            <input name="nama" class="form-control" value="<?php echo $datajatah->employeeName;?>" readonly>
          </div>
          <div class="form-group px-3">
            <label>Tahun</label>
            <input name="tahun_cuti" class="form-control" value="<?php echo $datajatah->tahun;?>" readonly>
          </div>
          <div class="form-group px-3">
            <label>Jumlah Cuti</label>
            <?php echo form_error('jumlah_cuti','<div class="alert alert-warning">','</div>'); ?>
            <input type="number" name="jumlah_cuti" class="form-control" value="<?php echo $datajatah->jumlahHari;?>" required placeholder="Jumlah Cuti">
          </div>
          <div class="col text-center p-3">
            <button type="submit" class="btn btn-success">Update Data</button>
          </div>
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