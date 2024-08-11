<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-9">
        <h1 class="m-0">Detail Proyek <?php echo $proyek->project_code; ?> (<?php echo $proyek->project_name; ?>)</h1>
      </div><!-- /.col -->
      <div class="col-sm-3">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item">Proyek</li>
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
      <div class="col-md-6">
        <div class="card card-info">

        <div class="card-header">
          <h3 class="card-title"><i class="nav-icon fas fa-info-circle"></i> Detail Proyek</h3>
        </div>
        <!-- /.card-header -->
        <div class="card-body">
          <div class="form-group row">
            <label class="col-sm-3 col-form-label">Kode Proyek</label>
            <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $proyek->project_code; ?>" disabled>
          </div>

          <div class="form-group row">
            <label class="col-sm-3 col-form-label">Nama Proyek</label>
            <textarea class="col-sm-9 form-control" disabled><?php echo $proyek->project_name; ?></textarea>
          </div>

          <div class="form-group row">
            <label class="col-sm-3 col-form-label">Type Proyek</label>
            <input class="col-sm-9 form-control" type="text" value="<?php echo $proyek->project_type; ?>" disabled>
          </div>

          <div class="form-group row">
            <label class="col-sm-3 col-form-label">Nilai Proyek</label>
            <input class="col-sm-9 form-control" type="text" value="<?php echo number_format($proyek->project_value,0,",","."); ?>" disabled>
          </div>

          <div class="form-group row">
            <label class="col-sm-3 col-form-label">Pimpro</label>
            <input class="col-sm-9 form-control" type="text"  value="<?php echo $proyek->pimpro; ?>" disabled>
          </div>          

          <div class="form-group row">
            <label class="col-sm-3 col-form-label">Tanggal Mulai</label>
            <input class="col-sm-9 form-control" type="text" value="<?php echo $proyek->tgl_mulai; ?>" disabled>
          </div>

          <div class="form-group row">
            <label class="col-sm-3 col-form-label">Tanggal Akhir</label>
            <input class="col-sm-9 form-control" type="text" value="<?php echo $proyek->tgl_akhir; ?>" disabled>
          </div>

          <div class="form-group row">
            <label class="col-sm-3 col-form-label">Status</label>
            <input class="col-sm-9 form-control" type="text" value="<?php echo $proyek->status; ?>" disabled>
          </div>

          <div class="form-group row">
            <label class="col-sm-3 col-form-label">Carry Over</label>
            <input class="col-sm-1" type="checkbox" <?php if($proyek->carry_over) echo "checked"; ?> disabled>
          </div>
        </div>

        </div>

        <div class="card card-secondary">
          <div class="card-header">
            <h3 class="card-title"><i class="fas fa-receipt"></i> DPB/DPJ</h3>
          </div>
          <div class="card-body">
            <table class="table table-bordered table-striped">
              <thead>
              <tr>
                <th>No</th>
                <th>Tanggal</th>
                <th>Jumlah</th>
                <th>Status</th>
              </tr>
              </thead>
              <tbody>
              <?php 
                if(isset($dpbs)):
                foreach($dpbs as $dt) : 
              ?>
              <tr>
                <td><?php echo $dt->document_no; ?></td>
                <td><?php echo $dt->document_date; ?></td>
                <td><?php echo number_format($dt->amount,0,",","."); ?></td>
                <td><?php echo $dt->status; ?></td>
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

      <div class="col-md-6">
        <div class="card card-warning">
          <div class="card-header">
            <h3 class="card-title"><i class="fas fa-coins"></i> Anggaran</h3>
          </div>
          <div class="card-body">
            <table class="table table-bordered table-striped">
              <thead>
              <tr>
                <th>Mata Anggaran</th>
                <th>Budget</th>
                <th>Penggunaan</th>
                <!-- <th>Realisasi</th> -->
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
                <td><?php echo number_format($data->budget,0,",","."); ?></td>
                <td><?php echo number_format($data->penggunaan,0,",","."); ?></td>
                <!-- <td><?php echo number_format($data->realisasi,0,",","."); ?></td> -->
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