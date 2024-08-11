<?php $this->load->view('header'); ?>

<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1 class="m-0">Izin dan Cuti</h1>
                </div>
                <!-- /.col -->
                <div class="col-sm-6">
                    <ol class="breadcrumb float-sm-right">
                        <li class="breadcrumb-item">
                            <a href="<?php echo site_url('dashboard'); ?>">Home</a>
                        </li>
                        <li class="breadcrumb-item active">Izin dan Cuti</li>
                        <li class="breadcrumb-item active">Status Pengajuan</li>
                    </ol>
                </div>
                <!-- /.col -->
            </div>
            <!-- /.row -->
        </div>
        <!-- /.container-fluid -->
    </div>
    <!-- /.content-header -->

    <!-- info izin dan sisa cuti -->
    <section class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-12">
                    <!--<div class="card">-->
                        <div class="row mb-2">
                            <div class="col-md-6">
                                <div class="info-box">
                                    <span class="info-box-icon bg-info">
                                        <i class="fas fa-calendar"></i>
                                    </span>
                                    <div class="info-box-content">
                                        <span class="info-box-text">Izin</span>
                                        <span class="info-box-number"><?php echo $akumulasiIzin->akumulasiIzin; ?></span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="info-box">
                                    <span class="info-box-icon bg-success">
                                        <i class="fas fa-calendar-check"></i>
                                    </span>
                                    <div class="info-box-content">
                                        <span class="info-box-text">Sisa Cuti</span>
                                        <span class="info-box-number"><?php echo $sisaCuti->sisaCuti; ?></span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-12">
                            <a href="<?php echo site_url('izincuti/pengajuan') ?>" class="btn btn-block bg-gradient-info btn-lg"><b>Ajukan Izin & Cuti</b></a><br/>
                            </div>
                        </div>
                    <!--</div> -->
                </div>
                <!-- /.col -->
            </div>
            <!-- /.row -->       
    

    <!-- Main content -->

            <div class="row">
                <div class="col-md-12">
                    <div class="card card-secondary">
                        <div class="card-header">
                            <h3 class="card-title"><i class="fas fa-file-invoice"></i> List Izin dan Cuti yang diajukan</h3>
                        </div>
                        <!-- /.card-header -->

                        <div class="card-body">
                            <table id="karyawandesc" class="table table-bordered table-striped">
                                <thead>
                                <tr>
                                    <th>Start Date</th>
                                    <th>End Date</th>
                                    <th>Nama Cuti</th>
                                    <th>Jenis Cuti</th>
                                    <th>Status Pengajuan</th>
                                </tr>
                                </thead>
                                <tbody>
                                    <?php if(isset($data)):
                                        foreach($data as $dt): 
                                    ?>
                                    <?php
                                        $date = $dt->endDate;
                                        $dateOnly = substr($date, 0, 10);
                                        $date1 = $dt->startDate;
                                        $dateOnly1= substr($date1, 0, 10);

                                    ?>
                                    <tr>
                                        <td><?php echo $dateOnly1; ?></td>
                                        <td><?php echo $dateOnly; ?></td>
                                        <td><?php echo $dt->jenisName; ?></td>
                                        <td><?php echo $dt->tipe; ?></td>
                                        <td>
                                            <a
                                                href="<?php echo site_url('izincuti/detailstatuspengajuanizincuti/'.$dt->id); ?>">
                                                <?php if($dt->status === 'PENDING'): ?>
                                                <button class="btn btn-warning btn-block"><?php echo $dt->status; ?></button>
                                            <?php elseif($dt->status === 'APPROVED'): ?>
                                                <button class="btn btn-success btn-block"><?php echo $dt->status; ?></button>
                                            <?php elseif($dt->status === 'REJECTED'): ?>
                                                <button class="btn btn-danger btn-block"><?php echo $dt->status; ?></button>
                                            <?php elseif($dt->status === 'CANCELLED'): ?>
                                                <button class="btn btn-danger btn-block"><?php echo $dt->status; ?></button>
                                            <?php elseif($dt->status === 'ON_GOING'): ?>
                                                <button class="btn btn-info btn-block"><?php echo $dt->status; ?></button>
                                            <?php elseif($dt->status === 'DONE'): ?>
                                                <button class="btn btn-info btn-block"><?php echo $dt->status; ?></button>
                                                <?php endif; ?>
                                            </a>
                                        </td>
                                    </tr>
                                    <?php endforeach; ?>
                                    <?php endif; ?>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <!-- /.col -->
            </div>
            <!-- /.row -->
        </div>
        <!-- /.container-fluid -->
    </section>
    <!-- /.content -->
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>