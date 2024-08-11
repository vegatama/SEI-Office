<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>SEI Office</title>

  <!-- Google Font: Source Sans Pro -->
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="<?php echo site_url('plugins/fontawesome-free/css/all.min.css'); ?>">
    <!-- DataTables -->
  <link rel="stylesheet" href="<?php echo site_url('plugins/datatables-bs4/css/dataTables.bootstrap4.min.css'); ?>">
  <link rel="stylesheet" href="<?php echo site_url('plugins/datatables-responsive/css/responsive.bootstrap4.min.css'); ?>">
  <link rel="stylesheet" href="<?php echo site_url('plugins/datatables-buttons/css/buttons.bootstrap4.min.css'); ?>">
  <!-- Theme style -->
  <link rel="stylesheet" href="<?php echo site_url('dist/css/adminlte.min.css'); ?>">
  <link href="<?php echo site_url('img/favico.ico'); ?>" rel="shortcut icon" type="image/x-icon">
</head>
<body>

<!-- Automatic element centering -->
<section class="content">
<div class="container-fluid">
  <div class="row">&nbsp;</div>
  <div class="row">
    <div class="col-1"></div>
    <div class="col-10">
      <div class="card">


  <?php if(isset($error)) : ?>
  <div class="alert alert-danger" align="center">
    <strong>Error : </strong><?php echo $error; ?>
  </div>
  <?php endif; ?>
  <?php if(isset($info)) : ?>
  <div class="alert alert-info" align="center">
    <strong>Info : </strong><?php echo $info; ?>
  </div>
  <?php endif; ?>

  <div class="card-header">
    <h3 class="card-title">Rekap Absensi: <?php echo $nama; ?> - <?php echo $nmbulan." ".$tahun ?></h3>
  </div>
  <div class="card-body">

  <table id="example1" class="table table-bordered table-striped">
    <thead>
    <tr>
      <th>Tanggal</th>
      <th>Status</th>
      <th>Hari</th>
      <th>Jam Masuk</th>
      <th>Jam Keluar</th>
      <th>Keterangan</th>
      <th>Form Lupa Check Time</th>
    </tr>
    </thead>
    <tbody>
      <?php 
        if(isset($absensi)):
        foreach($absensi as $data) :
          $tgl = $data->tanggal;
          $masuk = strtotime($data->jam_masuk);
          $keluar = strtotime($data->jam_keluar);
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
        <td><div class="btn <?php echo $btn; ?>"><?php echo $status; ?></div></td>
        <td><?php echo $hari; ?></td>
        <td><?php echo date('H:i',$masuk); ?></td>
        <td><?php echo date('H:i',$keluar); ?></td>
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
    </div>
  </div>
</div>
</section>
<!-- /.center -->

<!-- jQuery -->
<script src="<?php echo site_url('plugins/jquery/jquery.min.js'); ?>"></script>
<!-- Bootstrap 4 -->
<script src="<?php echo site_url('plugins/bootstrap/js/bootstrap.bundle.min.js'); ?>"></script>
<!-- DataTables  & Plugins -->
<script src="<?php echo site_url('plugins/datatables/jquery.dataTables.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/datatables-bs4/js/dataTables.bootstrap4.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/datatables-responsive/js/dataTables.responsive.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/datatables-responsive/js/responsive.bootstrap4.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/datatables-buttons/js/dataTables.buttons.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/datatables-buttons/js/buttons.bootstrap4.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/jszip/jszip.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/pdfmake/pdfmake.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/pdfmake/vfs_fonts.js'); ?>"></script>
<script src="<?php echo site_url('plugins/datatables-buttons/js/buttons.html5.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/datatables-buttons/js/buttons.print.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/datatables-buttons/js/buttons.colVis.min.js'); ?>"></script>
<script>
  $(function () {
    $("#example1").DataTable({
      "pageLength": 30,
      "responsive": true, "lengthChange": false, "autoWidth": false, "paging": false, "info": false,
      "buttons": ["copy", "csv", "excel", "pdf", "print"]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
  });
</script>
</body>
</html>
