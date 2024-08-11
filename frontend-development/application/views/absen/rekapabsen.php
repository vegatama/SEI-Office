<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Rekap Absen</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Rekap Absen</li>
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
          <h3 class="card-title">Rekap Absensi Karyawan SEI</h3>
        </div>
        <!-- /.card-header -->
        <div class="card-body">

          <?php echo form_open('absen/rekap'); ?>
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

          <table id="rekap" class="table table-bordered table-striped">
            <thead>
            <tr>
              <th>Employee Code</th>
              <th>Nama</th>
              <th>NIK</th>
              <th>Status</th>
              <th>Tahun</th>
              <th>Bulan</th>
              <th>Jumlah Lupa Check Time</th>
              <th>Form Lupa Check Time</th>
              <th>Tanpa Keterangan</th>
              <th>Keterlambatan (menit)</th>
              <th>Kurang Jam Kerja (menit)</th>
              <th>Izin Pribadi (hari)</th>
              <th>Izin Pribadi (menit)</th>
              <th>Cuti</th>
              <th>Sakit</th>
              <th>Sisa Cuti Sebelumnya</th>
              <th>Jumlah Cuti Terpakai (Cuti + Izin Pribadi)</th>
              <th>Sisa Cuti dikurangi Jumlah Cuti Terpakai</th>
              <th>THP</th>
              <th>Pemotong Harian</th>
              <th>Potongan Lupa Check Time</th>
              <th>Potongan Tanpa Keterangan</th>
              <th>Potongan Terlambat</th>
              <th>Potongan Kurang Jam</th>
              <th>Potongan Izin</th>
              <th>Total Potongan</th>
            </tr>
            </thead>
            <tbody>
              <?php foreach($rekap as $data) : ?>
              <tr>
                <td><?php echo anchor("absen/detil/".$data->employee_code."/".$tahun."/".$bulan,$data->employee_code); ?></td>
                <td><?php echo anchor("absen/detil/".$data->employee_code."/".$tahun."/".$bulan,$data->name); ?></td>
                <td><?php echo $data->nik; ?></td>
                <td><?php echo $data->status; ?></td>
                <td><?php echo $data->year; ?></td>
                <td><?php echo $data->month; ?></td>
                <td><?php echo $data->jumlah_lupa; ?></td>
                <td><?php echo $data->jumlah_form_lupa; ?></td>                
                <td><?php echo $data->jumlah_tanpa_keterangan; ?></td>
                <td><?php echo $data->jumlah_terlambat; ?></td>
                <td><?php echo $data->jumlah_kurang_jam; ?></td>
                <td><?php echo $data->jumlah_izin_hari; ?></td>
                <td><?php echo $data->jumlah_izin; ?></td>
                <td><?php echo $data->jumlah_cuti; ?></td>
                <td><?php echo $data->jumlah_sakit; ?></td>
                <td><?php echo $data->jumlah_cuti_awal; ?></td>
                <td><?php echo $data->jumlah_cuti_terpakai; ?></td>
                <td><?php echo $data->jumlah_sisa_cuti; ?></td>
                <td><?php echo number_format($data->thp,0); ?></td>
                <td><?php echo number_format($data->pemotong_harian,2); ?></td>
                <td><?php echo number_format($data->potongan_lupa_chekin,2); ?></td>
                <td><?php echo number_format($data->potongan_tanpa_keterangan,2); ?></td>
                <td><?php echo number_format($data->potongan_terlambat,2); ?></td>
                <td><?php echo number_format($data->potongan_kurang_jam,2); ?></td>
                <td><?php echo number_format($data->potongan_izin,2); ?></td>
                <td><?php echo number_format($data->total_potongan,2); ?></td>
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
  </div>
  </section>
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>