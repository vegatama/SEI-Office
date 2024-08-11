<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-9">
        <h1 class="m-0">Purchase Order</h1>
      </div><!-- /.col -->
      <div class="col-sm-3">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item">Purchase Order</li>
          <li class="breadcrumb-item active">List</li>
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
        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title"><i class="fas fa-file-invoice"></i> Purchase Order</h3>
          </div>
          <div class="card-body">

            <table id="po" class="table table-bordered table-striped">
              <thead>
              <tr>
                <th>PO Number</th>
                <th>Project Code</th>
                <th>Date</th>
                <th>Item</th>
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
              </tr>
              </thead>
              <tbody>
              <?php 
                if(isset($purchases)):
                foreach($purchases as $po) : 
              ?>
              <tr>
                <td><?php echo $po->po_number; ?></td>
                <td><?php echo $po->po_project_code; ?></td>
                <td><?php echo $po->po_date; ?></td>
                <td><?php echo $po->po_description; ?></td>
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
              <?php 
                endforeach; 
                endif; 
              ?>
              </tbody>
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