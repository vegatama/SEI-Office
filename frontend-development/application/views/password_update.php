<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Update Your Biodata</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Profile</li>
        </ol>
      </div><!-- /.col -->
    </div><!-- /.row -->
  </div><!-- /.container-fluid -->
</div>
<!-- /.content-header -->

<!-- Main content -->
<section class="content">
<div class="content">
  <div class="container-fluid">
    <div class="row">
      <div class="col-12">

        <div class="card card-primary">

          <div class="card-header">
            <h3 class="card-title">PERSONAL INFO</h3>
          </div>
          <!-- /.card-header -->
          
          <?php echo form_open('profil/pupdate'); ?>
          <div class="card-body">
            <div class="form-group">
              <label for="exampleInputEmail1">Old Password</label>
              <?php echo form_error('oldpass','<div class="alert alert-warning">','</div>'); ?>
              <input type="password" name="oldpass" class="form-control" required placeholder="Enter New Password">
            </div>
            <div class="form-group">
              <label for="exampleInputEmail1">New Password</label>
              <?php echo form_error('newpass','<div class="alert alert-warning">','</div>'); ?>
              <input type="password" name="newpass" class="form-control" required placeholder="Enter New Password">
            </div>
            <div class="form-group">
              <label for="exampleInputPassword1">Confirm New Password</label>
              <?php echo form_error('confirmnewpass','<div class="alert alert-warning">','</div>'); ?>
              <input type="password" name="confirmnewpass" class="form-control" required placeholder="Confirm New Password">
            </div>
          </div>
          <div class="card-footer">
            <button type="submit" class="btn btn-primary">Update Password</button>
          </div>
          <?php echo form_close(); ?>

        </div>  

      </div>    
    </div>
    <!-- /.row -->
  </div><!-- /.container-fluid -->
</div>
</section>
<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>