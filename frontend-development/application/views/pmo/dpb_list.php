<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-9">
        <h1 class="m-0">DPB/DPJ <?php echo $proyek->project_code; ?> (<?php echo $proyek->project_name; ?>)</h1>
      </div><!-- /.col -->
      <div class="col-sm-3">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item">Dpb</li>
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
        <div class="card card-info">
          <div class="card-header">
            <h3 class="card-title"><i class="fas fa-chart-bar"></i> DPB/DPJ</h3>
          </div>
          <div class="card-body">

            <?php echo form_open('dpb/list'); ?>
            <div class="form-group">
              <div class="row">
                <div class="col-md-8">
                  <div class="form-group row">
                    <label class="col-sm-2 col-form-label">Kode Proyek:</label>
                    <select class="form-control select2 col-sm-6" name="kdproyek" style="width: 100%;">
                      <?php foreach($proyeks as $pr): ?>
                        <option value="<?php echo $pr->project_code; ?>" <?php if($pr->project_code == $proyek->project_code) echo "selected"; ?>><?php echo $pr->project_code." (".$pr->project_name.")"; ?></option>
                      <?php endforeach; ?>  
                    </select>
                    &nbsp;&nbsp;
                    <button class="btn btn-primary col-sm-2" type="submit" name="tampil">Tampilkan Data</button>
                  </div>
                </div>
                <div class="col-md-2">
                  
                </div>
              </div>
              
            </div>
            <?php echo form_close(); ?>

            <table id="dpb" class="table table-bordered table-striped">
              <thead>
              <tr>
                <th>No</th>
                <th>Tanggal</th>
                <th>Amount</th>
                <th>Amount Inc VAT</th>
                <th>Tanggal Dibutuhkan</th>
                <th>Status</th>
                <th>&nbsp;</th>
              </tr>
              </thead>
              <tbody>
              <?php 
                if(isset($dpbs)):
                foreach($dpbs as $data) : 
              ?>
              <tr>
                <td><?php echo $data->document_no; ?></td>
                <td><?php echo $data->document_date; ?></td>
                <td><?php echo number_format($data->amount,0,",","."); ?></td>
                <td><?php echo number_format($data->amount_inc_vat,0,",","."); ?></td>
                <td><?php echo $data->tgl_dibutuhkan; ?></td>
                <td><?php echo $data->status; ?></td>
                <td><a class="btn btn-primary btn-sm" href="<?php echo site_url('dpb/detail/'.$data->document_no); ?>">
                              <i class="fas fa-clipboard-list">
                              </i>
                              Detail & Realisasi
                          </a></td>
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