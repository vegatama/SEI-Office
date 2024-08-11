<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Tambah Role</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item">Master Data</li>
          <li class="breadcrumb-item active">Roles</li>
        </ol>
      </div><!-- /.col -->
    </div><!-- /.row -->
  </div><!-- /.container-fluid -->
</div>
<!-- /.content-header -->

<!-- Main content -->
<section class="content">
<div class="content">
  <div class="container-fluid">
    <div class="row">
      <div class="col-12">
        <div class="card card-primary">

          <div class="card-header">
            <h3 class="card-title">Roles Data</h3>
          </div>
          <!-- /.card-header -->
          
          <?php echo form_open('role/addp'); ?>
          <div class="card-body">
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Nama :</label>
              <input type="text" name="nama" class="form-control col-5" required placeholder="Nama Role">
              <?php echo form_error('nama','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Keterangan :</label>
              <textarea name="keterangan" class="form-control col-7" placeholder="Keterangan"></textarea>
              <?php echo form_error('keterangan','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Default :</label>
              <input type="checkbox" name="default" />
              <?php echo form_error('default','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Hak Akses :</label>
              <table class="table table-bordered">
                <!-- <thead>
                    <tr>
                        <th>Menu</th>
                        <th>Permission</th>
                    </tr>
                </thead> -->
                <tr>
                    <td><strong>Master Data</strong></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td> - Hari Libur</td>
                    <td><input type="checkbox" name="masterHariLibur"  /></td>
                </tr>
                <tr>
                    <td> - Lokasi Absensi</td>
                    <td><input type="checkbox" name="masterLokasiAbsensi" /></td>
                </tr>
                <tr>
                    <td> - Roles</td>
                    <td><input type="checkbox" name="masterRoles" /></td>
                </tr>
                <tr>
                    <td> - Ruang Meeting</td>
                    <td><input type="checkbox" name="masterRuang" /></td>
                </tr>
                <tr>
                    <td> - Cuti</td>
                    <td><input type="checkbox" name="masterCuti" /></td>
                </tr>
                <tr>
                    <td> - Notifikasi</td>
                    <td><input type="checkbox" name="masterNotifikasi" /></td>
                </tr>

                <tr>
                    <td><strong>Absensi</strong></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td> - Dashboard</td>
                    <td><input type="checkbox" name="absensiDashboard" /></td>
                </tr>
                <tr>
                    <td> - Data Mentah</td>
                    <td><input type="checkbox" name="absensiDataMentah" /></td>
                </tr>
                <tr>
                    <td> - Rekap</td>
                    <td><input type="checkbox" name="absensiRekap" /></td>
                </tr>
                <tr>
                    <td> - Absen Approve</td>
                    <td><input type="checkbox" name="absensiApprove" /></td>
                </tr>
                <tr>
                    <td> - Absen Saya</td>
                    <td><input type="checkbox" name="absensiSaya" /></td>
                </tr>

                <tr>
                    <td><strong>Cuti & Izin</strong></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td> - Jatah Cuti</td>
                    <td><input type="checkbox" name="cutiJatah" /></td>
                </tr>
                <tr>
                    <td> - Cuti Saya</td>
                    <td><input type="checkbox" name="cutiSaya" /></td>
                </tr>
                <tr>
                    <td> - Approval Cuti</td>
                    <td><input type="checkbox" name="cutiApprove" /></td>
                </tr>

                <tr>
                    <td><strong>Karyawan</strong></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td> - Dashboard</td>
                    <td><input type="checkbox" name="karyawanDashboard" /></td>
                </tr>
                <tr>
                    <td> - Data Karyawan</td>
                    <td><input type="checkbox" name="karyawanData" /></td>
                </tr>

                <tr>
                    <td><strong>Project Monitoring</strong></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td> - Project</td>
                    <td><input type="checkbox" name="pmoProject" /></td>
                </tr>
                <tr>
                    <td> - Anggaran</td>
                    <td><input type="checkbox" name="pmoAnggaran" /></td>
                </tr>
                <tr>
                    <td> - DPB/J</td>
                    <td><input type="checkbox" name="pmoDpbj" /></td>
                </tr>
                <tr>
                    <td> - Purchase Order</td>
                    <td><input type="checkbox" name="pmoPo" /></td>
                </tr>

                <tr>
                    <td><strong>NAV Document</strong></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td> - Dashboard</td>
                    <td><input type="checkbox" name="navDashboard" /></td>
                </tr>
                <tr>
                    <td> - Budget Perubahan</td>
                    <td><input type="checkbox" name="navBudget" /></td>
                </tr>
                <tr>
                    <td> - Daftar Pembelian Barang</td>
                    <td><input type="checkbox" name="navDpb" /></td>
                </tr>
                <tr>
                    <td> - Daftar Pembelian Jasa</td>
                    <td><input type="checkbox" name="navDpj" /></td>
                </tr>
                <tr>
                    <td> - Penerimaan Barang</td>
                    <td><input type="checkbox" name="navBapb" /></td>
                </tr>
                <tr>
                    <td> - Perjalanan Dinas</td>
                    <td><input type="checkbox" name="navSppd" /></td>
                </tr>
                <tr>
                    <td> - Uang Muka</td>
                    <td><input type="checkbox" name="navUm" /></td>
                </tr>
                <tr>
                    <td> - Uang Muka Multiple</td>
                    <td><input type="checkbox" name="navUmm" /></td>
                </tr>
                <tr>
                    <td> - Pertanggungjawaban Uang Muka</td>
                    <td><input type="checkbox" name="navPjum" /></td>
                </tr>

                <tr>
                    <td><strong>Kendaraan Operasional</strong></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td> - Kendaraan</td>
                    <td><input type="checkbox" name="opskKendaraan" /></td>
                </tr>
                <tr>
                    <td> - Order</td>
                    <td><input type="checkbox" name="opskOrder" /></td>
                </tr>
                <tr>
                    <td> - Rekap Order</td>
                    <td><input type="checkbox" name="opskRekapOrder" /></td>
                </tr>
                <tr>
                    <td> - Need Approve Order</td>
                    <td><input type="checkbox" name="opskApproval" /></td>
                </tr>

                <tr>
                    <td><strong>Input Event / Kegiatan</strong></td>
                    <td><input type="checkbox" name="event" /></td>
                </tr>

                <tr>
                    <td><strong>Input Dokumen Perusahaan</strong></td>
                    <td><input type="checkbox" name="dokumen" /></td>
                </tr>
              </table>
            </div>
          </div>
          <div class="card-footer">
            <button type="submit" class="btn btn-primary">Simpan Data</button>
          </div>
          <?php echo form_close(); ?>

        </div>  

      </div>    
    </div>
    <!-- /.row -->
  </div><!-- /.container-fluid -->
</div>
</section>
<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>