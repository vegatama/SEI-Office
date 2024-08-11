<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-9">
        <h1 class="m-0">Budget Perubahan</h1>
      </div><!-- /.col -->
      <div class="col-sm-3">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('nav/dash'); ?>">NAV Document</a></li>
          <li class="breadcrumb-item active">Budget</li>
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
      <div class="col-md-12">
        <div class="card card-warning">
          <div class="card-header">
            <h3 class="card-title"><i class="fas fa-file-invoice"></i> Budget Perubahan Need Approve</h3>
          </div>
          <div class="card-body">

            <table id="nav" class="table table-bordered table-striped">
              <thead>
              <tr>
                <th>No</th>
                <th>Project Code</th>
                <th>Reason</th>
                <th>Status</th>
                <th>Need Approve User ID</th>
                <th>&nbsp;</th>
              </tr>
              </thead>
              <tbody>
              <?php 
                if(isset($budget)):
                foreach($budget as $dt) : 
              ?>
              <tr>
                <td><?php echo $dt->No; ?></td>
                <td><?php echo $dt->Kode_Proyek; ?></td>
                <td><?php echo $dt->Reason; ?></td>
                <td><?php echo $dt->Status; ?></td>
                <td><?php echo $dt->Need_Approve_User_Id; ?></td>
                <th><a class="btn btn-success btn-sm" href="<?php echo site_url('nav/budgetdetail/'.$dt->No); ?>">
                              <i class="fas fa-info">
                              </i>
                              Detail
                          </a></th>
              </tr>
              <?php 
                endforeach; 
                endif; 
              ?>
              </tbody>
            </table>
          </div>
        </div>
      </div>

    </div>

    <div class="row">
      <div class="col-md-12">
        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title"><i class="fas fa-file-invoice"></i> Budget Perubahan Approved</h3>
          </div>
          <div class="card-body">

            <table id="nava" class="table table-bordered table-striped">
              <thead>
              <tr>
                <th>No</th>
                <th>Project Code</th>
                <th>Reason</th>
                <th>Status</th>
                <th>Need Approve User ID</th>
                <th>&nbsp;</th>
              </tr>
              </thead>
              <tbody>
              <?php 
                if(isset($budgeta)):
                foreach($budgeta as $dta) : 
              ?>
              <tr>
                <td><?php echo $dta->No; ?></td>
                <td><?php echo $dta->Kode_Proyek; ?></td>
                <td><?php echo $dta->Reason; ?></td>
                <td><?php echo $dta->Status; ?></td>
                <td><?php echo $dta->Need_Approve_User_Id; ?></td>
                <th><a class="btn btn-success btn-sm" href="<?php echo site_url('nav/budgetdetail/'.$dta->No."/app"); ?>">
                              <i class="fas fa-info">
                              </i>
                              Detail
                          </a></th>
              </tr>
              <?php 
                endforeach; 
                endif; 
              ?>
              </tbody>
            </table>
          </div>
        </div>
      </div>

    </div>

  </div>
  <!-- /.content -->
  </section>
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>