<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Information</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Info</li>
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

      </div>

      <div class="col-lg-12 col-6">
        <!-- small box -->
        <div class="small-box bg-info">
          <div class="inner">
            <h3>Oops, Something went wrong</h3>

            <p>There was a problem in our system, please try again later.</p>
          </div>
          <div class="icon">
            <i class="fas fa-exclamation-circle"></i>
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