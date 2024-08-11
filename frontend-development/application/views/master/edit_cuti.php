<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Edit Data Cuti & Izin</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('master/cuti'); ?>">Master Data</a></li>
          <li class="breadcrumb-item active">Edit Data</li>
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
      <div class="card">
        <div class="card-header">
          <h3 class="card-title">Complete Form Below</h3>
        </div>
        <!-- /.card-header -->
        <?php echo form_open('cuti/update/'.$data_cuti->id); ?>
        <div class="card-body">
          <div class="row p-3">
            <div class="col-md">
            <div class="form-group pr-1">
                <label>Jenis Cuti</label>
                <?php echo form_error('jenis_cuti','<div class="alert alert-warning">','</div>'); ?>
                <input name="jenis_cuti" class="form-control" required placeholder="Jenis Cuti" value="<?php echo $data_cuti->izinCuti; ?>">
              </div>
              <div class="form-group pr-1">
                <label>Jenis Pengajuan</label>
                <select id="jenis_pengajuan" name="jenis_pengajuan" class="form-control" aria-label="Default select example">
                  <?php if($data_cuti->pengajuan == "HARIAN"): ?>
                    <option value="harian" selected>Harian</option>
                    <option value="menit">menit</option>
                  <?php else:?>
                    <option value="harian">Harian</option>
                    <option value="menit" selected>menit</option>
                  <?php endif;?>
                </select>
              </div>
              <div class="form-group pr-1 px-3">
              <?php if($data_cuti->cutCuti == True): ?>
                <input type="checkbox" id="cut_cuti" name="cut_cuti" style="text-align: center; vertical-align:middle; margin-all: 2px; transform: scale(1.4);" value="True" checked>
                <?php else:?>
                  <input type="checkbox" id="cut_cuti" name="cut_cuti" style="text-align: center; vertical-align:middle; margin-all: 2px; transform: scale(1.4);" value="True">
                <?php endif;?>
                <label class="px-1" for="cut_cuti">Potong Jatah Cuti</label>
              </div>
            </div>

            <div class="col-md">
              <div class="col">
                <label class="pl-1">Approval</label>
              </div>
              <div class="card" id="card1">
                <div class="card-body">
                <div class="row">
                      <div class="col-10">
                        <div class="col">
                        <label>Approval 1</label>
                        </div>
                        <select id="add1" name="approval_1" class="form-control js-single">
                          <?php if(null === $data_cuti->reviewers[0]){?>
                            <?php if($app_array[0] == 1){ ?>
                              <option value="0" selected>Atasan Pertama</option>
                              <option value="1">Atasan Kedua</option>
                              <option value="2">Atasan Ketiga</option>
                            <?php }else if($app_array[0] == 2){?>
                              <option value="0">Atasan Pertama</option>
                              <option value="1" selected>Atasan Kedua</option>
                              <option value="2">Atasan Ketiga</option>
                            <?php }else if($app_array[0] == 3){?>
                              <option value="0">Atasan Pertama</option>
                              <option value="1">Atasan Kedua</option>
                              <option value="2" selected>Atasan Ketiga</option>
                            <?php }else{?>
                              <option value="0">Atasan Pertama</option>
                              <option value="1">Atasan Kedua</option>
                              <option value="2">Atasan Ketiga</option>
                            <?php };?>
                        <?php }else{?>
                          <option value="0">Atasan Pertama</option>
                          <option value="1">Atasan Kedua</option>
                          <option value="2">Atasan Ketiga</option>
                        <?php };?>
                        <?php foreach($karyawan as $data):
                          if(isset($data->name) && $data->name != " " && $data->employee_code != " "):?>
                          <?php if($data->employee_code == $data_cuti->reviewers[0]): ?>
                            <option value="<?php echo $data->employee_code; ?>" selected><?php echo $data->name; ?> (<?php echo $data->unit_kerja; ?>)</option>
                          <?php else:?>
                            <option value="<?php echo $data->employee_code; ?>"><?php echo $data->name; ?> (<?php echo $data->unit_kerja; ?>)</option>
                          <?php endif;?>
                          <?php 
                          endif;
                          endforeach; ?>
                        </select>
                      </div>
                      <div id="button1" class="col-2 span2 px-3" style="display:flex;align-items:center;justify-content:center;">
                      <a class="btn btn-success py-1 px-2" onClick="Card2()"><i class="fa fa-solid fa-plus"></i></a>
                      </div>
                    </div>
                </div>
              </div>

              <div class="card" id="card2" style="display:none">
                <div class="card-body">
                <div class="row">
                      <div class="col-10">
                        <div class="col">
                        <label>Approval 2</label>
                        </div>
                        <select id="add2" name="approval_2" class="form-control js-single">
                        <?php if(null === $data_cuti->reviewers[1]){?>
                            <?php if($app_array[1] == 1){ ?>
                              <option value="0" selected>Atasan Pertama</option>
                              <option value="1">Atasan Kedua</option>
                              <option value="2">Atasan Ketiga</option>
                            <?php }else if($app_array[1] == 2){?>
                              <option value="0">Atasan Pertama</option>
                              <option value="1" selected>Atasan Kedua</option>
                              <option value="2">Atasan Ketiga</option>
                            <?php }else if($app_array[1] == 3){?>
                              <option value="0">Atasan Pertama</option>
                              <option value="1">Atasan Kedua</option>
                              <option value="2" selected>Atasan Ketiga</option>
                            <?php }else{?>
                              <option value="0">Atasan Pertama</option>
                              <option value="1">Atasan Kedua</option>
                              <option value="2">Atasan Ketiga</option>
                            <?php };?>
                        <?php }else{?>
                          <option value="0">Atasan Pertama</option>
                          <option value="1">Atasan Kedua</option>
                          <option value="2">Atasan Ketiga</option>
                        <?php };?>
                        <?php foreach($karyawan as $data):
                          if(isset($data->name) && $data->name != " " && $data->employee_code != " "):?>
                          <?php if($data->employee_code == $data_cuti->reviewers[1]): ?>
                            <option value="<?php echo $data->employee_code; ?>" selected><?php echo $data->name; ?> (<?php echo $data->unit_kerja; ?>)</option>
                          <?php else:?>
                            <option value="<?php echo $data->employee_code; ?>"><?php echo $data->name; ?> (<?php echo $data->unit_kerja; ?>)</option>
                          <?php endif;?>
                          <?php 
                          endif;
                          endforeach; ?>
                        </select>
                      </div>
                      <div id="button2" class="col-2 span2" style="display:flex;align-items:center;justify-content:center;">
                      <div class="row">
                        <div class="col-lg-6 py-1 px-2">
                        <a class="btn btn-success py-1 px-2" onClick="Card3()"><i class="fa fa-solid fa-plus"></i></a>
                        </div>
                        <div class="col-lg-6 py-1 px-2">
                        <a class="btn btn-danger py-1 px-2" onClick="Card1()"><i class="fa fa-solid fa-trash"></i></a>
                        </div>
                        </div>
                      </div>
                    </div>
                </div>
              </div>

              <div class="card" id="card3" style="display:none">
                <div class="card-body">
                <div class="row">
                      <div class="col-10">
                        <div class="col">
                        <label>Approval 3</label>
                        </div>
                        <select id="add3" name="approval_3" class="form-control js-single">
                        <?php if(null === $data_cuti->reviewers[2]){?>
                            <?php if($app_array[2] == 1){ ?>
                              <option value="0" selected>Atasan Pertama</option>
                              <option value="1">Atasan Kedua</option>
                              <option value="2">Atasan Ketiga</option>
                            <?php }else if($app_array[2] == 2){?>
                              <option value="0">Atasan Pertama</option>
                              <option value="1" selected>Atasan Kedua</option>
                              <option value="2">Atasan Ketiga</option>
                            <?php }else if($app_array[2] == 3){?>
                              <option value="0">Atasan Pertama</option>
                              <option value="1">Atasan Kedua</option>
                              <option value="2" selected>Atasan Ketiga</option>
                            <?php }else{?>
                              <option value="0">Atasan Pertama</option>
                              <option value="1">Atasan Kedua</option>
                              <option value="2">Atasan Ketiga</option>
                            <?php };?>
                        <?php }else{?>
                          <option value="0">Atasan Pertama</option>
                          <option value="1">Atasan Kedua</option>
                          <option value="2">Atasan Ketiga</option>
                        <?php };?>
                        <?php foreach($karyawan as $data):
                          if(isset($data->name) && $data->name != " " && $data->employee_code != " "):?>
                          <?php if($data->employee_code == $data_cuti->reviewers[2]): ?>
                            <option value="<?php echo $data->employee_code; ?>" selected><?php echo $data->name; ?> (<?php echo $data->unit_kerja; ?>)</option>
                          <?php else:?>
                            <option value="<?php echo $data->employee_code; ?>"><?php echo $data->name; ?> (<?php echo $data->unit_kerja; ?>)</option>
                          <?php endif;?>
                          <?php 
                          endif;
                          endforeach; ?>
                        </select>
                      </div>
                      <div id="button3" class="col-2 span2 px-3" style="display:flex;align-items:center;justify-content:center;">
                      <a class="btn btn-danger py-1 px-2" onClick="Card2()"><i class="fa fa-solid fa-trash"></i></a>
                      </div>
                    </div>
                </div>
              </div>
            </div>
            </div>
            <input id="jumlah_approval" class="form-control" name="jumlah_approval" value="" style="display:none">
          <div class="col text-center p-3">
            <button type="submit" class="btn btn-success">Update Data</button>
          </div>
          </div>
        </div>
      </div>
        <?php echo form_close(); ?>
    </div> 
    <!-- /.row -->
  </div><!-- /.container-fluid -->
</div>
</section>
<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>

<!-- /.select2 custom javasript -->
<script>
  function Card1() {
    document.getElementById('jumlah_approval').value = 1;
    var c2 = document.getElementById("card2");
    var c3 = document.getElementById("card3");
    var b1 = document.getElementById("button1");
    b1.style.display = "flex";
    c2.style.display = "none";
    c3.style.display = "none";
  }
  function Card2() {
    document.getElementById('jumlah_approval').value = 2;
    var c2 = document.getElementById("card2");
    var c3 = document.getElementById("card3");
    var b1 = document.getElementById("button1");
    var b2 = document.getElementById("button2");
    b1.style.display = "none";
    b2.style.display = "flex";
    c2.style.display = "block";
    c3.style.display = "none";
  }
  function Card3() {
    document.getElementById('jumlah_approval').value = 3;
    var c3 = document.getElementById("card3");
    var b2 = document.getElementById("button2");
    b2.style.display = "none";
    c3.style.display = "block";
  }

$(document).ready(function() {
    document.getElementById('jumlah_approval').value = <?php echo sizeof($data_cuti->reviewers); ?>;
    if(<?php echo sizeof($data_cuti->reviewers); ?> == 2){
      Card2();
    }else if(<?php echo sizeof($data_cuti->reviewers); ?> == 3){
      Card2();
      Card3();
    }

    $('.js-single').select2({
      placeholder: "Select position/name",
      width: '100%'
  });
});
</script>