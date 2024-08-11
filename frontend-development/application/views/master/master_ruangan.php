<?php $this->load->view('header'); ?>

<script src="https://cdn.jsdelivr.net/gh/davidshimjs/qrcodejs/qrcode.min.js"></script>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Master Data</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Master Data Ruang Meeting</li>
        </ol>
      </div><!-- /.col -->
    </div><!-- /.row -->
  </div><!-- /.container-fluid -->
</div>
<!-- /.content-header -->

	<script>
		function openQrModal(id) {
			// clear previous QR Code
			document.querySelector("#qrcode").innerHTML = "";
			// create QR Code
			var qrcode = new QRCode(document.getElementById("qrcode"), {
				width : 200,
				height : 200
			});
			qrcode.makeCode("<?php echo site_url('view/detailruang/'); ?>"+id);
			// set download link
			// document.querySelector("#qrModal #download").href = document.querySelector("#qrcode img").src;
			// wait for QR Code to be generated
			function saveImage() {
				let img = document.querySelector("#qrcode img");
				let button = document.querySelector("#qrModal #download");
				button.href = img.src;
			}
			// repeat until QR Code is generated
			var interval = setInterval(function() {
				if(document.querySelector("#qrcode img") != null) {
					clearInterval(interval);
					saveImage();
				}
			}, 100);
			$('#qrModal').modal('show');
		}
	</script>

<!-- Main content -->
<section class="content">
<div class="container-fluid">
  <div class="row">
    <div class="col-12">

      <div class="card">
        <div class="card-header">
          <h3 class="card-title">Ruang Meeting</h3>
          <a class="btn btn-primary btn-sm float-sm-right" href="<?php echo site_url('ruangan/tambah'); ?>">
              <i class="far fa-calendar-alt"></i> Tambah Ruang Meeting
          </a>
        </div>
        <!-- /.card-header -->
        <div class="card-body">
          <table id="karyawan" class="table table-bordered table-striped">
            <thead>
            <tr>
              <th>Nama Ruang</th>
              <th>Kapasitas</th>
              <th>Keterangan</th>
              <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <?php 
                if(isset($ruangan)):
                foreach($ruangan as $data) :                  
              ?>
              <tr>
                <td><?php echo $data->name; ?></td>
                <td><?php echo $data->capacity; ?></td>
                <td><?php echo $data->description; ?></td>
                <td>
<!--					button untuk melihat acara secara publik di /view/detailruang/{id}-->
<!--					<a class="btn btn-info py-1 px-3 mr-1" href="--><?php //echo site_url('view/detailruang/'.$data->id); ?><!--">View</a>-->
<!--						<a class="btn btn-primary py-1 px-3 mr-1" href="--><?php //echo site_url('ruangan/edit/'.$data->id); ?><!--">Edit</a>-->
<!--						<a class="btn btn-danger py-1 px-2" href="--><?php //echo site_url('ruangan/delete/'.$data->id); ?><!--"><i class="fa fa-solid fa-trash" style="color:white"></i></a>-->
<!--					group it in a row with a gap-->
					<div class="row">
							<a class="btn btn-info py-1 px-3 mr-1" href="<?php echo site_url('view/detailruang/'.$data->id); ?>">View</a>
						<a class="btn btn-primary py-1 px-3 mr-1" href="<?php echo site_url('ruangan/edit/'.$data->id); ?>">Edit</a>
						<a class="btn btn-danger py-1 px-2 mr-1" href="<?php echo site_url('ruangan/delete/'.$data->id); ?>"><i class="fa fa-solid fa-trash" style="color:white"></i></a>
						<a class="btn btn-success py-1 px-2" href="javascript:void(0)" onclick="openQrModal(<?php echo $data->id; ?>)"><i class="fa fa-qrcode" style="color:white"></i></a>
					</div>
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
<!--	modal dialog for showing and downloading QR Code of a ruang meeting link-->
	<div class="modal fade" id="qrModal" tabindex="-1" aria-labelledby="qrModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="qrModalLabel">QR Code Ruang Meeting</h5>
					<button type="button" class="close"
							data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="col-12">
							<div id="qrcode"></div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
							data-dismiss="modal">Close</button>
					<a id="download" class="btn btn-primary" href="" download="qrcode.png">Download</a>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>
