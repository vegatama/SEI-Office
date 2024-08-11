<?php $this->load->view('header'); ?>
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1 class="m-0">Detail Absen</h1>
                </div>
                <!-- /.col -->
                <div class="col-sm-6">
                    <ol class="breadcrumb float-sm-right">
                        <li class="breadcrumb-item">
                            <a href="<?php echo site_url('dashboard'); ?>">Home</a>
                        </li>
                        <li class="breadcrumb-item">
                            <a href="<?php echo site_url('izincuti/statuspengajuanizincuti'); ?>">Status Absen</a>
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
                            <h3 class="card-title">Detail Absen</h3>
                        </div>
                        <!-- /.card-header -->
                        <div class="card-body">
                            <!-- Tampilkan detail data izin cuti di sini -->
                            <div class="row p-3">
                                <div class="col-md">
                                    <div class="form-group pr-1">
                                        <label>Nama</label>
                                        <input
                                            class="form-control"
                                            value="<?php echo $attendance->employeeName; ?>"
                                            readonly="readonly">    
                                    </div>
                                    <div class="form-group pr-1">
                                        <label>Employee Code</label>
                                        <input
                                            class="form-control"
                                            value="<?php echo $attendance->employeeCode; ?>"
                                            readonly="readonly">    
                                    </div>
                                    <div class="form-group pr-1">
                                        <label>Position</label>
                                        <input
                                            class="form-control"
                                            value="<?php echo $attendance->employeeJobTitle; ?>"
                                            readonly="readonly">
                                    </div>
                                    <div class="form-group pr-1">
                                        <label>Keterangan</label>
                                        <input
                                            class="form-control"
                                            value="<?php echo $attendance->reason; ?>"
                                            readonly="readonly">
                                    </div>
                                    <div class="form-group pr-1">
                                        <label>Lokasi Absen</label>
                                        <input
                                            class="form-control"
                                            value="<?php echo $attendance->lokasiAbsen; ?>"
                                            readonly="readonly">
                                    </div>
                                    <div class="form-group pr-1">
                                        <label>Upload Photo</label> <br> 
                                        <img src="<?php echo $attendance->imageUrl; ?> " style="width: 300px" alt="Uploaded Photo">
                                    </div>

                                    <div class="form-group pr-1">
                                        <label>Date</label>
                                        <input
                                            class="form-control"
                                            value="<?php echo $attendance->date; ?>"
                                            readonly="readonly">
                                    </div>
                                    <div class="form-group pr-1">
                                        <label>Jam Absen</label>
                                        <input
                                            class="form-control"
                                            value="<?php echo $attendance->targetTime; ?>"
                                            readonly="readonly">
                                    </div>
                                    <div class="form-group pr-1">
                                        <label>Status Pengajuan</label>
                                        <input
                                            class="form-control"
                                            value="<?php echo $attendance->approved; ?>"
                                            readonly="readonly">
                                    </div>
                                    

                                   

    <?php $this->load->view('footer'); ?>