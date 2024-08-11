<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
	<script src="https://unpkg.com/slim-select@latest/dist/slimselect.min.js"></script>
	<link href="https://unpkg.com/slim-select@latest/dist/slimselect.css" rel="stylesheet"></link>
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Detail Pemesanan Kendaraan</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item">Mobil Operasional</li>
          <li class="breadcrumb-item active">Detail Pemesanan Kendaraan</li>
        </ol>
      </div><!-- /.col -->
    </div><!-- /.row -->
  </div><!-- /.container-fluid -->
</div>
<!-- /.content-header -->

<?php
$canApproveOrReject = $order->need_approve_id == $employee_id && $order->status == "Pending Approval" && $mode == "na";
?>

<!-- Main content -->
<section class="content">
<div class="content">
  <div class="container-fluid">
    <div class="row">
      <div class="col-12">
        <div class="card card-primary">

          <div class="card-header">
            <div class="row">
              <div class="col-sm-9">
                <h3 class="card-title">Data Pemesanan Kendaraan Operasional</h3>
              </div>

            </div>
          </div>
          <!-- /.card-header -->
            <?php echo form_open('approve/order', array('id' => 'approveForm')); ?>
			<?php echo form_hidden('order_id',$order->order_id); ?>
			<?php if ($order->approval != null) {
				echo form_hidden('approval', $order->approval);
            } ?>
          <div class="card-body">
            <div class="form-group row ">
              <label class="col-sm-3 col-form-label">Order ID :</label>
              <input type="text" class="form-control col-3" value="<?php echo $order->order_id; ?>" disabled>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Nama Pengguna :</label>
              <textarea class="form-control col-9" disabled><?php
                $i=1;
                foreach($order->users as $user){
                  if($i==1)
                    echo $user;
                  else
                    echo ", ".$user; 
                  $i++;
                }
              ?></textarea>
            </div>
            <div class="form-group row ">
              <label class="col-sm-3 col-form-label">Waktu Keberangkatan :</label>
              <input type="text" class="form-control col-3" value="<?php echo $order->waktu_berangkat; ?>" disabled>
            </div>
            <div class="form-group row ">
              <label class="col-sm-3 col-form-label">Tanggal Kembali :</label>
              <input type="text" class="form-control col-3" value="<?php echo $order->tanggal_kembali; ?>" disabled>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Tujuan :</label>              
              <input type="text" class="form-control col-3" disabled value="<?php echo $order->tujuan; ?>">
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Keperluan :</label>              
              <textarea disabled class="form-control col-5"><?php echo $order->keperluan; ?></textarea>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Kode Proyek / Rutin :</label>
              <input type="text" disabled class="form-control col-5" value="<?php echo $order->kode_proyek; ?>">
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Keterangan :</label>              
              <textarea name="keterangan" class="form-control col-9" disabled><?php echo $order->keterangan; ?></textarea>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Status :</label>              
              <input type="text" disabled class="form-control col-3" value="<?php echo $order->status; ?>">
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Need Approve :</label>              
              <input type="text" disabled class="form-control col-4" value="<?php echo $order->need_approve; ?>">
            </div>
		    <?php if ($order->approval != null): ?>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">Nama Driver :</label>
				<input type="text" id="driver" name="driver" required placeholder="Nama Driver" class="form-control col-4" value="<?php echo set_value('driver', $order->driver); ?>" <?php if ($order->status != "Pending Approval") echo "disabled"; ?>>
				<?php echo form_error('driver','<br/><div class="alert alert-warning">','</div>'); ?>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">No HP Driver :</label>
				<input type="text" id="no_hp_driver" required placeholder="No HP Driver" name="no_hp_driver" class="form-control col-4" value="<?php echo set_value('no_hp_driver', $order->hp_driver); ?>" <?php if ($order->status != "Pending Approval") echo "disabled"; ?>>
				<?php echo form_error('no_hp_driver','<br/><div class="alert alert-warning">','</div>'); ?>
<!--				Whatsapp Button-->
				<?php if ($order->status == "APPROVED"): ?>
				<a href="https://wa.me/<?php echo $hp_driver; ?>" class="btn btn-success ml-2" target="_blank">
					<i class="fab fa-whatsapp"></i>&nbsp; Whatsapp
				</a>
				<?php endif; ?>
			</div>
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">Kendaraan :</label>
<!--				SELECT FROM LIST "$vehicles" OR INSERT CUSTOM by selecting the "Lainnya" option-->
				<div class="form-group col p-0" <?php if ($order->status == "APPROVED" && $order->mobil != null && $order->mobil->vehicle_id != null) echo "onclick=\"onKendaraanSelectClicked(this)\""?>>
					<select class="form-control col-4" id="kendaraan" name="kendaraan" required onchange="onKendaraanSelectionChanged(this)" <?php if ($order->status != "Pending Approval") echo "disabled"; ?>>
						<?php foreach ($vehicles as $vehicle) : ?>
							<option value="<?php echo $vehicle->vehicle_id; ?>" <?php if ($order->mobil != null && $order->mobil->vehicle_id == $vehicle->vehicle_id) echo "selected"; ?>>
								<?php echo $vehicle->plat_number . " (" . $vehicle->merk . ")"; ?></option>
						<?php endforeach; ?>
						<option value="-1" <?php if ($order->mobil != null && $order->mobil->vehicle_id == null) echo "selected"; ?>>
							Lainnya</option>
					</select>
				</div>
				<script>
					function onKendaraanSelectClicked(select) {
						let value = document.getElementById("kendaraan").value;
						if (value === "-1" || !value) {
							return;
						}
						let url = '<?php echo site_url('mobil/detail'); ?>/' + value;
						// left click, open in new tab
						window.open(url, '_blank');
					}
					function onKendaraanSelectionChanged(select) {
						if (select.value === "-1") {
							document.getElementById("kendaraan_lainnya").style.display = "block";
						} else {
							document.getElementById("kendaraan_lainnya").style.display = "none";
						}
						checkForHidden();
					}
					new SlimSelect({
						select: '#kendaraan',
						settings: {
							placeholderText: 'Pilih Kendaraan',
						}
					})
					$(function() {
						// check if Lainnya is selected
						if ($('#kendaraan').val() === "-1") {
							document.getElementById("kendaraan_lainnya").style.display = "block";
						} else {
							document.getElementById("kendaraan_lainnya").style.display = "none";
						}
						checkForHidden();
					})
					function checkForHidden() {
                        let kendaraanLainnya = document.getElementById("kendaraan_lainnya");
                        // find all ".optionalwhenhidden" elements
						let elements = kendaraanLainnya.getElementsByClassName("optionalwhenhidden");
                        						// check if the element is hidden
						if (kendaraanLainnya.style.display === "none") {
							for (let i = 0; i < elements.length; i++) {
								elements[i].removeAttribute("required");
							}
                        } else {
							for (let i = 0; i < elements.length; i++) {
								elements[i].setAttribute("required", "true");
							}
						}
					}
				</script>
			</div>
				<div id="kendaraan_lainnya" style="display:none;">
					<div class="form-group row">
						<label class="col-sm-3 col-form-label">No. Polisi :</label>
						<input type="text" name="other_plat_number" class="form-control col-5 optionalwhenhidden" required placeholder="Nomor Polisi" id="other_plat_number" <?php if ($canApproveOrReject) echo "disabled"; ?> value="<?php if ($order->mobil != null) echo $order->mobil->plat_number; ?>">
						<?php echo form_error('other_plat_number','<br/><div class="alert alert-warning">','</div>'); ?>
					</div>
					<div class="form-group row">
						<label class="col-sm-3 col-form-label">Merek :</label>
						<input type="text" name="other_merk" class="form-control col-5 optionalwhenhidden" required placeholder="Merek" id="other_merk" <?php if ($canApproveOrReject) echo "disabled"; ?> value="<?php if ($order->mobil != null) echo $order->mobil->merk; ?>">
						<?php echo form_error('other_merk','<br/><div class="alert alert-warning">','</div>'); ?>
					</div>
					<div class="form-group row">
						<label class="col-sm-3 col-form-label">Tipe :</label>
						<input type="text" name="other_tipe" class="form-control col-5 optionalwhenhidden" required placeholder="Tipe" id="other_tipe" <?php if ($canApproveOrReject) echo "disabled"; ?> value="<?php if ($order->mobil != null) echo $order->mobil->type; ?>">
						<?php echo form_error('other_tipe','<br/><div class="alert alert-warning">','</div>'); ?>
					</div>
					<div class="form-group row">
						<label class="col-sm-3 col-form-label">Tahun :</label>
						<select name="tahun" class="form-control col-2 optionalwhenhidden" id="other_tahun" <?php if ($canApproveOrReject) echo "disabled"; ?>>
							<?php
							$thn = date('Y');
							for($i=$thn; $i>=$thn-15; $i--):
								?>
								<option value="<?php echo $i; ?>" <?php if ($order->mobil != null && $order->mobil->year == $i) echo "selected"; ?>>
									<?php echo $i; ?></option>
							<?php endfor; ?>
						</select>
						<?php echo form_error('tahun','<br/><div class="alert alert-warning">','</div>'); ?>
					</div>
					<div class="form-group row">
						<label class="col-sm-3 col-form-label">BBM :</label>
						<select name="bbm" class="form-control col-2 optionalwhenhidden" id="other_bbm" <?php if ($canApproveOrReject) echo "disabled"; ?> required>
							<option value="BENSIN" <?php if ($order->mobil != null && $order->mobil->bbm == "BENSIN") echo "selected"; ?>>
								BENSIN</option>
							<option value="SOLAR" <?php if ($order->mobil != null && $order->mobil->bbm == "SOLAR") echo "selected"; ?>>
								SOLAR</option>
						</select>
						<?php echo form_error('bbm','<div class="alert alert-warning">','</div>'); ?>
					</div>
					<div class="form-group row">
						<label class="col-sm-3 col-form-label">Pemilik :</label>
						<select name="pemilik" class="form-control col-2 optionalwhenhidden" id="other_pemilik" <?php if ($canApproveOrReject) echo "disabled"; ?> required>
							<option value="KANTOR" <?php if ($order->mobil != null && $order->mobil->ownership == "KANTOR") echo "selected"; ?>>
								KANTOR</option>
							<option value="RENTAL" <?php if ($order->mobil != null && $order->mobil->ownership == "RENTAL") echo "selected"; ?>>
								RENTAL</option>
						</select>
						<?php echo form_error('pemilik','<br/><div class="alert alert-warning">','</div>'); ?>
					</div>
					<div class="form-group row">
						<label class="col-sm-3 col-form-label">Masa Berlaku Pajak Kendaraan s/d :</label>
						<input type="text" name="other_pkb" class="form-control col-3" placeholder="Masa Berlaku Pajak Kendaraan" id="other_pkb" <?php if ($canApproveOrReject) echo "disabled"; ?> value="<?php if ($order->mobil != null) echo $order->mobil->certifcate_expired; ?>">
						<?php echo form_error('other_pkb','<br/><div class="alert alert-warning">','</div>'); ?>
					</div>
					<div class="form-group row">
						<label class="col-sm-3 col-form-label">Keterangan / Nama Rental :</label>
						<input type="text" name="other_keterangan" class="form-control col-7 optionalwhenhidden" required placeholder="Keterangan / Nama Rental" id="other_keterangan" <?php if ($canApproveOrReject) echo "disabled"; ?> value="<?php if ($order->mobil != null) echo $order->mobil->keterangan; ?>">
						<?php echo form_error('other_keterangan','<br/><div class="alert alert-warning">','</div>'); ?>
					</div>
				</div>
			<?php endif ?>
          </div>
			<div class="card-footer">
				<?php if($canApproveOrReject): ?>
					<div class="row">
                        <button type="button" class="btn btn-danger mr-1" data-toggle="modal" data-target="#rejectDialog">
                            <i class="fas fa-times"></i>&nbsp; Reject This Order
                        </button>
                        <button type="button" class="btn btn-success" data-toggle="modal" data-target="#approveDialog">
                            <i class="fas fa-check"></i>&nbsp; Approve This Order
                        </button>
                    </div>
				<?php endif; ?>
			</div>
<!--            show modal-->
			<div class="modal fade" id="approveDialog" tabindex="-1" role="dialog" aria-labelledby="approveModalLabel" aria-hidden="true">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title" id="approveModalLabel">Approve Order</h5>
							<button type="button" class="close" data-dismiss="modal" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body">
							Approve Pemesanan Kendaraan dengan no dokumen: <?php echo
							$order->order_id ?> ?
						</div>
						<div class="modal-footer">
							<script>
								function submitAndClose() {
                                    $('#approveDialog').modal('hide');
									setTimeout(function() {
                                        document.getElementById("approveForm").requestSubmit();
									}, 500);
								}
							</script>
							<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
							<button type="button" onclick="submitAndClose()" class="btn btn-success">Approve Order</button>
						</div>
					</div>
				</div>
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

<!-- Modal Approve -->
<!--<div class="modal fade" id="approveDialog" tabindex="-1" role="dialog" aria-labelledby="approveModalLabel" aria-hidden="true">-->
<!--  <div class="modal-dialog" role="document">-->
<!--    <div class="modal-content">-->
<!--      <div class="modal-header">-->
<!--        <h5 class="modal-title" id="exampleModalLabel">Approve Order</h5>-->
<!--        <button type="button" class="close" data-dismiss="modal" aria-label="Close">-->
<!--          <span aria-hidden="true">&times;</span>-->
<!--        </button>-->
<!--      </div>-->
<!--      --><?php //echo form_open('approve/order'); ?>
<!--      --><?php //echo form_hidden('order_id',$order->order_id); ?>
<!--		--><?php //if ($order->approval != null && $order->status == "Pending Approval"): ?>
<!--		<input type="hidden" name="kendaraan" value="" id="kendaraan_hidden">-->
<!--		<input type="hidden" name="driver" value="" id="driver_hidden">-->
<!--		<input type="hidden" name="no_hp_driver" value="" id="no_hp_driver_hidden">-->
<!--		<input type="hidden" name="other_plat_number" value="" id="other_plat_number_hidden">-->
<!--		<input type="hidden" name="other_merk" value="" id="other_merk_hidden">-->
<!--		<input type="hidden" name="other_tipe" value="" id="other_tipe_hidden">-->
<!--		<input type="hidden" name="other_tahun" value="" id="other_tahun_hidden">-->
<!--		<input type="hidden" name="other_bbm" value="" id="other_bbm_hidden">-->
<!--		<input type="hidden" name="other_pemilik" value="" id="other_pemilik_hidden">-->
<!--		<input type="hidden" name="other_pkb" value="" id="other_pkb_hidden">-->
<!--		<input type="hidden" name="other_keterangan" value="" id="other_keterangan_hidden">-->
<!--		<input type="hidden" name="approval" value="--><?php //echo $order->approval != null; ?><!--">-->
<!--		--><?php //endif ?>
<!--      <div class="modal-body">-->
<!--        Approve Pemesanan Kendaraan dengan no dokumen: --><?php //echo $order->order_id ?><!-- ?-->
<!--      </div>-->
<!--      <div class="modal-footer">-->
<!--        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>-->
<!--        <button type="submit" class="btn btn-success">Approve Order</button>-->
<!--      </div>-->
<!--      --><?php //echo form_close(); ?>
<!--    </div>-->
<!--  </div>-->
<!--</div>-->

<!-- Modal Reject -->
<div class="modal fade" id="rejectDialog" tabindex="-1" role="dialog" aria-labelledby="rejectModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Alasan Reject</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <?php echo form_open('reject/order'); ?>
      <?php echo form_hidden('orderid',$order->order_id); ?>
      <div class="modal-body">
        <textarea class="form-control" name="alasan" rows="3" required></textarea>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        <button type="submit" class="btn btn-danger">Reject Order</button>
      </div>
      <?php echo form_close(); ?>
    </div>
  </div>
</div>

<?php $this->load->view('footer'); ?>
