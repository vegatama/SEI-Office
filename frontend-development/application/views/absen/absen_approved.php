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
          <div class="card card-primary">
            <div class="card-header">
              <h3 class="card-title">Menunggu Persetujuan
                  <?php
                    if ($hari == 0) {
                        echo date('F', mktime(0, 0, 0, $bulan, 1)) . " " . $tahun;
                    } else {
                        echo $hari . " " . date('F', mktime(0, 0, 0, $bulan, 1)) . " " . $tahun;
                    }
                  ?>
              </h3>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
              <?php echo form_open('absen/approved', array('method' => 'get')); ?>
              <div class="form-group">
                <div class="row">
                  <div class="col-md-2">
                    <div class="form-group">
                      <label>Tanggal</label>
                      <select class="form-control select2" name="day" style="width: 100%;">
                        <option value="0">Semua Tanggal</option>
                        <?php $tgl = $hari;
                        for ($t = 1; $t <= 31; ++$t): ?>
                          <option value="<?php echo $t; ?>" <?php if ($tgl == $t)
                               echo "selected"; ?>>
                            <?php echo $t; ?>
                          </option>
                        <?php endfor; ?>
                      </select>
                    </div>
                  </div>

                  <div class="col-md-2">
                    <div class="form-group">
                      <label>Bulan</label>
                      <select class="form-control select2" name="bulan" style="width: 100%;">
                        <?php $bln = $bulan;
                        for ($m = 1; $m <= 12; ++$m): ?>
                          <option value="<?php echo $m; ?>" <?php if ($bln == $m)
                               echo "selected"; ?>>
                            <?php echo date('F', mktime(0, 0, 0, $m, 1)); ?>
                          </option>
                        <?php endfor; ?>
                      </select>
                    </div>
                  </div>

                  <div class="col-md-2">
                    <div class="form-group">
                      <label>Tahun</label>
                      <select class="form-control select2" name="tahun" style="width: 100%;">
                        <?php for ($i = -1; $i <= 1; $i++):
                          $thn = $tahun + $i; ?>
                          <option value="<?php echo $thn; ?>" <?php if ($tahun == $thn)
                               echo "selected"; ?>>
                            <?php echo $thn; ?>
                          </option>
                        <?php endfor; ?>
                      </select>
                    </div>
                  </div>
                </div>
                <button class="btn btn-primary" type="submit" name="tampil">Tampilkan Data</button>
              </div>
              <?php echo form_close(); ?><br />

              <table id="statusdesc" class="table table-bordered table-striped">
                <thead>
                  <tr>
                    <th>Nama</th>
                    <th>Divisi</th>
                    <th>Lokasi Absen</th>
                    <th>Jarak Jauh</th>
				  	<th>Tipe</th>
					  <th>Status</th>
                    <th>Action Information</th>
                  </tr>
                </thead>
                <tbody>
                  <!-- DAtabase List tabel Approval -->
                  <?php
                  if (isset($absensi)):
                    foreach ($absensi->requests as $data):
                  if ($data->approved == 0):
                      ?>
                      <tr>
                        <td>
                          <?php echo $data->employeeName; ?>
                        </td>
                        <td>
                          <?php echo $data->employeeJobTitle;?>
                        </td>
                        <td>
                          <?php echo $data->lokasiAbsen; ?>
                        </td>
                        <td>
                            <?php
                            // distance is in meter, convert to KM and then round to 2 decimal places
                            if ($data->distance >= 1000) {
                                echo round($data->distance / 1000, 2) . ' KM';
                            } else {
                                echo $data->distance . ' Meter';
                            }
                            ?>
                        </td>
					  <td>
						  <?php
						  if ($data->checkOut) {
							  echo 'Check Out';
						  } else {
							  echo 'Check In';
						  }
						  ?>
					  </td>
						  <td>
<?php
							  if ($data->approved == 0) {
								  echo 'Menunggu Persetujuan';
							  } else if ($data->approved == 1) {
								  echo 'Disetujui';
							  } else {
								  echo 'Ditolak: ' . $data->rejectReason;
							  }
							  ?>
						  </td>
                        <td>
                          <!-- tambahbutton button detail, approved, reject -->
<!--                          <a href="--><?php //echo site_url('absen/view_attendance/' . $data->id); ?><!--" class="btn btn-primary">Detail</a>-->
							<button type="button"
									class="btn btn-primary"
									data-toggle="modal"
									data-target="#attendanceDetail"
									data-id="<?php echo $data->id; ?>"
									data-employee-id="<?php echo $data->employeeId; ?>"
									data-employee-code="<?php echo $data->employeeCode; ?>"
									data-employee-name="<?php echo $data->employeeName; ?>"
									data-employee-job-title="<?php echo $data->employeeJobTitle; ?>"
									data-reason="<?php echo $data->reason; ?>"
									data-longitude="<?php echo $data->longitude; ?>"
									data-latitude="<?php echo $data->latitude; ?>"
									data-center-longitude="<?php echo $data->centerLongitude; ?>"
									data-center-latitude="<?php echo $data->centerLatitude; ?>"
									data-radius="<?php echo $data->radius; ?>"
									data-date="<?php echo $data->date; ?>"
									data-time="<?php echo $data->time; ?>"
									data-target-time="<?php echo $data->targetTime; ?>"
									data-check-out="<?php echo $data->checkOut; ?>"
									data-image-url="<?php echo $data->imageUrl; ?>"
									data-lokasi-absen="<?php echo $data->lokasiAbsen; ?>"
									data-status="<?php echo $data->approved; ?>"
									data-distance="<?php echo $data->distance; ?>"
							>Detail</button>
                          <?php if ($data->approved == 0): ?>
<!--                          <a href="--><?php //echo site_url('absen/acceptapproval/' . $data->id); ?><!--" class="btn btn-success">Approve</a>-->
<!--                          <a href="--><?php //echo site_url('absen/rejectapproval/' . $data->id); ?><!--" class="btn btn-danger">Reject</a> -->
						  <button type="button" class="btn btn-success" onclick="showAcceptConfirmationModal(<?php echo $data->id; ?>)">Approve</button>
						  <button type="button" class="btn btn-danger" onclick="showRejectReasonModal(<?php echo $data->id; ?>)">Reject</button>
                          <?php endif; ?>
                        </td>
                      <?php endif;
                      endforeach; ?>
                    <?php endif; ?>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <!-- /.row -->
        </div><!-- /.container-fluid -->
      </div>
    </div>

	  <script>
		  $(document).ready(function() {
              let query = new URLSearchParams(window.location.search);
              let detailId = query.get('detail');
              // if detailId is set, show the detail modal
			  if (detailId) {
				  // TODO: show detail modal
			  }

			  $('#attendanceDetail').on('show.bs.modal', function(event) {
				  let button = $(event.relatedTarget);
				  let attendance = {
					  id: button.data('id'),
					  employeeId: button.data('employee-id'),
					  employeeCode: button.data('employee-code'),
					  employeeName: button.data('employee-name'),
					  employeeJobTitle: button.data('employee-job-title'),
					  reason: button.data('reason'),
					  longitude: button.data('longitude'),
					  latitude: button.data('latitude'),
					  centerLongitude: button.data('center-longitude'),
					  centerLatitude: button.data('center-latitude'),
					  radius: button.data('radius'),
					  date: button.data('date'),
					  time: button.data('time'),
					  targetTime: button.data('target-time'),
					  checkOut: button.data('check-out'),
					  imageUrl: button.data('image-url'),
					  lokasiAbsen: button.data('lokasi-absen'),
					  status: button.data('status'),
					  distance: button.data('distance')
				  };
				  showDetail(attendance);
			  })
		  } );
		  function showDetail(attendanceData) {
			  let modal = $('#attendanceDetail');
			  if (attendanceData['status'] === -1) {
				  attendanceData['statusString'] = 'Ditolak';
			  } else if (attendanceData['status'] === 0) {
				  attendanceData['statusString'] = 'Menunggu Persetujuan';
			  } else if (attendanceData['status'] === 1) {
				  attendanceData['statusString'] = 'Disetujui';
			  }
			  let distance = attendanceData['distance']; // is in meter
			  // format distance in KM
			  if (distance >= 1000) {
				  attendanceData['distance'] = (distance / 1000).toFixed(2) + ' KM';
			  } else {
				  attendanceData['distance'] = distance + ' Meter';
			  }
			  let jamAbsen = attendanceData['time'].split('.')[0].split(':').slice(0, 2).join(':');
			  attendanceData['time'] = jamAbsen;
			  modal.find('.modal-body .attendance-detail').each(function() {
				  let element = $(this);
				  // console.log(element.tagName);
				  // if (element.tagName === 'img') {
					//   element.attr('src', attendanceData[element.attr('name')]);
				  // } else if (element.tagName === 'input') {
					//   element.val(attendanceData[element.attr('name')]);
				  // }
				  if (element.is('img')) {
					  element.attr('src', attendanceData[element.attr('name')]);
				  } else if (element.is('input')) {
					  element.val(attendanceData[element.attr('name')]);
				  }
			  });
			  // button
			  let accButton = modal.find('#acc-button');
			  let rejButton = modal.find('#rej-button');
			  if (attendanceData['status'] === 0) {
				  accButton.show();
				  rejButton.show();
			  } else {
				  accButton.hide();
				  rejButton.hide();
			  }
			  // set event listener
			  accButton.off('click').on('click', function() {
				  showAcceptConfirmationModal(attendanceData['id']);
			  });
			  rejButton.off('click').on('click', function() {
				  showRejectReasonModal(attendanceData['id']);
			  });
			  setTimeout(() => {
				  initMap(attendanceData.longitude, attendanceData.latitude, attendanceData.centerLongitude, attendanceData.centerLatitude, attendanceData.radius);
			  }, 500);
		  }
		  let map;
		  let employeeMarker;
		  let centerMarker;
		  let circle;
		  let line;
		  function initMap(employeeLongitude, employeeLatitude, centerLongitude, centerLatitude, radius) {
			  console.log(employeeLongitude, employeeLatitude, centerLongitude, centerLatitude, radius);
			  if (!map) {
				  map = L.map('map').setView([centerLatitude, centerLongitude], 13);
				  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
					  maxZoom: 19,
				  }).addTo(map);
				  map.setMinZoom(10);
				  map.setMaxZoom(19);
				  employeeMarker = L.marker([employeeLatitude, employeeLongitude]).addTo(map);
				  centerMarker = L.marker([centerLatitude, centerLongitude]).addTo(map);
				  circle = L.circle([centerLatitude, centerLongitude], {
					  color: 'blue',
					  fillColor: '#3186cc',
					  fillOpacity: 0.5,
					  radius: radius
				  }).addTo(map);
				  line = L.polyline([
					  [employeeLatitude, employeeLongitude],
					  [centerLatitude, centerLongitude]
				  ], {color: 'red'}).addTo(map);
			  } else {
				  // map.fitWorld();
				  employeeMarker.setLatLng([employeeLatitude, employeeLongitude]);
				  centerMarker.setLatLng([centerLatitude, centerLongitude]);
				  circle.setLatLng([centerLatitude, centerLongitude]);
				  circle.setRadius(radius);
				  line.setLatLngs([
					  [employeeLatitude, employeeLongitude],
					  [centerLatitude, centerLongitude]
				  ]);
			  }
			  // fit to bounds
			  let corner1 = L.latLng(employeeLatitude, employeeLongitude);
			  let corner2 = L.latLng(centerLatitude, centerLongitude);
			  let bounds = L.latLngBounds(corner1, corner2);
			  // add padding
			  bounds = bounds.pad(0.1);
			  map.fitBounds(bounds);
			  map.invalidateSize();
		  }
		  function showRejectReasonModal(attendanceId) {
			  $('#rejectReason #attendanceId').val(attendanceId);
			  $('#rejectReason').modal('show');
		  }
		  function showAcceptConfirmationModal(attendanceId) {
			  $('#acceptConfirmation #attendanceId').val(attendanceId);
			  $('#acceptConfirmation').modal('show');
		  }
	  </script>
    <!-- /.content -->
<!--	  MODAL FOR DETAIL-->
	  <div class="modal fade" id="attendanceDetail" tabindex="-1" role="dialog" aria-labelledby="attendanceDetailLabel" aria-hidden="true">
		  <div class="modal-dialog" role="document">
			  <div class="modal-content" style="width: 800px">
				  <div class="modal-header">
					  <h5 class="modal-title" id="attendanceDetailLabel">Detail Absen</h5>
					  <button type="button" class="close" data-dismiss="modal" aria-label="Close">
						  <span aria-hidden="true">&times;</span>
					  </button>
				  </div>
				  <div class="modal-body">
					  <div class="row">
						  <div class="col-md">
							  <div class="form-group">
								  <label>Nama</label>
								  <input class="form-control attendance-detail" value="" readonly="readonly" name="employeeName">
							  </div>
							  <div class="form-group">
								  <label>Job Title</label>
								  <input class="form-control attendance-detail" value="" readonly="readonly" name="employeeJobTitle">
							  </div>
							  <div class="form-group">
								  <label>Keterangan</label>
								  <input class="form-control attendance-detail" value="" readonly="readonly" name="reason">
							  </div>
							  <div class="form-group">
								  <label>Status</label>
								  <input class="form-control attendance-detail" value="" readonly="readonly" name="statusString">
							  </div>
							  <div class="form-group">
								  <label>Lokasi Absen</label>
								  <input class="form-control attendance-detail" value="" readonly="readonly" name="lokasiAbsen">
							  </div>
							  <div class="form-group">
								  <label>Bukti Foto</label> <br>
								  <img src="" style="width: 300px" alt="Uploaded Photo" class="attendance-detail" name="imageUrl">
							  </div>
							  <div class="form-group">
								  <label>Tanggal</label>
								  <input class="form-control attendance-detail" value="" readonly="readonly" name="date">
							  </div>
							  <div class="form-group">
								  <label>Jam Absen</label>
								  <input class="form-control attendance-detail" value="" readonly="readonly" name="time">
							  </div>
							  <div class="form-group">
								  <label>Jarak</label>
								  <input class="form-control attendance-detail" value="" readonly="readonly" name="distance">
							  </div>
<!--							  SHOW GOOGLE MAP-->
							  <div class="form-group">
								  <h3>Maps</h3>
								  <div id="map" style="height: 600px; width: 100%;">
								  </div>
							  </div>
						  </div>
					  </div>
				  </div>
				  <div class="modal-footer">
<!--					  Accept and Reject button-->
					<button id="acc-button" type="button" class="btn btn-success" data-dismiss="modal">Accept</button>
<!--					<button id="rej-button" type="button" class="btn btn-danger" data-dismiss="modal">Reject</button>-->
					  <button id="rej-button" type="button" class="btn btn-danger">Reject</button>
					  <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
				  </div>
			  </div>
		  </div>
	  </div>
<!--	  MODAL FOR REJECT REASON INPUT-->
	  <div class="modal fade" id="rejectReason" tabindex="-1" role="dialog" aria-labelledby="rejectReasonLabel" aria-hidden="true">
		  <div class="modal-dialog" role="document">
			  <div class="modal-content">
				  <div class="modal-header">
					  <h5 class="modal-title" id="rejectReasonLabel">Alasan Penolakan</h5>
					  <button type="button" class="close" data-dismiss="modal" aria-label="Close">
						  <span aria-hidden="true">&times;</span>
					  </button>
				  </div>
				  <?php echo form_open('absen/rejectapproval', array('method' => 'post')); ?>
				  <input type="hidden" name="attendanceId" id="attendanceId" value="">
				  <div class="modal-body">
					  <div class="form-group">
						  <label>Alasan Penolakan</label>
						  <textarea class="form-control" name="rejectReason" id="rejectReason" rows="3" required></textarea>
						  <?php echo form_error('rejectReason', '<br/><div class="alert alert-warning">', '</div>'); ?>
					  </div>
				  </div>
				  <div class="modal-footer">
					  <button type="submit" class="btn btn-danger">Tolak</button>
					  <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
				  </div>
				  <?php echo form_close(); ?>
			  </div>
		  </div>
	  </div>
<!--	  MODAL FOR ACCEPT CONFIRMATION-->
	  <div class="modal fade" id="acceptConfirmation" tabindex="-1" role="dialog" aria-labelledby="acceptConfirmationLabel" aria-hidden="true">
		  <div class="modal-dialog" role="document">
			  <div class="modal-content">
				  <?php echo form_open('absen/acceptapproval', array('method' => 'post')); ?>
				  <input type="hidden" name="attendanceId" id="attendanceId" value="">
				  <div class="modal-header">
					  <h5 class="modal-title" id="acceptConfirmationLabel">Konfirmasi Persetujuan</h5>
					  <button type="button" class="close" data-dismiss="modal" aria-label="Close">
						  <span aria-hidden="true">&times;</span>
					  </button>
				  </div>
				  <div class="modal-body">
					  <p>Apakah Anda yakin ingin menyetujui absen ini?</p>
				  </div>
				  <div class="modal-footer">
					  <button type="submit" class="btn btn-success">Ya</button>
					  <button type="button" class="btn btn-secondary" data-dismiss="modal">Tidak</button>
				  </div>
				  <?php echo form_close(); ?>
			  </div>
		  </div>
	  </div>
  </section>
</div>
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"
	  integrity="sha256-p4NxAoJBhIIN+hmNHrzRCf9tD/miZyoHS5obTRR9BMY="
	  crossorigin=""/>
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"
		integrity="sha256-20nQCchB9co0qIjJZRGuk2/Z9VM+kNiyxNV1lvTlZBo="
		crossorigin=""></script>

<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>
