<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Master Data</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Master Data Hari Libur Nasional</li>
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
    <div class="col-12">
    <div class="card">
      <div class="card">
        <div class="card-header">
          <h3 class="card-title">Hari Libur Nasional</h3>
          <a class="btn btn-primary btn-sm float-sm-right" href="<?php echo site_url('libur/tambah'); ?>">
              <i class="far fa-calendar-alt"></i> Tambah Hari Libur
          </a>
        </div>
        <div class="card-header">
        <?php echo form_open('master/libur'); ?>
            <div class="form-group">
              <label>Tahun</label>
              <div class="row">
              <div class="col-md-4">
              <select class="form-control select2" name="tahun" style="width: 90%;">
                <?php for($i=-3; $i<=3; $i++): $thn = $tahun + $i;?>
                  <option value="<?php echo $thn; ?>" <?php if($tahun == $thn) echo "selected"; ?>><?php echo $thn; ?></option>
                <?php endfor; ?>
              </select>
              </div>
              <div class="col-md-8">
              <button class="btn btn-primary" type="submit" name="tampil">Tampilkan Data</button>
              </div>
              </div>
            </div>
          <?php echo form_close(); ?><br/>
        <!-- /.card-header -->
        <div class="card-body">
          <table id="karyawan" class="table table-bordered table-striped">
            <thead>
            <tr>
              <th>Tanggal</th>
              <th>Keterangan</th>
              <th>Action</th>
            </tr>
            </thead>
            <tbody>
              <?php 
                if(isset($libur)):
                foreach($libur as $data) :                  
              ?>
              <tr>
                <td><?php echo $data->tanggal; ?></td>
                <td><?php echo $data->keterangan; ?></td>
                <td>
                    <a class="btn btn-primary py-1 px-3 mr-1" href="<?php echo site_url('libur/edit/'.$data->id); ?>">Edit</a>
                    <a class="btn btn-danger py-1 px-2" href="<?php echo site_url('libur/delete/'.$data->id); ?>"><i class="fa fa-solid fa-trash" style="color:white"></i></a>
                </td>
              </tr>
            <?php 
              endforeach;
              endif; 
            ?>
            </tbody>
          </table>
        </div>
      </div>
      <!-- /.row -->
    </div><!-- /.container-fluid -->
  </div>
  <!-- /.content -->
  </section>
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>