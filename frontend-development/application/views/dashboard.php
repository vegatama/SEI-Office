<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Dashboard</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
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
      
      <div class="col-lg-3 col-6">
        <!-- small box -->
        <div class="small-box bg-info">
          <div class="inner">
            <h3><?php echo $cekin_today; ?></h3>

            <p>Check In Hari ini</p>
          </div>
          <div class="icon">
            <i class="ion ion-ios-alarm"></i>
          </div>
        </div>
      </div>

      <div class="col-lg-3 col-6">
        <!-- small box -->
        <div class="small-box bg-info">
          <div class="inner">
            <h3><?php echo $cekout_today; ?></h3>

            <p>Check Out Hari ini</p>
          </div>
          <div class="icon">
            <i class="ion ion-ios-alarm"></i>
          </div>
        </div>
      </div>

      <div class="col-lg-3 col-6">
        <!-- small box -->
        <div class="small-box bg-success">
          <div class="inner">
            <h3><?php echo $cekin_yesterday; ?></h3>

            <p>Check In Kemarin</p>
          </div>
          <div class="icon">
            <i class="ion ion-android-alarm-clock"></i>
          </div>
        </div>
      </div>

      <div class="col-lg-3 col-6">
        <!-- small box -->
        <div class="small-box bg-success">
          <div class="inner">
            <h3><?php echo $cekout_yesterday; ?></h3>

            <p>Check Out Kemarin</p>
          </div>
          <div class="icon">
            <i class="ion ion-android-alarm-clock"></i>
          </div>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-4 col-sm-6 col-12">
        <div class="info-box shadow-none">
          <span class="info-box-icon bg-warning"><i class="fas fa-history"></i></span>

          <div class="info-box-content">
            <span class="info-box-number"><?php echo $total_terlambat; ?> menit</span>
            <span class="info-box-text">Total Keterlambatan Bulan Ini</span>
          </div>
          <!-- /.info-box-content -->
        </div>
        <!-- /.info-box -->
      </div>
      <div class="col-md-4 col-sm-6 col-12">
        <div class="info-box shadow-none">
          <span class="info-box-icon bg-warning"><i class="fas fa-user-clock"></i></span>

          <div class="info-box-content">
            <span class="info-box-number"><?php echo $total_kurang_jam; ?> menit</span>
            <span class="info-box-text">Total Kurang Jam Kerja Bulan Ini</span>
          </div>
          <!-- /.info-box-content -->
        </div>
        <!-- /.info-box -->
      </div>
      <div class="col-md-4 col-sm-6 col-12">
        <div class="info-box shadow-none">
          <span class="info-box-icon bg-warning"><i class="fas fa-fingerprint"></i></span>

          <div class="info-box-content">
            <span class="info-box-number"><?php echo $total_lct; ?> kali</span>
            <span class="info-box-text">Total Lupa Check Time Bulan Ini</span>
          </div>
          <!-- /.info-box-content -->
        </div>
        <!-- /.info-box -->
      </div>
    </div>

    <div class="row">
      <div class="col-md-12">
      <div class="card card-info">
        <div class="card-header">
          <h3 class="card-title">SEI Event Calendar</h3>
        </div>
        <!-- /.card-header -->
        <div class="card-body">

            <div class="card card-primary">
              <div class="card-body p-0">
                <!-- THE CALENDAR -->
                <div id="calendar"></div>
              </div>
              <!-- /.card-body -->
            </div>
            <!-- /.card -->
          </div>

        </div>
      </div>

    </div>

      
  </div><!-- /.container-fluid -->
</section>
<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>