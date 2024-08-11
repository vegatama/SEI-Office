<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Kirim Email Rekap Absen</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Kirim Email Rekap Absen</li>
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
          <h3 class="card-title">Kirim Email Rekap Absensi Karyawan SEI</h3>
        </div>
        <!-- /.card-header -->
        <div class="card-body">

          <?php echo form_open('absen/emailrekap'); ?>
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

          <?php 
            echo form_open('absen/kirimemail');
            echo form_hidden('tahun',$tahun);
            echo form_hidden('bulan',$bulan); 
          ?>
          <table class="table table-bordered table-striped">
            <thead>
            <tr>
              <th>Employee Code</th>
              <th>Nama</th>
              <th>NIK</th>
              <th>Status</th>
              <th>THP</th>
              <th>Total Potongan</th>
              <th>Email</th>
              <th>Kirim Email (<a href="#" onclick="unchecked()">unchecked all</a>)</th>
            </tr>
            </thead>
            <tbody>
              <?php 
                foreach($rekap as $data) :
                  $emp = $data->employee_code;
              ?>
              <tr>
                <td><?php echo anchor("absen/detil/".$emp."/".$tahun."/".$bulan,$data->employee_code); ?></td>
                <td><?php echo anchor("absen/detil/".$emp."/".$tahun."/".$bulan,$data->name); ?></td>
                <td><?php echo $data->nik; ?></td>
                <td><?php echo $data->status; ?></td>
                <td><?php echo number_format($data->thp,0); ?></td>
                <td><?php echo number_format($data->total_potongan,2); ?></td>
                <td><?php echo $data->email; ?></td>
                <td><input type="checkbox" <?php if($data->total_potongan > 0 && $emp != "EMP003" && $emp != "EMP101" && $emp != "EMP176" && $data->email != "" && $emp != "EMP353" && $emp != "EMP378") echo "checked"; ?> name="kirim[]" value="<?php echo $emp; ?>" /></td>
              </tr>
            <?php endforeach; ?>
              <tr>
                <td colspan="8">Batas Konfirmasi: <input type="text" id="datepicker" name="tanggal_akhir" readonly required />&nbsp;&nbsp;<button name="submit" class="btn btn-primary">Kirim Email</button></td>
              </tr>
            </tbody>
          </table>
          <?php echo form_close(); ?>

        </div>
      </div>
      <!-- /.row -->
    </div><!-- /.container-fluid -->
  </div>
  <!-- /.content -->
  </div>
  </section>
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>