<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Master Data</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Master Data</li>
          <li class="breadcrumb-item active">Roles</li>
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

      <div class="card">
        <div class="card-header">
          <h3 class="card-title">Roles</h3>
          <a class="btn btn-primary btn-sm float-sm-right" href="<?php echo site_url('role/tambah'); ?>">
              <i class="far fa-calendar-alt"></i> Tambah Roles
          </a>
        </div>
        <!-- /.card-header -->
        <div class="card-body">
          <table id="karyawan" class="table table-bordered table-striped">
            <thead>
            <tr>
              <th>Roles</th>
              <th>Keterangan</th>
              <th></th>
            </tr>
            </thead>
            <tbody>
              <?php 
                if(isset($roles)):
                foreach($roles as $data) :                  
              ?>
              <tr>
                <td><?php echo $data->name; ?></td>
                <td><?php echo $data->description; ?></td>
                <td>
                  <a class="btn btn-success btn-sm" href="<?php echo site_url('role/detail/'.$data->id); ?>">
                    <i class="fas fa-info"></i> Detail
                  </a>
                  <a class="btn btn-warning btn-sm" href="<?php echo site_url('role/update/'.$data->id); ?>">
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
                        <?php echo form_open('role/delete'); ?>
                        <?php echo form_hidden('id',$data->id); ?>
                        <div class="modal-body">
                          Hapus data Role : <?php echo $data->name; ?> ?
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
      <!-- /.row -->
    </div><!-- /.container-fluid -->
  </div>
  <!-- /.content -->
  </section>
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>