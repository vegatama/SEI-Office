<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>SEI Office - Suryaenergi.my.id</title>

  <!-- Google Font: Source Sans Pro -->
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="<?php echo site_url('plugins/fontawesome-free/css/all.min.css'); ?>">
  <!-- icheck bootstrap -->
  <link rel="stylesheet" href="<?php echo site_url('plugins/icheck-bootstrap/icheck-bootstrap.min.css'); ?>">
  <!-- Theme style -->
  <link rel="stylesheet" href="<?php echo site_url('dist/css/adminlte.min.css'); ?>">
  <link href="<?php echo site_url('img/favico.ico'); ?>" rel="shortcut icon" type="image/x-icon">
  <style>
     #hint {
      cursor: pointer;
      pointer-events: all;
    }
  </style>
</head>
<body class="hold-transition login-page">
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
<div class="login-box">
  <div class="login-logo">
    SEI Office
  </div>
  <!-- /.login-logo -->
  <div class="card">
    <div class="card-body login-card-body">
      <p class="login-box-msg">Silahkan login menggunakan akun Anda</p>

      <?php echo form_open("login/proses"); ?>
        <?php echo form_error('email', '<div class="alert alert-warning">', '</div>'); ?>
        <div class="input-group mb-3">
          <input type="email" class="form-control" placeholder="Email" name="email">
          <div class="input-group-append">
            <div class="input-group-text">
              <span class="fas fa-envelope"></span>
            </div>
          </div>
        </div>
        <?php echo form_error('password', '<div class="alert alert-warning">', '</div>'); ?>
        <div class="input-group mb-3">
          <input type="password" class="form-control" placeholder="Password" name="password" id="password">
          <div class="input-group-append">
            <div class="input-group-text">
              <span class="fas fa-eye" id="hint"></span>
            </div>
          </div>
        </div>
        <div class="row">
          <!-- /.col -->
          <div class="col-12">
            <button type="submit" class="btn btn-primary btn-block">Login</button>
          </div>
          <!-- /.col -->
        </div>
      <?php echo form_close(); ?>

      <div class="social-auth-links text-center mb-3">
        <p>- OR -</p>
        <a href="<?php echo $login_google; ?>" class="btn btn-block btn-success">
          <i class="fab fa-google mr-2"></i> Sign in using Suryaenergi Account
        </a>
      </div>
      <!-- /.social-auth-links -->
    <!-- /.login-card-body -->
  </div>
</div>
<!-- /.login-box -->

<!-- jQuery -->
<script src="<?php echo site_url('plugins/jquery/jquery.min.js'); ?>"></script>
<!-- Bootstrap 4 -->
<script src="<?php echo site_url('plugins/bootstrap/js/bootstrap.bundle.min.js'); ?>"></script>
<!-- AdminLTE App -->
<script src="<?php echo site_url('dist/js/adminlte.min.js'); ?>"></script>
<script>
  function myFunction() {
    var x = document.getElementById("password");
    if (x.type === "password") {
      x.type = "text";
    } else {
      x.type = "password";
    }
  }
  $('#hint').on('click', function() {
    $(this).toggleClass('fa-eye-slash').toggleClass('fa-eye'); // toggle our classes for the eye icon
    myFunction();
  });
</script>
</body>
</html>