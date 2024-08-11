<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Risalah Kegiatan</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('hadir'); ?>">Event / Kegiatan</a></li>
          <li class="breadcrumb-item active">Risalah</li>
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
          <div class="row">
            <div class="col-6">
                <h3 class="card-title">Risalah Kegiatan</h3>
            </div>
            <div class="col-6">
                <a type="button" class="btn btn-success btn-sm float-sm-right" target="_blank" href="<?php echo site_url('hadir/downloadr/'.$hadir->daftar_hadir_id); ?>">
                    <i class="fas fa-file-download"></i>&nbsp; Download Risalah
                </a>
            </div>
          </div>
          </div>
          <!-- /.card-header -->          
          <div class="card-body">
          <div class="form-group row">
              <label class="col-sm-3 col-form-label">Kegiatan :</label>              
              <input type="text" name="kegiatan" class="form-control col-5" value="<?php echo $hadir->kegiatan; ?>" disabled>
              <?php echo form_error('kegiatan','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Subyek :</label>              
              <textarea name="subyek" class="form-control col-5" disabled><?php echo $hadir->subyek; ?></textarea>
              <?php echo form_error('subyek','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
            <div class="form-group row ">
                <label class="col-sm-3 col-form-label">Waktu :</label>
                <input type="text" class="form-control col-sm-3" value="<?php echo $hadir->tanggal.', '.$hadir->waktu_mulai.' - '.$hadir->waktu_selesai; ?>" name="tanggal" disabled/>                
            </div>        
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Pimpinan :</label>              
              <input type="text" name="pimpinan" class="form-control col-5" required placeholder="Pimpinan / Trainer" value="<?php echo $hadir->pimpinan; ?>" disabled />
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Tempat :</label>              
              <input type="text" name="tempat" class="form-control col-5" required placeholder="Tempat Kegiatan" value="<?php echo $hadir->tempat; ?>" disabled />
            </div>
          </div>

        </div>
        <?php 
            echo form_open('hadir/risalahp'); 
            echo form_hidden('dhid',$hadir->daftar_hadir_id);
        ?>
        <div class="card card-warning">

          <div class="card-header">
            <h3 class="card-title">Risalah Kegiatan</h3>
          </div>
          <!-- /.card-header -->          
          <div class="card-body">            
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Risalah Rapat :</label>              
              <textarea name="risalah" id="summernoterisalah" class="form-control col-5" placeholder="Risalah Kegiatan"><?php echo $hadir->risalah; ?></textarea>
              <?php echo form_error('risalah','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
          </div>

          <div class="card-footer">
            <button type="submit" class="btn btn-primary">Simpan Risalah</button>
          </div> 

        </div>
        <?php echo form_close(); ?>
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