<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-9">
        <h1 class="m-0">Permohonan Uang Muka Multiple</h1>
      </div><!-- /.col -->
      <div class="col-sm-3">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('nav/dash'); ?>">NAV Document</a></li>
          <li class="breadcrumb-item active">UM Multiple</li>
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
            <h3 class="card-title"><i class="fas fa-file-invoice"></i> Uang Muka Multiple Need Approve</h3>
          </div>
          <div class="card-body">

            <table id="nav" class="table table-bordered table-striped">
              <thead>
              <tr>
                <th>No</th>
                <th>Project Code</th>
                <th>Project Name</th>
                <th>Amount</th>
                <th>Status</th>
                <th>Need Approve User ID</th>
                <th>&nbsp;</th>
              </tr>
              </thead>
              <tbody>
              <?php 
                if(isset($um)):
                foreach($um as $dt) : 
              ?>
              <tr>
                <td><?php echo $dt->Nomor; ?></td>
                <td><?php echo $dt->Kode_Kegiatan; ?></td>
                <td><?php echo $dt->Nama_Kegiatan; ?></td>
                <td><?php echo number_format($dt->gVarTotalAmount,2,",","."); ?></td>
                <td><?php echo $dt->Status; ?></td>
                <td><?php echo $dt->Need_Approve_User_Id; ?></td>
                <th><a class="btn btn-success btn-sm" href="<?php echo site_url('nav/umdetail/'.$dt->Nomor."/no/multi"); ?>">
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
          <div class="card-header">Uang Muka Multiple Approved</h3>
          </div>
          <div class="card-body">

            <table id="nava" class="table table-bordered table-striped">
              <thead>
              <tr>
                <th>No</th>
                <th>Project Code</th>
                <th>Project Name</th>
                <th>Amount</th>
                <th>Status</th>
                <th>Need Approve User ID</th>
                <th>&nbsp;</th>
              </tr>
              </thead>
              <tbody>
              <?php 
                if(isset($uma)):
                foreach($uma as $dta) : 
              ?>
              <tr>
                <td><?php echo $dta->Nomor; ?></td>
                <td><?php echo $dta->Kode_Kegiatan; ?></td>
                <td><?php echo $dta->Nama_Kegiatan; ?></td>
                <td><?php echo number_format($dta->gVarTotalAmount,2,",","."); ?></td>
                <td><?php echo $dta->Status; ?></td>
                <td><?php echo $dta->Need_Approve_User_Id; ?></td>
                <th><a class="btn btn-success btn-sm" href="<?php echo site_url('nav/umdetail/'.$dta->Nomor."/app/multi"); ?>">
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