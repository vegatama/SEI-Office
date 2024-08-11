<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Tambah Notifikasi</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('Master/Notifikasi'); ?>">Master Data Notifikasi</a></li>
          <li class="breadcrumb-item active">Tambah Notifikasi</li>
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
        <?php echo form_open('notifikasi/add');?>
        <div class="card-body">
          <div class="row justify-content-center p-3">
                <div class="card">
                  <div class="card-body">
                  <div class="form-group pr-1">
                  <label class="pl-1">Nama Karyawan</label>
                  <select id="name" name="name" class="form-control js-single" required>
                          <option></option>
                          <?php foreach($karyawan as $data):
                            if(isset($data->name) && $data->name != " " && $data->employee_code != " "):?>
                              <option value="<?php echo $data->employee_code; ?>"><?php echo $data->name; ?> (<?php echo $data->unit_kerja; ?>)</option>
                            <?php 
                            endif;
                            endforeach; ?>
                        </select>
                        </div>
                        <!-- /.<div class="form-group pr-1 px-3">
                    <input type="checkbox" id="notif_kendaraan" name="notif_kendaraan" style="text-align: center; vertical-align:middle; margin-all: 2px; transform: scale(1.4);" value="True">
                    <label class="px-1" for="notif_kendaraan">Notifikasi Kendaraan</label>
                    </div>-->
                </div>
              </div>
            </div>
          <div class="col text-center p-3">
            <button type="submit" name="import" class="btn btn-success">Tambah Data</button>
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

<script>
$(document).ready(function() {
    $('.js-single').select2({
      placeholder: "Select position/name",
      width: '100%'
  });
});
</script>