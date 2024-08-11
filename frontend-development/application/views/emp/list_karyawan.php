<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Karyawan</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Karyawan</li>
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
    <div class="col-12">

      <div class="card">
        <div class="card-header">
          <h3 class="card-title">Data Karyawan</h3>
        </div>
        <!-- /.card-header -->
        <div class="card-body">
          <table id="karyawanlist" class="table table-bordered table-striped">
            <thead>
            <tr>
              <th>NIK</th>
              <th>Nama</th>
              <th>Direktorat</th>
              <th>Unit Kerja</th>
              <th>&nbsp;</th>
            </tr>
            </thead>
            <tbody>
              <?php 
                if(isset($employees)):
                foreach($employees as $data) :  
                    if(trim($data->nik) != "") :                
              ?>
              <tr>
                <td><?php echo $data->nik; ?></td>
                <td><?php echo $data->name; ?></td>
                <td><?php echo $data->direktorat; ?></td>
                <td><?php echo $data->unit_kerja; ?></td>
                <td>
                  <a class="btn btn-success btn-sm" href="<?php echo site_url('karyawan/detail/'.$data->id); ?>">
                    <i class="fas fa-info"></i> Detail
                  </a>
                </td>
              </tr>
              <?php 
                    endif;  
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