<!DOCTYPE html>
<html lang="en">
<head>
	<link href="<?php echo site_url('img/favico.ico'); ?>" rel="shortcut icon" type="image/x-icon">
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@event-calendar/build@3.0.1/event-calendar.min.css">
	<script src="https://cdn.jsdelivr.net/npm/@event-calendar/build@3.0.1/event-calendar.min.js"></script>
	<script src="https://code.jquery.com/jquery-2.2.4.min.js"></script>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $ruang->name ?></title>
    <style>
		.container {
			padding: 20px;
			height: 100vh;
			position: relative;
		}
		.card {
			height: 100%;
			padding: 20px;
			margin-bottom: 20px;
			overflow-y: auto;
		}
		@media (max-width: 575.98px) {
			body {
				padding: 0;
			}
			html {
				font-size: 12px;
			}
			h4 {
				margin-top: 0;
			}
		}
		/*@media (min-width: 576px) {*/
		/*	.ec {*/
		/*		height: 700px;*/
		/*	}*/
		/*	.ec.ec-day-grid {*/
		/*		height: 500px;*/
		/*	}*/
		/*}*/
		/*@media (min-width: 992px) {*/
		/*	.ec {*/
		/*		height: 800px;*/
		/*	}*/
		/*	.ec.ec-day-grid {*/
		/*		height: 700px;*/
		/*	}*/
		/*}*/
		/*@media (min-width: 1200px) {*/
		/*	.ec.ec-day-grid {*/
		/*		height: 800px;*/
		/*	}*/
		/*}*/
		body {
			background-color: #222426;
		}
		/* create shadow on .card */
		.card {
			background-color: #ffffff;
			border-radius: 10px;
			box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
		}
		.ec {
			width: 100%;
		}
</style>
<script>
	function generateColor(text) {
		let hash = 0;
		for (let i = 0; i < text.length; i++) {
			hash = text.charCodeAt(i) + ((hash << 5) - hash);
		}
		let brightness = 0.4;
		let saturation = 0.3;
		let hue = hash % 360;
		return `hsl(${hue}, ${saturation * 100}%, ${brightness * 100}%)`;
	}
	document.addEventListener('DOMContentLoaded', function() {
		let calendarEl = document.getElementById('calendar');
		if (!calendarEl) return;
		let calendar = new EventCalendar(calendarEl, {
			view: 'timeGridDay',
			headerToolbar: {
				start: 'prev,next today',
				center: 'title',
				end: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
			},
			events: [
				<?php foreach($ruang->events as $row): ?>
					{
						title: {html: '<span style="font-size: 16px"><b>Kegiatan: </b><?php echo $row->kegiatan; ?><br><b>Subyek: </b><?php echo $row->subyek; ?><br><b>Pimpinan: </b><?php echo $row->pimpinan; ?></span>'},
						start: '<?php echo $row->start; ?>',
						end: '<?php echo $row->end; ?>',
						color: generateColor('<?php echo $row->kegiatan; ?>'),
						extendedProps: {
							kegiatan: '<?php echo $row->kegiatan; ?>',
							subyek: '<?php echo $row->subyek; ?>',
							pimpinan: '<?php echo $row->pimpinan; ?>',
						}
					},
				<?php endforeach; ?>
			],
			eventClick: function(info) {
				console.log(info);
				showPopUp(info.event.extendedProps);
			}
		});

	});

	function showPopUp(ruangInfo) {
		document.getElementById('kegiatan').value = ruangInfo.kegiatan;
		document.getElementById('subyek').value = ruangInfo.subyek;
		document.getElementById('pimpinan').value = ruangInfo.pimpinan;
		$('#detailModal').modal('show');
	}
</script>
</head>
<body>
<main class="container">
	<div class="card" style="display: flex">
		<h1><?php echo $ruang->name ?></h1>
		<div style="height: 0; flex-grow: 1; display: flex;" id = "calendar"></div>
	</div>
	<div class="modal fade" id="detailModal" tabindex="-1" aria-labelledby="detailModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="detailModalLabel">Detail Kegiatan</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
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
					<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
</main>
</body>
</html>
