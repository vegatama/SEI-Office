<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-8">
        <h1 class="m-0">Detail Document <?php echo $bapbhead->No; ?></h1>
      </div><!-- /.col -->
      <div class="col-sm-4">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('nav/dash'); ?>">NAV Document</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('nav/dpb'); ?>">BAPB</a></li>
          <li class="breadcrumb-item active">BAPB Detail</li>
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
                <h3 class="card-title"><i class="fas fa-chart-bar"></i> Berita Acara Penerimaan Barang</h3>
              </div>
              <div class="col-6">
                <button type="button" class="btn btn-light btn-sm float-sm-right" data-toggle="modal" data-target="#historyDialog">
                  <i class="fas fa-history"></i>&nbsp; Approval Reject History
                </button>
              </div>
              
            </div>
          </div>

          <div class="card-body">
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">No Document</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $bapbhead->No; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Tanggal Dokumen</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $bapbhead->Document_Date; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">No DPB/J</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $bapbhead->DPB_No_Header; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">No SP/PO</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $bapbhead->No_SP_PO; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">No SPM/Kontrak</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $bapbhead->No_SPM_Kontrak; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Vendor No</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $bapbhead->Vendor_No; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Kode Kegiatan</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $bapbhead->Proyek_Code; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Nama Kegiatan</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $bapbhead->Proyek_Name; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Status</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $bapbhead->Status; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Need Approve User ID</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $bapbhead->Need_Approve_User_Id; ?>" disabled>
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
                    <th>Item Number</th>                   
                    <th>Item Description</th>
                    <th>Qty Barang Datang</th>
                    <th>Qty Diterima Baik</th>
                  </thead>
                  <tbody>
                    <?php if(isset($bapbline)): ?>
                    <?php foreach ($bapbline as $ln): ?>
                    <tr>
                      <td><?php echo $ln->Item_No; ?></td>
                      <td><?php echo $ln->Item_Description; ?></td>
                      <td><?php echo number_format($ln->Qty_Barang_Datang,2,",","."); ?></td>
                      <td><?php echo number_format($ln->Quantity,2,",","."); ?></td>
                    </tr>
                    <?php endforeach; ?>
                  <?php endif; ?>
                  </tbody>
                </table>

            </div>            
          </div>
        </div>
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