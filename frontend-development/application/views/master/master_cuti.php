<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Master Data Cuti & Izin</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Cuti & Izin</li>
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
          <h3 class="card-title">List Data Cuti & Izin</h3>
          <a class="btn btn-success btn-sm float-sm-right" href="<?php echo site_url('cuti/tambah'); ?>">
              <i class="fa fa-solid fa-plus"></i> Tambah Data
          </a>
        </div>
        <!-- /.card-header -->

        <div class="card-body">
          <table id="karyawan" class="table table-bordered table-striped">
            <thead>
            <tr>
              <th>ID</th>
              <th>Nama</th>
              <th>Cut Cuti</th>
              <th>Approval</th>
              <th>Jenis Pengajuan</th>
              <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <?php 
              if(isset($izincuti)):
                foreach($izincuti as $data) :                  
              ?>
                        <tr>
                            <td style="vertical-align:middle;"><?php echo $data->id; ?></td>
                            <td style="vertical-align:middle;"><?php echo $data->namaJenis; ?></td>
                            <td style="text-align:center; vertical-align:middle;">
                            <?php if($data->cutCuti == True):?>
                            <i class="fa fa-solid fa-check" style="color:green">
                            <?php else:?>
                            <i class="fa fa-solid fa-times" style="color:red">
                            <?php endif;?>
                            </td style="vertical-align:middle;">
                            <td><ol type="1">
                            <?php $app_number = 0;
                                  $array_app = array(0,0,0);
                            foreach($data->reviewers as $list) :
                                  if(property_exists($list, 'layerIndex') == False){ ?>
                                  <li><?php echo $list->empName; ?> (<?php echo $list->empCode; ?>)</li>
                                  <?php }else if($list->layerIndex === 0){ 
                                    $array_app[$app_number] = 1;?>
                                  <li>Atasan Pertama</li>
                                  <?php }else if($list->layerIndex === 1){ 
                                    $array_app[$app_number] = 2;?>
                                  <li>Atasan Kedua</li>
                                  <?php }else if($list->layerIndex === 2){ 
                                    $array_app[$app_number] = 3;?>
                                  <li>Atasan Ketiga</li>
                                  <?php } ?>
                            <?php $app_number = $app_number + 1;
                          endforeach; ?>
                            </ol></td>
                            <td style="vertical-align:middle;"><?php echo $data->pengajuan; ?></td>
                            <td style="text-align:center; vertical-align:middle; width:125px;">
                                <a class="btn btn-primary py-1 px-3 mr-1" href="<?php echo site_url('cuti/edit/'.$data->id.'/'.$array_app[0].'/'.$array_app[1].'/'.$array_app[2]); ?>">Edit</a>
                                <a class="btn btn-danger py-1 px-2" href="<?php echo site_url('cuti/delete/'.$data->id); ?>"><i class="fa fa-solid fa-trash" style="color:white"></i></a>
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
