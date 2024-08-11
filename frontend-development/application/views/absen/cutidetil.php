<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Cuti Detil</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Cuti Detil</li>
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
          <h3 class="card-title">Rekap Cuti <?php echo $emp; ?></h3>
        </div>
        <!-- /.card-header -->
        <div class="card-body">

          <table id="example1" class="table table-bordered table-striped">
            <thead>
            <tr>
              <th>Employee Code</th>
              <th>Nama</th>
              <th>Tanggal Cuti</th>
              <th>Keterangan</th>
            </tr>
            </thead>
            <tbody>
              <?php 
                if(isset($cuti)):
                foreach($cuti as $data) :
              ?>
              <tr>
                <td><?php echo $employee_code; ?></td>
                <td><?php echo $nama; ?></td>
                <td><?php echo $data->tgl; ?></td>
                <td><?php if($data->keterangan != NULL) echo $data->keterangan; ?></td>
              </tr>
            <?php endforeach;?>
            <?php endif; ?>
            </tbody>
          </table>
        </div>
      </div>
      <!-- /.row -->
    </div><!-- /.container-fluid -->
  </div>
</div>
  <!-- /.content -->
</section>
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>