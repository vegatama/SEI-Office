<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-8">
        <h1 class="m-0">Rekap Pemesanan</h1>
      </div><!-- /.col -->
      <div class="col-sm-4">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item">Rekap Pemesanan</li>
          <li class="breadcrumb-item active">Rekap Pemesanan</li>
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
        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title"><i class="fas fa-file-invoice"></i> Rekap Pemesanan Kendaraan Operasional</h3>
          </div>
          <div class="card-body">

            <table id="car" class="table table-bordered table-striped">
              <thead>
              <tr>
                <th width="12%">No. Order</th>
                <th>Waktu Berangkat</th>
                <th>Tujuan</th>                
                <th>Kode Proyek</th>
                <th>Action</th>
              </tr>
              </thead>
              <tbody>
              <?php 
                if(isset($rekap)):
                foreach($rekap as $dt) : 
              ?>
              <tr>
                <td><?php echo $dt->order_id; ?></td>
                <td><?php echo $dt->waktu_berangkat; ?></td>
                <td><?php echo $dt->tujuan; ?></td>
                <td><?php echo $dt->kode_proyek; ?></td>
                <td class="col-3 text-center">
                    <a class="btn btn-success btn-sm" href="<?php echo site_url('order/rekapDetail/'.$dt->order_id); ?>">
                        <i class="fas fa-info"></i> Detail
                    </a>
                    <a class="btn btn-primary btn-sm" href="<?php echo site_url('order/updaterekap/'.$dt->order_id); ?>">
                        <i class="fas fa-edit"></i> Update
                    </a>
                    <a class="btn btn-success btn-sm" href="<?php echo site_url('order/done/'.$dt->order_id); ?>">
                        <i class="fas fa-check"></i> Selesai
                    </a>
                    <a class="btn btn-info btn-sm" href="<?php echo site_url('order/download/'.$dt->order_id); ?>">
                        <i class="fas fa-print"></i> Print
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