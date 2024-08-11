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
          <li class="breadcrumb-item active">Pemesanan</li>
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
            <h3 class="card-title"><i class="fas fa-file-invoice"></i> Pemesanan Kendaraan Operasional</h3>
            <a class="btn btn-secondary btn-sm float-sm-right" href="<?php echo site_url('order/add'); ?>">
              <i class="fas fa-car"></i>&nbsp; Tambah Pesanan
            </a>
          </div>
          <div class="card-body">

            <table id="car" class="table table-bordered table-striped">
              <thead>
              <tr>
                <th width="12%">No. Order</th>
                <th>Waktu Berangkat</th>
                <th>Tujuan</th>                
                <th>Kode Proyek</th>
                <th>Status</th>
                <th>Need Approve</th>
                <th>&nbsp;</th>
              </tr>
              </thead>
              <tbody>
              <?php 
                if(isset($orders)):
                foreach($orders as $dt) : 
              ?>
              <tr>
                <td><?php echo $dt->order_id; ?></td>
                <td><?php echo $dt->waktu_berangkat; ?></td>
                <td><?php echo $dt->tujuan; ?></td>
                <td><?php echo $dt->kode_proyek; ?></td>
                <td>
                  <?php if($dt->status == "APPROVED") $btn = "btn-success"; elseif($dt->status == "Pending Approval") $btn = "btn-warning"; elseif($dt->status == "REJECTED") $btn = "btn-danger"; else $btn = "btn-secondary"; ?>
                  <button type="button" class="btn <?php echo $btn; ?> btn-sm">
                    <?php echo $dt->status; ?>
                  </button>
                </td>
                <td><?php echo $dt->need_approve; ?></td>
                <td class="col-3 text-center">
                  <a class="btn btn-success btn-sm" href="<?php echo site_url('order/detail/'.$dt->order_id); ?>">
                    <i class="fas fa-info"></i> Detail
                  </a>
                  <?php if($dt->status != "APPROVED"): ?>
                  <a class="btn btn-warning btn-sm" href="<?php echo site_url('order/update/'.$dt->order_id); ?>">
                    <i class="fas fa-wrench"></i> Update
                  </a>
                  <button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#deleteDialog<?php echo $dt->order_id; ?>">
                    <i class="fas fa-ban"></i>&nbsp; Delete
                  </button>
                  <?php endif; ?>

                  <!-- Modal Delete -->
                  <div class="modal fade" id="deleteDialog<?php echo $dt->order_id; ?>" tabindex="-1" role="dialog" aria-labelledby="approveModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                      <div class="modal-content">
                        <div class="modal-header">
                          <h5 class="modal-title" id="exampleModalLabel">Hapus Data</h5>
                          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                          </button>
                        </div>
                        <?php echo form_open('order/delete'); ?>
                        <?php echo form_hidden('id',$dt->order_id); ?>
                        <div class="modal-body">
                          Hapus data order kendaraan : <?php echo $dt->waktu_berangkat; ?>, Tujuan: <?php echo $dt->tujuan; ?> ?
                        </div>
                        <div class="modal-footer">
                          <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                          <button type="submit" class="btn btn-danger">Confirm Delete</button>
                        </div>
                        <?php echo form_close(); ?>
                      </div>
                    </div>
                  </div>

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