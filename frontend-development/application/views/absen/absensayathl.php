<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Absen Saya</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Absen Saya</li>
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
          <h3 class="card-title">Rekap Absensi</h3>
        </div>
        <!-- /.card-header -->
        <div class="card-body">

          <?php echo form_open('absen/saya'); ?>
          <div class="form-group">
            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label>Bulan</label>
                  <select class="form-control select2" name="bulan" style="width: 100%;">
                    <?php $bln = $bulan; for($m=1; $m<=12; ++$m): ?>
                      <option value="<?php echo $m; ?>" <?php if($bln == $m) echo "selected"; ?>><?php echo date('F', mktime(0, 0, 0, $m, 1)); ?></option>
                    <?php endfor; ?>  
                  </select>
                </div>
              </div>

              <div class="col-md-6">
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
              <th>Tanggal</th>
              <th>Jam Masuk</th>
              <th>Jam Keluar</th>
              <th>Status</th>
              <th>Keterangan</th>
            </tr>
            </thead>
            <tbody>
              <?php 
                if(isset($absensi)):
                foreach($absensi as $data) :
                  $tgl = $data->tanggal;
                  $masuk = date('H:i',strtotime($data->jam_masuk));
                  $keluar = date('H:i',strtotime($data->jam_keluar));
                  $keterangan = $data->keterangan;
                  $terlambat = $data->terlambat;
                  $kurangjam = $data->kurang_jam;
                  $status = $data->status;
                  $btn = "btn-primary";
                  if($status == "KURANG JAM"){
                    $btn = "btn-warning";
                    $keterangan = "Kurang Jam: ".$kurangjam." menit";
                  }else if($status == "TERLAMBAT" || $status == "TERLAMBAT & LUPA CHECK TIME"){
                    if($status == "TERLAMBAT & LUPA CHECK TIME")
                      $btn = "btn-danger";
                    else
                      $btn = "btn-warning";
                    $keterangan = "Terlambat: ".$terlambat." menit";
                  }else if($status == "IZIN PRIBADI" || $status == "HARI LIBUR"){
                    $btn = "btn-info";
                    /*if(!$data->_whole_day && $masuk != $keluar)
                      $keterangan .= "<br/>IZIN PRIBADI : ".$kurangjam+$terlambat." menit";*/
                  }else if($status == "TERLAMBAT & KURANG JAM"){
                    $btn = "btn-warning";
                    $keterangan = "Terlambat: ".$terlambat." menit & Kurang Jam: ".$kurangjam." menit";  
                  }else if($status == "TANPA KETERANGAN"){
                    $btn = "btn-danger";
                  }else if($status == "LUPA CHECK TIME"){
                    if($data->_form_chekin){
                      $btn = "btn-success";
                      $status = "FORM LUPA CHECK TIME";
                    }
                    else
                      $btn = "btn-danger";
                  }else if($status == "SAKIT" || $status == "CUTI"){
                    $btn = "btn-info";
                  }

                  if($tgl!=""):
              ?>
              <tr>
                <td><?php echo $tgl; ?></td>
                <td><?php echo $masuk; ?></td>
                <td><?php echo $keluar; ?></td>
                <td><?php echo $status; ?></td>
                <td><?php if($keterangan != NULL) echo $keterangan; ?></td>
              </tr>
            <?php endif; endforeach; ?>
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