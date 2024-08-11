<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Input Jatah Cuti Per Tahun</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('Izincuti/jatahCuti'); ?>">Input Jatah Cuti</a></li>
          <li class="breadcrumb-item active">Import Jatah Cuti</li>
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
        <?php echo form_open_multipart('Jatahcuti/import');?>
        <div class="card-body">
          <div class="row justify-content-center p-3">
            <div class="form-group pr-1">
                <label class="pl-1">Upload File (Sesuai Template)</label>
                <div class="card">
                  <div class="card-body">
                    <input type="file" class="form-control" name="inputFile" accept=".xls, .xlsx" required>
                  </div>
                </div>
              </div>
            </div>
          <div class="col text-center p-3">
            <button type="submit" name="import" class="btn btn-success">Import Data</button>
          </div>
          </div>
        </div>
      </div>
        <?php echo form_close(); ?>
    </div> 
    <!-- /.row -->
  </div><!-- /.container-fluid -->
</div>
</section>
<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>