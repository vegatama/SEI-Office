<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Slip Gaji</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Slip Gaji</li>
          <li class="breadcrumb-item active">List</li>
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
			<?php
			$namaBulan = array(
				'1' => 'Januari',
				'2' => 'Februari',
				'3' => 'Maret',
				'4' => 'April',
				'5' => 'Mei',
				'6' => 'Juni',
				'7' => 'Juli',
				'8' => 'Agustus',
				'9' => 'September',
				'10' => 'Oktober',
				'11' => 'November',
				'12' => 'Desember'
			);
			?>
      <div class="card">
        <div class="card-header">
          <h3 class="card-title">Data Slip Gaji</h3>
          <a class="btn btn-success btn-sm float-sm-right" href="<?php echo site_url('slipgaji/updateTemplate'); ?>">
              <i class="fas fa-plus"></i>&nbsp; Tambah Slip Gaji
            </a>
        </div>
        <!-- /.card-header -->
        <div class="card-body">
          <table id="slipgajitable" class="table table-bordered table-striped">
            <thead>
            <tr>
							<th>Nama Template</th>
              <th>Tahun</th>
              <th>Bulan</th>
							<th>Revisi ke-</th>
							<th>Status</th>
              <th class="no-sort"></th>
            </tr>
            </thead>
            <tbody>
              <?php 
                if(isset($slipgaji)):
                foreach($slipgaji as $data) :              
              ?>
              <tr>
								<td><?php echo isset($data->name) ? $data->name : "(Tidak ada nama)"; ?></td>
                <td><?php echo $data->tahun; ?></td>
                <td>
									<?php echo $data->bulan; ?>
								</td>
								<td><?php echo $data->revision; ?></td>
								<td>
<!--									Status are FILLED, INCOMPLETE, FILLED_SOME, and EMPTY-->
<!--									Convert into badge status-->
									<h5>
										<?php if($data->status == "FILLED"){ ?>
											<span class="badge badge-success badge-lg">Siap Dikirim</span>
										<?php }elseif($data->status == "INCOMPLETE"){ ?>
											<span class="badge badge-warning">Data belum lengkap</span>
										<?php }elseif($data->status == "FILLED_SOME"){ ?>
											<span class="badge badge-warning">Beberapa data sudah lengkap</span>
										<?php }else{ ?>
											<span class="badge badge-danger">Kosong</span>
										<?php } ?>
									</h5>
								</td>
                <td>
                  <a class="btn btn-success btn-sm" href="<?php echo site_url('slipgaji/downloadTemplate/'.$data->id); ?>">
                    <i class="fas fa-download"></i> Download Template
                  </a>
                  <a class="btn btn-primary btn-sm" href="<?php echo site_url('slipgaji/updateTemplate/'.$data->id); ?>">
                    <i class="fas fa-pen"></i> Update Template
                  </a>
                  <a class="btn btn-info btn-sm" href="<?php echo site_url('slipgaji/upload/'.$data->id); ?>">
                    <i class="fas fa-upload"></i> Upload Data
                  </a>
                  <a class="btn btn-primary btn-sm" href="<?php echo site_url('slipgaji/detail/'.$data->id); ?>">
                    <i class="fas fa-info"></i> Detail
                  </a>
                  <?php  if($data->canSend){ ?>
                  <button type="button" class="btn btn-warning btn-sm" data-toggle="modal" data-target="#kirimDialog<?php echo $data->id; ?>">
                    <i class="fas fa-paper-plane"></i>&nbsp; Kirim Slip
                  </button>
										<!-- Modal kirim slip -->
										<div class="modal fade" id="kirimDialog<?php echo $data->id; ?>"
												 tabindex="-1" role="dialog" aria-labelledby="approveModalLabel" aria-hidden="true">
											<div class="modal-dialog" role="document">
												<div class="modal-content">
													<div class="modal-header">
														<h5 class="modal-title" id="exampleModalLabel">Kirim Email</h5>
														<button type="button" class="close" data-dismiss="modal" aria-label="Close">
															<span aria-hidden="true">&times;</span>
														</button>
													</div>
													<?php echo form_open('slipgaji/kirim/'.$data->id); ?>
													<div class="modal-body">
														Kirim Slip Gaji : <?php echo $data->name . " (" . $namaBulan[$data->bulan] . " " . $data->tahun . ")"; ?> ke email karyawan?
													</div>
													<div class="modal-footer">
														<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
														<button type="submit" class="btn btn-success">Confirm Kirim</button>
													</div>
													<?php echo form_close(); ?>
												</div>
											</div>
										</div>
                  <?php }else{ ?>
                    <a class="btn btn-secondary btn-sm"  disabled>
                    <i class="fas fa-paper-plane"></i> Kirim Slip
                  </a>
                  <?php } ?>
<!--									Duplicate button-->
									<a class="btn btn-primary btn-sm" href="<?php echo site_url('slipgaji/updateTemplate/'.$data->id.'/duplicate'); ?>">
										<i class="fas fa-copy"></i> Duplikat Template
									</a>
                </td>
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
<script>
	$('#slipgajitable').DataTable({
		"paging": true,
		"lengthChange": false,
		"searching": true,
		"ordering": true,
		"info": true,
		"autoWidth": false,
		"responsive": true,
		"lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"order": [[ 0, "desc" ], [ 1, "desc" ]],
		"columnDefs": [
			// disable sorting on the last column
			{ "orderable": false, "targets": "no-sort" },
		]
	});
</script>
