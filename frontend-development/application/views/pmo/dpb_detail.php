<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-9">
        <h1 class="m-0">Detail DPB/DPJ <?php echo $dpb->document_no; ?></h1>
      </div><!-- /.col -->
      <div class="col-sm-3">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item">Dpb</li>
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
        <div class="card card-info">
          <div class="card-header">
            <h3 class="card-title"><i class="fas fa-chart-bar"></i> DPB/DPJ</h3>
          </div>
          <div class="card-body">
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">No DPB/DPJ</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $dpb->document_no; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Project Code</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $dpb->project_code; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Project Name</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $dpb->project_name; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Tanggal Dokumen</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $dpb->document_date; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Tanggal Dibutuhkan</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $dpb->tgl_dibutuhkan; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Status</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $dpb->status; ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Amount</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo number_format($dpb->amount,2,",","."); ?>" disabled>
            </div>

            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Amount Inc VAT</label>
              <input class="col-sm-9 form-control" type="text" class="" value="<?php echo number_format($dpb->amount_inc_vat,2,",","."); ?>" disabled>
            </div>
          </div>
        </div>
      </div>

    </div>

    <div class="row">
      <div class="col-md-12">
        <div class="card card-warning">
          <div class="card-header">
            <h3 class="card-title"><i class="fas fa-clipboard-list"></i> Lines & Realisasi</h3>
          </div>
          <div class="card-body">
            <?php foreach($lines as $line): ?>
            <div class="card card-secondary">
              <div class="card-header">
                <h3 class="card-title">Item No: <?php echo $line->item_no; ?></h3>
              </div>
              <div class="card-body">
                <div class="form-group row">
                  <label class="col-sm-3 col-form-label">Item No:</label>
                  <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $line->item_no; ?>" disabled>
                </div>
                <div class="form-group row">
                  <label class="col-sm-3 col-form-label">Item Description:</label>
                  <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $line->item_description; ?>" disabled>
                </div>
                <div class="form-group row">
                  <label class="col-sm-3 col-form-label">Qty:</label>
                  <input class="col-sm-9 form-control" type="text" class="" value="<?php echo number_format($line->qty,2,",","."); ?>" disabled>
                </div>
                <div class="form-group row">
                  <label class="col-sm-3 col-form-label">Unit of Measure:</label>
                  <input class="col-sm-9 form-control" type="text" class="" value="<?php echo $line->unit_of_measure; ?>" disabled>
                </div>
                <div class="form-group row">
                  <label class="col-sm-3 col-form-label">Direct Unit Cost:</label>
                  <input class="col-sm-9 form-control" type="text" class="" value="<?php echo number_format($line->direct_unit_cost,2,",","."); ?>" disabled>
                </div>
                <div class="form-group row">
                  <label class="col-sm-3 col-form-label">Unit Cost LCY:</label>
                  <input class="col-sm-9 form-control" type="text" class="" value="<?php echo number_format($line->unit_cost_lcy,2,",","."); ?>" disabled>
                </div>
                <div class="form-group row">
                  <label class="col-sm-3 col-form-label">Amount:</label>
                  <input class="col-sm-9 form-control" type="text" class="" value="<?php echo number_format($line->amount,2,",","."); ?>" disabled>
                </div>
                <div class="form-group row">
                  <label class="col-sm-3 col-form-label">Vat Percent:</label>
                  <input class="col-sm-9 form-control" type="text" class="" value="<?php echo number_format($line->vat_percent,2,",","."); ?>" disabled>
                </div>
                <div class="form-group row">
                  <label class="col-sm-3 col-form-label">Amount Inc VAT:</label>
                  <input class="col-sm-9 form-control" type="text" class="" value="<?php echo number_format($line->amount_inc_vat,2,",","."); ?>" disabled>
                </div>

                <table class="table table-bordered table-striped">
                  <thead>
                    <th>PO Number</th>
                    <th>Date</th>
                    <th>Buy From</th>
                    <th>Qty</th>
                    <th>Unit of Measure</th>
                    <th>PO Direct Unit Cost</th>
                    <th>PO Unit Cost LCY</th>
                    <th>Amount</th>
                    <th>VAT</th>
                    <th>Amount Inc VAT</th>
                    <th>Amount LCY</th>
                    <th>Status</th>
                  </thead>
                  <tbody>
                    <?php if(isset($line->purchases)): ?>
                    <?php foreach ($line->purchases as $po): ?>
                    <?php if(!isset($po->po_amount_lcy)) $po->po_amount_lcy = 0; ?>
                    <tr>
                      <td><?php echo $po->po_number; ?></td>
                      <td><?php echo $po->po_date; ?></td>
                      <td><?php echo $po->po_buy_from; ?></td>
                      <td><?php echo number_format($po->po_qty,2,",","."); ?></td>
                      <td><?php echo $po->po_unit_of_measure; ?></td>
                      <td><?php echo number_format($po->po_direct_unit_cost,2,",","."); ?></td>
                      <td><?php echo number_format($po->po_unit_cost_lcy,2,",","."); ?></td>
                      <td><?php echo number_format($po->po_amount,2,",","."); ?></td>
                      <td><?php echo number_format($po->po_vat_percent,2,",","."); ?></td>
                      <td><?php echo number_format($po->po_amount_include_vat,2,",","."); ?></td>
                      <td><?php echo number_format($po->po_amount_lcy,2,",","."); ?></td>
                      <td><?php echo $po->po_status; ?></td>
                    </tr>
                    <?php endforeach; ?>
                  <?php endif; ?>
                  </tbody>
                </table>
              </div>
            </div>
            <?php endforeach; ?>
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