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
          <li class="breadcrumb-item active">Master Data Karyawan</li>
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
          <h3 class="card-title">Karyawan</h3>
          <!-- <a class="btn btn-primary btn-sm float-sm-right" href="<?php echo site_url('karyawan/tambah'); ?>">
              <i class="fas fa-user"></i> Tambah Karyawan
          </a> -->
        </div>
        <!-- /.card-header -->
        <div class="card-body">
          <table id="karyawan" class="table table-bordered table-striped">
            <thead>
            <tr>
              <th>Kode</th>
              <th>Nama</th>
              <th>Status</th>
              <th>Email</th>
              <th></th>
            </tr>
            </thead>
            <tbody>
              <?php 
                foreach($karyawan as $data) :                  
              ?>
              <tr>
                <td><?php echo $data->employee_code; ?></td>
                <td><?php echo $data->name; ?></td>
                <td><?php echo $data->status; ?></td>
                <td><?php echo $data->email; ?></td>
                <td><a class="btn btn-primary btn-sm" href="<?php echo site_url('karyawan/view/'.$data->id); ?>" alt="view">
                              <i class="fas fa-folder"></i>
                          </a>
                          <a class="btn btn-info btn-sm" href="<?php echo site_url('karyawan/edit/'.$data->id); ?>" alt="edit">
                              <i class="fas fa-pencil-alt"></i>
                          </a>
                          <a class="btn btn-danger btn-sm" href="<?php echo site_url('karyawan/delete/'.$data->id); ?>" alt="delete">
                              <i class="fas fa-trash"></i>
                          </a></td>
              </tr>
            <?php endforeach; ?>
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