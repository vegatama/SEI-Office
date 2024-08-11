<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Edit Data Lokasi Absensi</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('master/lokasi'); ?>">Master Data</a></li>
          <li class="breadcrumb-item active">Lokasi Absensi</li>
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
        
        <?php echo form_open('lokasi/update/'.$data_lokasi->id); ?>
        <div class="card-body">
          <div class="row p-3">
            <div class="card w-50 p-1">
              <div class="card-body" id="map">
              </div>
              <script>
                    function initMap() {
                        var map;
                        map = new google.maps.Map(document.getElementById("map"), {
                            center: {lat: <?php echo $data_lokasi->latitude;?>, lng: <?php echo $data_lokasi->longitude?>},
                            zoom: 17,
                            scrollwheel: true,
                        });

                        var circle;
                        var marker;
                        marker = new google.maps.Marker({
                          map: map,
                          position: map.getCenter(),
                          title: "name"
                        });

                        circle = new google.maps.Circle({
                              map: map,
                              radius: <?php echo $data_lokasi->radius;?>,
                              fillColor: '#555',
                              strokeColor: '#ffffff',
                              strokeOpacity: 0.1,
                              strokeWeight: 3
                            });
                            circle.bindTo('center', marker, 'position');


                        $('#lng').change(function() {
                          var lat = parseFloat(document.getElementById("lat").value);
                          var lng = parseFloat(document.getElementById("lng").value);
                          map = new google.maps.Map(document.getElementById("map"), {
                            center: {lat: lat, lng: lng},
                            zoom: 17,
                            scrollwheel: true,
                        });
                        marker = new google.maps.Marker({
                          map: map,
                          position: map.getCenter(),
                          title: "name"
                        });
                        circle = new google.maps.Circle({
                              map: map,
                              radius: <?php echo $data_lokasi->radius;?>,
                              fillColor: '#555',
                              strokeColor: '#ffffff',
                              strokeOpacity: 0.1,
                              strokeWeight: 3
                            });
                            circle.bindTo('center', marker, 'position');
                  
                      });
                      // Add circle overlay and bind to marker
                      $('#radius').change(function() {
                          var new_rad = $(this).val();
                          var rad = new_rad * 35;
                          if (!circle || !circle.setRadius) {
                            circle = new google.maps.Circle({
                              map: map,
                              radius: rad,
                              fillColor: '#555',
                              strokeColor: '#ffffff',
                              strokeOpacity: 0.1,
                              strokeWeight: 3
                            });
                            circle.bindTo('center', marker, 'position');
                          } else circle.setRadius(rad);
                        });
                    }
                </script>
                <script async
                  src="https://maps.googleapis.com/maps/api/js?key=&loading=async&callback=initMap">
                </script>
            </div>
            <input type="hidden" class="form-control" name="isDefault" value="<?php echo $data_lokasi->isDefault;?>">
            <div class="col w-50">
              <div class="form-group pl-3">
                <label>Latitude</label>
                <?php echo form_error('latitude','<div class="alert alert-warning">','</div>'); ?>
                <input type="number" step=any name="latitude" id="lat" class="form-control" value="<?php echo $data_lokasi->latitude;?>" required placeholder="Latitude">
              </div>
              <div class="form-group pl-3">
                <label>Longitude</label>
                <?php echo form_error('longitude','<div class="alert alert-warning">','</div>'); ?>
                <input type="number" step=any name="longitude" id="lng" class="form-control" value="<?php echo $data_lokasi->longitude;?>" required placeholder="Longitude">
              </div>
              <div class="form-group pl-3">
                <label>Radius</label>
                  <div class="form-control">
                  <?php echo form_error('radius','<div class="alert alert-warning">','</div>'); ?>
                  <input type="range" name="radius" class="form-range w-100" id="radius" min="1" max="10" value="<?php echo $data_lokasi->radius/35;?>">
                </div>
              </div>
            </div>
          </div>
          <div class="form-group px-3">
            <label>Nama Tempat</label>
            <?php echo form_error('nama_lokasi','<div class="alert alert-warning">','</div>'); ?>
            <input name="nama_lokasi" class="form-control" value="<?php echo $data_lokasi->lokasi_absen;?>" required placeholder="Nama Lokasi">
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