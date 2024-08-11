<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Absensi</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Absensi</li>
          <li class="breadcrumb-item active">Dashboard</li>
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
      
      <div class="card col-12">
        <div class="card-body">

          <?php echo form_open('absen/emp'); ?>
          <div class="form-group">
            <div class="row">
              <div class="col-md-3">
                <div class="form-group">
                  <label>Tanggal</label>
                  <select class="form-control select2" name="tanggal" style="width: 100%;">
                    <?php $tgl = $hari; for($t=1; $t<=31; ++$t): ?>
                      <option value="<?php echo $t; ?>" <?php if($tgl == $t) echo "selected"; ?>><?php echo $t; ?></option>
                    <?php endfor; ?>  
                  </select>
                </div>
              </div>

              <div class="col-md-5">
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
        </div>
      </div>

      <!-- /.col -->
      <div class="col-md-12">
        <!-- Widget: user widget style 1 -->
        <div class="card card-widget widget-user">
          <!-- Add the bg color to the header using any of the bg-* classes -->
          <a href="<?php echo site_url('absen/mentah/'.$tahun."/".$bulan."/".$hari."/all"); ?>">
          <div class="widget-user-header bg-info">
            <h1 class="widget-user-username" style="font-size: 50px;"><strong><?php echo $sum_all; ?></strong></h1>
            <h5 class="widget-user-desc">Jumlah Karyawan SEI yang Melakukan Checktime pada Tanggal <?php echo $tanggal; ?></h5>
          </div>
          </a>
          <div class="card-footer">
            <div class="row">
              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-info">
                  <div class="inner">
                    <h3><?php echo $sum_sekper; ?></h3>
                    <p>Sekretariat Perusahaan</p>
                  </div>
                  <div class="icon">
                    <i class="ion ion-ios-alarm"></i>
                  </div>
                  <a href="<?php echo site_url('absen/mentah/'.$tahun."/".$bulan."/".$hari."/5788629d-5ac1-45af-b9e6-ec334fbc8303"); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>

              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-info">
                  <div class="inner">
                    <h3><?php echo $sum_eng; ?></h3>
                    <p>Engineering</p>
                  </div>
                  <div class="icon">
                    <i class="ion ion-ios-alarm"></i>
                  </div>
                  <a href="<?php echo site_url('absen/mentah/'.$tahun."/".$bulan."/".$hari."/9a579b74-41e2-4c3d-87d9-328b387019df"); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>

              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-success">
                  <div class="inner">
                    <h3><?php echo $sum_mppp; ?></h3>
                    <p>Manajemen Proyek</p>
                  </div>
                  <div class="icon">
                    <i class="ion ion-android-alarm-clock"></i>
                  </div>
                  <a href="<?php echo site_url('absen/mentah/'.$tahun."/".$bulan."/".$hari."/23be5996-7ed4-45ea-9712-db81649db7d3"); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>

              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-success">
                  <div class="inner">
                    <h3><?php echo $sum_keu; ?></h3>
                    <p>Akuntansi dan Keuangan</p>
                  </div>
                  <div class="icon">
                    <i class="ion ion-android-alarm-clock"></i>
                  </div>
                  <a href="<?php echo site_url('absen/mentah/'.$tahun."/".$bulan."/".$hari."/c2c9ded5-9b2e-4410-93f6-d739705d42e5"); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
            </div>
            <!-- /.row -->

            <div class="row">
              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-info">
                  <div class="inner">
                    <h3><?php echo $sum_sdmu; ?></h3>
                    <p>SDM & Umum</p>
                  </div>
                  <div class="icon">
                    <i class="ion ion-ios-alarm"></i>
                  </div>
                  <a href="<?php echo site_url('absen/mentah/'.$tahun."/".$bulan."/".$hari."/1ebffc4b-16be-4793-9762-a57bcdad108a"); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>

              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-info">
                  <div class="inner">
                    <h3><?php echo $sum_bis; ?></h3>
                    <p>Pengembangan Bisnis</p>
                  </div>
                  <div class="icon">
                    <i class="ion ion-ios-alarm"></i>
                  </div>
                  <a href="<?php echo site_url('absen/mentah/'.$tahun."/".$bulan."/".$hari."/f1197c7d-07a3-4a4f-ab75-fd106648bc5b"); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>

              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-success">
                  <div class="inner">
                    <h3><?php echo $sum_pem; ?></h3>
                    <p>Pemasaran & Penjualan</p>
                  </div>
                  <div class="icon">
                    <i class="ion ion-android-alarm-clock"></i>
                  </div>
                  <a href="<?php echo site_url('absen/mentah/'.$tahun."/".$bulan."/".$hari."/627d2d0e-5502-451f-8560-f9dec058f0ce"); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>

              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-success">
                  <div class="inner">
                    <h3><?php echo $sum_log; ?></h3>
                    <p>Logistik</p>
                  </div>
                  <div class="icon">
                    <i class="ion ion-android-alarm-clock"></i>
                  </div>
                  <a href="<?php echo site_url('absen/mentah/'.$tahun."/".$bulan."/".$hari."/4b0d9612-6963-4951-a6db-2c8f3cf72f9a"); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
            </div>
            <!-- /.row -->
          </div>
        </div>
        <!-- /.widget-user -->
      </div>
      <!-- /.col -->


    </div>

      
  </div><!-- /.container-fluid -->
</section>
<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>