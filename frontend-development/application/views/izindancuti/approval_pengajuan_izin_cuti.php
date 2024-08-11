<?php $this->load->view('header'); ?>

<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1 class="m-0">Approval Izin dan Cuti</h1>
                </div>
                <!-- /.col -->
                <div class="col-sm-6">
                    <ol class="breadcrumb float-sm-right">
                        <li class="breadcrumb-item">
                            <a href="<?php echo site_url('dashboard'); ?>">Home</a>
                        </li>
                        <li class="breadcrumb-item active">Izin dan Cuti</li>
                        <li class="breadcrumb-item active">Approval Izin dan Cuti</li>
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
                            <div class="row justify-content-end">
                                <div class="col-md-6 align-self-center">
                                    <h4 class="card-title">Filter by :</h4>
                                </div>
                                <div class="col-md-6">
                                    <select id="filterSelect" class="form-control">
                                        <option value="">Semua</option>
                                        <option value="PENDING" selected="selected">Pending</option>
                                        <option value="APPROVED">Approved</option>
                                        <option value="REJECTED">Rejected</option>
                                        <option value="CANCELLED">Cancelled</option>
                                        <option value="ON_GOING">On Going</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <!-- /.card-header -->

                        <div class="card-body">
                            <table id="karyawan" class="table table-bordered table-striped">
                                <thead>
                                    <tr>
                                        <th>Nama Pengaju</th>
                                        <th>Divisi</th>
                                        <th>Nama Cuti</th>
                                        <th>Date & Time</th>
                                        <th>Keterangan</th>
                                        <th>Action Information</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <?php if(isset($data)): 
                                        foreach($data as $dt): ?>

                                    <?php
                                        $date = $dt->startDate;
                                        $dateOnly = substr($date, 0, 10);

                                    ?>
                                    <tr>
                                        <td><?php echo $dt->employeeName; ?></td>
                                        <td><?php echo $dt->employeeCode; ?></td>
                                        <td><?php echo $dt->jenisName; ?></td>
                                        <td><?php echo $dateOnly ?></td>
                                        <td><?php echo $dt->status; ?></td>
                                        <td>
                                            <a
                                                href="<?php echo site_url('izincuti/detailapprovalpengajuanizincuti/'.$dt->id); ?>">
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

<script>
    function setDefaultFilter() {
        var filter = "PENDING".toUpperCase();
        var rows = document.querySelector("#karyawan tbody").rows;

        for (var i = 0; i < rows.length; i++) {
            var fifthCol = rows[i].cells[4].textContent.toUpperCase();

            if (filter === "" || fifthCol.indexOf(filter) > -1) {
                rows[i].style.display = "";
            } else {
                rows[i].style.display = "none";
            }
        }
    }

    document.getElementById('filterSelect').addEventListener('change', function () {
        var filter = this.value.toUpperCase();
        var rows = document.querySelector("#karyawan tbody").rows;

        for (var i = 0; i < rows.length; i++) {
            var fifthCol = rows[i].cells[4].textContent.toUpperCase();

            if (filter === "" || fifthCol.indexOf(filter) > -1) {
                rows[i].style.display = "";
            } else {
                rows[i].style.display = "none";
            }
        }
    });

    window.onload = setDefaultFilter;
</script>