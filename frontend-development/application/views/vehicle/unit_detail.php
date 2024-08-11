<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Detil Kendaraan</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item">Mobil Operasional</li>
          <li class="breadcrumb-item active">Detil Kendaraan</li>
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
        <div class="card card-primary">

          <div class="card-header">
            <h3 class="card-title">Data Kendaraan</h3>
          </div>
          <!-- /.card-header -->
          
          <div class="card-body">
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">No. Polisi :</label>
              <input type="text" name="nopol" class="form-control col-5" value="<?php echo $vehicle->plat_number; ?>" disabled>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Merek :</label>
              <input type="text" name="merk" class="form-control col-5" disabled value="<?php echo $vehicle->merk; ?>">
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Tipe :</label>
              <input type="text" name="tipe" class="form-control col-5" disabled value="<?php echo $vehicle->type; ?>">
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Tahun :</label>
              <input type="text" name="tipe" class="form-control col-3" disabled value="<?php echo $vehicle->year; ?>">
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">BBM :</label>
              <input type="text" name="tipe" class="form-control col-3" disabled value="<?php echo $vehicle->bbm; ?>">
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Pemilik :</label>
              <input type="text" name="tipe" class="form-control col-3" disabled value="<?php echo $vehicle->ownership; ?>">
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Masa Berlaku Pajak Kendaraan s/d :</label>
              <input type="text" name="pkb" class="form-control col-3" disabled value="<?php echo $vehicle->certifcate_expired; ?>">
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Keterangan / Nama Rental :</label>
              <input type="text" name="ket" class="form-control col-7" disabled value="<?php echo $vehicle->keterangan; ?>">
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