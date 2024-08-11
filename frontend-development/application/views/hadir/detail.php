<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Detail Daftar Hadir</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('hadir'); ?>">Event / Kegiatan</a></li>
          <li class="breadcrumb-item active">Detail Daftar Hadir</li>
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
                <h3 class="card-title">Daftar Hadir Kegiatan</h3>
            </div>

			<div class="col-6 " style="display: flex; gap: 8px; justify-content: flex-end;">
                <a type="button" class="btn btn-success btn-sm float-sm-right" target="_blank" href="<?php echo site_url('hadir/download/'.$hadir->daftar_hadir_id); ?>">
                    <i class="fas fa-file-download"></i>&nbsp; Download Daftar Hadir
                </a>
				<?php if ($hadir->notulis != null && $employee_id == $hadir->notulis->id): ?>
				<a type="button" class="btn btn-warning btn-sm float-sm-right" href="<?php echo site_url('hadir/update/'.$hadir->daftar_hadir_id); ?>">
					<i class="fas fa-edit"></i>&nbsp; Edit Daftar Hadir
				</a>
				<?php endif; ?>
				<?php if ($hadir->pembuat_id == $employee_id): ?>
				<a type="button" class="btn btn-warning btn-sm float-sm-right" href="<?php echo site_url('hadir/update/'.$hadir->daftar_hadir_id); ?>">
					<i class="fas fa-edit"></i>&nbsp; Edit Daftar Hadir
				</a>
				<?php endif; ?>
				<?php if ($hadir->jumlah_peserta == 0 && $hadir->pembuat_id == $employee_id): ?>
					<button type="button" class="btn btn-danger btn-sm"
							data-toggle="modal"
							data-target="#deleteDialog<?php echo $hadir->daftar_hadir_id; ?>">
						<i class="fas fa-ban"></i>&nbsp; Delete
					</button>

					<!-- Modal Delete -->
					<div class="modal fade" style="color: initial"
						 id="deleteDialog<?php echo $hadir->daftar_hadir_id; ?>"
						 tabindex="-1" role="dialog"
						 aria-labelledby="approveModalLabel" aria-hidden="true">
						<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title" id="exampleModalLabel">
										Hapus Data</h5>
									<button type="button" class="close"
											data-dismiss="modal" aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
								</div>
								<?php echo form_open('hadir/delete'); ?>
								<?php echo form_hidden('dhid', $hadir->daftar_hadir_id); ?>
								<div class="modal-body">
									Hapus daftar hadir
									: <?php echo $hadir->kegiatan; ?>,
									Tanggal: <?php echo $hadir->tanggal; ?> ?
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-secondary"
											data-dismiss="modal">Close
									</button>
									<button type="submit" class="btn btn-danger">
										Confirm Delete
									</button>
								</div>
								<?php echo form_close(); ?>
							</div>
						</div>
					</div>
				<?php endif; ?>
            </div>
          </div>
          </div>
          <!-- /.card-header -->          
          <div class="card-body">
          <div class="form-group row">
              <label class="col-sm-3 col-form-label">Kegiatan :</label>              
              <input type="text" name="kegiatan" class="form-control col-5" value="<?php echo $hadir->kegiatan; ?>" disabled>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Subyek :</label>              
              <textarea name="subyek" class="form-control col-5" disabled><?php echo $hadir->subyek; ?></textarea>
            </div>
            <div class="form-group row ">
                <label class="col-sm-3 col-form-label">Tanggal :</label>
                <input type="text" class="form-control col-sm-3" value="<?php echo $hadir->tanggal; ?>" name="tanggal" disabled />                
            </div>
            <div class="form-group row ">
                <label class="col-sm-3 col-form-label">Waktu :</label>
                <input type="text" class="form-control col-sm-3" value="<?php echo $hadir->waktu_mulai.' - '.$hadir->waktu_selesai; ?>" name="tanggal" disabled/>                
            </div>        
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Pimpinan :</label>              
              <input type="text" name="pimpinan" class="form-control col-5" required placeholder="Pimpinan / Trainer" value="<?php echo $hadir->pimpinan; ?>" disabled />
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Tempat :</label>              
              <input type="text" name="tempat" class="form-control col-5" required placeholder="Tempat Kegiatan" value="<?php echo $hadir->tempat; ?>" disabled />
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Notulis :</label>              
              <input type="text" name="notulis" class="form-control col-5" required placeholder="Notulis" value="<?php echo $hadir->notulis == null ? "" : $hadir->notulis->name; ?>" disabled />
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Keterangan :</label>              
              <textarea name="keterangan" class="form-control col-5" disabled><?php echo $hadir->keterangan; ?></textarea>
            </div>
          </div>

        </div>
        
        <div class="card card-warning">

          <div class="card-header">
            <h3 class="card-title">Peserta Kegiatan</h3>
          </div>
          <!-- /.card-header -->          
          <div class="card-body">
            <table class="table table-stripped" id="example1">
            <thead>
                <tr>
                    <th>Nama</th>
                    <th>Divisi / Bagian</th>
                    <th>Email / Phone</th>
                </tr>
            </thead>
            <tbody>
                <?php if(count($hadir->data_peserta) > 0): ?>
                <?php foreach($hadir->data_peserta as $dt): ?>
                <tr>
                    <td><?php echo $dt->nama; ?></td>
                    <td><?php echo $dt->bagian; ?></td>
                    <td><?php echo $dt->email_phone; ?></td>
                </tr>
                <?php endforeach; ?>
                <?php endif; ?>
            </tbody>
            </table>
          </div>

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
