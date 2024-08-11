<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>SEI Office</title>

  <!-- Google Font: Source Sans Pro -->
  <link rel="stylesheet"
    href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
  <!-- Font Awesome Icons -->
  <link rel="stylesheet" href="<?php echo site_url('plugins/fontawesome-free/css/all.min.css'); ?>">
  <!-- DataTables -->
  <link rel="stylesheet" href="<?php echo site_url('plugins/datatables-bs4/css/dataTables.bootstrap4.min.css'); ?>">
  <link rel="stylesheet"
    href="<?php echo site_url('plugins/datatables-responsive/css/responsive.bootstrap4.min.css'); ?>">
  <link rel="stylesheet" href="<?php echo site_url('plugins/datatables-buttons/css/buttons.bootstrap4.min.css'); ?>">
   <!-- Select2 -->
  <script src="https://code.jquery.com/jquery-2.2.4.min.js"></script>
  <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
  <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js" defer></script>
  <!-- Daterangepicker -->
  <script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
  <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js" defer></script>
  <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
  <!-- Theme style -->
  <link rel="stylesheet" href="<?php echo site_url('dist/css/adminlte.min.css'); ?>">
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
  <link href="<?php echo site_url('img/favico.ico'); ?>" rel="shortcut icon" type="image/x-icon">
  <!-- Ionicons -->
  <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
  <!-- fullCalendar -->
  <link rel="stylesheet" href="<?php echo site_url('plugins/fullcalendar/main.css'); ?>">
  <!-- Tempusdominus Bootstrap 4 -->
  <link rel="stylesheet"
    href="<?php echo site_url('plugins/tempusdominus-bootstrap-4/css/tempusdominus-bootstrap-4.min.css'); ?>">
  <!-- Toastr -->
  <link rel="stylesheet" href="<?php echo site_url('plugins/toastr/toastr.min.css'); ?>">

  <?php if ($page == "carorder" || $page == "daftarhadir"): ?>
    <script src="https://unpkg.com/slim-select@latest/dist/slimselect.min.js"></script>
    <link href="https://unpkg.com/slim-select@latest/dist/slimselect.css" rel="stylesheet">
    </link>
  <?php endif; ?>
  <?php if ($page == "daftarhadir"): ?>
    <!-- summernote -->
    <link rel="stylesheet" href="<?php echo site_url('plugins/summernote/summernote-bs4.min.css'); ?>">
  <?php endif; ?>
</head>

<body class="hold-transition sidebar-mini">
  <div class="wrapper">

    <!-- Navbar -->
<nav class="main-header navbar navbar-expand navbar-white navbar-light">
    <!-- Left navbar links -->
    <ul class="navbar-nav">
        <li class="nav-item">
            <a class="nav-link" data-widget="pushmenu" href="#" role="button"><i class="fas fa-bars"></i></a>
        </li>
        <li class="nav-item d-none d-sm-inline-block">
            <div class="row">
                <div class="col">
                    <a href="<?php echo site_url('dashboard'); ?>" class="nav-link">Home</a>
                </div>
            </div>
        </li>
    </ul>

    <!-- Right navbar links -->
    <ul class="navbar-nav ml-auto">
		<script>
			let jwtExpiration = <?php echo $this->session->userdata('expireUntil'); ?>;
			let loginRedirect = "<?php echo site_url('login'); ?>";
            function openSite(url) {
                window.open(url, '_blank');
            }
			let notificationTypes = {
				'AttendanceSwipeRequest': {
					onClick: (data) => {
						let pendingSwipeId = data[1];
                        openSite("<?php echo site_url('absen/approved'); ?>");
					}
				},
				'AttendanceSwipeResponseAccepted': {
					onClick: (data) => {
						let time = data[1];
					}
				},
				'AttendanceSwipeResponseRejected': {
					onClick: (data) => {
						let time = data[1];
					}
				},
				'IzinCutiResponseAccepted': {
					onClick: (data) => {
						let izinCutiId = data[1];
					}
				},
				'IzinCutiResponseRejected': {
					onClick: (data) => {
						let izinCutiId = data[2];
					}
				},
				'NotificationIzinCutiRequest': {
					onClick: (data) => {
						let izinCutiId = data[2];
					}
				},
				'VehiclePendingRequest': {
					onClick: (data) => {
						let vehicleRequestId = data[1];
					}
				},
                'VehicleRequestAccepted': {
                    onClick: (data) => {
                        let vehicleRequestId = data[1];

                    }
                },
                'VehicleRequestReady': {
                    onClick: (data) => {
                        let vehicleRequestId = data[0];
                    }
                },
                'VehicleRequestRejected': {
                    onClick: (data) => {
                        let vehicleRequestId = data[1];
                    }
                },
			}
			// THIS IS AJAX SCRIPT TO GET NOTIFICATION
			let totalUnreadNotifications = 0;
			const maxSavedNotifications = 100;

			// get the last notif id from local storage
			function getLastNotificationId() {
				let item = localStorage.getItem('lastNotificationId');
				if (item) {
					return parseInt(item);
				}
				return null;
			}
			function saveNotification(notification) {
				// save the notification to local storage
				let notifications = localStorage.getItem('notifications');
				if (notifications) {
					notifications = JSON.parse(notifications);
				} else {
					notifications = {};
				}
				notifications[notification.id] = notification;
				// max 100 notifications
				let keys = Object.keys(notifications);
				if (keys.length > maxSavedNotifications) {
					let minId = keys.reduce((a, b) => notifications[a].timestamp < notifications[b].timestamp ? a : b);
					delete notifications[minId];
				}
				localStorage.setItem('notifications', JSON.stringify(notifications));
			}
			function setLastNotificationId(id) {
				localStorage.setItem('lastNotificationId', id);
			}
			function onNotificationReceived(notification) {
				saveNotification(notification);
				// unreadNotifications.push(notification);
				// changeNotificationBadge(unreadNotifications.length);
				totalUnreadNotifications++;
				changeNotificationBadge(totalUnreadNotifications);
				localStorage.setItem('unreadNotifications', String(totalUnreadNotifications));
				renderNotification(notification);
				toastr.info(notification.message);
			}
			function changeNotificationBadge(count) {
				$('#notificationBadge').text(count);
			}
			function renderNotification(notification) {
				// check if there's empty notification component
				// find for #emptyNotificationItem
				if ($('#emptyNotificationItem').length > 0) {
					$('#emptyNotificationItem').remove();
				}

				let type = notification.typeName;
				let data = notification.data;
                let notificationType = notificationTypes[type];
                if (!notificationType) {
                    console.log('Unknown notification type: ' + type);
                    return;
                }
				let onClick = notificationTypes[type].onClick;
				let id = notification.id;
				let message = notification.message;
				let timestamp = notification.timestamp; // ISO 8601 format
				let isRead = notification.read;
				// parse the timestamp
				let date = new Date(timestamp);
				let time = date.toLocaleString();
				let html;
				if (!isRead) {
					html = `<div class="dropdown-divider"></div>
					<a id="notification-${id}" href="#" class="dropdown-item" style="white-space: normal; display: flex; position: relative; padding: 4px 8px 28px 8px; align-items: center; gap: 12px; background-color: #f0f0f0;">
						<i class="fas fa-envelope mr-2 ml-2"></i>
						<div style="flex: 1;">${message}</div>
						<span class="text-muted text-sm" style="position: absolute; bottom: 4px; right: 12px;">${time}</span>
					</a>`;
				} else {
					html = `
					<div class="dropdown-divider"></div>
					<a id="notification-${id}" href="#" class="dropdown-item" style="white-space: normal; display: flex; position: relative; padding: 4px 8px 28px 8px; align-items: center; gap: 12px">
						<i class="fas fa-envelope mr-2 ml-2"></i>
						<div style="flex: 1;">${message}</div>
						<span class="text-muted text-sm" style="position: absolute; bottom: 4px; right: 12px;">${time}</span>
					</a>`;
				}
				// remove existing notification
				$(`#notification-${id}`).remove();
				$('#notificationList').prepend(html);
				// $('#notificationList a').first().click(function() {
				// 	onClick(data);
				// 	markNotificationAsRead(id);
				// });
				$(`#notification-${id}`).click(function() {
					console.log('clicked');
					onClick(data);
					markNotificationAsRead(notification);
				});
			}

			function markNotificationAsRead(notification) {
				$.ajax({
					url: "<?php echo site_url('notification/markAsRead'); ?>",
					type: "GET",
					data: {
						id: notification.id
					},
					success: function(response) {
						response = JSON.parse(response);
						console.log(response);
						if (response.msg === 'SUCCESS') {
							notification.read = true;
							// re-render the notification
							renderNotification(notification);
							saveNotification(notification);
						}
					}
				});
			}

			function fetchNotifications() {
				// check for jwt expiration, jwt is in seconds since epoch
				let now = new Date().getTime() / 1000;
				if (now > jwtExpiration) {
					// redirect to login
					console.log('jwt expired, redirecting to login (' +now + ' > ' + jwtExpiration + ')');
					window.location = loginRedirect;
					return;
				}
				let lastNotificationId = getLastNotificationId();
				if (lastNotificationId) {
					$.ajax({
						url: "<?php echo site_url('notification/list'); ?>",
						type: "GET",
						data: {
							after: lastNotificationId
						},
						success: function(response) {
							response = JSON.parse(response);
							if (response.notifications.length > 0) {
								let maxId = lastNotificationId;
								for (let notification of response.notifications) {
									if (notification.id > maxId) {
										maxId = notification.id;
									}
									onNotificationReceived(notification);
								}
								setLastNotificationId(maxId);
							}
						}
					});
				} else {
					// get the first notification for the first time
					$.ajax({
						url: "<?php echo site_url('notification/history'); ?>",
						type: "GET",
						success: function(response) {
							response = JSON.parse(response);
							if (response.notifications.length <= 0) {
								return;
							}
							let maxId = 0;
							for (let notification of response.notifications) {
								if (notification.id > maxId) {
									maxId = notification.id;
								}
							}
							setLastNotificationId(maxId);
						}
					});
				}
			}

			function renderSavedNotifications() {
				let notifications = localStorage.getItem('notifications');
				if (notifications) {
					notifications = JSON.parse(notifications);
					for (let id in notifications) {
						let notification = notifications[id];
						renderNotification(notification);
					}
				}
				let unread = localStorage.getItem('unreadNotifications');
				if (unread) {
					totalUnreadNotifications = parseInt(unread);
					changeNotificationBadge(totalUnreadNotifications === 0 ? null : totalUnreadNotifications);
				}
				// check if its empty, then create an empty component
				if ($('#notificationList').children().length === 0) {
					$('#notificationList').append('<a href="#" class="dropdown-item" id="emptyNotificationItem">No notifications</a>');
				}
			}

			function clearSavedNotifications() {
				localStorage.removeItem('notifications');
			}

			function resetNotification() {
				setLastNotificationId(1);
				clearSavedNotifications();
			}

			function readNotificationCount() {
				totalUnreadNotifications = 0;
				changeNotificationBadge(null);
				// reset the unread notification count
				localStorage.setItem('unreadNotifications', '0');
			}

			//let url = "<?php //echo site_url('notification/get'); ?>//";
			$(document).ready(function() {
				renderSavedNotifications();
				fetchNotifications();
				// interval to fetch notification every 5 seconds
				setInterval(fetchNotifications, 5000);

				// on open notification dropdown, read notification count
				let notificationDropdown = $('#notificationList').parent();
				notificationDropdown.on('show.bs.dropdown', function() {
					readNotificationCount();
				});
			})
		</script>
      <li class="nav-item dropdown">
          <a class="nav-link" data-toggle="dropdown" href="#">
              <i class="far fa-bell"></i>
          </a>
		  <span class="badge badge-danger navbar-badge" id="notificationBadge"></span>
          <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right" id="notificationList" style="max-height: 400px; overflow-y: auto;">
          </div>
      </li>
      <!-- Messages Dropdown Menu -->
      <li class="nav-item dropdown">
        <a class="nav-link" data-toggle="dropdown" href="#">
            <i class="far fa-user"></i>&nbsp;&nbsp;<?php echo $this->session->userdata('nama'); ?>
        </a>
        <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right">
          <a href="<?php echo site_url('profil/update'); ?>" class="dropdown-item dropdown-footer">Profile</a>
          <div class="dropdown-divider"></div>
          <a href="<?php echo site_url('profil/changepassword'); ?>" class="dropdown-item dropdown-footer">Change Password</a>
          <div class="dropdown-divider"></div>
          <a href="<?php echo site_url('profil/telegram'); ?>" class="dropdown-item dropdown-footer">Telegram Notification</a>
          <div class="dropdown-divider"></div>
          <a href="<?php echo site_url('login/out'); ?>" class="dropdown-item dropdown-footer">Logout</a>
        </div>
      </li>
    </ul>
</nav>
<!-- /.navbar -->


    <!-- Main Sidebar Container -->
    <aside class="main-sidebar sidebar-dark-primary elevation-4">
      <!-- Brand Logo -->
      <a href="<?php echo site_url('dashboard'); ?>" class="brand-link">
        <img src="<?php echo site_url('img/logo.png'); ?>" alt="PT. Surya Energi Indotama"
          class="brand-image elevation-3">
        <span class="brand-text font-weight-light">Office</span>
      </a>

      <!-- Sidebar -->
      <div class="sidebar">

        <!-- Sidebar Menu -->
        <nav class="mt-2">
          <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">
            <!-- Add icons to the links using the .nav-icon class
               with font-awesome or any other icon font library -->
          <li class="nav-item">
            <a href="<?php echo site_url('dashboard'); ?>" class="nav-link <?php if($page == 'dashboard') echo 'active'; ?>">
              <i class="nav-icon fas fa-home"></i>
              <p>
                Home                
              </p>
            </a>
          </li>

          <?php if($permission->master): ?>
            <li class="nav-item <?php if($page == 'notif' || $page == 'karyawan' || $page == 'libur' || $page == 'lokasi' || $page == 'roles' || $page == 'ruangan' || $page == 'cuti') echo 'menu-open'; ?>">
              <a href="#" class="nav-link">
                <i class="nav-icon fas fa-server"></i>
                <p>
                  Master Data
                  <i class="right fas fa-angle-left"></i>
                </p>
              </a>
              <ul class="nav nav-treeview">
                <?php if($permission->master_hari_libur): ?>
                  <li class="nav-item">
                    <a href="<?php echo site_url('master/libur'); ?>" class="nav-link <?php if($page == 'libur') echo 'active'; ?>">
                      <i class="fas fa-check nav-icon"></i>
                      <p>Hari Libur Nasional</p>
                    </a>
                  </li>
                <?php endif; ?>
                <?php if($permission->master_lokasi_absensi): ?>
                  <li class="nav-item">
                    <a href="<?php echo site_url('master/lokasi'); ?>" class="nav-link <?php if($page == 'lokasi') echo 'active'; ?>">
                      <i class="fas fa-check nav-icon"></i>
                      <p>Lokasi Absensi</p>
                    </a>
                  </li>
                <?php endif; ?>
                <?php if($permission->master_roles): ?>
                  <li class="nav-item">
                    <a href="<?php echo site_url('master/roles'); ?>" class="nav-link <?php if($page == 'roles') echo 'active'; ?>">
                      <i class="fas fa-check nav-icon"></i>
                      <p>Roles</p>
                      </a>
                  </li>
                <?php endif; ?>
                <?php if($permission->master_ruang): ?>
                  <li class="nav-item">
                    <a href="<?php echo site_url('master/ruangan'); ?>" class="nav-link <?php if($page == 'ruangan') echo 'active'; ?>">
                      <i class="fas fa-check nav-icon"></i>
                      <p>Ruang Meeting</p>
                      </a>
                  </li>
                <?php endif; ?>
                <?php if($permission->master_cuti): ?>
                  <li class="nav-item">
                    <a href="<?php echo site_url('master/cuti'); ?>" class="nav-link <?php if($page == 'cuti') echo 'active'; ?>">
                      <i class="fas fa-check nav-icon"></i>
                      <p>Cuti & Izin</p>
                    </a>
                  </li>
                <?php endif; ?>
                <?php if($permission->master_notifikasi): ?>
                  <li class="nav-item">
                    <a href="<?php echo site_url('master/notifikasi'); ?>" class="nav-link <?php if($page == 'notif') echo 'active'; ?>">
                      <i class="fas fa-check nav-icon"></i>
                      <p>Notifikasi</p>
                      </a>
                  </li>
                <?php endif; ?>
              </ul>
            </li>
          <?php endif; ?>  

          <?php if($permission->absensi): ?>
            <li class="nav-item <?php if($page == 'absenrekap' || $page == 'absenrekapthl' || $page == 'absensaya' || $page == 'emailrekap' || $page == 'absenrekapcuti' || $page == 'empabsen' || $page == 'absenmentah' || $page == 'absenapproved') echo 'menu-open'; ?>">
              <a href="#" class="nav-link">
                <i class="nav-icon fas fa-clock"></i>
                <p>
                  Absensi
                  <i class="right fas fa-angle-left"></i>
                </p>
              </a>
              <ul class="nav nav-treeview">
                <?php if($permission->absensi_dashboard): ?>
                  <li class="nav-item">
                    <a href="<?php echo site_url('absen/emp'); ?>" class="nav-link <?php if($page == 'empabsen') echo 'active'; ?>">
                      <i class="fas fa-check nav-icon"></i>
                      <p>Dashboard</p>
                    </a>
                  </li>
                <?php endif; ?>
                <?php if($permission->absensi_data_mentah): ?>
                  <li class="nav-item">
                    <a href="<?php echo site_url('absen/mentah'); ?>" class="nav-link <?php if($page == 'absenmentah') echo 'active'; ?>">
                      <i class="fas fa-check nav-icon"></i>
                      <p>Data Mentah</p>
                    </a>
                  </li>
                <?php endif; ?>
                <?php if($permission->absensi_rekap): ?>
                  <li class="nav-item <?php if ($page == 'absenrekap' || $page == 'absenrekapthl' || $page == 'emailrekap' || $page == 'absenrekapcuti') echo 'menu-open'; ?>">
                    <a href="#" class="nav-link">
                      <i class="fas fa-check nav-icon"></i>
                      <p>
                        Rekap
                        <i class="right fas fa-angle-left"></i>
                      </p>
                    </a>
                    <ul class="nav nav-treeview">
                      <li class="nav-item">
                        <a href="<?php echo site_url('absen/rekap'); ?>"
                          class="nav-link <?php if ($page == 'absenrekap')
                            echo 'active'; ?>">
                          <i class="far fa-dot-circle nav-icon"></i>
                          <p>Rekap Absen Karyawan</p>
                        </a>
                      </li>
                      <li class="nav-item">
                        <a href="<?php echo site_url('absen/rekapthl'); ?>"
                          class="nav-link <?php if ($page == 'absenrekapthl')
                            echo 'active'; ?>">
                          <i class="far fa-dot-circle nav-icon"></i>
                          <p>Rekap Absen THL</p>
                        </a>
                      </li>
                      <li class="nav-item">
                        <a href="<?php echo site_url('absen/emailrekap'); ?>"
                          class="nav-link <?php if ($page == 'emailrekap')
                            echo 'active'; ?>">
                          <i class="far fa-dot-circle nav-icon"></i>
                          <p>Kirim Email</p>
                        </a>
                      </li>
                      <li class="nav-item">
                        <a href="<?php echo site_url('absen/rekapcuti'); ?>"
                          class="nav-link <?php if ($page == 'absenrekapcuti')
                            echo 'active'; ?>">
                          <i class="far fa-dot-circle nav-icon"></i>
                          <p>Rekap Cuti</p>
                        </a>
                      </li>
                    </ul>
                  </li>
                <?php endif; ?>
                
                <?php if($permission->absensi_approve): ?>
                  <li class="nav-item">
                    <a href="<?php echo site_url('absen/approved'); ?>" class="nav-link <?php if ($page == 'absenapproved')
                          echo 'active'; ?>">
                      <i class="fas fa-check nav-icon"></i>
                      <p>Need Approve</p>
                    </a>
                  </li>
                <?php endif; ?>
                
                <?php if($permission->absensi_saya): ?>
                  <li class="nav-item">
                    <a href="<?php echo site_url('absen/saya'); ?>"
                      class="nav-link <?php if ($page == 'absensaya')
                        echo 'active'; ?>">
                      <i class="fas fa-check nav-icon"></i>
                      <p>Absen Saya</p>
                    </a>
                  </li>
                <?php endif; ?>
              </ul>
            </li>
          <?php endif; ?>
          
          <!-- Menu Izin dan Cuti -->
          <?php if($permission->cuti): ?>
          <li class="nav-item <?php if($page == 'jatahcuti' || $page == 'pengajuan' || $page == 'statuspengajuanizincuti' || $page == 'approvalpengajuanizincuti') echo 'menu-open'; ?>">
            <a href="#" class="nav-link">
              <i class="nav-icon fas fa-calendar"></i>
              <p>
                Izin dan Cuti
                <i class="right fas fa-angle-left"></i>
              </p>
            </a>
            <ul class="nav nav-treeview">
              <?php if($permission->cuti_jatah): ?>
              <li class="nav-item">
                <a href="<?php echo site_url('izincuti/jatahCuti'); ?>" class="nav-link <?php if($page == 'jatahcuti') echo 'active'; ?>">
                  <i class="fas fa-check nav-icon"></i>
                  <p>Jatah Cuti Karyawan</p>
                </a>
              </li>
              <?php endif; ?>
              <?php if($permission->cuti_saya): ?>
              <li class="nav-item">
                <a href="<?php echo site_url('izincuti/statuspengajuanizincuti'); ?>" class="nav-link <?php if($page == 'statuspengajuanizincuti') echo 'active'; ?>">
                  <i class="fas fa-check nav-icon"></i>
                  <p>Izin dan Cuti Saya</p>
                </a>
              </li>
              <?php endif; ?>
              <?php if($permission->cuti_approve): ?>
              <li class="nav-item">
                  <a
                    href="<?php echo site_url('izincuti/approvalpengajuanizincuti'); ?>"
                    class="nav-link <?php if($page == 'approvalpengajuanizincuti') echo 'active'; ?>">
                    <i class="fas fa-check nav-icon"></i>
                    <p>Approval Izin dan Cuti</p>
                  </a>
              </li>
              <?php endif; ?>
            </ul>
          </li>
          <?php endif; ?>
          <!-- End Menu Izin dan Cuti -->

          <?php if($permission->karyawan): ?>
            <li
              class="nav-item <?php if ($page == 'kontraklist' || $page == 'dashkaryawan' || $page == 'karyawan' || $page == 'kontrakeval' || $page == 'slipgaji')
                echo 'menu-open'; ?>">
              <a href="#" class="nav-link">
                <i class="nav-icon fas fa-id-card-alt"></i>
                <p>
                  Karyawan
                  <i class="right fas fa-angle-left"></i>
                </p>
              </a>
              <ul class="nav nav-treeview">
              <?php if($permission->karyawan_dashboard): ?>
                  <li class="nav-item">
                    <a href="<?php echo site_url('karyawan/dashboard'); ?>"
                      class="nav-link <?php if ($page == 'dashkaryawan')
                        echo 'active'; ?>">
                      <i class="fas fa-check nav-icon"></i>
                      <p>Dashboard</p>
                    </a>
                  </li>
                <?php endif; ?>
                <?php if($permission->karyawan_data): ?>
                  <li class="nav-item">
                    <a href="<?php echo site_url('karyawan/list'); ?>"
                      class="nav-link <?php if ($page == 'karyawan')
                        echo 'active'; ?>">
                      <i class="fas fa-check nav-icon"></i>
                      <p>Data Karyawan</p>
                    </a>
                  </li>
                <?php endif; ?>
                <?php if($permission->karyawan_data): ?>
                  <li class="nav-item">
                    <a href="<?php echo site_url('karyawan/slipgaji'); ?>"
                      class="nav-link <?php if ($page == 'slipgaji')
                        echo 'active'; ?>">
                      <i class="fas fa-check nav-icon"></i>
                      <p>Slip Gaji</p>
                    </a>
                  </li>
                <?php endif; ?>
              </ul>
            </li>
          <?php endif; ?>

          <?php if($permission->pmo): ?>
            <li
              class="nav-item <?php if ($page == 'dashproyek' || $page == 'budget' || $page == 'dpb' || $page == 'purchase')
                echo 'menu-open'; ?>">
              <a href="#" class="nav-link">
                <i class="nav-icon fas fa-chart-line"></i>
                <p>
                  Project Monitoring
                  <i class="right fas fa-angle-left"></i>
                </p>
              </a>
              <ul class="nav nav-treeview">
                <?php if($permission->pmo_project): ?>
                  <li class="nav-item">
                    <a href="<?php echo site_url('proyek/dashboard'); ?>"
                      class="nav-link <?php if ($page == 'dashproyek')
                        echo 'active'; ?>">
                      <i class="fas fa-check nav-icon"></i>
                      <p>Project</p>
                    </a>
                  </li>
                <?php endif; ?>
                <?php if($permission->pmo_anggaran): ?>
                  <li class="nav-item">
                    <a href="<?php echo site_url('budget/list'); ?>"
                      class="nav-link <?php if ($page == 'budget')
                        echo 'active'; ?>">
                      <i class="fas fa-check nav-icon"></i>
                      <p>Anggaran</p>
                    </a>
                  </li>
                <?php endif; ?>
                <?php if($permission->pmo_dpbj): ?>
                  <li class="nav-item">
                    <a href="<?php echo site_url('dpb/list'); ?>"
                      class="nav-link <?php if ($page == 'dpb')
                        echo 'active'; ?>">
                      <i class="fas fa-check nav-icon"></i>
                      <p>DPB/DPJ</p>
                    </a>
                  </li>
                <?php endif; ?>
                <?php if($permission->pmo_po): ?>
                  <li class="nav-item">
                    <a href="<?php echo site_url('purchase/list'); ?>"
                      class="nav-link <?php if ($page == 'purchase')
                        echo 'active'; ?>">
                      <i class="fas fa-check nav-icon"></i>
                      <p>Purchase Order</p>
                    </a>
                  </li>
                <?php endif; ?>
              </ul>
            </li>
          <?php endif; ?>

          <?php if($permission->nav): ?>
            <li
              class="nav-item <?php if ($page == 'dashnav' || $page == 'navdpb' || $page == 'navdpj' || $page == 'navbudget' || $page == 'navbapb' || $page == 'navsppd' || $page == 'navum' || $page == 'navumm' || $page == 'navpjum')
                echo 'menu-open'; ?>">
              <a href="#" class="nav-link">
                <i class="nav-icon fab fa-microsoft"></i>
                <p>
                  NAV Document
                  <i class="right fas fa-angle-left"></i>
                </p>
              </a>
              <ul class="nav nav-treeview">
              <?php if($permission->nav_dashboard): ?>
                <li class="nav-item">
                  <a href="<?php echo site_url('nav/dash'); ?>"
                    class="nav-link <?php if ($page == 'dashnav')
                      echo 'active'; ?>">
                    <i class="fas fa-check nav-icon"></i>
                    <p>Dashboard</p>
                  </a>
                </li>
              <?php endif; ?>
              <?php if($permission->nav_budget): ?>
                <li class="nav-item">
                  <a href="<?php echo site_url('nav/budget'); ?>"
                    class="nav-link <?php if ($page == 'navbudget')
                      echo 'active'; ?>">
                    <i class="fas fa-check nav-icon"></i>
                    <p>Budget Perubahan</p>
                  </a>
                </li>
              <?php endif; ?>
              <?php if($permission->nav_dpb): ?>
                <li class="nav-item">
                  <a href="<?php echo site_url('nav/dpb'); ?>"
                    class="nav-link <?php if ($page == 'navdpb')
                      echo 'active'; ?>">
                    <i class="fas fa-check nav-icon"></i>
                    <p>Daftar Pembelian Barang</p>
                  </a>
                </li>
              <?php endif; ?>
              <?php if($permission->nav_dpj): ?>
                <li class="nav-item">
                  <a href="<?php echo site_url('nav/dpj'); ?>"
                    class="nav-link <?php if ($page == 'navdpj')
                      echo 'active'; ?>">
                    <i class="fas fa-check nav-icon"></i>
                    <p>Daftar Pembelian Jasa</p>
                  </a>
                </li>
              <?php endif; ?>
              <?php if($permission->nav_bapb): ?>
                <li class="nav-item">
                  <a href="<?php echo site_url('nav/bapb'); ?>"
                    class="nav-link <?php if ($page == 'navbapb')
                      echo 'active'; ?>">
                    <i class="fas fa-check nav-icon"></i>
                    <p>Penerimaan Barang</p>
                  </a>
                </li>
              <?php endif; ?>
              <?php if($permission->nav_sppd): ?>
                <li class="nav-item">
                  <a href="<?php echo site_url('nav/sppd'); ?>"
                    class="nav-link <?php if ($page == 'navsppd')
                      echo 'active'; ?>">
                    <i class="fas fa-check nav-icon"></i>
                    <p>Perjalanan Dinas</p>
                  </a>
                </li>
              <?php endif; ?>
              <?php if($permission->nav_um): ?>
                <li class="nav-item">
                  <a href="<?php echo site_url('nav/um'); ?>"
                    class="nav-link <?php if ($page == 'navum')
                      echo 'active'; ?>">
                    <i class="fas fa-check nav-icon"></i>
                    <p>Uang Muka</p>
                  </a>
                </li>
              <?php endif; ?>
              <?php if($permission->nav_umm): ?>
                <li class="nav-item">
                  <a href="<?php echo site_url('nav/ummulti'); ?>"
                    class="nav-link <?php if ($page == 'navumm')
                      echo 'active'; ?>">
                    <i class="fas fa-check nav-icon"></i>
                    <p>Uang Muka Multiple</p>
                  </a>
                </li>
              <?php endif; ?>
              <?php if($permission->nav_pjum): ?>
                <li class="nav-item">
                  <a href="<?php echo site_url('nav/pjum'); ?>"
                    class="nav-link <?php if ($page == 'navpjum')
                      echo 'active'; ?>">
                    <i class="fas fa-check nav-icon"></i>
                    <p>PJ Uang Muka</p>
                  </a>
                </li>
              <?php endif; ?>
              </ul>
            </li>
          <?php endif; ?>

          <?php if($permission->opsk): ?>
            <li
              class="nav-item <?php if ($page == 'dashcar' || $page == 'carorder' || $page == 'carorderrekap' || $page == 'carunit' || $page == 'carorderna')
                echo 'menu-open'; ?>">
              <a href="#" class="nav-link">
                <i class="nav-icon fas fa-car"></i>
                <p>
                  Kendaraan Operasional
                  <i class="right fas fa-angle-left"></i>
                </p>
              </a>
              <ul class="nav nav-treeview">
              <?php if($permission->opsk_kendaraan): ?>
                <li class="nav-item">
                  <a href="<?php echo site_url('mobil/unit'); ?>"
                    class="nav-link <?php if ($page == 'carunit')
                      echo 'active'; ?>">
                    <i class="fas fa-check nav-icon"></i>
                    <p>Kendaraan</p>
                  </a>
                </li>
              <?php endif; ?>
              <?php if($permission->opsk_order): ?>
                <li class="nav-item">
                  <a href="<?php echo site_url('order/list'); ?>"
                    class="nav-link <?php if ($page == 'carorder')
                      echo 'active'; ?>">
                    <i class="fas fa-check nav-icon"></i>
                    <p>Order</p>
                  </a>
                </li>
              <?php endif; ?>
              <?php if($permission->opsk_rekap_order): ?>
                <li class="nav-item">
                  <a href="<?php echo site_url('order/rekap'); ?>"
                    class="nav-link <?php if ($page == 'carorderrekap')
                      echo 'active'; ?>">
                    <i class="fas fa-check nav-icon"></i>
                    <p>Rekap Order</p>
                  </a>
                </li>
              <?php endif; ?>
              <?php if($permission->opsk_approval): ?>
                <li class="nav-item">
                  <a href="<?php echo site_url('order/na'); ?>"
                    class="nav-link <?php if ($page == 'carorderna')
                      echo 'active'; ?>">
                    <i class="fas fa-check nav-icon"></i>
                    <p>Need Approve Order</p>
                  </a>
                </li>
              <?php endif; ?>
              </ul>
            </li>
          <?php endif; ?>

          <li class="nav-item">
            <a href="<?php echo site_url('hadir'); ?>"
              class="nav-link <?php if ($page == 'daftarhadir')
                echo 'active'; ?>">
              <i class="nav-icon fas fa-clipboard-list"></i>
              <p>
                Event / Kegiatan
              </p>
            </a>
          </li>

          <li class="nav-item">
                  <a href="<?php echo site_url('dokumen'); ?>"
                    class="nav-link <?php if ($page == 'docnav')
                      echo 'active'; ?>">
                    <i class="fas fa-file nav-icon"></i>
                    <p>Dokumen Perusahaan</p>
                  </a>
                </li>

        </ul>
      </nav>
      <!-- /.sidebar-menu -->
      </div>
      <!-- /.sidebar -->
    </aside>

    <!-- Content Wrapper. Contains page content -->


