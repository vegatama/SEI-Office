<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Cuti Karyawan</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Rekap Cuti Karyawan</li>
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
          <h3 class="card-title">Rekap Cuti Karyawan</h3>
        </div>
        <!-- /.card-header -->
        <div class="card-body">

          <?php echo form_open('absen/rekapcuti'); ?>
          <div class="form-group">
            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label>Bulan</label>
                  <select class="form-control select2" name="bulan" style="width: 100%;">
                    <?php $bln = $bulan; for($m=1; $m<=12; ++$m): ?>
                      <option value="<?php echo $m; ?>" <?php if($bln == $m) echo "selected"; ?>><?php echo date('F', mktime(0, 0, 0, $m, 1)); ?></option>
                    <?php endfor; ?>  
                  </select>
                </div>
              </div>

              <div class="col-md-6">
                <div class="form-group">
                  <label>Tahun</label>
                  <select class="form-control select2" name="tahun" style="width: 100%;">
                    <?php for($i=-1; $i<=1; $i++): $thn = $tahun + $i;?>
                      <option value="<?php echo $thn; ?>" <?php if($tahun == $thn) echo "selected"; ?>><?php echo $thn; ?></option>
                    <?php endfor; ?>
                  </select>
                </div>
              </div>
            </div>
            <button class="btn btn-primary" type="submit" name="tampil">Tampilkan Data</button>
          </div>
          <?php echo form_close(); ?><br/>

          <table id="karyawan" class="table table-bordered table-striped">
            <thead>
            <tr>
              <th>Kode</th>
              <th>Nama</th>
              <th>NIK</th>
              <th>Bagian Fungsi</th>
              <th>Sisa Cuti</th>
              <th>Jumlah Cuti Bulan Ini</th>
              <th>Tanggal Cuti</th>
            </tr>
            </thead>
            <tbody>
              <?php 
                foreach($cuti as $data) :                  
              ?>
              <tr>
                <td><?php echo anchor("absen/detilcuti/".$data->employee_code."/".$tahun."/".$bulan,$data->employee_code); ?></td>
                <td><?php echo anchor("absen/detilcuti/".$data->employee_code."/".$tahun."/".$bulan,$data->name); ?></td>
                <td><?php echo $data->nik; ?></td>
                <td><?php echo $data->bagian_fungsi; ?></td>
                <td><?php echo $data->sisa_cuti; ?></td>
                <td><?php echo $data->jumlah_cuti; ?> hari</td>
                <td><?php echo $data->tgl_cuti; ?></td>
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