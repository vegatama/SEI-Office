<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-9">
        <h1 class="m-0">Kendaraan</h1>
      </div><!-- /.col -->
      <div class="col-sm-3">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item">Mobil Operasional</li>
          <li class="breadcrumb-item active">List</li>
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
            <h3 class="card-title"><i class="fas fa-file-invoice"></i> Kendaraan</h3>
            <a class="btn btn-secondary btn-sm float-sm-right" href="<?php echo site_url('mobil/add'); ?>">
              <i class="fas fa-car"></i>&nbsp; Tambah Kendaraan
            </a>
          </div>
          <div class="card-body">

            <table id="car" class="table table-bordered table-striped">
              <thead>
              <tr>
                <th>No. Plat</th>
                <th>Tahun</th>
                <th>Merk</th>
                <th>Tipe</th>
                <th>BBM</th>
                <th>Ownership</th>
                <th>&nbsp;</th>
              </tr>
              </thead>
              <tbody>
              <?php 
                if(isset($vehicles)):
                foreach($vehicles as $dt) : 
              ?>
              <tr>
                <td><?php echo $dt->plat_number; ?></td>
                <td><?php echo $dt->year; ?></td>
                <td><?php echo $dt->merk; ?></td>
                <td><?php echo $dt->type; ?></td>
                <td><?php echo $dt->bbm; ?></td>
                <td><?php echo $dt->ownership; ?></td>
                <td class="col-3 text-center">
                  <a class="btn btn-success btn-sm" href="<?php echo site_url('mobil/detail/'.$dt->vehicle_id); ?>">
                    <i class="fas fa-info"></i> Detail
                  </a>
                  <a class="btn btn-warning btn-sm" href="<?php echo site_url('mobil/update/'.$dt->vehicle_id); ?>">
                    <i class="fas fa-wrench"></i> Update
                  </a>
                  <button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#deleteDialog">
                    <i class="fas fa-ban"></i>&nbsp; Delete
                  </button>

                  <!-- Modal Delete -->
                  <div class="modal fade" id="deleteDialog" tabindex="-1" role="dialog" aria-labelledby="approveModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                      <div class="modal-content">
                        <div class="modal-header">
                          <h5 class="modal-title" id="exampleModalLabel">Hapus Data</h5>
                          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                          </button>
                        </div>
                        <?php echo form_open('mobil/delete'); ?>
                        <?php echo form_hidden('id',$dt->vehicle_id); ?>
                        <div class="modal-body">
                          Hapus data kendaraan : <?php echo $dt->plat_number; ?> tahun <?php echo $dt->year; ?> ?
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
