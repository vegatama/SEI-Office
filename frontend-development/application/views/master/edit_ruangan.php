<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Edit Data Ruangan Meeting</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('master/ruangan'); ?>">Master Data Ruang Meeting</a></li>
          <li class="breadcrumb-item active">Edit Ruangan Meeting</li>
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
        
        <?php echo form_open('ruangan/update/'.$ruangan->id); ?>
        <div class="card-body">
          <div class="form-group">
            <label>Nama Ruang</label>
            <textarea name="name" class="form-control"><?php echo $ruangan->name;?></textarea>
            <?php echo form_error('name','<div class="alert alert-warning">','</div>'); ?>
          </div>
          <div class="form-group">
            <label>Kapasitas</label>
            <textarea name="capacity" class="form-control"><?php echo $ruangan->capacity;?></textarea>
            <?php echo form_error('capacity','<div class="alert alert-warning">','</div>'); ?>
            
          </div>
          <div class="form-group">
            <label>Keterangan</label>
            <?php echo form_error('description','<div class="alert alert-warning">','</div>'); ?>
            <textarea name="description" class="form-control"><?php echo $ruangan->description;?></textarea>
          </div>
        </div>
        <div class="card-footer">
          <button type="submit" class="btn btn-primary">Edit Data</button>
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