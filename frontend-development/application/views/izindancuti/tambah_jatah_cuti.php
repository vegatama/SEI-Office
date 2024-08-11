<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Tambah Data Jatah Izin Cuti</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('Izincuti/jatahCuti'); ?>">Jatah Izin Cuti</a></li>
          <li class="breadcrumb-item active">Tambah jatah Izin Cuti</li>
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
        
        <?php echo form_open('Jatahcuti/add'); ?>
        <div class="card-body">
          <div class="form-group px-3">
            <label>Nama</label>
            <?php echo form_error('nama','<div class="alert alert-warning">','</div>'); ?>
            <select id="nama" name="nama" class="form-control js-single" required>
              <option></option>
              <?php foreach($karyawan as $data):
                if(isset($data->name) && $data->name != " " && $data->employee_code != " "):?>
                  <option value="<?php echo $data->employee_code; ?>"><?php echo $data->name; ?> (<?php echo $data->unit_kerja; ?>)</option>
                <?php 
                endif;
                endforeach; ?>
            </select>
          </div>
          <div class="form-group px-3">
            <label>Tahun</label>
            <?php echo form_error('tahun_cuti','<div class="alert alert-warning">','</div>'); ?>
            <input type="number" name="tahun_cuti" class="form-control" value="<?php echo $tahun;?>" required>
          </div>
          <div class="form-group px-3">
            <label>Jumlah Cuti</label>
            <?php echo form_error('jumlah_cuti','<div class="alert alert-warning">','</div>'); ?>
            <input type="number" name="jumlah_cuti" class="form-control" required placeholder="Jumlah Cuti">
          </div>
          <div class="col text-center p-3">
            <button type="submit" class="btn btn-success">Tambah Data</button>
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

<script>
$(document).ready(function() {
    $('.js-single').select2({
      placeholder: "Select position/name",
      width: '100%'
  });
});
</script>