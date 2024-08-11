<?php $this->load->view('header'); ?>

<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<div class="content-header">
		<div class="container-fluid">
			<div class="row mb-2">
				<div class="col-sm-9">
					<h1 class="m-0">Daftar Kegiatan</h1>
				</div><!-- /.col -->
				<div class="col-sm-3">
					<ol class="breadcrumb float-sm-right">
						<li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
						<li class="breadcrumb-item active">Daftar Kegiatan</li>
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
				<div class="col-md-12">
					<div class="card card-info">
						<div class="card-header">
							<h3 class="card-title">SEI Event Calendar</h3>
							<?php if($permission->event): ?>
								<a class="btn btn-secondary btn-sm float-sm-right"
								   href="<?php echo site_url('hadir/add'); ?>">
									<i class="fas fa-clipboard-list"></i>&nbsp; Tambah Kegiatan
								</a>
							<?php endif; ?>
						</div>
						<!-- /.card-header -->
						<div class="card-body">
							<style>
								/* row height: 25px */
								#event_grid tr td {
									height: 25px;
								}
								#event_grid tr {
									height: 25px;
								}
								#event_grid tbody tr td {
									padding: 0;
								}
								#event_grid_time {
									width: 100px;
								}
								#event_grid_time tbody tr td {
									text-align: center;
									padding: 0 20px;
								}
								#event_grid_data tbody tr td {
									cursor: pointer;
								}
								#event_grid_data td {
									min-width: 10em;
								}
								#event_grid_data {
									overflow-x: scroll;
								}
								#event_grid_data thead th, #event_grid_time thead th {
									height: 25px;
									padding: 0;
									text-align: center;
									vertical-align: middle;
								}
								.event_cell_new:hover::after {
									content: 'Tambahkan Kegiatan';
									text-align: center;
									top: -1px;
									left: 0;
									right: 0;
									bottom: -1px;
									position: absolute;
									background-color: blueviolet;
									color: white;
									vertical-align: middle;
									display: flex;
									justify-content: center;
									align-items: center;
									border-radius: 5px;
								}
								.event_cell_new {
									text-align: center;
								}
								.event_cell_readonly {
									text-align: center;
								}
								#waktu_header {
									width: 5em;
									padding: 0;
									text-align: center;
									vertical-align: middle;
								}
								#event_grid_time .time_cell {
									translate: 0 12px;
								}
								#event_grid {
									display: flex;
									width: 100%;
									justify-content: start;
									align-items: start;
								}
								#event_grid_container {
									overflow: scroll;
									width: 100%;
								}
								.event_cell {
									padding: 2px 8px;
									border-radius: 5px;
									color: white;
									cursor: pointer;
								}
							</style>
							<div class="card card-primary">
								<div class="card-body">
									<div class="row">
										<div class="btn-group">
											<button onclick="today()" type="button" class="btn btn-primary">Today</button>
											<button onclick="prevDay()" type="button" class="btn btn-primary">Previous</button>
											<button onclick="nextDay()" type="button" class="btn btn-primary">Next</button>
										</div>
										<h3 id="event_grid_title" style="flex-grow: 1; text-align: center">
											SEI Event Calendar
										</h3>
										<input type="text" value="<?php echo date('d/m/Y'); ?>" name="tanggal" class="form-control" style="width: initial" id="eventdatepicker" required/>
									</div>
								</div>
								<div class="card-body p-0">
									<!-- THE CALENDAR -->
									<div id="event_grid">
										<table class="table table-bordered" id="event_grid_time">
											<thead>
											<tr></tr>
											</thead>
											<tbody></tbody>
										</table>
										<div id="event_grid_container">
											<table class="table table-bordered" id="event_grid_data">
												<thead>
												<tr>
													<!--													<th id="waktu_header">Waktu</th>-->
													<!--													<th>Lainnya</th>-->
												</tr>
												</thead>
												<tbody>
												</tbody>
											</table>
										</div>
									</div>
									<script>
										let eventReadOnly = <?php echo $permission->event ? 'false' : 'true'; ?>;
										let selectRuangMeetingId;
										let selectFromHour;
										let selectToHour;
										let currentDay = new Date();
										let columns = [
											<?php
											foreach($ruang_meeting as $rm){
												echo "{id: ".$rm->id.", name: '".$rm->name."'},";
											}
											?>
										];
										let eventData = [
											// {"daftar_hadir_id": "id", "tanggal": "2021-01-01", "waktu_mulai": "08:00", "waktu_selesai": "09:00", "tempat": "Ruang Meeting 1", "subyek": "Meeting", "ruang_meeting_id": 1/null},
											<?php
											foreach($hadirs as $dt){
												$tgl = $dt->tanggal;
												$arrtgl = explode("/", $tgl);
												$formattgl = $arrtgl[2]."-".$arrtgl[1]."-".$arrtgl[0];
												$ruang_meeting = $dt->ruang_meeting;
												$tempat = $dt->tempat;
												$canEdit = $dt->pembuat == $employee_id ? "true" : "false";
												$canDelete = $dt->pembuat == $employee_id && $dt->jumlah_peserta == 0 ? "true" : "false";
												if ($ruang_meeting == null) {
													echo "{daftar_hadir_id: '".$dt->daftar_hadir_id."', tanggal: '".$formattgl."', waktu_mulai: '".$dt->waktu_mulai."', waktu_selesai: '".$dt->waktu_selesai."', tempat: '".$tempat."', subyek: '".$dt->subyek."', kegiatan: '".$dt->kegiatan."', pimpinan: '".$dt->pimpinan."', canEdit: ".$canEdit.", canDelete: ".$canDelete."},";
												} else {
													$tempat = $ruang_meeting->name;
													echo "{daftar_hadir_id: '".$dt->daftar_hadir_id."', tanggal: '".$formattgl."', waktu_mulai: '".$dt->waktu_mulai."', waktu_selesai: '".$dt->waktu_selesai."', tempat: '".$tempat."', subyek: '".$dt->subyek."', ruang_meeting_id: ".$ruang_meeting->id.", kegiatan: '".$dt->kegiatan."', pimpinan: '".$dt->pimpinan."', canEdit: ".$canEdit.", canDelete: ".$canDelete."},";
												}
											}
											?>
										];
										function nextDay() {
											currentDay.setDate(currentDay.getDate() + 1);
											renderEvent();
										}
										function prevDay() {
											currentDay.setDate(currentDay.getDate() - 1);
											renderEvent();
										}
										function today() {
											currentDay = new Date();
											renderEvent();
										}
										String.prototype.hashCode = function() {
											let hash = 0,
												i, chr;
											if (this.length === 0) return hash;
											for (i = 0; i < this.length; i++) {
												chr = this.charCodeAt(i);
												hash = ((hash << 5) - hash) + chr;
												hash |= 0; // Convert to 32bit integer
											}
											return hash;
										}
										function refreshCellClasses() {
											let cells = document.querySelectorAll('#event_grid_data tbody tr td');
											let fromHour = selectFromHour;
											let toHour = selectToHour;
											let reversed = false;
											if (fromHour != null && toHour != null) {
												if (fromHour > toHour) {
													let temp = fromHour;
													fromHour = toHour;
													toHour = temp;
													reversed = true;
												}
											}
											let timeStart = null;
											let timeEnd = null;
											let accepted = [];
 											for (let i = 0; i < cells.length; i++) {
												let cell = cells[i];
												let cellIndex = parseInt(cell.dataset.index);
												let ruangMeetingId = parseInt(cell.dataset.ruangMeetingId);
												let hour = parseInt(cell.dataset.hour);
												let minute = parseInt(cell.dataset.minute);
												let time = hour * 60 + minute;
												let targetCell = cell.querySelector('.event_cell_new');
												if (ruangMeetingId == null || ruangMeetingId.length === 0) {
													ruangMeetingId = -1;
												}
												if (selectRuangMeetingId != null && fromHour <= cellIndex && cellIndex <= toHour) {
													if (ruangMeetingId === selectRuangMeetingId) {
														if (timeStart == null || time < timeStart) {
															timeStart = time;
														}
														if (timeEnd == null || time > timeEnd) {
															timeEnd = time;
														}
														accepted.push(cell);
														targetCell.style.backgroundColor = 'blueviolet';
														targetCell.style.color = 'white';
													} else {
														targetCell.style.backgroundColor = '';
													}
												} else {
													targetCell.style.backgroundColor = '';
												}
											}
											if (timeEnd != null) {
												timeEnd += 30;
											}
											for (let i = 0; i < accepted.length; i++) {
												let acc = accepted[i].querySelector('.event_cell_new');
												if (!acc) continue;
												if (accepted.length === 1) {
													acc.style.borderRadius = '5px';
												} else if (i === 0) {
													acc.style.borderRadius = '5px 5px 0 0';
												} else if (i === accepted.length - 1) {
													acc.style.borderRadius = '0 0 5px 5px';
												} else {
													acc.style.borderRadius = null;
												}
											}
											if (accepted.length > 0) {
												let selected = reversed ? accepted[accepted.length - 1] : accepted[0];
												let hourStart = Math.floor(timeStart / 60);
												let hourEnd = Math.floor(timeEnd / 60);
												let minuteStart = timeStart % 60;
												let minuteEnd = timeEnd % 60;
												// pad with 0
												minuteStart = minuteStart < 10 ? '0' + minuteStart : minuteStart;
												minuteEnd = minuteEnd < 10 ? '0' + minuteEnd : minuteEnd;
												for (let cell of cells) {
													let targetCell = cell.querySelector('.event_cell_new');
													if (cell === selected) {
														targetCell.innerHTML = hourStart + ':' + minuteStart + ' - ' + hourEnd + ':' + minuteEnd;
													} else {
														targetCell.innerHTML = '';
													}
												}
											}
										}
										function clearTable(table) {
											let all = table.querySelectorAll('th');
											for (let i of all) {
												i.remove();
											}
											all = table.querySelectorAll('tbody tr')
											for (let i of all) {
												i.remove();
											}
										}
										function formatDate(date) {
											// to format date to dd/MM/yyyy
											// where digits are padded with 0
											let day = date.getDate();
											let month = date.getMonth() + 1;
											let year = date.getFullYear();
											return (day < 10 ? '0' : '') + day + '/' + (month < 10 ? '0' : '') + month + '/' + year;
										}
										function renderEvent() {
											// set the param query ?tanggal=dd/MM/yyyy
											let url = new URL(window.location.href);
											url.searchParams.set('tanggal', formatDate(currentDay));
											window.history.replaceState({}, '', url);

											let table = document.querySelector('#event_grid_data tbody');
											table.onmouseleave = function() {
												selectRuangMeetingId = null;
												selectFromHour = null;
												selectToHour = null;
												refreshCellClasses();
											}

											// clear table
											clearTable(document.querySelector('#event_grid_data'))
											clearTable(document.querySelector('#event_grid_time'))

											// set the datepicker
											let datepicker = document.getElementById('eventdatepicker');
											// format date to dd/MM/yyyy
											datepicker.value = formatDate(currentDay);

											let title = document.getElementById('event_grid_title');
											title.innerHTML = currentDay.toDateString();

											let header = document.querySelector('#event_grid_data thead tr');
											// generate header ("Waktu", columns, "Lainnya")
											let headerTime = document.querySelector('#event_grid_time thead tr');
											let th = document.createElement('th');
											th.id = "waktu_header";
											th.innerHTML = "Waktu";
											headerTime.appendChild(th);
											for (let i = 0; i < columns.length; i++) {
												let th = document.createElement('th');
												th.innerHTML = columns[i].name;
												header.appendChild(th);
											}
											th = document.createElement('th');
											th.innerHTML = "Lainnya";
											header.appendChild(th);

											let body = document.querySelector('#event_grid_data tbody');
											let bodyTime = document.querySelector('#event_grid_time tbody');
											let startHour = 8;
											let endHour = 17;

											// check if there is an event that exceeds the endHour or startHour
											for (let i = 0; i < eventData.length; i++) {
												let dt = eventData[i];
												let date = new Date(dt.tanggal);

												if (date.getDate() !== currentDay.getDate() || date.getMonth() !== currentDay.getMonth() || date.getFullYear() !== currentDay.getFullYear()) {
													continue;
												}
												let timeStart = dt.waktu_mulai;
												let timeEnd = dt.waktu_selesai;
												let timeStartHour = parseInt(timeStart.split(":")[0]);
												let timeEndHour = parseInt(timeEnd.split(":")[0]);
												if (timeStartHour < startHour) {
													startHour = timeStartHour;
												}
												if (timeEndHour > endHour) {
													endHour = timeEndHour;
												}
											}

											// adjustment
											startHour = Math.max(0, startHour);
											endHour = Math.min(24, endHour);

											// generate first column
											for (let i = startHour; i <= endHour; i++) {
												for (let k = 0; k < 2; k++) {
													let currentIndex = (i - startHour + k * 0.5) * 2;
													let tr = document.createElement('tr');
													let trTime = document.createElement('tr');
													let timeHour = i;
													let timeMinute = k === 0 ? 30 : 0;
													if (k === 1) timeHour++;
													if (i !== endHour || k !== 1) {
														let td = document.createElement('td');
														td.innerHTML = timeHour + ':' + (timeMinute === 0 ? '00' : '30');
														td.className = 'time_cell';
														trTime.appendChild(td);
													}
													for (let j = 0; j < columns.length; j++) {
														let td = document.createElement('td');
														td.style.position = 'relative';
														td.dataset.ruangMeetingId = columns[j].id;
														td.dataset.hour = i;
														td.dataset.minute = k === 0 ? 0 : 30;
														td.dataset.index = currentIndex;
														td.innerHTML = '';
														tr.appendChild(td);

														let newEventChild = document.createElement('div');
														if (eventReadOnly) {
															newEventChild.className = 'event_cell_readonly';
														} else {
															newEventChild.className = 'event_cell_new';
														}
														newEventChild.style.position = 'absolute';
														newEventChild.style.top = '-1px';
														newEventChild.style.bottom = '-1px';
														newEventChild.style.width = '100%';

														newEventChild.onmousedown = function(event) {
															event.preventDefault();
															selectRuangMeetingId = columns[j].id;
															selectFromHour = currentIndex;
															selectToHour = currentIndex;
															refreshCellClasses();
														}
														newEventChild.onmousemove = function(event) {
															if (selectRuangMeetingId) {
																event.preventDefault();
																if (selectRuangMeetingId !== columns[j].id) {
																	selectRuangMeetingId = null;
																	selectFromHour = null;
																	selectToHour = null;
																	refreshCellClasses();
																	return
																}
																selectToHour = currentIndex;
																refreshCellClasses();
															}
														}
														newEventChild.onmouseup = function(event) {
															if (selectRuangMeetingId && selectFromHour != null && selectToHour != null) {
																event.preventDefault();
																selectToHour = currentIndex;
																let toIndex = selectToHour;
																let fromIndex = selectFromHour;
																if (toIndex < fromIndex) {
																	let temp = toIndex;
																	toIndex = fromIndex;
																	fromIndex = temp;
																}
																// 1 cell is 30 minutes
																toIndex += 1;
																let timeStart = fromIndex * 30 + (startHour) * 60;
																let timeEnd = toIndex * 30 + (startHour) * 60;
																let fromHour = Math.floor(timeStart / 60);
																let fromMinute = timeStart % 60;
																let toHour = Math.floor(timeEnd / 60);
																let toMinute = timeEnd % 60;
																let builddate = formatDate(currentDay);
																//window.open('<?php //echo site_url('hadir/add/'); ?>//' + '?tanggal=' + builddate + '&waktu_mulai=' + fromIndex + ':' + fromMinute + '&waktu_selesai=' + toIndex + ':' + toMinute + '&ruang_meeting_id=' + selectRuangMeetingId, '_blank');
																window.open('<?php echo site_url('hadir/add/'); ?>' + '?tanggal=' + builddate + '&waktu_mulai=' + fromHour + ':' + fromMinute + '&waktu_selesai=' + toHour + ':' + toMinute + '&ruang_meeting_id=' + selectRuangMeetingId, '_blank');
																selectRuangMeetingId = null;
																selectFromHour = null;
																selectToHour = null;
																refreshCellClasses();
															}
														}

														if (!eventReadOnly) {
                                                            td.appendChild(newEventChild);
														}
													}
													td = document.createElement('td');
													td.dataset.ruangMeetingId = -1;
													td.dataset.hour = i;
													td.dataset.minute = k === 0 ? 0 : 30;
													td.dataset.index = currentIndex;
													td.style.position = 'relative';
													td.innerHTML = "";
													tr.appendChild(td);

													let newEventChild = document.createElement('div');
													newEventChild.className = 'event_cell_new';
													newEventChild.style.position = 'absolute';
													newEventChild.style.top = '-1px';
													newEventChild.style.bottom = '-1px';
													newEventChild.style.width = '100%';
													newEventChild.onmousedown = function(event) {
														event.preventDefault();
														selectRuangMeetingId = -1;
														selectFromHour = currentIndex;
														selectToHour = currentIndex;
														refreshCellClasses();
													}
													newEventChild.onmousemove = function(event) {
														if (selectRuangMeetingId) {
															event.preventDefault();
															if (selectRuangMeetingId !== -1) {
																selectRuangMeetingId = null;
																selectFromHour = null;
																selectToHour = null;
																refreshCellClasses();
																return
															}
															selectToHour = currentIndex;
															refreshCellClasses();
														}
													}
													newEventChild.onmouseup = function(event) {
														if (selectRuangMeetingId) {
															event.preventDefault();
															selectToHour = currentIndex;
															let toIndex = selectToHour;
															let fromIndex = selectFromHour;
															if (toIndex < fromIndex) {
																let temp = toIndex;
																toIndex = fromIndex;
																fromIndex = temp;
															}
															// 1 cell is 30 minutes
															toIndex += 1;
															let timeStart = fromIndex * 30 + (startHour) * 60;
															let timeEnd = toIndex * 30 + (startHour) * 60;
															let fromHour = Math.floor(timeStart / 60);
															let fromMinute = timeStart % 60;
															let toHour = Math.floor(timeEnd / 60);
															let toMinute = timeEnd % 60;
															let builddate = formatDate(currentDay);
															window.open('<?php echo site_url('hadir/add/'); ?>' + '?tanggal=' + builddate + '&waktu_mulai=' + fromHour + ':' + fromMinute + '&waktu_selesai=' + toHour + ':' + toMinute + '&ruang_meeting_id=-1', '_blank');
															selectRuangMeetingId = null;
															selectFromHour = null;
															selectToHour = null;
															refreshCellClasses();
														}
													}
													td.appendChild(newEventChild);
													body.appendChild(tr);
													bodyTime.appendChild(trTime);
												}
											}

											const rowHeight = 50;
											// render events
											for (let i = 0; i < eventData.length; i++) {
												let dt = eventData[i];
												let date = new Date(dt.tanggal);

												if (date.getDate() !== currentDay.getDate() || date.getMonth() !== currentDay.getMonth() || date.getFullYear() !== currentDay.getFullYear()) {
													continue;
												}

												let timeStart = dt.waktu_mulai;
												let timeEnd = dt.waktu_selesai;
												let timeStartHour = parseInt(timeStart.split(":")[0]);
												let timeEndHour = parseInt(timeEnd.split(":")[0]);
												let timeStartMinute = parseInt(timeStart.split(":")[1]);
												let timeEndMinute = parseInt(timeEnd.split(":")[1]);
												let timeStartTop = timeStartMinute / 60 * rowHeight;
												let timeStartInMinutes = timeStartHour * 60 + timeStartMinute;
												let timeEndInMinutes = timeEndHour * 60 + timeEndMinute;
												let durationInMinutes = timeEndInMinutes - timeStartInMinutes;
												let height = durationInMinutes / 60 * rowHeight;
												let targetCell;

												let k = 0;
												if (timeStartMinute >= 30) {
													k = 1;
												}

												if (dt.ruang_meeting_id) {
													targetCell = document.querySelector('#event_grid_data tbody tr td[data-ruang-meeting-id="' + dt.ruang_meeting_id + '"][data-hour="' + timeStartHour + '"][data-minute="0"]');
												} else {
													targetCell = document.querySelector('#event_grid_data tbody tr td[data-ruang-meeting-id="-1"][data-hour="' + timeStartHour + '"][data-minute="0"]');
												}

												if (targetCell == null) {
													console.log('target cell not found', timeStartHour, timeStartMinute, dt.ruang_meeting_id);
													continue;
												}

												let textChild = document.createElement('div');
												textChild.className = 'event_cell';
												// the child would overflow the parent
												textChild.style.position = 'absolute';
												textChild.style.top = timeStartTop + 'px';
												textChild.style.height = height + 'px';
												textChild.style.width = '100%';
												textChild.style.zIndex = '1';
												textChild.style.overflow = 'hidden';
												textChild.onmouseenter = function() {
													selectRuangMeetingId = null;
													selectFromHour = null;
													selectToHour = null;
													refreshCellClasses();
												}
												let subyekHash = dt.subyek.hashCode() * dt.daftar_hadir_id.hashCode();
												let randomHue = (subyekHash * (new Date().getMilliseconds())) % 360;
												let saturation = 70;
												let lightness = 30;
												textChild.style.backgroundColor = 'hsl(' + randomHue + ', ' + saturation + '%, ' + lightness + '%)';

												// if (dt.ruang_meeting_id) {
												// 	textChild.innerHTML = '<b>Subjek: </b>' + dt.subyek + '<br><b>Kegiatan: </b>' + dt.kegiatan + '<br>' + dt.waktu_mulai + '-' + dt.waktu_selesai;
												// } else {
												// 	textChild.innerHTML = '<b>Subjek: </b>' + dt.subyek + '<br><b>Tempat: </b>' + dt.tempat + '<br><b>Kegiatan: </b>' + dt.kegiatan + '<br>' + dt.waktu_mulai + '-' + dt.waktu_selesai;
												// }
												// urutkan kembali: Kegiatan, Subjek, Tempat (Jika ruang meeting tidak ada), Waktu Mulai - Waktu Selesai
												if (dt.ruang_meeting_id) {
													textChild.innerHTML = '<span style="font-size: 16px"><b>Kegiatan: </b>' + dt.kegiatan + '<br><b>Subjek: </b>' + dt.subyek + '<br><b>Pimpinan: </b>' + dt.pimpinan + '<br><b>Waktu: </b>' + dt.waktu_mulai + ' - ' + dt.waktu_selesai + '</span>';
												} else {
													textChild.innerHTML = '<span style="font-size: 16px"><b>Kegiatan: </b>' + dt.kegiatan + '<br><b>Subjek: </b>' + dt.subyek + '<br><b>Pimpinan: </b>' + dt.pimpinan + '<br><b>Tempat: </b>' + dt.tempat + '<br><b>Waktu: </b>' + dt.waktu_mulai + ' - ' + dt.waktu_selesai + '</span>';
												}

												textChild.onclick = function() {
													//window.open('<?php //echo site_url('hadir/detail/'); ?>//' + dt.daftar_hadir_id, '_blank');
													showPopUp(dt, dt.canEdit, dt.canDelete);
												}

												targetCell.appendChild(textChild);
											}
										}
										$(function() {
											// parse the date from the query
											let url = new URL(window.location.href);
											let date = url.searchParams.get('tanggal');
											if (date) {
												let arr = date.split('/');
												currentDay = new Date(arr[2], arr[1] - 1, arr[0]);
											}
											renderEvent();
											$('#eventdatepicker').datepicker({
												format: 'dd/mm/yyyy',
												dateFormat: 'dd/mm/yy',
												autoclose: false
											}).on('change', function(e) {
												let target = e.target;
												let date = target.value;
												let arr = date.split('/');
												currentDay = new Date(arr[2], arr[1] - 1, arr[0]);
												renderEvent();
											});
										})
										function showPopUp(ruangInfo, canEdit, canDelete) {
											document.getElementById('kegiatan').value = ruangInfo.kegiatan;
											document.getElementById('subyek').value = ruangInfo.subyek;
											document.getElementById('pimpinan').value = ruangInfo.pimpinan;
											if (canEdit) {
												document.getElementById('detailEditButton').style.display = 'block';
												document.getElementById('detailEditButton').onclick = function() {
													window.open('<?php echo site_url('hadir/update/'); ?>' + ruangInfo.daftar_hadir_id, '_blank');
												}
											} else {
												document.getElementById('detailEditButton').style.display = 'none';
											}
											if (canDelete) {
												let deleteButton = document.getElementById('detailDeleteButton');
												deleteButton.style.display = 'block';
												// set to this
												/*
												<button type="button" class="btn btn-danger btn-sm"
															data-toggle="modal"
															data-target="#deleteDialog<?php echo $dt->daftar_hadir_id; ?>">
														<i class="fas fa-ban"></i>&nbsp; Delete
													</button>
												 */
												deleteButton.dataset.toggle = 'modal';
												deleteButton.dataset.target = '#deleteDialog' + ruangInfo.daftar_hadir_id;
											} else {
												document.getElementById('detailDeleteButton').style.display = 'none';
											}
											document.getElementById('detailDetailButton').onclick = function() {
												window.open('<?php echo site_url('hadir/detail/'); ?>' + ruangInfo.daftar_hadir_id, '_blank');
											}
											$('#detailModal').modal('show');
										}
									</script>
								</div>
								<!-- /.card-body -->
							</div>
							<!-- /.card -->
						</div>

					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="card card-primary">
						<div class="card-header">
							<h3 class="card-title"><i class="fas fa-clipboard-list"></i> Daftar Kegiatan</h3>
							<?php if($permission->event): ?>
								<a class="btn btn-secondary btn-sm float-sm-right"
								   href="<?php echo site_url('hadir/add'); ?>">
									<i class="fas fa-clipboard-list"></i>&nbsp; Tambah Kegiatan
								</a>
							<?php endif; ?>
						</div>
						<div class="card-body">

							<table id="hadir" class="table table-bordered table-striped">
								<thead>
								<tr>
									<th width="10%">Tanggal</th>
									<th>Waktu</th>
									<th>Subyek</th>
									<th>Tempat</th>
									<th width="5%">&nbsp;</th>
								</tr>
								</thead>
								<tbody>
								<?php
								if (isset($hadirs)):
									foreach ($hadirs as $dt) :
										$tgl = $dt->tanggal;
										$mulai = $dt->waktu_mulai;
										$selesai = $dt->waktu_selesai;
										$waktus = date('H:i');
										$tgls = date('Y-m-d');
										$arrtgl = explode("/", $tgl);
										$formattgl = $arrtgl[2] . "-" . $arrtgl[1] . "-" . $arrtgl[0];
										//echo $selesai." = ".$waktus;
										?>
										<tr>
											<td><?php echo $formattgl; ?></td>
											<td><?php echo $dt->waktu_mulai . " - " . $dt->waktu_selesai; ?></td>
											<td><?php echo $dt->subyek; ?></td>
											<td><?php echo $dt->tempat; ?></td>
											<td class="col-5 text-center">
												<a class="btn btn-info btn-sm"
												   href="<?php echo site_url('view/event/' . $dt->daftar_hadir_id); ?>"
												   target="_blank">
													<i class="fas fa-link"></i> Link
												</a>
												<a class="btn btn-success btn-sm"
												   href="<?php echo site_url('hadir/detail/' . $dt->daftar_hadir_id); ?>">
													<i class="fas fa-info"></i> Detail
												</a>
												<a class="btn btn-primary btn-sm" target="_blank"
												   href="<?php echo site_url('hadir/download/' . $dt->daftar_hadir_id); ?>">
													<i class="fas fa-file-download"></i> Download
												</a>

												<?php if ($employee_id == $dt->notulis): ?>
													<a class="btn btn-secondary btn-sm"
													   href="<?php echo site_url('hadir/risalah/' . $dt->daftar_hadir_id); ?>">
														<i class="fas fa-book-open"></i> Risalah
													</a>
												<?php endif; ?>

												<?php if ($dt->pembuat == $employee_id): ?>
													<?php if ($tgls <= $formattgl): ?>
													<a class="btn btn-warning btn-sm"
													   href="<?php echo site_url('hadir/update/' . $dt->daftar_hadir_id); ?>">
														<i class="fas fa-wrench"></i> Update
													</a>
													<?php endif; ?>
													<?php if($tgls == $formattgl && $selesai > $waktus): ?>
													<a class="btn btn-dark btn-sm"
													   href="<?php echo site_url('hadir/finish/' . $dt->daftar_hadir_id); ?>">
														<i class="fas fa-stop-circle"></i> Selesai
													</a>
													<?php endif; ?>
												<?php endif; ?>
												<?php if ($dt->jumlah_peserta == 0 && $dt->pembuat == $employee_id && $tgls <= $formattgl): ?>
													<button type="button" class="btn btn-danger btn-sm"
															data-toggle="modal"
															data-target="#deleteDialog<?php echo $dt->daftar_hadir_id; ?>">
														<i class="fas fa-ban"></i>&nbsp; Delete
													</button>

													<!-- Modal Delete -->
													<div class="modal fade"
														 id="deleteDialog<?php echo $dt->daftar_hadir_id; ?>"
														 tabindex="-1" role="dialog"
														 aria-labelledby="approveModalLabel" aria-hidden="true">
														<div class="modal-dialog" role="document">
															<div class="modal-content">
																<div class="modal-header">
																	<h5 class="modal-title" id="exampleModalLabel">
																		Hapus Data</h5>
																	<button type="button" class="close"
																			data-dismiss="modal" aria-label="Close">
																		<span aria-hidden="true">&times;</span>
																	</button>
																</div>
																<?php echo form_open('hadir/delete'); ?>
																<?php echo form_hidden('dhid', $dt->daftar_hadir_id); ?>
																<div class="modal-body">
																	Hapus daftar hadir
																	: <?php echo $dt->kegiatan; ?>,
																	Tanggal: <?php echo $dt->tanggal; ?> ?
																</div>
																<div class="modal-footer">
																	<button type="button" class="btn btn-secondary"
																			data-dismiss="modal">Close
																	</button>
																	<button type="submit" class="btn btn-danger">
																		Confirm Delete
																	</button>
																</div>
																<?php echo form_close(); ?>
															</div>
														</div>
													</div>
												<?php endif; ?>

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
				</div>

			</div>
			<!-- /.content -->
	</section>
	<div class="modal fade" id="detailModal" tabindex="-1" aria-labelledby="detailModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="detailModalLabel">Detail Kegiatan</h5>
					<button type="button" class="close"
							data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="col-12">
							<!--							create form with disabled input-->
							<div class="form-group">
								<label for="kegiatan" class="form-label">Kegiatan</label>
								<input type="text" class="form-control mb-3" id="kegiatan" disabled>
							</div>
							<div class="form-group">
								<label for="subyek" class="form-label">Subyek</label>
								<input type="text" class="form-control mb-3" id="subyek" disabled>
							</div>
							<div class="form-group">
								<label for="pimpinan" class="form-label">Pimpinan</label>
								<input type="text" class="form-control mb-3" id="pimpinan" disabled>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button id="detailEditButton" type="button" class="btn btn-primary"
							data-dismiss="modal">Edit
					</button>
					<button id="detailDeleteButton" type="button" class="btn btn-danger"
							data-dismiss="modal">Delete
					</button>
					<button id="detailDetailButton" type="button" class="btn btn-success"
							data-dismiss="modal">Detail
					</button>
					<button type="button" class="btn btn-secondary"
							data-dismiss="modal">Close
					</button>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>
