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
    <div class="row">
        <div class="col-sm-3">
            <img src="<?php echo base_url('/img/logo-len.png'); ?>" />
        </div>
        <div class="col-sm-9">
            <img class="float-sm-right" src="<?php echo base_url('/img/logo-sei.png'); ?>" />
        </div>
    </div>
    <p class="text-center"><strong><u>DAFTAR HADIR</u></strong></p>
  </div>
  <div class="card-body">

    <table id="example1" class="table table-striped">
        <tbody>
            <tr>
                <td class="col-4">Kegiatan</td>
                <td class="col-sm-1">:</td>
                <td><?php echo $hadir->kegiatan; ?></td>
            </tr>
            <tr>
                <td>Subyek</td>
                <td>:</td>
                <td><?php echo $hadir->subyek; ?></td>
            </tr>
            <tr>
                <td>Tanggal</td>
                <td>:</td>
                <td><?php echo $hadir->tanggal; ?></td>
            </tr>
            <tr>
                <td>Waktu</td>
                <td>:</td>
                <td><?php echo $hadir->waktu_mulai." - ".$hadir->waktu_selesai; ?></td>
            </tr>
            <tr>
                <td>Trainer / Pimpinan Rapat</td>
                <td>:</td>
                <td><?php echo $hadir->pimpinan; ?></td>
            </tr>
        </tbody>
    </table>

  </div>
  <hr/>
    <div class="card-body">
    <table id="peserta" class="table table-striped table-bordered">
    <thead>
        <tr>
            <th class="col-4">Nama</th>
            <th class="col-4">Divisi / Bagian</th>
            <th class="col-4">Email / Phone</th>
        </tr>
    </thead>
    <tbody>
        <?php foreach($hadir->data_peserta as $ps): ?>
        <tr>
            <td><?php echo $ps->nama; ?></td>
            <td><?php echo $ps->bagian; ?></td>
            <td><?php echo $ps->email_phone; ?></td>
        </tr>
        <?php endforeach; ?>
    </tbody>
    </table>
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
    $("#peserta").DataTable({
      "pageLength": 20,
      "responsive": true, "lengthChange": false, "autoWidth": false, "paging": false, "info": false
    }).buttons().container().appendTo('#peserta_wrapper .col-md-6:eq(0)');
  });
</script>
</body>
</html>
