<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Tambah Pemesanan Kendaraan</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item">Mobil Operasional</li>
          <li class="breadcrumb-item active">Tambah Pemesanan Kendaraan</li>
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
            <h3 class="card-title">Data Pemesanan Kendaraan Operasional</h3>
          </div>
          <!-- /.card-header -->
          
          <?php echo form_open('order/addp'); ?>
          <div class="card-body">
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Nama Pengguna :</label>
              <div class="input-group col-sm-9"> 
                <select class="col-9" id="penumpang" multiple name="pengguna[]">
                  <?php foreach ($employee as $dt) : if(trim($dt->name) != ""): ?>
                  <option value="<?php echo $dt->id; ?>"><?php echo $dt->name." (".$dt->unit_kerja.")"; ?></option>
                  <?php endif; endforeach; ?>
                </select>
                <?php echo form_error('pengguna[]','<br/><div class="alert alert-warning">','</div>'); ?>
                <script>
                  new SlimSelect({
                    select: '#penumpang',
                    settings: {
                      placeholderText: 'Tambahkan Nama Pengguna disini',
                    }
                  })
                </script>
              </div>
            </div>
            <div class="form-group row ">
              <label class="col-sm-3 col-form-label">Jadwal Keberangkatan :</label>
              <div class="input-group date col-sm-3">
                <input type="text" value="<?php echo set_value('tanggal_berangkat'); ?>" name="tanggal_berangkat" class="form-control" id="datepickerpergi" required/>
                <div class="input-group-append">
                    <div class="input-group-text"><i class="fa fa-calendar"></i></div>
                </div>
              </div>
              <div class="input-group date col-sm-2" data-target="#timepicker" data-toggle="datetimepicker">
                <input type="text" id="timepicker" name="waktu_berangkat" class="form-control datetimepicker-input" required/>
                <div class="input-group-append">
                    <div class="input-group-text"><i class="far fa-clock"></i></div>
                </div>
              </div>
              <?php echo form_error('tanggal_berangkat','<br/><div class="alert alert-warning">','</div>'); ?>              
            </div>
            <div class="form-group row ">
              <label class="col-sm-3 col-form-label">Jadwal Kembali :</label>
              <div class="input-group date col-sm-3">
                <input type="text" value="<?php echo set_value('tanggal_kembali'); ?>" name="tanggal_kembali" class="form-control" id="datepickerkembali" required/>
                <div class="input-group-append">
                    <div class="input-group-text"><i class="fa fa-calendar"></i></div>
                </div>
              </div>
              <?php echo form_error('tanggal_kembali','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Tujuan :</label>
              <div class="input-group col-sm-9">               
                <input type="text" name="tujuan" class="form-control col-5" required placeholder="Tujuan">
                <?php echo form_error('tujuan','<br/><div class="alert alert-warning">','</div>'); ?>
              </div>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Keperluan :</label>     
              <div class="input-group col-sm-9">          
              <textarea name="keperluan" required class="form-control col-9" placeholder="Keperluan"></textarea>
                <?php echo form_error('keperluan','<br/><div class="alert alert-warning">','</div>'); ?>
              </div>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Kode Proyek / Rutin :</label>
              <div class="input-group col-sm-9"> 
                <select class="form-control col-9" id="proyek" name="kdproyek">
                  <?php foreach ($proyeks as $pr) : if(trim($pr->project_name) != "" && $pr->status == "Open"): ?>
                  <option value="<?php echo $pr->project_code; ?>"><?php echo $pr->project_name." (".$pr->project_code.")"; ?></option>
                  <?php endif; endforeach; ?>
                </select>
                <script>
                  new SlimSelect({
                    select: '#proyek',
                    settings: {
                      placeholderText: 'Kode Proyek / Rutin',
                    }
                  })
                </script>
              </div>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Keterangan :</label>              
              <div class="input-group col-sm-9"> 
                <textarea name="keterangan" class="form-control col-9" placeholder="Keterangan"></textarea>
                <?php echo form_error('keterangan','<br/><div class="alert alert-warning">','</div>'); ?>
              </div>
            </div>
          </div>
          <div class="card-footer">
            <button type="submit" class="btn btn-primary">Tambah Pemesanan</button>
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