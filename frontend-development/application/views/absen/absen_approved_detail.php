<?php $this->load->view('header'); ?>

<div class="content-wrapper">
  <!-- Content Header (Page header) -->
  <div class="content-header">
    <div class="container-fluid">
      <div class="row mb-2">
        <div class="col-sm-6">
          <h1 class="m-0">Absen Approved</h1>
        </div><!-- /.col -->
        <div class="col-sm-6">
          <ol class="breadcrumb float-sm-right">
            <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
            <li class="breadcrumb-item active">Absen Approved</li>
            <li class="breadcrumb-item active">Approved Data</li>
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
            <!-- <div class="card-header">
          <h3 class="card-title">Approved Data : <?php echo $bulan . " " . $tahun; ?></h3>
        </div> -->
            <!-- /.card-header -->
            <div class="card-body">

              <?php echo form_open('absen/mentah'); ?>
              <div class="container">
                <div class="row mt-5">
                
                  <!-- DB laravel Untuk Detail Data -->
                 <div class="col-md-6">
                <h3>Detail Absen</h3>
                <p><strong>Nama: </strong> <?php echo $employeeName; ?></p>
                <p><strong>Divisi: </strong> <?php echo $employeeJobTitle; ?></p>
                <p><strong>Lokasi Absen: </strong> <?php echo $longitude;
                echo $latitude; ?></p>
                <p><strong>Jarak: </strong> <?php echo $radius; ?></p>
                <p><strong>Alasan: </strong> <?php echo $reason; ?></p>
                 </div>


                  <div class="col-md-6">
                    <h3>Maps</h3>
                    <div id="map" style="height: 400px;"></div>
                  </div>
                </div>
                <div class="row mt-3">
                  <div class="col-md-12 text-center">
                    <br><br>
                    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#gambarModal">
                      Tampilkan Gambar
                    </button>
    
                     <!-- Pop Up menggunakan DB  -->
                  
                  <div class="modal fade" id="gambarModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                   <div class="modal-dialog modal-dialog-centered" role="document">
                    <div class="modal-content">
                     <div class="modal-body">
                <img src="<?php echo $urlGambar; ?>" class="img-fluid imageApproval">
                  </div>
                 </div>
                </div>
               </div>
                 

                  </div>
                </div>
                <div class="row mt-3">
                  <div class="col-md-4 offset-md-4 text-center d-flex justify-content-between">
                    <div class="container mt-5">
                      <div class="container mt-5">
                        <button type="button" class="btn btn-success" data-toggle="modal" data-target="#approvedModal">
                          Approved
                        </button>

                        <button type="button" class="btn btn-danger" data-toggle="modal"
                          data-target="#notApprovedModal">
                          Not Approved
                        </button>
                      </div>

                      <!-- Approved Modal -->
                      <div class="modal fade" id="approvedModal" tabindex="-1" role="dialog"
                        aria-labelledby="approvedModalLabel" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                          <div class="modal-content">
                            <div class="modal-header">
                              <h5 class="modal-title" id="approvedModalLabel">Approved</h5>
                              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                              </button>
                            </div>
                            <div class="modal-body">
                              Data Approved. Redirecting...
                            </div>
                          </div>
                        </div>
                      </div>

                      <!-- Not Approved Modal -->
                      <div class="modal fade" id="notApprovedModal" tabindex="-1" role="dialog"
                        aria-labelledby="notApprovedModalLabel" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                          <div class="modal-content">
                            <div class="modal-header">
                              <h5 class="modal-title" id="notApprovedModalLabel">Not Approved</h5>
                              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                              </button>
                            </div>
                            <div class="modal-body">
                              Data Not Approved. Redirecting...
                            </div>
                          </div>
                        </div>
                      </div>

                    </div>
                  </div>
                </div>

                <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
                <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

                <!-- Load Google Maps API -->
                <!-- Load Google Maps API -->
                <script
                  src="https://maps.googleapis.com/maps/api/js?key=&callback=initMap"
                  async defer></script>
                <script>
                  // Initialize and add the map
                  function initMap() {
                    // The location coordinates (latitude and longitude)
                    var locationCoordinates = { lat: -6.94756144265983, lng: 107.60087363098609 };

                    // The map, centered at the location coordinates
                    var map = new google.maps.Map(
                      document.getElementById('map'), { zoom: 15, center: locationCoordinates });

                    // The marker, positioned at the location coordinates
                    var marker = new google.maps.Marker({ position: locationCoordinates, map: map });
                  }
                  // Redirect to Approved data page when Approved modal is shown
                  // Redirect to Approved data page when Approved modal is shown
                  $('#approvedModal').on('shown.bs.modal', function () {
                    setTimeout(function () {
                      window.location.href = 'http://localhost:8081/absen/approved';
                    }, 2000); // 2000 milliseconds = 2 seconds
                  });

                  // Redirect to homepage when Not Approved modal is shown
                  $('#notApprovedModal').on('shown.bs.modal', function () {
                    setTimeout(function () {
                      window.location.href = 'http://localhost:8081/absen/approved';
                    }, 2000); // 2000 milliseconds = 2 seconds
                  });
                </script>

                <?php echo form_close(); ?><br />


              </div>
            </div>
            <!-- /.row -->
          </div><!-- /.container-fluid -->
        </div>
      </div>
      <!-- /.content -->
  </section>
</div>
<!-- /.content-wrapper -->



<?php $this->load->view('footer'); ?>