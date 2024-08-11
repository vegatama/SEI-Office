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
              <th>Hari</th>
              <th>Jam Masuk</th>
              <th>Jam Keluar</th>
              <th>Status</th>
              <th>Keterangan</th>
              <th>Form Lupa Check Time</th>
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
                  $hari = $data->hari;
                  $btn = "btn-primary";
                  if($status == "KURANG JAM" || $status == "KURANG JAM & IZIN PRIBADI"){
                    $btn = "btn-warning";
                    $keterangan = "Kurang Jam: ".$kurangjam." menit";
                  }else if($status == "TERLAMBAT" || $status == "TERLAMBAT & LUPA CHECK TIME" || $status == "TERLAMBAT & IZIN PRIBADI"){
                    if($status == "TERLAMBAT & LUPA CHECK TIME"){
                      if($data->_form_chekin)
                        $btn = "btn-warning";
                      else
                        $btn = "btn-danger";
                    }
                    else
                      $btn = "btn-warning";
                    $keterangan = "Terlambat: ".$terlambat." menit";
                  } else if($status == "TERLAMBAT + KURANG JAM & IZIN PRIBADI"){
                    $btn = "btn-warning";
                    $keterangan = "Terlambat: ".$terlambat." menit";
                    $keterangan .= ", Kurang Jam: ".$kurangjam." menit";
                  }
                  else if($status == "IZIN PRIBADI" || $status == "HARI LIBUR"){
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
                <td><?php echo $hari; ?></td>
                <td><?php echo $masuk; ?></td>
                <td><?php echo $keluar; ?></td>
                <td><div class="btn <?php echo $btn; ?>"><?php echo $status; ?></div></td>
                <td><?php if($keterangan != NULL) echo $keterangan; ?></td>
                <td><input type="checkbox" onclick="return false;" <?php if($data->_form_chekin) echo "checked" ?> /></td>
              </tr>
            <?php endif; endforeach; ?>
            <?php endif; ?>
            </tbody>
          </table>
          <?php if(isset($absensi)): ?>
          <table class="table table-bordered table-striped" id="data1">
            <tr>
              <td>
                <b>Total Lupa Check Time:</b> <?php echo $total_lupa; ?><br/>
                <b>Total Form Lupa Check Time:</b> <?php echo $total_form_lupa; ?><br/>
                <b>Total Keterlambatan:</b> <?php echo $total_terlambat; ?> menit<br/>
                <b>Total Izin Pribadi:</b> <?php echo $total_izin_hari; ?> hari & <?php echo $total_izin; ?> menit<br/>
                <b>Total Kurang Jam:</b> <?php echo $total_kurang_jam; ?> menit<br/>
                <b>Total Tanpa Keterangan:</b> <?php echo $total_alpha; ?> hari<br/>
              </td>
            </tr>
          </table>
        <?php endif; ?>
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