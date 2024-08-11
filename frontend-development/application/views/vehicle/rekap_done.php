<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Selesaikan Pemesanan Kendaraan</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item">Mobil Operasional</li>
          <li class="breadcrumb-item active">Selesaikan Pemesanan Kendaraan</li>
        </ol>
      </div><!-- /.col -->
    </div><!-- /.row -->
  </div><!-- /.container-fluid -->
</div>
<!-- /.content-header -->

<!-- Main content -->
<section class="content">
<div class="content">
  <div class="container-fluid">
    <div class="row">
      <div class="col-12">
        <div class="card card-primary">

          <div class="card-header">
            <h3 class="card-title">Data Pemesanan Kendaraan Operasional</h3>
          </div>
          <!-- /.card-header -->
          
          <?php echo form_open('order/donep'); ?>
			<?php echo form_hidden('order_id',$order->order_id); ?>
          <div class="card-body">
            <div class="form-group row ">
              <label class="col-sm-3 col-form-label">Jadwal Kembali :</label>
              <div class="input-group date col-sm-3">
                <input type="text" value="<?php echo $order->tanggal_kembali; ?>" name="tanggal_kembali" class="form-control" id="datepickerkembali" required/>
                <div class="input-group-append">
                    <div class="input-group-text"><i class="fa fa-calendar"></i></div>
                </div>
              </div>
              <?php echo form_error('tanggal_kembali','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
            <label class="col-sm-3 col-form-label">Jam Kembali :</label>
            <div class="input-group date col-sm-3" data-target="#timepicker" data-toggle="datetimepicker">
                <input type="text" id="timepicker" name="jam_kembali" class="form-control datetimepicker-input" required value="<?php echo $order->jam_kembali?>"/>
                <div class="input-group-append">
                    <div class="input-group-text"><i class="far fa-clock"></i></div>
                </div>
              </div>
          </div>
          
          <div class="card-footer">
            <?php if ($order->status == 'DONE'): ?>
            <button type="submit" class="btn btn-primary" disabled>Pemesanan Telah Diselesaikan</button>
            <?php else: ?>
            <button type="submit" class="btn btn-primary">Selesaikan Pemesanan</button>
            <?php endif; ?>
          </div>
          <?php echo form_close(); ?>

        </div>  

      </div>    
    </div>
    <!-- /.row -->
  </div><!-- /.container-fluid -->
</div>
</section>
<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>
