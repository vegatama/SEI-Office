<?php $this->load->view('header'); ?>
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1 class="m-0">Detail Status Pengajuan Izin Cuti</h1>
                </div>
                <!-- /.col -->
                <div class="col-sm-6">
                    <ol class="breadcrumb float-sm-right">
                        <li class="breadcrumb-item">
                            <a href="<?php echo site_url('dashboard'); ?>">Home</a>
                        </li>
                        <li class="breadcrumb-item">
                            <a href="<?php echo site_url('izincuti/statuspengajuanizincuti'); ?>">Status Pengajuan Izin Cuti</a>
                        </li>
                        <li class="breadcrumb-item active">Detail</li>
                    </ol>
                </div>
                <!-- /.col -->
            </div>
            <!-- /.row -->
        </div>
        <!-- /.container-fluid -->
    </div>
    <!-- /.content-header -->

    <!-- Main content -->
    <section class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-header">
                            <h3 class="card-title">Detail Data Izin Cuti yang Diajukan</h3>
                        </div>
                        <!-- /.card-header -->
                        <div class="card-body">
                            <!-- Tampilkan detail data izin cuti di sini -->
                            <div class="row p-3">
                                <div class="col-md">
                                    <div class="form-group pr-1">
                                        <label>Status Pengajuan</label>
                                        <input
                                            class="form-control"
                                            value="<?php echo $status->status; ?>"
                                            readonly="readonly">
                                    </div>
                                    <div class="form-group pr-1">
                                        <label>Jenis Cuti</label>
                                        <input
                                            class="form-control"
                                            value="<?php echo $jenis->namaJenis; ?>"
                                            readonly="readonly">
                                    </div>
                                    <div class="form-group pr-1">
                                        <label>Jenis Pengajuan</label>
                                        <input
                                            class="form-control"
                                            value="<?php echo $jenis->pengajuan; ?>"
                                            readonly="readonly">
                                    </div>
                                    <div class="form-group pr-1">
                                        <label for="cut_cuti">Potong Jatah Cuti</label><br>
                                        <input
                                            class="form-control <?php echo ($jenis->cutCuti == true) ? 'text-success' : 'text-danger'; ?>"
                                            value="<?php echo ($jenis->cutCuti == true) ? 'Ya' : 'Tidak'; ?>"
                                            readonly="readonly">
                                    </div>
                                </div>
                                <div class="col-md">
                                    <div class="form-group pr-1">
                                        <label>Start Date & Time</label>
                                        <?php 
                                            $date = $startDate->startDate;
                                            $dateOnly = substr($date, 0, 10);
                                            $date1 = $endDate->endDate;
                                            $dateOnly1 = substr($date1, 0, 10);
                                        ?>
                                        <input
                                            class="form-control"
                                            value="<?php echo $dateOnly; ?>"
                                            readonly="readonly">
                                    </div>
                                    <div class="form-group pr-1">
                                        <label>End Date & Time</label>
                                        <input
                                            class="form-control"
                                            value="<?php echo $dateOnly1 ?>"
                                            readonly="readonly">
                                    </div>
                                    <div class="form-group pr-1">
                                        <label>Keterangan</label>
                                        <input
                                            class="form-control"
                                            value="<?php echo $reason->reason; ?>"
                                            readonly="readonly">
                                    </div>
                                    <div class="form-group pr-1">
                                        <div class="form-group pr-1">
                                            <label>Upload File</label>
                                            <?php if (!empty($files)) : ?>
                                                <?php foreach ($files as $file) : ?>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text">
                                                                <i class="fas fa-file"></i>
                                                            </span>
                                                        </div>
                                                        <input type="text" class="form-control" value="<?php echo $file->fileDownloadUri; ?>" readonly>
                                                    </div>
                                                <?php endforeach; ?>
                                            <?php else : ?>
                                                <input type="text" class="form-control" value="Tidak ada file" readonly>
                                            <?php endif; ?>
                                        </div>

                                        <div class="col">
                                            <label class="pl-1">Approval</label>
                                        </div>
                                        <div class="card" id="card1">
                                            <div class="card-body">
                                                <?php foreach($approvals as $approval): ?>
                                                <div class="col">
                                                    <input
                                                        class="form-control"
                                                        value="<?php echo $approval->reviewerName; ?>"
                                                        readonly="readonly">
                                                </div>
                                                <?php endforeach; ?>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer d-flex justify-content-center">
                                <?php if ($status->status === "PENDING"): ?>
                                    <a href="<?php echo base_url('izincuti/cancel/'.$id->id); ?>" class="btn btn-danger btn-lg mr-2">CANCEL</a>
                                <?php elseif ($status->status === "APPROVED"): ?>
                                    <button type="button" class="btn btn-success btn-lg">Pengajuan Anda Telah Diterima</button>
                                <?php elseif ($status->status === "REJECTED"): ?>
                                    <button type="button" class="btn btn-danger btn-lg">Pengajuan Anda Ditolak</button>
                                <?php elseif ($status->status === "CANCELLED"): ?>
                                    <button type="button" class="btn btn-danger btn-lg">Pengajuan Dicancel</button>
                                <?php elseif ($status->status == "ON_GOING"): ?>
                                    <button type="button" class="btn btn-info btn-lg">Cuti atau Izin Sedang Berlangsung</button>
                                    <?php endif; ?>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- /.row -->
            </div>
            <!-- /.container-fluid -->
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <?php $this->load->view('footer'); ?>