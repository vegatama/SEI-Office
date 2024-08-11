<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Edit Data Hari Libur nasional</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('master/libur'); ?>">Master Data</a></li>
          <li class="breadcrumb-item active">Hari Libur Nasional</li>
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
        
        <?php echo form_open('libur/update/'.$libur->id); ?>
        <div class="card-body">
          <div class="form-group">
            <label>Tanggal</label>
            <input type="date" name="tanggal" class="form-control" value="<?php echo $libur->tanggal;?>" required>
            <?php echo form_error('tanggal','<div class="alert alert-warning">','</div>'); ?>
          </div>
          <div class="form-group">
            <label>Keterangan</label>
            <textarea name="keterangan" class="form-control"><?php echo $libur->keterangan;?></textarea>
            <?php echo form_error('keterangan','<div class="alert alert-warning">','</div>'); ?>
            
          </div>
        </div>
        <div class="card-footer">
          <button type="submit" class="btn btn-primary">Update Data</button>
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