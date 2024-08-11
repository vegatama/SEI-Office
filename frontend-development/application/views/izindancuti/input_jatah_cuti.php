<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Input Jatah Cuti Per Tahun</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Input Jatah Cuti</li>
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
        <div class="card-header">
          <h3 class="card-title">List Data jatah Cuti</h3>
          <div class="row px-3 float-sm-right">
          <a class="btn btn-success btn-sm" href="<?php echo site_url('jatahcuti/tambahdata'); ?>">
            <i class="fa fa-solid fa-plus"></i> TAMBAH DATA
            </a>
            </div>
          <div class="row px-2 float-sm-right">
          <a class="btn btn-primary btn-sm" href="<?php echo site_url('jatahcuti/downloadTemplate'); ?>">
                <i class="fa fa-solid fa-download"></i> FORMAT EXCEL
            </a>
            </div>
            <div class="row px-3 float-sm-right">
            <a class="btn btn-success btn-sm" href="<?php echo site_url('jatahcuti/importfile'); ?>">
                IMPORT
            </a>
            </div>
        </div>
        <!-- /.card-header -->

        <div class="card-body">
        <?php echo form_open('izincuti/jatahCuti'); ?>
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

          <table id="karyawan" class="table table-bordered table-striped">
            <thead>
            <tr>
              <th>ID</th>
              <th>Nama</th>
              <th>Tahun Cuti</th>
              <th>Jatah Cuti</th>
              <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <?php 
              if(isset($listjatahcuti)):
                foreach($listjatahcuti as $data) :                  
              ?>
                        <tr>
                            <td style="vertical-align:middle;"><?php echo $data->empcode; ?></td>
                            <td style="vertical-align:middle;"><?php echo $data->employeeName; ?></td> 
                            <td style="vertical-align:middle;"><?php echo $data->tahun; ?></td> 
                            <td style="vertical-align:middle;"><?php echo $data->jumlahCuti; ?></td>
                            <td style="text-align:center; vertical-align:middle; width:125px;">
                                <a class="btn btn-primary py-1 px-3 mr-1" href="<?php echo site_url('Jatahcuti/edit/'.$data->empcode.'/'.$data->tahun); ?>">Edit</a>
                                <!--
                                <a class="btn btn-danger py-1 px-2" href="<?php //echo site_url('Jatahcuti/delete/'.$data->id); ?>"><i class="fa fa-solid fa-trash" style="color:white"></i></a>
                                -->
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
