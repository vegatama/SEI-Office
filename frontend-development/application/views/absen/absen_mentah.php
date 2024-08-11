<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Data Mentah Absen</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Data Mentah Absen</li>
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
          <h3 class="card-title">Data Mentah Absen: <?php echo $nama_bulan." ".$tahun; ?></h3>
        </div>
        <!-- /.card-header -->
        <div class="card-body">

          <?php echo form_open('absen/mentah'); ?>
          <div class="form-group">
            <div class="row">
              <div class="col-md-2">
                <div class="form-group">
                  <label>Tanggal</label>
                  <select class="form-control select2" name="tanggal" style="width: 100%;">
                    <option value="0">Semua Tanggal</option>
                    <?php $tgl = $tanggal; for($t=1; $t<=31; ++$t): ?>
                      <option value="<?php echo $t; ?>" <?php if($tgl == $t) echo "selected"; ?>><?php echo $t; ?></option>
                    <?php endfor; ?>  
                  </select>
                </div>
              </div>

              <div class="col-md-2">
                <div class="form-group">
                  <label>Bulan</label>
                  <select class="form-control select2" name="bulan" style="width: 100%;">
                    <?php $bln = $bulan; for($m=1; $m<=12; ++$m): ?>
                      <option value="<?php echo $m; ?>" <?php if($bln == $m) echo "selected"; ?>><?php echo date('F', mktime(0, 0, 0, $m, 1)); ?></option>
                    <?php endfor; ?>  
                  </select>
                </div>
              </div>

              <div class="col-md-2">
                <div class="form-group">
                  <label>Tahun</label>
                  <select class="form-control select2" name="tahun" style="width: 100%;">
                    <?php for($i=-1; $i<=1; $i++): $thn = $tahun + $i;?>
                      <option value="<?php echo $thn; ?>" <?php if($tahun == $thn) echo "selected"; ?>><?php echo $thn; ?></option>
                    <?php endfor; ?>
                  </select>
                </div>
              </div>

              <div class="col-md-6">
                <div class="form-group">
                  <label>Departemen</label>
                  <select class="form-control select2" name="dept" style="width: 100%;">
                    <option value="all">Semua Departemen</option>
                    <?php foreach($dept as $dpt): ?>
                      <option value="<?php echo $dpt->id; ?>" <?php if($departemen == $dpt->id) echo "selected"; ?>><?php echo $dpt->name; ?></option>
                    <?php endforeach; ?>
                  </select>
                </div>
              </div>
            </div>
            <button class="btn btn-primary" type="submit" name="tampil">Tampilkan Data</button>
          </div>
          <?php echo form_close(); ?><br/>

          <table id="karyawan" class="table table-bordered table-striped">
            <thead>
            <tr>
              <th>Employee Code</th>
              <th>Nama</th>
              <th>Tanggal</th>
              <th>Hari</th>
              <th>Jam Masuk</th>
              <th>Jam Keluar</th>
              <th>Status</th>
            </tr>
            </thead>
            <tbody>
              <?php 
                if(isset($absensi)):
                foreach($absensi as $data) :
              ?>
              <tr>
                <td><?php echo $data->empcode; ?></td>
                <td><?php echo $data->name; ?></td>
                <td><?php echo $data->tgl; ?></td>
                <td><?php echo $data->hari; ?></td>
                <td><?php echo $data->jam_masuk; ?></td>
                <td><?php echo $data->jam_keluar; ?></td>
                <td><?php echo $data->status; ?></td>
              </tr>
            <?php endforeach; ?>
            <?php endif; ?>
            </tbody>
          </table>
        </div>
      </div>
      <!-- /.row -->
    </div><!-- /.container-fluid -->
  </div>
</div>
  <!-- /.content -->
</section>
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>