<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Tambah Kendaraan</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item">Mobil Operasional</li>
          <li class="breadcrumb-item active">Tambah Kendaraan</li>
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
          
          <?php echo form_open('mobil/addp'); ?>
          <div class="card-body">
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">No. Polisi :</label>
              <input type="text" name="nopol" class="form-control col-5" required placeholder="Nomor Polisi">
              <?php echo form_error('nopol','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Merek :</label>
              <input type="text" name="merk" class="form-control col-5" required placeholder="Merek">
              <?php echo form_error('merk','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Tipe :</label>
              <input type="text" name="tipe" class="form-control col-5" required placeholder="Tipe">
              <?php echo form_error('tipe','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Tahun :</label>
              <select name="tahun" class="form-control col-2">
                <?php 
                  $thn = date('Y');
                  for($i=$thn; $i>=$thn-15; $i--):
                ?>
                <option value="<?php echo $i; ?>"><?php echo $i; ?></option>
                <?php endfor; ?>
              </select>
              <?php echo form_error('tahun','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">BBM :</label>              
              <select name="bbm" class="form-control col-2">
                <option value="BENSIN">BENSIN</option>
                <option value="SOLAR">SOLAR</option>
              </select>
              <?php echo form_error('bbm','<div class="alert alert-warning">','</div>'); ?>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Pemilik :</label>
              <select name="pemilik" class="form-control col-2">
                <option value="KANTOR">KANTOR</option>
                <option value="RENTAL">RENTAL</option>
              </select>
              <?php echo form_error('pemilik','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Masa Berlaku Pajak Kendaraan s/d :</label>              
              <input type="text" name="pkb" class="form-control col-3" placeholder="Masa Berlaku Pajak Kendaraan">
              <?php echo form_error('pkb','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Keterangan / Nama Rental :</label>
              <input type="text" name="ket" class="form-control col-7" placeholder="Keterangan / Nama Rental">
              <?php echo form_error('ket','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
          </div>
          <div class="card-footer">
            <button type="submit" class="btn btn-primary">Simpan Data</button>
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