<?php $this->load->view('header'); ?>
<div class="content-wrapper">
    <script src="https://unpkg.com/slim-select@latest/dist/slimselect.min.js"></script>
    <link href="https://unpkg.com/slim-select@latest/dist/slimselect.css" rel="stylesheet"></link>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.js"></script>

    <div class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1 class="m-0">Update Data Pemesanan Kendaraan</h1>
                </div>
                <div class="col-sm-6">
                    <ol class="breadcrumb float-sm-right">
                        <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
                        <li class="breadcrumb-item"><a href="<?php echo site_url('order/rekap'); ?>">Rekap Order</a></li>
                        <li class="breadcrumb-item active">Detail Rekap Order</li>
                    </ol>
                </div>
            </div>
        </div>
    </div>

    <section class="content">
        <div class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-12">
                        <div class="card card-primary">
                            <div class="card-header">
                                <div class="row">
                                    <div class="col-sm-9">
                                        <h3 class="card-title">Ubah Data Driver</h3>
                                    </div>
                                </div>
                            </div>
                            <!-- /.card-header -->
                            <?php echo form_open('order/updatedriver', array('id' => 'approveForm')); ?>
                            <?php echo form_hidden('order_id', $order->order_id); ?>
                            <div class="card-body">
                                <div class="form-group row">
                                    <label class="col-sm-3 col-form-label">Nama Driver :</label>
                                    <input type="text" id="driver" name="driver" required placeholder="Nama Driver"
                                        class="form-control col-4" value="<?php echo set_value('driver', $order->driver); ?>">
                                    <?php echo form_error('driver', '<br/><div class="alert alert-warning">', '</div>'); ?>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-3 col-form-label">No HP Driver :</label>
                                    <input type="text" id="no_hp_driver" required placeholder="No HP Driver"
                                        name="no_hp_driver" class="form-control col-4"
                                        value="<?php echo set_value('no_hp_driver', $order->hp_driver); ?>">
                                    <?php echo form_error('no_hp_driver', '<br/><div class="alert alert-warning">', '</div>'); ?>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-3 col-form-label">Kendaraan :</label>
                                    <div class="form-group col p-0">
                                        <select class="form-control col-4" id="kendaraan" name="kendaraan" required
                                            onchange="onKendaraanSelectionChanged(this)">
                                            <?php foreach ($vehicles as $vehicle): ?>
                                                <option value="<?php echo $vehicle->vehicle_id; ?>" <?php if ($order->mobil != null && $order->mobil->vehicle_id == $vehicle->vehicle_id) echo "selected"; ?>>
                                                    <?php echo $vehicle->plat_number . " (" . $vehicle->merk . ")"; ?>
                                                </option>
                                            <?php endforeach; ?>
                                            <option value="-1" <?php if ($order->mobil != null && $order->mobil->vehicle_id == null) echo "selected"; ?>>
                                                Lainnya
                                            </option>
                                        </select>
                                    </div>
                                    <script>
                                        function onKendaraanSelectionChanged(select) {
                                            if (select.value === "-1") {
                                                document.getElementById("kendaraan_lainnya").style.display = "block";
                                            } else {
                                                document.getElementById("kendaraan_lainnya").style.display = "none";
                                            }
                                            checkForHidden();
                                        }

                                        function checkForHidden() {
                                            let kendaraanLainnya = document.getElementById("kendaraan_lainnya");
                                            let elements = kendaraanLainnya.getElementsByClassName("optionalwhenhidden");
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

                                        new SlimSelect({
                                            select: '#kendaraan',
                                            settings: {
                                                placeholderText: 'Pilih Kendaraan',
                                            }
                                        });

                                        $(function () {
                                            if ($('#kendaraan').val() === "-1") {
                                                document.getElementById("kendaraan_lainnya").style.display = "block";
                                            } else {
                                                document.getElementById("kendaraan_lainnya").style.display = "none";
                                            }
                                            checkForHidden();
                                        });
                                    </script>
                                </div>
                                <div id="kendaraan_lainnya" style="display:none;">
                                    <div class="form-group row">
                                        <label class="col-sm-3 col-form-label">No. Polisi :</label>
                                        <input type="text" name="nopol" class="form-control col-5 optionalwhenhidden"
                                            required placeholder="Nomor Polisi" id="other_plat_number"
                                            value="<?php if ($order->mobil != null) echo $order->mobil->plat_number; ?>">
                                        <?php echo form_error('nopol', '<br/><div class="alert alert-warning">', '</div>'); ?>
                                    </div>
                                    <div class="form-group row">
                                        <label class="col-sm-3 col-form-label">Merek :</label>
                                        <input type="text" name="merk" class="form-control col-5 optionalwhenhidden"
                                            required placeholder="Merek" id="other_merk"
                                            value="<?php if ($order->mobil != null) echo $order->mobil->merk; ?>">
                                        <?php echo form_error('merk', '<br/><div class="alert alert-warning">', '</div>'); ?>
                                    </div>
                                    <div class="form-group row">
                                        <label class="col-sm-3 col-form-label">Tipe :</label>
                                        <input type="text" name="tipe" class="form-control col-5 optionalwhenhidden"
                                            required placeholder="Tipe" id="other_tipe"
                                            value="<?php if ($order->mobil != null) echo $order->mobil->type; ?>">
                                        <?php echo form_error('tipe', '<br/><div class="alert alert-warning">', '</div>'); ?>
                                    </div>
                                    <div class="form-group row">
                                        <label class="col-sm-3 col-form-label">Tahun :</label>
                                        <select name="tahun" class="form-control col-2 optionalwhenhidden" id="other_tahun">
                                            <?php
                                            $thn = date('Y');
                                            for ($i = $thn; $i >= $thn - 15; $i--): ?>
                                                <option value="<?php echo $i; ?>" <?php if ($order->mobil != null && $order->mobil->year == $i) echo "selected"; ?>>
                                                    <?php echo $i; ?>
                                                </option>
                                            <?php endfor; ?>
                                        </select>
                                        <?php echo form_error('tahun', '<br/><div class="alert alert-warning">', '</div>'); ?>
                                    </div>
                                    <div class="form-group row">
                                        <label class="col-sm-3 col-form-label">BBM :</label>
                                        <select name="bbm" class="form-control col-2 optionalwhenhidden" id="other_bbm" required>
                                            <option value="BENSIN" <?php if ($order->mobil != null && $order->mobil->bbm == "BENSIN") echo "selected"; ?>>
                                                BENSIN</option>
                                            <option value="SOLAR" <?php if ($order->mobil != null && $order->mobil->bbm == "SOLAR") echo "selected"; ?>>
                                                SOLAR</option>
                                        </select>
                                        <?php echo form_error('bbm', '<div class="alert alert-warning">', '</div>'); ?>
                                    </div>
                                    <div class="form-group row">
                                        <label class="col-sm-3 col-form-label">Pemilik :</label>
                                        <select name="pemilik" class="form-control col-2 optionalwhenhidden" id="other_pemilik" required>
                                            <option value="KANTOR" <?php if ($order->mobil != null && $order->mobil->ownership == "KANTOR") echo "selected"; ?>>
                                                KANTOR</option>
                                            <option value="RENTAL" <?php if ($order->mobil != null && $order->mobil->ownership == "RENTAL") echo "selected"; ?>>
                                                RENTAL</option>
                                        </select>
                                        <?php echo form_error('pemilik', '<br/><div class="alert alert-warning">', '</div>'); ?>
                                    </div>
                                    <div class="form-group row">
                        <label class="col-sm-3 col-form-label">Masa Berlaku Pajak Kendaraan s/d :</label>
                        <div class="input-group date col-sm-2" data-target="#datepicker" data-toggle="datetimepicker">
                            <input type="text" id="datepicker" name="pkb" class="form-control datetimepicker-input" placeholder="Masa Berlaku Pajak Kendaraan" value="<?php if ($order->mobil != null) echo $order->mobil->certifcate_expired; ?>">
                            <div class="input-group-append">
                                <div class="input-group-text"><i class="far fa-calendar-alt"></i></div>
                            </div>
                            <?php echo form_error('pkb','<br/><div class="alert alert-warning">','</div>'); ?>
                            <script>
                                $(function () {
                                    $('#datepicker').datetimepicker({
                                        format: 'DD/MM/YYYY',
                                    });
                                });
                            </script>
                        </div>
                    </div>
                                    <div class="form-group row">
                                        <label class="col-sm-3 col-form-label">Keterangan / Nama Rental :</label>
                                        <input type="text" name="ket" class="form-control col-7 optionalwhenhidden"
                                            required placeholder="Keterangan / Nama Rental" id="other_keterangan"
                                            value="<?php if ($order->mobil != null) echo $order->mobil->keterangan; ?>">
                                        <?php echo form_error('ket', '<br/><div class="alert alert-warning">', '</div>'); ?>
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer">
                                <div class="row">
                                    <button type="button" class="btn btn-primary btn-sm" data-toggle="modal"
                                        data-target="#approveDialog">
                                        <i class="fas fa-edit"></i>&nbsp; Simpan Perubahan
                                    </button>
                                </div>
                            </div>
                            <!-- show modal -->
                            <div class="modal fade" id="approveDialog" tabindex="-1" role="dialog"
                                aria-labelledby="approveModalLabel" aria-hidden="true">
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="approveModalLabel">Simpan Perubahan</h5>
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                        <div class="modal-body">
                                            Simpan Perubahan dengan no dokumen: <?php echo $order->order_id ?> ?
                                        </div>
                                        <div class="modal-footer">
                                            <script type="text/javascript">
                                                function submitAndClose() {
                                                    jQuery.noConflict();
                                                    jQuery('#approveForm').submit();
                                                    jQuery('#approveDialog').modal('hide');
                                                    setTimeout(function () {
                                                        document.getElementById("approveForm").requestSubmit();
                                                    }, 500);
                                                }
                                            </script>
                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                            <button type="button" onclick="submitAndClose()" class="btn btn-success">Simpan</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <?php echo form_close(); ?>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>
<?php $this->load->view('footer'); ?>
