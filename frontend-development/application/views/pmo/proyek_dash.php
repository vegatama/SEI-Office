<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Dashboard Proyek</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item">Proyek</li>
          <li class="breadcrumb-item active">Dashboard</li>
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
          <h3 class="card-title">Daftar Proyek</h3>
        </div>
        <!-- /.card-header -->
        <div class="card-body">

          <?php echo form_open('proyek/dashboard'); ?>
          <div class="form-group">
            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label>Tipe</label>
                  <select class="form-control select2" name="tipe" style="width: 100%;">
                      <option value="Proyek" <?php if($tipe=='Proyek') echo 'selected'; ?>>Proyek</option>
                      <option value="Rutin" <?php if($tipe=='Rutin') echo 'selected'; ?>>Rutin</option>
                      <option value="Investasi" <?php if($tipe=='Investasi') echo 'selected'; ?>>Investasi</option>
                  </select>
                </div>
              </div>

              <div class="col-md-6">
                <div class="form-group">
                  <label>Tahun</label>
                  <select class="form-control select2" name="tahun" style="width: 100%;">
                    <?php for($i=-2; $i<=0; $i++): $thn = date('Y') + $i;?>
                      <option value="<?php echo $thn; ?>" <?php if($tahun == $thn) echo "selected"; ?>><?php echo $thn; ?></option>
                    <?php endfor; ?>
                  </select>
                </div>
              </div>
            </div>
            <button class="btn btn-primary" type="submit" name="tampil">Tampilkan Data</button>
          </div>
          <?php echo form_close(); ?><br/>

          <table id="proyek" class="table table-bordered table-striped">
            <thead>
            <tr>
              <th>Kode Proyek</th>
              <th>Nama</th>
              <th>Pimpro</th>
              <th>Nilai Proyek</th>
              <th>&nbsp;</th>
            </tr>
            </thead>
            <tbody>
              <?php 
                if(isset($proyeks)):
                foreach($proyeks as $data) :                  
              ?>
              <tr>
                <td><?php echo $data->project_code; ?></td>
                <td><?php echo $data->project_name; ?></td>
                <td><?php echo $data->pimpro; ?></td>
                <td><?php echo number_format($data->project_value,0,",","."); ?></td>
                <td><a class="btn btn-primary btn-sm" href="<?php echo site_url('proyek/detail/'.$data->project_code); ?>">
                              <i class="fas fa-info">
                              </i>
                              Detail
                          </a>
                          <a class="btn btn-info btn-sm" href="<?php echo site_url('proyek/budget/'.$data->project_code); ?>">
                              <i class="fas fa-coins">
                              </i>
                              Anggaran
                          </a>
                          <a class="btn btn-warning btn-sm" href="<?php echo site_url('proyek/dpb/'.$data->project_code); ?>">
                              <i class="fas fa-chart-bar">
                              </i>
                              DPB
                          </a></td>
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