<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Lokasi Karyawan</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('master/lokasi'); ?>">Master Data</a></li>
          <li class="breadcrumb-item active">Lihat Lokasi Karyawan</li>
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
          <h3 class="card-title">List Lokasi Karyawan</h3>
        </div>
        <!-- /.card-header -->

        <div class="card-body">
          <table id="karyawan" class="table table-bordered table-striped">
            <thead>
            <tr>
              <th>ID</th>
              <th>Nama</th>
              <th>Divisi</th>
              <th>Lokasi Absen</th>
              <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <?php 
                foreach($karyawan as $data) :                  
              ?>
                        <tr>
                            <td><?php echo $data->employee_code; ?></td>
                            <td><?php echo $data->name; ?></td>
                            <td><?php echo $data->unit_kerja; ?></td>
                            <td><?php echo $data->lokasi_absen; ?></td>
                            <td>
                                <a class="btn btn-primary w-100 py-1 px-3 mr-2" href="<?php echo site_url('lokasi/editLokasiKaryawan/'.$data->employee_code); ?>">Edit</a>
                            </td>
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
