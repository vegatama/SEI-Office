<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-9">
        <h1 class="m-0">Anggaran Proyek <?php echo $proyek->project_code; ?> (<?php echo $proyek->project_name; ?>)</h1>
      </div><!-- /.col -->
      <div class="col-sm-3">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item">Proyek</li>
          <li class="breadcrumb-item">Anggaran</li>
          <li class="breadcrumb-item active"><?php echo $proyek->project_code; ?></li>
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
            <h3 class="card-title"><i class="fas fa-coins"></i> Anggaran</h3>
          </div>
          <div class="card-body">
            <table id="budget" class="table table-bordered table-striped">
              <thead>
              <tr>
                <th>Mata Anggaran</th>
                <th>Nama Anggaran</th>
                <th>Budget</th>
                <th>Penggunaan</th>
                <!-- <th>Realisasi</th> -->
                <th>Sisa</th>
              </tr>
              </thead>
              <tbody>
              <?php 
                if(isset($budgets)):
                foreach($budgets as $data) : 
                  if($data->budget != 0 && $data->penggunaan != 0) :
              ?>
              <tr>
                <td><?php echo $data->gl_account; ?></td>
                <td><?php echo $data->gl_name; ?></td>
                <td><?php echo number_format($data->budget,0,",","."); ?></td>
                <td><?php echo number_format($data->penggunaan,0,",","."); ?></td>
                <!-- <td><?php echo number_format($data->realisasi,0,",","."); ?></td> -->
                <?php $sisa = $data->budget - $data->penggunaan; ?>
                <td><?php echo number_format($sisa,0,",","."); ?></td>
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
      </div>

    </div>
  </div>
  <!-- /.content -->
  </section>
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>