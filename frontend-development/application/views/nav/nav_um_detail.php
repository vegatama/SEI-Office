<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-8">
        <h1 class="m-0">Detail Document <?php echo $umhead->Nomor; ?></h1>
      </div><!-- /.col -->
      <div class="col-sm-4">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('nav/dash'); ?>">NAV Document</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('nav/um'); ?>">UM</a></li>
          <li class="breadcrumb-item active">UM Detail</li>
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
        <div class="card card-info">
          <div class="card-header">
            <div class="row">
              <div class="col-6">
                <h3 class="card-title"><i class="fas fa-chart-bar"></i> Permohonan Uang Muka</h3>
              </div>
              <div class="col-6 float-sm-right">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <button type="button" class="btn btn-light btn-sm" data-toggle="modal" data-target="#historyDialog">
                  <i class="fas fa-history"></i>&nbsp; Approval Reject History
                </button>
                <button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#rejectDialog">
                  <i class="fas fa-times"></i>&nbsp; Reject This Document
                </button>
                <button type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#approveDialog">
                  <i class="fas fa-clipboard-check"></i>&nbsp; Approve This Document
                </button>
              </div>
              
            </div>
          </div>

          <div class="card-body">
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">No Document</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $umhead->Nomor; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Tanggal Dokumen</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $umhead->Tanggal; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Nama Kegiatan</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $umhead->Nama_Kegiatan; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Kode Kegiatan</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $umhead->Kode_Kegiatan; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Untuk Keperluan</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $umhead->Untuk_Keperluan; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Jangka Waktu Mulai</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $umhead->Jangka_Waktu_Mulai; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Jangka Waktu Akhir</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $umhead->Jangka_Waktu_Akhir; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Amount</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo number_format($umhead->gVarTotalAmount,2,",","."); ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Status</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $umhead->Status; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Need Approve User ID</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $umhead->Need_Approve_User_Id; ?>" disabled>
            </div>
          </div>
        </div>
      </div>

    </div>

    <div class="row">
      <div class="col-md-12">
        <div class="card card-warning">
          <div class="card-header">
            <h3 class="card-title"><i class="fas fa-clipboard-list"></i> Lines</h3>
          </div>
          <div class="card-body">
            
            <div class="card card-secondary">

                <table class="table table-bordered table-striped">
                  <thead>
                    <th>Kode Kegiatan</th>                   
                    <th>Keperluan</th>
                    <th>Amount</th>
                    <th>Kode Mata Anggaran</th>
                  </thead>
                  <tbody>
                    <?php if(isset($umline)): ?>
                    <?php foreach ($umline as $ln): ?>
                    <tr>
                      <td><?php echo $ln->Kode_Kegiatan; ?></td>
                      <td><?php echo $ln->Untuk_Keperluan; ?></td>
                      <td><?php echo number_format($ln->gVarTotalAmount,2,",","."); ?></td>
                      <td><?php echo $ln->Kode_Mata_Anggaran; ?></td>
                    </tr>
                    <?php endforeach; ?>
                  <?php endif; ?>
                  </tbody>
                </table>

            </div>            
          </div>
          <button type="button" class="btn btn-success btn-md" data-toggle="modal" data-target="#approveDialog">
              <i class="fas fa-clipboard-check"></i>&nbsp; Approve This Document
            </button>
        </div>
      </div>
    </div>

  </div>

  <!-- Modal Approve -->
  <div class="modal fade" id="approveDialog" tabindex="-1" role="dialog" aria-labelledby="approveModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">Approve Document</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <?php echo form_open('approve/um'); ?>
        <?php echo form_hidden('nodoc',$umhead->Nomor); ?>
        <div class="modal-body">
          Approve Document Uang Muka dengan no: <?php echo $umhead->Nomor ?> ?
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
          <button type="submit" class="btn btn-success">Approve Document</button>
        </div>
        <?php echo form_close(); ?>
      </div>
    </div>
  </div>

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
        <?php echo form_open('reject/um'); ?>
        <?php echo form_hidden('nodoc',$umhead->Nomor); ?>
        <div class="modal-body">
          <textarea class="form-control" name="alasan" rows="3" required></textarea>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
          <button type="submit" class="btn btn-danger">Reject Document</button>
        </div>
        <?php echo form_close(); ?>
      </div>
    </div>
  </div>

  <!-- Modal History -->
  <div class="modal fade" id="historyDialog" tabindex="-1" role="dialog" aria-labelledby="approveModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">Approve Reject Log</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <table class="table table-striped table-bordered">
            <thead>
              <th>Entry No</th>
              <th>Document No</th>
              <th>Type</th>
              <th>From User ID</th>
              <th>Alasan Reject</th>
              <th>Entry Date Time</th>
            </thead>
            <tbody>
              <?php foreach($logs as $log): ?>
              <tr>
                <td><?php echo $log->Urutan_No; ?></td>
                <td><?php echo $log->Document_No; ?></td>
                <td><?php echo $log->Type_Process; ?></td>
                <td><?php echo $log->From_ID; ?></td>
                <td><?php echo $log->Alasan_Reject; ?></td>
                <td><?php echo $log->Entry_Date_Time; ?></td>
              </tr>
              <?php endforeach; ?>
            </tbody>
          </table>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>

  <!-- /.content -->
  </section>
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>