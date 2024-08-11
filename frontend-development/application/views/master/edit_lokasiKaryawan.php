<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Edit Data Lokasi Absensi Karyawan</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('master/lokasi'); ?>">Master Data</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('lokasi/lihatlokasi'); ?>">Lokasi Karyawan</a></li>
          <li class="breadcrumb-item active">Edit Lokasi Absensi Karyawan</li>
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
        
        <?php echo form_open('lokasi/updateKaryawan/'.$EMPCODE); ?>
        <div class="card-body">
        <div class="d-flex justify-content-center">
            <div class="card w-50 p-1" style="height: 18rem;">
              <div class="card-body" id="map">
              </div>
              <script>
                    function initMap() {
                      var map = new google.maps.Map(document.getElementById("map"), {
                            center: { lat: -3.1750, lng: 119.8283},
                            zoom: 4,
                            scrollwheel: true,
                        });

                        $('#lok').change(function() {
                                      <?php 
                        if(isset($data_lokasi)):
                        foreach($data_lokasi as $data):              
                        ?>
                          var lokid = parseFloat(document.getElementById("lok").value);

                          if(<?php echo $data->id;?> == lokid){
                          var map = new google.maps.Map(document.getElementById("map"), {
                            center: {lat: <?php echo $data->latitude;?>, lng: <?php echo $data->longitude; ?>},
                            zoom: 17,
                            scrollwheel: true,
                        });

                        var circle;
                        var marker = new google.maps.Marker({
                          map: map,
                          position: map.getCenter(),
                          title: "name"
                        });

                        circle = new google.maps.Circle({
                              map: map,
                              radius: <?php echo $data->radius; ?>,
                              fillColor: '#555',
                              strokeColor: '#ffffff',
                              strokeOpacity: 0.1,
                              strokeWeight: 3
                            });
                        circle.bindTo('center', marker, 'position');
                          }
                          <?php 
                          endforeach; 
                          endif;
                          ?>
                      });
                    }
                </script>
                <script async
                  src="https://maps.googleapis.com/maps/api/js?key=&loading=async&callback=initMap">
                </script>
            </div>
            </div>
          <div class="form-group px-3">
            <label>Nama Lokasi</label>
            <?php echo form_error('id_lokasi','<div class="alert alert-warning">','</div>'); ?>
            <select id="lok" name="id_lokasi" class="form-control" aria-label="Default select example" required placeholder="ID Lokasi">
              <option value="" selected>Open this select menu</option>
                <?php 
            if(isset($data_lokasi)):
            foreach($data_lokasi as $data):                 
            ?>
              <option value=<?php echo $data->id; ?>><?php echo $data->lokasi_absen; ?></option>
              <?php 
          endforeach; 
          endif;
          ?>
            </select>
          </div>
          <div class="col text-center p-3">
            <button type="submit" class="btn btn-success">Update Data</button>
          </div>
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