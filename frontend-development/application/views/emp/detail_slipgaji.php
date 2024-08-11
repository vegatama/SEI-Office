<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-8">
				<?php
				$months = array(
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
        <h1 class="m-0">Detail Slip Gaji <?php echo $dataslip->name . " (" . $months[$dataslip->bulan] . " " . $dataslip->tahun . ")"; ?></h1>
      </div><!-- /.col -->
      <div class="col-sm-4">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item">Slipgaji</li>
          <li class="breadcrumb-item active">Detail</li>
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
      <div class="col-md-12">
        <div class="card card-warning">
          <div class="card-header">
            <h3 class="card-title"><i class="fas fa-file-invoice"></i> Fields Pada Slip Gaji</h3>
          </div>
          <div class="card-body">
            <table id="carna" class="table table-bordered table-striped">
							<?php
							// group by category (PEMASUKAN and PENGELUARAN)
							$pemasukan = array();
							$pengeluaran = array();
							$maxRow = 0;
							if (isset($dataslip->fields)) {
								foreach ($dataslip->fields as $dt) {
									if ($dt->category == "PEMASUKAN") {
										$pemasukan[] = $dt;
									} else {
										$pengeluaran[] = $dt;
									}
								}
								$maxRow = max(count($pemasukan), count($pengeluaran));
								// sort by order
								usort($pemasukan, function ($a, $b) {
									return $a->order - $b->order;
								});
								usort($pengeluaran, function ($a, $b) {
									return $a->order - $b->order;
								});
							}
							?>
              <thead>
              <tr>
								<th>PEMASUKAN</th>
								<th>PENGELUARAN</th>
              </tr>
              </thead>
              <tbody>
							<?php

								for ($i = 0; $i < $maxRow; $i++) {
									?>
									<tr>
										<td><?php echo isset($pemasukan[$i]) ? $pemasukan[$i]->name : ""; ?></td>
										<td><?php echo isset($pengeluaran[$i]) ? $pengeluaran[$i]->name : ""; ?></td>
									</tr>
								<?php
								}
								?>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
		<div class="row">
			<div class="col-md-12">
				<div class="card card-info">
					<div class="card-header">
						<h3 class="card-title"><i class="fas fa-file-invoice"></i> List Isi Data Karyawan Pada Slip Gaji</h3>
					</div>
					<div class="card-body">
						<table id="karyawan" class="table table-bordered table-striped w-100">
							<thead>
							<tr>
								<th rowspan="2">No.</th>
								<th rowspan="2">Kode Karyawan</th>
								<th rowspan="2">Nama Karyawan</th>
								<?php
								if (count($pemasukan) > 0) {
								?>
								<th colspan="<?php echo count($pemasukan); ?>">Pemasukan</th>
								<?php
								}
								if (count($pengeluaran) > 0) {
								?>
								<th colspan="<?php echo count($pengeluaran); ?>">Pengeluaran</th>
								<?php
								}
								?>
								<th rowspan="2">Status</th>
							</tr>
							<tr>
								<?php
								foreach ($pemasukan as $dt) {
									?>
									<th><?php echo $dt->name; ?></th>
								<?php
								}
								foreach ($pengeluaran as $dt) {
									?>
									<th><?php echo $dt->name; ?></th>
								<?php
								}
								?>
							</thead>
							<?php
							if (isset($dataslip->data)) {
								function findDataByFieldId($data, $fieldId)
								{
									foreach ($data as $dt) {
										if ($dt->idField == $fieldId) {
											return $dt;
										}
									}
									return null;
								}
								$no = 1;
								foreach ($dataslip->data as $dt) {
									?>
									<tr style="<?php
									if ($dt->sent && $dt->complete) {
										echo "background-color: #d4edda;";
									} else if (!$dt->complete) {
										echo "background-color: #f8d7da;";
									}
									?>">
										<td><?php echo $no++; ?></td>
										<td><?php echo $dt->employeeCode; ?></td>
										<td><?php echo $dt->employeeName; ?></td>
										<?php
										foreach ($pemasukan as $pem) {
											?>
											<td><?php
												$dtPemasukan = findDataByFieldId($dt->dataList, $pem->id);
												echo isset($dtPemasukan) ? $dtPemasukan->value : "";
												?></td>
										<?php
										}
										foreach ($pengeluaran as $peng) {
											?>
											<td><?php
												$dtPengeluaran = findDataByFieldId($dt->dataList, $peng->id);
												echo isset($dtPengeluaran) ? $dtPengeluaran->value : "";
												?></td>
										<?php
										}
										?>
										<td>
											<?php
											if ($dt->sent && $dt->complete) {
												echo "Terkirim";
											} else if ($dt->complete) {
												echo "Belum Dikirim";
											} else {
												echo "Belum Lengkap";
											}
											?>
										</td>
									</tr>
								<?php
								}
							}
							?>
						</table>
					</div>
				</div>
			</div>
		</div>
  </div>
  <!-- /.content -->
  </section>
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>
