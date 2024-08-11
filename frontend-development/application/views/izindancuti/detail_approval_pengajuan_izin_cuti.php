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
                            <a href="<?php echo site_url('izincuti/approvalpengajuanizincuti'); ?>">Approval Izin dan Cuti</a>
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
                                        <label>Nama Pengaju</label>
                                        <input
                                            class="form-control"
                                            value="<?php echo $employeeName->employeeName; ?>"
                                            readonly="readonly">
                                    </div>
                                    <div class="form-group pr-1">
                                        <label>Divisi</label>
                                        <input
                                            class="form-control"
                                            value="<?php echo $employeeCode->employeeCode; ?>"
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
                                        <label>Date & Time</label>
                                        <?php
                                        $date = $startDate->startDate;
                                        $dateOnly = substr($date, 0, 10);
                                        ?>
                                        <input
                                            class="form-control"
                                            value="<?php echo $dateOnly ?>"
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
                                    <a href="<?php echo base_url('izincuti/approve/'.$id->id.'/'.$approval->reviewerEmployeeCode); ?>" class="btn btn-success btn-lg mr-2">Terima</a>
                                    <button type="button" class="btn btn-danger btn-lg mr-2" data-toggle="modal" data-target="#rejectModal">Tolak</button>
                            <?php elseif ($status->status === "APPROVED"): ?>
                                <button type="button" class="btn btn-success btn-lg">Anda Sudah Meng-approve</button>
                            <?php elseif ($status->status === "REJECTED"): ?>
                                <button type="button" class="btn btn-danger btn-lg">Anda Menolak Approve</button>
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

    <!-- Modal -->
    <div class="modal fade" id="rejectModal" tabindex="-1" role="dialog" aria-labelledby="rejectModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="rejectModalLabel">Alasan Penolakan</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form action="<?php echo base_url('izincuti/reject/'.$id->id.'/'.$approval->reviewerEmployeeCode); ?>" method="post">
                    <input type="hidden" name="<?= $this->security->get_csrf_token_name(); ?>" value="<?= $this->security->get_csrf_hash(); ?>" />
                    <div class="modal-body">
                        <div class="form-group">
                            <label for="reason">Alasan:</label>
                            <textarea class="form-control" id="reason" name="reason" rows="3" required></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Batal</button>
                        <button type="submit" class="btn btn-danger">Tolak</button>
                    </div>
                </form>
            </div>
        </div>
    </div>


    <?php $this->load->view('footer'); ?>