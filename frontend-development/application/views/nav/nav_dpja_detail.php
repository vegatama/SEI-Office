<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-8">
        <h1 class="m-0">Detail DPJ <?php echo $dpbhead->No; ?></h1>
      </div><!-- /.col -->
      <div class="col-sm-4">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('nav/dash'); ?>">NAV Document</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('nav/dpb'); ?>">DPJ</a></li>
          <li class="breadcrumb-item active">DPJ Detail</li>
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
              <div class="col-sm-8">
                <h3 class="card-title"><i class="fas fa-chart-bar"></i> Daftar Pembelian Jasa</h3>
              </div>
              <div class="col-sm-4">
                <button type="button" class="btn btn-light btn-sm float-sm-right" data-toggle="modal" data-target="#historyDialog">
                  <i class="fas fa-history"></i>&nbsp; Approval Reject History
                </button>
              </div>              
            </div>
          </div>

          <div class="card-body">
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">No DPJ</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $dpbhead->No; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Project Code</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $dpbhead->Kode_Proyek; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Project Name</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $dpbhead->Project_Name; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Tanggal Dokumen</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $dpbhead->Document_Date; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Tanggal Dibutuhkan</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $dpbhead->Tanggal_Dibutuhkan; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Amount</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo number_format($dpbhead->Amount,2,",","."); ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Status</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $dpbhead->Status; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Need Approve User ID</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $dpbhead->Need_Approve_User_Id; ?>" disabled>
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
                    <th>Line Number</th>
                    <th>Item Number</th>
                    <th>Description</th>
                    <th>Qty</th>
                    <th>Unit of Measure</th>
                    <th>Direct Unit Cost</th>
                    <th>Unit Cost LCY</th>
                    <th>Amount</th>
                    <th>VAT</th>
                    <th>Amount Inc VAT</th>
                  </thead>
                  <tbody>
                    <?php if(isset($dpbline)): ?>
                    <?php foreach ($dpbline as $ln): ?>
                    <tr>
                      <td><?php echo $ln->Line_No; ?></td>
                      <td><?php echo $ln->ItemNo; ?></td>
                      <td><?php echo $ln->Description; ?></td>
                      <td><?php echo number_format($ln->Quantity,2,",","."); ?></td>
                      <td><?php echo $ln->Unit_of_Measure; ?></td>
                      <td><?php echo number_format($ln->Direct_Unit_Cost,2,",","."); ?></td>
                      <td><?php echo number_format($ln->Unit_Cost_LCY,2,",","."); ?></td>
                      <td><?php echo number_format($ln->Amount,2,",","."); ?></td>
                      <td><?php echo number_format($ln->VAT_Percent,2,",","."); ?></td>
                      <td><?php echo number_format($ln->Amount_Including_VAT,2,",","."); ?></td>
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
              <?php foreach($dpblog as $log): ?>
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