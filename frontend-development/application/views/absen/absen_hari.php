<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Absen Karyawan Per Hari</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Absen Karyawan Per Hari</li>
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
          <h3 class="card-title">Absensi Karyawan: <?php echo $tanggal; ?></h3>
        </div>
        <!-- /.card-header -->
        <div class="card-body">

          <?php echo form_open('absen/emp'); ?>
          <div class="form-group">
            <div class="row">
              <div class="col-md-4">
                <div class="form-group">
                  <label>Tanggal</label>
                  <select class="form-control select2" name="bulan" style="width: 100%;">
                    <?php $day = $hari; for($d=1; $d<=31; ++$d): ?>
                      <option value="<?php echo $d; ?>" <?php if($day == $d) echo "selected"; ?>><?php echo $d; ?></option>
                    <?php endfor; ?>  
                  </select>
                </div>
              </div>

              <div class="col-md-4">
                <div class="form-group">
                  <label>Bulan</label>
                  <select class="form-control select2" name="bulan" style="width: 100%;">
                    <?php $bln = $bulan; for($m=1; $m<=12; ++$m): ?>
                      <option value="<?php echo $m; ?>" <?php if($bln == $m) echo "selected"; ?>><?php echo date('F', mktime(0, 0, 0, $m, 1)); ?></option>
                    <?php endfor; ?>  
                  </select>
                </div>
              </div>

              <div class="col-md-4">
                <div class="form-group">
                  <label>Tahun</label>
                  <select class="form-control select2" name="tahun" style="width: 100%;">
                    <?php for($i=-1; $i<=1; $i++): $thn = $tahun + $i;?>
                      <option value="<?php echo $thn; ?>" <?php if($tahun == $thn) echo "selected"; ?>><?php echo $thn; ?></option>
                    <?php endfor; ?>
                  </select>
                </div>
              </div>
            </div>
            <button class="btn btn-primary" type="submit" name="tampil">Tampilkan Data</button>
          </div>
          <?php echo form_close(); ?><br/>

          <table id="example1" class="table table-bordered table-striped">
            <thead>
            <tr>
              <th>Employee Code</th>
              <th>Nama</th>
              <th>Tanggal</th>
              <th>Hari</th>
              <th>Absen Masuk</th>
              <th>Absen Keluar</th>
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
                <td><?php echo $tanggal; ?></td>
                <td><?php echo $namahari; ?></td>
                <td><?php echo $data->jam_masuk; ?></td>
                <td><?php echo $data->jam_keluar; ?></td>
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