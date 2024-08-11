<?php $this->load->view("header"); ?>
<div class="content-wrapper" xmlns="http://www.w3.org/1999/html">
	<!-- Content Header (Page header) -->
	<div class="content-header">
		<div class="container-fluid">
			<div class="row mb-2">
				<div class="col-sm-6">
					<h1 class="m-0"><?php echo isset($mode) && $mode == "edit"
						? "Edit"
						: "Tambah"; ?> Slip Gaji</h1>
				</div><!-- /.col -->
				<div class="col-sm-6">
					<ol class="breadcrumb float-sm-right">
						<li class="breadcrumb-item"><a href="<?php echo site_url(
							"dashboard"
						); ?>">Home</a></li>
						<li class="breadcrumb-item"><a href="<?php echo site_url(
							"karyawan/slipgaji"
						); ?>">Slip Gaji</a></li>
						<li class="breadcrumb-item active">Tambah Data</li>
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
							<form id="validitycheckform">
								<div class="card-body">
									<div class="row p-3">
										<div class="col-md">
											<div class="form-group col p-0">
												<label>Nama Template</label>
												<input id="name" type="text" name="name" class="form-control w-100"
															 required placeholder="<?php echo isset($data->name) ? $data->name : "Nama Template" ?>" value=<?php echo isset(
													$data->name
												)
													? $data->name
													: ""; ?>>
											</div>
											<div class="row">
<!--												name-->
												<div class="form-group pr-1 col">
													<label>Tahun</label>
													<?php echo form_error(
														"jenis_cuti",
														'<div class="alert alert-warning">',
														"</div>"
													); ?>
													<input id="tahun" type="number" name="tahun" class="form-control w-100"
														required placeholder="input tahun" value=<?php echo isset(
															$data->tahun
														)
															? $data->tahun
															: date("Y"); ?>>
												</div>
												<div class="form-group pr-1 col">
													<label>Bulan</label>
													<select id="bulan" name="bulan" class="form-control w-100">
														<?php
														$bulan = array(
															1 => "Januari",
															2 => "Februari",
															3 => "Maret",
															4 => "April",
															5 => "Mei",
															6 => "Juni",
															7 => "Juli",
															8 => "Agustus",
															9 => "September",
															10 => "Oktober",
															11 => "November",
															12 => "Desember",
														);
														if (isset($data->bulan)) {
															foreach ($bulan as $key => $value) {
																if ($key == $data->bulan) {
																	echo '<option value="' .
																		$key .
																		'" selected>' .
																		$value .
																		"</option>";
																} else {
																	echo '<option value="' .
																		$key .
																		'">' .
																		$value .
																		"</option>";
																}
															}
														} else {
															$currentMonth = date("m");
															foreach ($bulan as $key => $value) {
																echo '<option value="' .
																	$key .
																	'" ' .
																	($currentMonth == $key ? "selected" : "");
																echo ">" .
																	$value .
																	"</option>";
															}
														}
														?>
													</select>
												</div>
											</div>
<!--											 card info on bulan and tahun already exists, and add button to suggest to edit it instead-->
											<div class="row" id="dataexistalert" style="display:none;">
												<div class="col">
													<!--													card-->
													<div class="alert alert-warning row" role="alert">
<!--														<div class="col align-content-center">Template Slip Gaji untuk Bulan <span id="bulanalert"></span> dan Tahun <span id="tahunalert"></span> sudah ada, apakah anda ingin mengeditnya?</div>-->
														<div class="col align-content-center">Template Slip Gaji <span id="namealert"></span> untuk Bulan <span id="bulanalert"></span> dan Tahun <span id="tahunalert"></span> sudah ada dengan nama yang sama, ubah ke nama yang lain untuk menghindari ambigu atau edit template yang sudah ada?</div>
<!--														add button at the end of it-->
														<button type="button" class="btn btn-light" onclick="goEditCurrent(this)">Edit</button>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col">
													<label class="pl-1">Pemasukan</label>
													<div class="field-list" id="pemasukanList">
														<div class="field-list-none">
															<span class="text-muted">Kosong</span>
														</div>
														<div class="field-list-items">
														</div>
														<button type="button" class="btn btn-success"
															onclick="add_field(null, '', null, 'pemasukanList', 'PEMASUKAN')">
															<i class="fa fa-solid fa-plus"></i>
														</button>
													</div>
												</div>
												<div class="col">
													<label class="pl-1">Pengeluaran</label>
													<div class="field-list" id="pengeluaranList">
														<div class="field-list-none">
															<span class="text-muted">Kosong</span>
														</div>
														<div class="field-list-items">
														</div>
														<button type="button" class="btn btn-success"
															onclick="add_field(null, '', null, 'pengeluaranList', 'PENGELUARAN')">
															<i class="fa fa-solid fa-plus"></i>
														</button>
													</div>
												</div>
											</div>
											<div class="col text-center p-3">
												<button type="button" onclick="submitForm()"
													class="btn btn-primary">Simpan</button>
												<?php if (isset($mode) && $mode == "edit") { ?>
													<button type="button" class="btn btn-danger" data-toggle="modal"
														data-target="#resetFieldsModal">Reset</button>
												<?php } ?>
											</div>
										</div>
									</div>
								</div>
							</form>
						</div>
						<!-- /.row -->
					</div><!-- /.container-fluid -->
				</div>
	</section>
	<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<?php $this->load->view("footer"); ?>

<style>
	.field-list {
		position: relative;
		display: flex;
		flex-direction: column;
		justify-content: stretch;
		gap: 5px;
	}

	.field-list-items {
		display: flex;
		flex-direction: column;
		justify-content: stretch;
		gap: 5px;
		min-height: 50px;
	}

	.field-list-none {
		position: absolute;
		width: 100%;
		height: 50px;
		display: flex;
		justify-content: center;
		align-items: center;
		background-color: #f9f9f9;
		border-radius: 5px;
		border: 1px solid #ccc;
	}

	.field-list-item {
		position: relative;
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		background-color: #f9f9f9;
		border-radius: 5px;
		border: 1px solid #ccc;
		gap: 5px;
		padding: 5px;
	}

	.field-list-item-badges {
		display: flex;
		flex-direction: row;
		justify-content: flex-end;
		align-items: center;
		gap: 5px;
		position: absolute;
		top: -5px;
		right: 50px;
	}
	.sortable-ghost {
		opacity: 0;
	}
	.sortable-drag {
		opacity: 1 !important;
	}
</style>
<!-- confirmation reset fields modal-->
<div class="modal fade" id="resetFieldsModal" tabindex="-1" role="dialog" aria-labelledby="resetFieldsModalLabel"
	aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">Reset Fields</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				Apakah anda yakin ingin mereset fields?
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-danger" onclick="resetFields()" data-dismiss="modal">Reset</button>
			</div>
		</div>
	</div>
</div>
<!-- jsDelivr :: Sortable :: Latest (https://www.jsdelivr.com/package/npm/sortablejs) -->
<script src="https://cdn.jsdelivr.net/npm/sortablejs@latest/Sortable.min.js"></script>
<!-- /.select2 custom javasript -->
<?php echo form_open("slipgaji/update/" . $mode, array("id" => "dummyform")); ?>
<?php if (isset($id)) {
	echo form_hidden("id", $id);
} ?>
<input type="hidden" name="name" required>
<input type="hidden" name="tahun" required>
<input type="hidden" name="bulan" required>
<div id="dummyformfields">
</div>
<?php echo form_close(); ?>
<script>

	function goEdit(id) {
	  if (id == null) {
			return;
		}
	  window.location.href = "<?php echo site_url("slipgaji/updateTemplate/") ?>" + id;
	}

	function goEditCurrent(button) {
		let idElement = button.dataset.id;
		if (idElement != null) {
			goEdit(idElement);
		}
	}

	function updateDummyForm(data) {
		let form = document.getElementById('dummyform');
		form.querySelector('input[name="name"]').value = data.name;
		form.querySelector('input[name="tahun"]').value = data.tahun;
		form.querySelector('input[name="bulan"]').value = data.bulan;
		let fields = form.querySelector('#dummyformfields');
		fields.innerHTML = '';
		data.fields.forEach((field) => {
			let input = document.createElement('input');
			input.type = 'hidden';
			input.name = 'fields[]';
			if (field.name == null || field.name === '') {
				input.value = '';
			} else {
				input.value = JSON.stringify(field);
			}
			input.required = true;
			fields.appendChild(input);
		});
	}

	function add_field(id, value, order, target, category) {
		let element = document.createElement('div');
		if (id != null) {
			element.dataset.id = id;
		}
		if (value != null && value !== '') {
			element.dataset.value = value;
		}
		if (order != null) {
			element.dataset.order = order;
		}
		if (category != null && category !== '') {
			element.dataset.category = category;
		}
		let oldCategory = category;
		element.classList.add('field-list-item');
		element.innerHTML = `
	<button type="button" class="btn drag-button" style="cursor: grab">
			<i class="fa fa-solid fa-grip-lines"></i>
		</button>
		<input type="text" class="form-control" value="${value}" required placeholder="Nama Field">
		<button type="button" class="btn btn-danger" onclick="this.parentElement.remove()">
			<i class="fa fa-solid fa-trash"></i>
		</button>
		<div class="field-list-item-badges">
		</div>
  `;

		// append at field-list-items in target
		let targetElement = document.getElementById(target);
		targetElement.querySelector('.field-list-items').appendChild(element);
		let badgeList = element.querySelector('.field-list-item-badges');
		let badges = [];
		function newBadge(value, type) {
			let badge = document.createElement('span');
			badge.classList.add('badge', 'badge-' + type);
			badge.innerText = value;
			return badge;
		}
		let oldValue = value;
		function updateBadges() {
			badgeList.innerHTML = '';
			if (badges.length > 0) {
				badges.forEach((badge) => {
					badgeList.appendChild(badge);
				});
			}
		}

		function updateItemBadges() {
			badges = [];
			if (oldValue != null && oldValue !== '' && oldValue !== element.querySelector('input').value) {
				badges.push(newBadge('Sebelumnya ' + oldValue, 'warning'));
			}
			if (oldCategory != null && oldCategory !== element.dataset.category && id != null) {
				badges.push(newBadge('Sebelumnya ' + oldCategory, 'warning'));
			}
			if (element.dataset.value === '') {
				badges.push(newBadge('Tidak boleh kosong', 'danger'));
			}
			if (id == null) {
				badges.push(newBadge('Baru', 'primary'));
			}
			updateBadges();
		}

		updateItemBadges();

		// listen to changes, and then set into element dataset
		let inputChange = (e) => {
			element.dataset.value = e.target.value;
		};
		let itemInput = element.querySelector('input');
		itemInput.addEventListener('change', inputChange);
		itemInput.addEventListener('propertychange', inputChange);
		itemInput.addEventListener('input', inputChange);
		// set placeholder to the oldValue, only if its not null
		if (oldValue != null && oldValue !== '') {
			itemInput.placeholder = oldValue;
		}

		// observe changes in dataset, and then update badges
		let observer = new MutationObserver((mutations) => {
			updateItemBadges();
		});
		observer.observe(element, { attributes: true });
	}

	function registerFieldList(fieldList, category) {
		let items = fieldList.querySelector('.field-list-items');
		let sortable = new Sortable(items, {
			handle: '.drag-button',
			group: 'field-list',
			draggable: '.field-list-item',
			// set order to dataset
			onEnd: (e) => {
				let items = e.from.children;
				for (let i = 0; i < items.length; i++) {
					let item = items[i];
					item.dataset.order = i;
					item.dataset.category = category;
				}
			},
			onAdd: (e) => {
				let item = e.item;
				item.dataset.category = category;
			},
			animation: 150,
			forceFallback: true
		});

		// detect field-list-items children, if its empty, then show field-list-none
		// else, hide field-list-none
		let none = fieldList.querySelector('.field-list-none');
		let config = { childList: true };
		let observer = new MutationObserver((mutations) => {
			if (items.children.length === 0) {
				none.style.display = 'flex';
			} else {
				none.style.display = 'none';
			}
			// update order
			let fieldListItems = fieldList.querySelectorAll('.field-list-item');
			fieldListItems.forEach((item, index) => {
				item.dataset.order = index;
			});
		});
		observer.observe(items, config);
	}
  let existingDates = [
		<?php if (isset($existingDates)) {
		foreach ($existingDates as $dt) {
			echo "{tahun: " . $dt->tahun . ", bulan: " . $dt->bulan . ", name: '" . $dt->name . "', id: " . $dt->id . "},";
		}
	} ?>
  ];
	function checkOnTahunBulanChanged() {
	  let idElement = document.querySelector('#dummyform input[name="id"]');
	  let nameElement = document.getElementById('name');
	  let name = nameElement.value;
		let tahunElement = document.getElementById('tahun');
		let tahun = parseInt(tahunElement.value);
		let bulanElement = document.getElementById('bulan');
		let bulan = parseInt(bulanElement.value);
		let dataExistAlert = document.getElementById('dataexistalert');
		// let isExist = existingDates.some((dt) => {
		//   if (idElement != null) {
		// 		let id = parseInt(idElement.value);
		// 		return dt.tahun === tahun && dt.bulan === bulan && dt.name === name && dt.id !== id;
		// 	}
		// 	return dt.tahun === tahun && dt.bulan === bulan && dt.name === name;
		// });
		let foundId = existingDates.find((dt) => {
			if (idElement != null) {
				let id = parseInt(idElement.value);
				return dt.tahun === tahun && dt.bulan === bulan && dt.name === name && dt.id !== id;
			}
			return dt.tahun === tahun && dt.bulan === bulan && dt.name === name;
		});
		let editButton = document.querySelector('#dataexistalert button');
		if (foundId) {
			let bulanAlert = document.getElementById('bulanalert');
			let tahunAlert = document.getElementById('tahunalert');
			let nameAlert = document.getElementById('namealert');
			if (!name || name === '') {
				name = '(Tidak ada nama)';
			}
			nameAlert.innerText = name;
			bulanAlert.innerText = bulanElement.options[bulanElement.selectedIndex].text;
			tahunAlert.innerText = tahunElement.value;
	  	dataExistAlert.style.display = null;
		  editButton.dataset.id = foundId.id;
		  let foundName = foundId.name;
		  if (!foundName || foundName === '') {
				foundName = '(Tidak ada nama)';
	  	}
		  let months = [
				"Januari",
				"Februari",
				"Maret",
				"April",
				"Mei",
				"Juni",
				"Juli",
				"Agustus",
				"September",
				"Oktober",
				"November",
				"Desember"
			];
		  editButton.innerText = 'Edit ' + foundName + ' (' + months[bulan - 1] + ' ' + tahun + ')';
		} else {
		  editButton.dataset.id = null;
			dataExistAlert.style.display = 'none';
		}
	}

	$(document).ready(function () {
		$('.js-single').select2({
			placeholder: "Select position/name",
			width: '100%'
		});
		// find all field-list
		let pemList = document.getElementById('pemasukanList');
		let pengList = document.getElementById('pengeluaranList');
		registerFieldList(pemList, 'PEMASUKAN');
		registerFieldList(pengList, 'PENGELUARAN');
		resetFields();
		// check if tahun and bulan already exists
		let tahun = document.getElementById('tahun');
		let bulan = document.getElementById('bulan');
		let name = document.getElementById('name');
		tahun.addEventListener('change', checkOnTahunBulanChanged);
		bulan.addEventListener('change', checkOnTahunBulanChanged);
		name.addEventListener('change', checkOnTahunBulanChanged);
		name.addEventListener('input', checkOnTahunBulanChanged);
		name.addEventListener('propertychange', checkOnTahunBulanChanged);
		checkOnTahunBulanChanged();
	});

	function resetFields() {
		let fieldLists = document.querySelectorAll('.field-list');
		fieldLists.forEach((fieldList) => {
			let items = fieldList.querySelector('.field-list-items');
			items.innerHTML = '';
		});
		let existingFields = [
			<?php if (isset($data->fields)) {
				// sort by order
				usort($data->fields, function ($a, $b) {
					$aOrder = 0;
					$bOrder = 0;
					if (isset($a->order)) {
						$aOrder = $a->order;
					}
					if (isset($b->order)) {
						$bOrder = $b->order;
					}
					return $aOrder - $bOrder;
				});
				foreach ($data->fields as $dt) {
					// check if $dt is an object or an array
					if (is_array($dt)) {
						$dt = (object) $dt;
					}
					$category = isset($dt->category) &&
						$dt->category == "PEMASUKAN" ? "pemasukanList" : "pengeluaranList";
					echo "{";
					if (isset($dt->id)) {
						echo "id: " . $dt->id . ",";
					}
					if (isset($dt->name)) {
						echo "name: '" . $dt->name . "',";
					}
					if (isset($dt->order)) {
						echo "order: " . $dt->order . ",";
					}
					if (isset($dt->category)) {
						echo "category: '" . $dt->category . "',";
					}
					echo "target: '" . $category . "'";
					echo "},";
				}
			} ?>
		];
		existingFields.forEach((field) => {
			add_field(field.id, field.name, field.order, field.target, field.category);
		});
	}

	function collectFields(fieldList, category) {
		let items = fieldList.querySelectorAll('.field-list-item');
		let fields = [];
		items.forEach((item) => {
			fields.push({
				id: item.dataset.id,
				name: item.querySelector('input').value,
				order: item.dataset.order,
				category: category
			});
		});
		return fields;
	}

	function submitForm() {
		let data = {
			tahun: document.querySelector('input[name="tahun"]').value,
			bulan: document.querySelector('select[name="bulan"]').value,
			name: document.querySelector('input[name="name"]').value,
			fields: []
		};
		let pemasukanList = document.getElementById('pemasukanList');
		let pengeluaranList = document.getElementById('pengeluaranList');
		data.fields = data.fields.concat(collectFields(pemasukanList, 'PEMASUKAN'));
		data.fields = data.fields.concat(collectFields(pengeluaranList, 'PENGELUARAN'));
		updateDummyForm(data);
		let form = document.getElementById('dummyform');
		let validityCheckForm = document.getElementById('validitycheckform');
		if (validityCheckForm.reportValidity()) {
			form.submit();
		}
		// validate form
		// if (form.reportValidity()) {
		// 	form.requestSubmit();
		// }
	}
</script>
