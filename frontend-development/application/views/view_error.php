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
  <div class="row">&nbsp;</div>
  <div class="row">
    <div class="col-1"></div>
    <div class="col-10">
    <?php if(isset($error)) : ?>
    <div class="alert alert-danger" align="center">
      <strong>Error : </strong><?php echo $error; ?>
    </div>
    <?php endif; ?>
    <?php if(isset($info)) : ?>
    <div class="alert alert-warning" align="center">
      <strong>Info : </strong><?php echo $info; ?>
    </div>
    <?php endif; ?>
    <br/>
    </div>
  </div>

  <div class="row">
    <div class="col-1"></div>
    <div class="col-10">
      <div class="card">


  

  <!--<div class="card-header">
    <h3 class="card-title">Error</h3>
  </div>-->
    <div class="card-body">

        <div class="col-lg-12 col-6">
            <!-- small box -->
            <div class="small-box bg-info">
            <div class="inner">
                <h3>Oops, Something went wrong</h3>

                <p>There was a problem, please try again later.</p>
            </div>
            <div class="icon">
                <i class="fas fa-exclamation-circle"></i>
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
