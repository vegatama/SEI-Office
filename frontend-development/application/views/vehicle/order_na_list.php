<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-8">
        <h1 class="m-0">Daftar Pemesanan</h1>
      </div><!-- /.col -->
      <div class="col-sm-4">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item">Kendaraan Operasional</li>
          <li class="breadcrumb-item active">Need Approve</li>
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
            <h3 class="card-title"><i class="fas fa-file-invoice"></i> Pemesanan Kendaraan Operasional Need Approve</h3>
          </div>
          <div class="card-body">

            <table id="carna" class="table table-bordered table-striped">
              <thead>
              <tr>
                <th>Waktu Berangkat</th>
                <th>Tujuan</th>
                <th>Keperluan</th>
                <th>Kode Proyek</th>
                <th>Status</th>
                <th>Need Approve</th>
                <th>&nbsp;</th>
              </tr>
              </thead>
              <tbody>
              <?php 
                if(isset($ordersna)):
                foreach($ordersna as $dt) : 
              ?>
              <tr>
                <td><?php echo $dt->waktu_berangkat; ?></td>
                <td><?php echo $dt->tujuan; ?></td>
                <td><?php echo $dt->keperluan; ?></td>
                <td><?php echo $dt->kode_proyek; ?></td>
                <td>
                  <?php if($dt->status == "APPROVED") $btn = "btn-success"; elseif($dt->status == "Pending Approval") $btn = "btn-warning"; elseif($dt->status == "Rejected") $btn = "btn-danger"; ?>
                  <button type="button" class="btn <?php echo $btn; ?> btn-sm">
                    <?php echo $dt->status; ?>
                  </button>
                </td>
                <td><?php echo $dt->need_approve; ?></td>
                <td class="col-1 text-center">
                  <a class="btn btn-success btn-sm" href="<?php echo site_url('order/detail/'.$dt->order_id."/carorderna"); ?>">
                    <i class="fas fa-info"></i> Detail
                  </a>
                </td>
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

  <div class="row">
      <div class="col-md-12">
        <div class="card card-success">
          <div class="card-header">
            <h3 class="card-title"><i class="fas fa-file-invoice"></i> Pemesanan Kendaraan Operasional Approved</h3>
          </div>
          <div class="card-body">

            <table id="carapp" class="table table-bordered table-striped">
              <thead>
              <tr>
                <th>Waktu Berangkat</th>
                <th>Tujuan</th>
                <th>Keperluan</th>
                <th>Kode Proyek</th>
                <th>Status</th>
                <th>Need Approve</th>
                <th>&nbsp;</th>
              </tr>
              </thead>
              <tbody>
              <?php 
                if(isset($ordersapp)):
                foreach($ordersapp as $dt) : 
              ?>
              <tr>
                <td><?php echo $dt->waktu_berangkat; ?></td>
                <td><?php echo $dt->tujuan; ?></td>
                <td><?php echo $dt->keperluan; ?></td>
                <td><?php echo $dt->kode_proyek; ?></td>
                <td>
                  <?php if($dt->status == "APPROVED") $btn = "btn-success"; elseif($dt->status == "Pending Approval") $btn = "btn-warning"; elseif($dt->status == "Rejected") $btn = "btn-danger"; ?>
                  <button type="button" class="btn <?php echo $btn; ?> btn-sm">
                    <?php echo $dt->status; ?>
                  </button>
                </td>
                <td><?php echo $dt->need_approve; ?></td>
                <td class="col-1 text-center">
                  <a class="btn btn-success btn-sm" href="<?php echo site_url('order/detail/'.$dt->order_id."/carorderna/app"); ?>">
                    <i class="fas fa-info"></i> Detail
                  </a>
                </td>
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
