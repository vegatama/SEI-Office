<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Pengajuan Cuti & Izin</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Pengajuan Cuti & Izin</li>
        </ol>
      </div><!-- /.col -->
    </div><!-- /.row -->
  </div><!-- /.container-fluid -->
</div>
<!-- /.content-header -->

<!-- info izin dan sisa cuti -->
<section class="content">
        <div class="container-fluid">
          <div class="row mb-2">
              <div class="col-md-6">
                  <div class="info-box">
                      <span class="info-box-icon bg-info">
                          <i class="fas fa-calendar"></i>
                      </span>
                      <div class="info-box-content">
                          <span class="info-box-text">Izin Dipakai</span>
                          <span class="info-box-number"><?php echo $dashboard->akumulasiIzin; ?> Menit</span>
                      </div>
                  </div>
              </div>
              <div class="col-md-6">
                  <div class="info-box">
                      <span class="info-box-icon bg-success">
                          <i class="fas fa-calendar-check"></i>
                      </span>
                      <div class="info-box-content">
                          <span class="info-box-text">Sisa Cuti</span>
                          <span class="info-box-number"><?php echo $dashboard->sisaCuti; ?> Hari</span>
                      </div>
                  </div> 
                </div>
                <!-- /.col -->
            </div>
            <!-- /.row -->
        </div>
        <!-- /.container-fluid -->
    </section>

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
        <?php echo form_open_multipart('Izincuti/ajukan');?>
        <div class="card-body">
          <div class="row p-3">
            <div class="col-md">
              <div class="form-group pr-1">
                <label>Jenis Cuti</label>
                <?php echo form_error('jenis_cuti','<div class="alert alert-warning">','</div>'); ?>
                <select id="jenis_cuti" name="jenis_cuti" class="form-control js-single" aria-label="Default select example" required placeholder="Jenis Cuti" <?php echo $this->session->flashdata('jenis_cuti'); ?>>
                <option value=""></option>
                <?php foreach($data_cuti as $data):?>
                  <option value="<?php echo $data->id; ?>" <?php echo ($this->session->flashdata('jenis_cuti') == $data->id) ? 'selected' : ''; ?>>
                    <?php echo $data->namaJenis; ?>
                  </option>
                <?php endforeach; ?>
                </select>
              </div>
              <div class="form-group pr-1">
                <label>Jenis Pengajuan</label>
                <input id="jenis_pengajuan" name="jenis_pengajuan" class="form-control" type="text" value="<?php echo $this->session->flashdata('jenis_pengajuan'); ?>" aria-label="readonly input example" readonly >
              </div>
              <div class="form-group pr-1">
                <label>Date & Time</label>
                <div class="row px-2" id="dateharian" name="dateharian">
                  <div class="col-1 px-0">
                  <div class="form-control" style="display:flex;align-items:center;justify-content:center;">
                    <i class="fa fa-calendar"></i>
                  </div>
                  </div>
                  <div class="col-11 px-0">
                    <input class="form-control daterange" type="text" id="daterange" name="daterange" value="<?php echo $this->session->flashdata('daterange'); ?>"/>
                  </div>
                </div>
                <div class="row px-2" id="datemenit" name="datemenit" style="display:none">
                  <div class="col-1 px-0">
                  <div class="form-control" style="display:flex;align-items:center;justify-content:center;">
                    <i class="fa fa-calendar"></i>
                  </div>
                  </div>
                  <div class="col-5 pl-0 pr-2">
                    <input class="form-control datesingle" type="text" id="datesingle" name="datesingle" value="<?php echo $this->session->flashdata('datesingle'); ?>"/>
                  </div>
                  <div class="col-1 px-0">
                  <div class="form-control" style="display:flex;align-items:center;justify-content:center;">
                    <i class="fa fa-clock"></i>
                  </div>
                  </div>
                  <div class="col-5 px-0">
                    <input class="form-control timerange" type="text" id="timerange" name="timerange" value="<?php echo $this->session->flashdata('timerange'); ?>"/>
                  </div>
                </div>
              </div>   
            </div>
            <div class="col-md">
            <div class="form-group pr-1">
                <label class="pl-1">Keterangan</label>
                <?php echo form_error('keterangan','<div class="alert alert-warning">','</div>'); ?>
                <textarea class="form-control" id="keterangan" name="keterangan" rows="3" required placeholder="Keterangan"> <?php echo $this->session->flashdata('keterangan'); ?> </textarea>
              </div>
              <div class="form-group pr-1">
                <label class="pl-1">Upload File (optional Max. 3)</label>
                <div class="card">
                  <div class="card-body">
                    <div class="custom-file">
                    <input type="file" class="custom-file-input" id="customFile" name="customFile[]" multiple="multiple">
                    <label class="custom-file-label" for="customFile">Choose file</label>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            </div>
            <input id="jumlah_approval" class="form-control" name="jumlah_approval" value="" style="display:none">
          <div class="col text-center p-3">
            <button type="submit" class="btn btn-success">Ajukan</button>
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

<script>
  $(document).ready(function() {
    $('.js-single').select2({
      placeholder: "Select cuti & izin",
      width: '100%'
    });
    $('.daterange').daterangepicker({
      locale: {
                format: 'YYYY-MM-DD'
            }
    });
    $('.datesingle').daterangepicker({
      singleDatePicker: true,
      showDropdowns: true,
      minYear: parseInt(moment().format('YYYY'),10),
      maxYear: parseInt(moment().format('YYYY'),10)+1,
      locale: {
                format: 'YYYY-MM-DD'
            }
    });
    $('.timerange').daterangepicker({
            timePicker: true,
            timePicker24Hour: true,
            timePickerIncrement: 1,
            locale: {
                format: 'HH:mm'
            }
        }).on('show.daterangepicker', function (ev, picker) {
            picker.container.find(".calendar-table").hide();
    });
    $(".custom-file-input").on("change", function() {
      if(this.files[0].size > 2097152) {
        alert("File is too big!");
        this.value = "";
      }
      var files = Array.from(this.files)
      var fileName = files.map(f =>{return f.name}).join(", ")
      $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    });
  });

  $('#jenis_cuti').change(function() {
    <?php 
      if(isset($data_cuti)):
      foreach($data_cuti as $data):              
      ?>
    var id_cuti = parseInt(document.getElementById("jenis_cuti").value);
    if(<?php echo $data->id;?> == id_cuti){
      document.getElementById('jenis_pengajuan').value = "<?php echo $data->pengajuan; ?>";

      var dh = document.getElementById("dateharian");
      var dm = document.getElementById("datemenit");

      if("<?php echo $data->pengajuan;?>" == "HARIAN"){
        dh.style.display = "flex";
        dm.style.display = "none";
      }else{
        dh.style.display = "none";
        dm.style.display = "flex";
      };
      
    }
    <?php 
      endforeach; 
      endif;
      ?>
  });
</script>