<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Absen Saya</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item active">Absen Saya</li>
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
          <h3 class="card-title">Rekap Absensi</h3>
        </div>
        <!-- /.card-header -->
        <div class="card-body">

          <?php echo form_open('absen/saya', array('method' => 'get')); ?>
          <div class="form-group">
            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label>Bulan</label>
                  <select class="form-control select2" name="bulan" style="width: 100%;">
                    <?php $bln = $bulan; for($m=1; $m<=12; ++$m): ?>
                      <option value="<?php echo $m; ?>" <?php if($bln == $m) echo "selected"; ?>><?php echo date('F', mktime(0, 0, 0, $m, 1)); ?></option>
                    <?php endfor; ?>  
                  </select>
                </div>
              </div>

              <div class="col-md-6">
                <div class="form-group">
                  <label>Tahun</label>
                  <select class="form-control select2" name="tahun" style="width: 100%;">
                    <?php for($i=-1; $i<=1; $i++): $thn = $tahun + $i;?>
                      <option value="<?php echo $thn; ?>" <?php if($tahun == $thn) echo "selected"; ?>><?php echo $thn; ?></option>
                    <?php endfor; ?>
                  </select>
                </div>
              </div>
            </div>
            <button class="btn btn-primary" type="submit" name="tampil">Tampilkan Data</button>
          </div>
          <?php echo form_close(); ?><br/>

			<table id="karyawandesc" class="table table-bordered table-striped">
				<thead>
				<tr>
					<th>Tanggal</th>
					<th>Hari</th>
					<th>Waktu Check In</th>
					<th>Waktu Check Out</th>
					<th>Status</th>
					<th>Action Information</th>
				</tr>
				</thead>
				<tbody>
				<!-- DAtabase List tabel Approval -->
				<?php
				if (isset($absensi)):
				foreach ($absensi->data as $data):
				?>
				<tr>
					<td>
						<?php
						// convert from day, month, year to a date obj
						$date = DateTime::createFromFormat('d-m-Y', $data->day . '-' . $data->month . '-' . $data->year);
						echo $date->format('d-m-Y');
						?>
					</td>
					<td>
						<?php echo $date->format('l'); ?>
					<td>
						<?php
//						echo $data->firstCheckIn;
						// format it
						if ($data->firstCheckIn != null) {
							$firstCheckIn = DateTime::createFromFormat('H:i:s+', $data->firstCheckIn);
							echo $firstCheckIn->format('H:i:s');
						} else {
							echo '-';
						}
						?>
					</td>
					<td>
						<?php
//						echo $data->lastCheckOut;
						// format it
						if ($data->lastCheckOut != null) {
							$lastCheckOut = DateTime::createFromFormat('H:i:s+', $data->lastCheckOut);
							echo $lastCheckOut->format('H:i:s');
						} else {
							echo '-';
						}
						?>
					</td>
					<td>
						<?php
						echo $data->status;
						?>
					</td>
					<td>
						<!-- tambahbutton button detail, approved, reject -->
						<!--                          <a href="--><?php //echo site_url('absen/view_attendance/' . $data->id); ?><!--" class="btn btn-primary">Detail</a>-->
						<?php if ($data->firstCheckIn != null) : ?>
						<button type="button"
								class="btn btn-primary"
								data-toggle="modal"
								data-target="#attendanceDetail"
								data-day="<?php echo $data->day; ?>"
								data-month="<?php echo $data->month; ?>"
								data-year="<?php echo $data->year; ?>"
								data-jam-masuk="<?php echo $data->jamMasuk; ?>"
								data-status="<?php echo $data->status; ?>"
								data-time="<?php echo $data->firstCheckIn; ?>"
								data-keterangan="<?php echo $data->keterangan; ?>"
								data-approved="<?php echo $data->approved; ?>"
								data-latitude="<?php echo $data->latitude; ?>"
								data-longitude="<?php echo $data->longitude; ?>"
								data-center-latitude="<?php echo $data->centerLatitude; ?>"
								data-center-longitude="<?php echo $data->centerLongitude; ?>"
								data-radius="<?php echo $data->radius; ?>"
								data-distance="<?php echo $data->distance; ?>"
								data-reject-reason="<?php echo $data->rejectReason; ?>"
								data-type="check-in"
						>Detail Check In</button>
						<?php endif; ?>
						<?php if ($data->lastCheckOut != null) : ?>
						<button type="button"
								class="btn btn-primary"
								data-toggle="modal"
								data-target="#attendanceDetail"
								data-day="<?php echo $data->day; ?>"
								data-month="<?php echo $data->month; ?>"
								data-year="<?php echo $data->year; ?>"
								data-jam-masuk="<?php echo $data->jamMasuk; ?>"
								data-status="<?php echo $data->status; ?>"
								data-time="<?php echo $data->lastCheckOut; ?>"
								data-approved="<?php echo $data->approvedCheckOut; ?>"
								data-keterangan="<?php echo $data->keteranganCheckOut; ?>"
								data-latitude="<?php echo $data->latitudeCheckOut; ?>"
								data-longitude="<?php echo $data->longitudeCheckOut; ?>"
								data-center-latitude="<?php echo $data->centerLatitude; ?>"
								data-center-longitude="<?php echo $data->centerLongitude; ?>"
								data-radius="<?php echo $data->radius; ?>"
								data-distance="<?php echo $data->distanceCheckOut; ?>"
								data-reject-reason="<?php echo $data->checkOutRejectReason; ?>"
								data-type="check-out"
						>Detail Check Out</button>
						<?php endif; ?>
					</td>
					<?php endforeach; ?>
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
  <!-- /.content -->
</section>
	<script>
		$(document).ready(function() {
			/*
			 {
            "day": 30,
            "month": 6,
            "year": 2024,
            "jamMasuk": "08:00:00",
            "status": "LIBUR",
            "firstCheckIn": null,
            "lastCheckOut": null,
            "keterangan": "Hari Libur",
            "approved": 0,
            "approvedCheckOut": 0,
            "keteranganCheckOut": null,
            "longitude": null,
            "latitude": null,
            "distance": null,
            "distanceCheckOut": null,
            "centerLongitude": null,
            "centerLatitude": null,
            "radius": null,
            "imageUrl": null,
            "longitudeCheckOut": null,
            "latitudeCheckOut": null
        },
			 */
			$('#attendanceDetail').on('show.bs.modal', function(event) {
				let button = $(event.relatedTarget);
				let attendance = {
					day: button.data('day'),
					month: button.data('month'),
					year: button.data('year'),
					jamMasuk: button.data('jam-masuk'),
					status: button.data('status'),
					time: button.data('time'),
					keterangan: button.data('keterangan'),
					approved: button.data('approved'),
					type: button.data('type'),
					longitude: button.data('longitude'),
					latitude: button.data('latitude'),
					centerLongitude: button.data('center-longitude'),
					centerLatitude: button.data('center-latitude'),
					radius: button.data('radius'),
					distance: button.data('distance'),
					rejectReason: button.data('reject-reason'),
				};
				showDetail(attendance);
			})
		} );
		function showDetail(attendanceData) {
			let modal = $('#attendanceDetail');
			let title = 'Detail Check In';
			if (attendanceData['type'] === 'check-out') {
				title = 'Detail Check Out';
			}
			modal.find('.modal-title').text(title);
			let distance = attendanceData['distance']; // is in meter
			// format distance in KM
			if (distance >= 1000) {
				attendanceData['distance'] = (distance / 1000).toFixed(2) + ' KM';
			} else {
				attendanceData['distance'] = distance + ' Meter';
			}
			let approved = attendanceData['approved'];
			if (approved === 0) {
				attendanceData['approved'] = 'Pending';
			} else if (approved === 1) {
				attendanceData['approved'] = 'Disetujui';
			} else {
				attendanceData['approved'] = 'Ditolak: ' + attendanceData['rejectReason'];
			}
			attendanceData['date'] = attendanceData['day'] + '-' + attendanceData['month'] + '-' + attendanceData['year'];
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
				$.ajax({
					url: '<?php echo site_url('absen/acceptapproval/'); ?>' + attendanceData['id'],
					method: 'GET',
					success: function(data) {
						console.log(data);
						location.reload();
					},
					error: function(err) {
						console.error(err);
					}
				});
			});
			rejButton.off('click').on('click', function() {
				$.ajax({
					url: '<?php echo site_url('absen/rejectapproval/'); ?>' + attendanceData['id'],
					method: 'GET',
					success: function(data) {
						console.log(data);
						location.reload();
					},
					error: function(err) {
						console.error(err);
					}
				});
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
			if (employeeLongitude === "" || employeeLatitude === "" || centerLongitude === "" || centerLatitude === "" || radius === "") {
				// hide map
				$('#map-container').hide();
				return;
			}
			$('#map-container').show();
			if (map === undefined || !map) {
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
								<label>Tanggal</label>
								<input class="form-control attendance-detail" value="" readonly="readonly" name="date">
							</div>
							<div class="form-group">
								<label>Jam</label>
								<input class="form-control attendance-detail" value="" readonly="readonly" name="time">
							</div>
							<div class="form-group">
								<label>Status</label>
								<input class="form-control attendance-detail" value="" readonly="readonly" name="status">
							</div>
							<div class="form-group">
								<label>Keterangan</label>
								<input class="form-control attendance-detail" value="" readonly="readonly" name="keterangan">
							</div>
							<div class="form-group">
								<label>Approved</label>
								<input class="form-control attendance-detail" value="" readonly="readonly" name="approved">
							</div>
							<div class="form-group">
								<label>Jarak</label>
								<input class="form-control attendance-detail" value="" readonly="readonly" name="distance">
							</div>
							<!--							  SHOW GOOGLE MAP-->
							<div class="form-group" id="map-container">
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
					<button id="rej-button" type="button" class="btn btn-danger" data-dismiss="modal">Reject</button>
					<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>

</div>
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"
	  integrity="sha256-p4NxAoJBhIIN+hmNHrzRCf9tD/miZyoHS5obTRR9BMY="
	  crossorigin=""/>
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"
		integrity="sha256-20nQCchB9co0qIjJZRGuk2/Z9VM+kNiyxNV1lvTlZBo="
		crossorigin=""></script>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>
