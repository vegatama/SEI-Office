<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">NAV Document</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">NAV Document</li>
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
      
      <!-- /.col -->
      <div class="col-md-12">
        <div class="card card-widget widget-user"> 
          <div class="card-header">
            <h4>Document Need Approve</h4>
          </div>        
          <div class="card-footer">
            <div class="row">
              <div class="col-lg-3 col-10">
                <!-- small box -->
                <div class="small-box bg-info">
                  <div class="inner">
                    <h3><?php echo $budget; ?></h3>
                    <p>Perubahan Budget</p>
                  </div>
                  <div class="icon">
                    <i class="fas fa-search-dollar"></i>
                  </div>
                  <a href="<?php echo site_url('nav/budget/'); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>

              <div class="col-lg-3 col-10">
                <!-- small box -->
                <div class="small-box bg-success">
                  <div class="inner">
                    <h3><?php echo $dpb; ?></h3>
                    <p>Daftar Pembelian Barang</p>
                  </div>
                  <div class="icon">
                    <i class="fas fa-luggage-cart"></i>
                  </div>
                  <a href="<?php echo site_url('nav/dpb/'); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>

              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-success">
                  <div class="inner">
                    <h3><?php echo $dpj; ?></h3>
                    <p>Daftar Pembelian Jasa</p>
                  </div>
                  <div class="icon">
                    <i class="fas fa-concierge-bell"></i>
                  </div>
                  <a href="<?php echo site_url('nav/dpj/'); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>

              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-primary">
                  <div class="inner">
                    <h3><?php echo $bapb; ?></h3>
                    <p> Berita Acara Penerimaan Barang</p>
                  </div>
                  <div class="icon">
                    <i class="fas fa-file-invoice"></i>
                  </div>
                  <a href="<?php echo site_url('nav/bapb/'); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
            </div>
            <!-- /.row -->

            <div class="row">
              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-secondary">
                  <div class="inner">
                    <h3><?php echo $sppd; ?></h3>
                    <p>Surat Perjalanan Dinas</p>
                  </div>
                  <div class="icon">
                    <i class="fas fa-car"></i>
                  </div>
                  <a href="<?php echo site_url('nav/sppd/'); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>

              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-light">
                  <div class="inner">
                    <h3><?php echo $uangmuka; ?></h3>
                    <p>Permohonan Uang Muka / Penggantian</p>
                  </div>
                  <div class="icon">
                    <i class="fas fa-money-check-alt"></i>
                  </div>
                  <a href="<?php echo site_url('nav/um/'); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>

              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-light">
                  <div class="inner">
                    <h3><?php echo $ummulti; ?></h3>
                    <p>Permohonan Uang Muka Multiple</p>
                  </div>
                  <div class="icon">
                    <i class="fas fa-money-check-alt"></i>
                  </div>
                  <a href="<?php echo site_url('nav/ummulti/'); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>

              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-warning">
                  <div class="inner">
                    <h3><?php echo $pjum; ?></h3>
                    <p>Pertanggung Jawaban Uang Muka</p>
                  </div>
                  <div class="icon">
                    <i class="fas fa-cart-arrow-down"></i>
                  </div>
                  <a href="<?php echo site_url('nav/pjum/'); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
            </div>
            <!-- /.row -->
          </div>
        </div>
        <!-- /.widget-user -->
      </div>
      <!-- /.col -->

      <!-- /.col -->
      <div class="col-md-12">
        <div class="card card-widget widget-user"> 
          <div class="card-header">
            <h4>Document Approved</h4>
          </div>        
          <div class="card-footer">
            <div class="row">
              <div class="col-lg-3 col-10">
                <!-- small box -->
                <div class="small-box bg-info">
                  <div class="inner">
                    <h3><?php echo $budgeta; ?></h3>
                    <p>Perubahan Budget</p>
                  </div>
                  <div class="icon">
                    <i class="fas fa-search-dollar"></i>
                  </div>
                  <a href="<?php echo site_url('nav/budget/'); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>

              <div class="col-lg-3 col-10">
                <!-- small box -->
                <div class="small-box bg-success">
                  <div class="inner">
                    <h3><?php echo $dpba; ?></h3>
                    <p>Daftar Pembelian Barang</p>
                  </div>
                  <div class="icon">
                    <i class="fas fa-luggage-cart"></i>
                  </div>
                  <a href="<?php echo site_url('nav/dpb/'); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>

              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-success">
                  <div class="inner">
                    <h3><?php echo $dpja; ?></h3>
                    <p>Daftar Pembelian Jasa</p>
                  </div>
                  <div class="icon">
                    <i class="fas fa-concierge-bell"></i>
                  </div>
                  <a href="<?php echo site_url('nav/dpj/'); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>

              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-primary">
                  <div class="inner">
                    <h3><?php echo $bapba; ?></h3>
                    <p> Berita Acara Penerimaan Barang</p>
                  </div>
                  <div class="icon">
                    <i class="fas fa-file-invoice"></i>
                  </div>
                  <a href="<?php echo site_url('nav/bapb/'); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
            </div>
            <!-- /.row -->

            <div class="row">
              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-secondary">
                  <div class="inner">
                    <h3><?php echo $sppda; ?></h3>
                    <p>Surat Perjalanan Dinas</p>
                  </div>
                  <div class="icon">
                    <i class="fas fa-car"></i>
                  </div>
                  <a href="<?php echo site_url('nav/sppd/'); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>

              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-light">
                  <div class="inner">
                    <h3><?php echo $uangmukap; ?></h3>
                    <p>Permohonan Uang Muka / Penggantian</p>
                  </div>
                  <div class="icon">
                    <i class="fas fa-money-check-alt"></i>
                  </div>
                  <a href="<?php echo site_url('nav/um/'); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>

              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-light">
                  <div class="inner">
                    <h3><?php echo $ummultip; ?></h3>
                    <p>Permohonan Uang Muka Multiple</p>
                  </div>
                  <div class="icon">
                    <i class="fas fa-money-check-alt"></i>
                  </div>
                  <a href="<?php echo site_url('nav/ummulti/'); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>

              <div class="col-lg-3 col-6">
                <!-- small box -->
                <div class="small-box bg-warning">
                  <div class="inner">
                    <h3><?php echo $pjuma; ?></h3>
                    <p>Pertanggung Jawaban Uang Muka</p>
                  </div>
                  <div class="icon">
                    <i class="fas fa-cart-arrow-down"></i>
                  </div>
                  <a href="<?php echo site_url('nav/pjum/'); ?>" class="small-box-footer">Lihat Detil <i class="fas fa-arrow-circle-right"></i></a>
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