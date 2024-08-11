<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Tambah Kegiatan</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('hadir'); ?>">Event / Kegiatan</a></li>
          <li class="breadcrumb-item active">Tambah Kegiatan</li>
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
		  <?php
			  $hadir = array(
				  'tanggal' => date('d/m/Y'),
				  'waktu_mulai' => date('H:i'),
				  'waktu_selesai' => date('H:i'),
				  'kegiatan' => '',
				  'subyek' => '',
				  'pimpinan' => '',
				  'notulis' => '',
				  'keterangan' => ''
			  );
			  $ruang_meeting_id = -1;
			  $tempat_meeting = "";
			  // convert to object
			  $hadir = (object) $hadir;
			  if (isset($hadir_repopulate)) {
				  $ruang_meeting_id = -1;
				  $tempat_meeting = "";
				  // check for flashdata to see if it needs to repopulate the form
				  $hadir->tanggal = $hadir_repopulate['tanggal'] ?? $hadir->tanggal;
				  $hadir->waktu_mulai = $hadir_repopulate['waktu_mulai'] ?? $hadir->waktu_mulai;
				  $hadir->waktu_selesai = $hadir_repopulate['waktu_selesai'] ?? $hadir->waktu_selesai;
				  $hadir->kegiatan = $hadir_repopulate['kegiatan'] ?? $hadir->kegiatan;
				  $hadir->subyek = $hadir_repopulate['subyek'] ?? $hadir->subyek;
				  $hadir->pimpinan = $hadir_repopulate['pimpinan'] ?? $hadir->pimpinan;
				  $hadir->notulis = $hadir_repopulate['notulis'] ?? $hadir->notulis;
				  $hadir->keterangan = $hadir_repopulate['keterangan'] ?? $hadir->keterangan;
				  $ruang_meeting_id = $hadir_repopulate['ruang_meeting'] ?? $ruang_meeting_id;
				  $tempat_meeting = $hadir_repopulate['tempat'] ?? $tempat_meeting;
			  }
		  $mulai = $hadir->waktu_mulai;
		  $selesai = $hadir->waktu_selesai;
		  $arrmulai = explode(":", $mulai);
		  $arrselesai = explode(":",$selesai);
			  ?>
        <?php echo form_open('hadir/addp'); ?>
		  <script>
			  // echo all the flashdata
			  console.log(<?php echo json_encode($this->session->flashdata()); ?>)
		  </script>
        <div class="card card-primary">

          <div class="card-header">
            <h3 class="card-title">Detail Kegiatan</h3>
          </div>

			<script>
				// deteksi query url
				$(function() {
					var url = new URL(window.location.href);
					var searchParams = new URLSearchParams(url.search);
					//http://localhost:8081/hadir/add/?tanggal=2024-05-18&waktu_mulai=7:00&waktu_selesai=8:00&ruang_meeting_id=3
					if (searchParams.has('tanggal')) {
						$('#datepicker').val(searchParams.get('tanggal'));
					}
					if (searchParams.has('waktu_mulai')) {
						$('#timepicker').val(searchParams.get('waktu_mulai'));
					}
					if (searchParams.has('waktu_selesai')) {
						$('#timepicker2').val(searchParams.get('waktu_selesai'));
					}
					if (searchParams.has('ruang_meeting_id')) {
						let ruangMeetingId = searchParams.get('ruang_meeting_id');
						let ruangMeetingSelect = document.getElementById('ruang_meeting');
						// select the option
						for (let i = 0; i < ruangMeetingSelect.options.length; i++) {
							let option = ruangMeetingSelect.options[i];
							// option.setAttribute('selected', option.value === ruangMeetingId);
							if (option.value === ruangMeetingId) {
								option.setAttribute('selected', 'selected');
							}
						}
					}
				})
			</script>
          <!-- /.card-header -->

			<div class="card-body">
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">Tanggal :</label>
					<div class="input-group date col-sm-3">
						<input type="text" value="<?php echo $hadir->tanggal; ?>" name="tanggal" class="form-control" id="datepicker" required/>
						<div class="input-group-append">
							<div class="input-group-text"><i class="fa fa-calendar"></i></div>
						</div>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">Waktu Mulai Kegiatan :</label>
					<div class="input-group date col-sm-2" data-target="#timepicker" data-toggle="datetimepicker">
						<input type="text" id="timepicker" value="<?php echo $hadir->waktu_mulai; ?>" name="waktu_mulai" class="form-control datetimepicker-input" required/>
						<div class="input-group-append">
							<div class="input-group-text"><i class="far fa-clock"></i></div>
						</div>
					</div>
					<!-- /.input group -->
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">Waktu Akhir Kegiatan :</label>
					<div class="input-group date col-sm-2" data-target="#timepicker2" data-toggle="datetimepicker">
						<input type="text" id="timepicker2" value="<?php echo $hadir->waktu_selesai; ?>" name="waktu_selesai" class="form-control datetimepicker-input" required/>
						<div class="input-group-append">
							<div class="input-group-text"><i class="far fa-clock"></i></div>
						</div>
					</div>
					<!-- /.input group -->
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">Kegiatan :</label>
					<div class="input-group col-sm-9">
						<input type="text" name="kegiatan" class="form-control col-9" required placeholder="Nama Kegiatan" value="<?php echo $hadir->kegiatan; ?>">
						<?php echo form_error('kegiatan','<br/><div class="alert alert-warning">','</div>'); ?>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">Subyek :</label>
					<div class="input-group col-sm-9">
						<textarea name="subyek" class="form-control col-9" required placeholder="Subyek"><?php echo $hadir->subyek; ?></textarea>
						<?php echo form_error('subyek','<br/><div class="alert alert-warning">','</div>'); ?>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">Pimpinan :</label>
					<div class="input-group col-sm-9">
						<input type="text" name="pimpinan" class="form-control col-9" required placeholder="Pimpinan / Trainer" value="<?php echo $hadir->pimpinan; ?>">
						<?php echo form_error('pimpinan','<br/><div class="alert alert-warning">','</div>'); ?>
					</div>
				</div>
				<div class="form-group row">
				  <label class="col-sm-3 col-form-label">Tempat :</label>
				  <div class="input-group col-sm-9 flex-column">
					  <!--                <input type="text" name="tempat" class="form-control col-9" required placeholder="Tempat Kegiatan" value="--><?php //echo set_value('tempat'); ?><!--">-->
					  <select class="col-7" id="ruang_meeting" name="ruang_meeting" required onchange="onRuangMeetingSelectionChanged(this)">
						  	<?php foreach ($ruang_meeting as $dt) : ?>
							  	<option value="<?php echo $dt->id; ?>" <?php if($ruang_meeting_id == $dt->id) echo "selected"; ?>>
                				<?php echo $dt->description != "" ? $dt->name ." (kapasitas: ".$dt->capacity." orang - " . $dt->description . ")" : $dt->name . " (kapasitas: ". $dt->capacity ."orang)";  ?> </option>
						  	<?php endforeach; echo "ruangmid : ".$ruang_meeting_id;?>
						  <option value="-1" <?php if ($ruang_meeting_id == "") echo "selected"; ?>>
							  Lainnya</option>
					  </select>
					  <?php echo form_error('ruang_meeting','<br/><div class="alert alert-warning">','</div>'); ?>
          </div>
          <label class="col-sm-3 col-form-label">&nbsp;</label>
          <div class="input-group col-sm-9 flex-column">
					  <input type="text" style="display: <?php echo $ruang_meeting_id == "" ? null : 'none'; ?>;width: 100%" id="tempat_kegiatan" name="tempat" class="form-control col-7 mt-auto" placeholder="Tempat Kegiatan" value="<?php echo $tempat_meeting; ?>">
					  <?php echo form_error('tempat','<br/><div class="alert alert-warning">','</div>'); ?>
					  <script>
						  new SlimSelect({
							  select: '#ruang_meeting',
							  settings: {
								  placeholderText: 'Ruang Meeting',
							  }
						  })

						  function onRuangMeetingSelectionChanged(e) {
							  var selectedValue = e.value;
							  let tempatKegiatanElement = document.getElementById('tempat_kegiatan');
							  if (selectedValue === "-1") {
								  tempatKegiatanElement.style.display = null;
								  tempatKegiatanElement.setAttribute('required', 'true')
							  } else {
								  tempatKegiatanElement.style.display = 'none';
								  tempatKegiatanElement.removeAttribute('required');
							  }
						  }
					  </script>
				  </div>
			  </div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">Notulis :</label>
					<div class="input-group col-sm-9">
						<select class="col-5" id="notulis" name="notulis">
							<option value="">&nbsp;</option>
							<?php foreach ($employee as $dt) : if(trim($dt->name) != ""): ?>
								<option value="<?php echo $dt->id; ?>" <?php if(isset($hadir->notulis)) {if($hadir->notulis == $dt->id) echo "selected";} ?>><?php echo $dt->name." (".$dt->bagian_fungsi.")"; ?></option>
							<?php endif; endforeach; ?>
						</select>
						<?php echo form_error('notulis','<br/><div class="alert alert-warning">','</div>'); ?>
						<script>
							new SlimSelect({
								select: '#notulis',
								settings: {
									placeholderText: 'Notulis',
								}
							})
						</script>
					</div>
				</div>
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">Keterangan :</label>
					<div class="input-group col-sm-9">
						<textarea name="keterangan" class="form-control col-9" required placeholder="keterangan"><?php echo $hadir->keterangan; ?></textarea>
						<?php echo form_error('keterangan','<br/><div class="alert alert-warning">','</div>'); ?>
					</div>
				</div>
			</div>

        </div>  

        <div class="card card-success">

          <div class="card-header">
            <h3 class="card-title">Undangan Kegiatan (Opsional)</h3>
          </div>
          <!-- /.card-header -->
          <div class="card-body">
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Undangan Internal :</label>
              <select class="col-9" id="undangan_internal" multiple name="undangan_internal[]">
                <?php foreach ($employee as $dt) : if(trim($dt->name) != ""): ?>
                <option value="<?php echo $dt->email; ?>"><?php echo $dt->name." (".$dt->bagian_fungsi.")"; ?></option>
                <?php endif; endforeach; ?>
              </select>
              <?php echo form_error('undangan_internal[]','<br/><div class="alert alert-warning">','</div>'); ?>
              <script>
                new SlimSelect({
                  select: '#undangan_internal',
                  settings: {
                    placeholderText: 'Tambahkan Nama Undangan disini',
                  }
                })
              </script>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Undangan External :<br/><small><em>isi dengan email, pisahkan dengan koma</em></small></label>              
              <textarea name="undangan_external" class="form-control col-9" placeholder="Tambahkan Email Undangan external disini (pisahkan dengan koma)"><?php echo set_value('undangan_external'); ?></textarea>
              <?php echo form_error('undangan_external','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Isi Undangan :</label>              
              <textarea rows=10 name="isi_undangan" id="summernote" class="form-control col-9" placeholder="Isi Undangan"><?php echo set_value('isi_undangan'); ?></textarea>
              <?php echo form_error('isi_undangan','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
          </div>
          <div class="card-footer">
            <button type="submit" class="btn btn-primary">Tambah Kegiatan</button>
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
