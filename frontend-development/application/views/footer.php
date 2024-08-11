  <!-- Main Footer -->
  <footer class="main-footer">
    <!-- Default to the left -->
    &copy; 2022 <a href="https://www.suryaenergi.com" target="_blank">PT. Surya Energi Indotama</a>.
  </footer>
</div>
<!-- ./wrapper -->

<!-- REQUIRED SCRIPTS -->

<!-- jQuery -->
<script src="<?php echo site_url('plugins/jquery/jquery.min.js'); ?>"></script>
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<!-- Bootstrap 4 -->
<script src="<?php echo site_url('plugins/bootstrap/js/bootstrap.bundle.min.js'); ?>"></script>
<!-- AdminLTE App -->
<script src="<?php echo site_url('dist/js/adminlte.min.js'); ?>"></script>
<!-- DataTables  & Plugins -->
<script src="<?php echo site_url('plugins/datatables/jquery.dataTables.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/datatables-bs4/js/dataTables.bootstrap4.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/datatables-responsive/js/dataTables.responsive.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/datatables-responsive/js/responsive.bootstrap4.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/datatables-buttons/js/dataTables.buttons.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/datatables-buttons/js/buttons.bootstrap4.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/jszip/jszip.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/pdfmake/pdfmake.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/pdfmake/vfs_fonts.js'); ?>"></script>
<script src="<?php echo site_url('plugins/datatables-buttons/js/buttons.html5.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/datatables-buttons/js/buttons.print.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/datatables-buttons/js/buttons.colVis.min.js'); ?>"></script>

<!-- fullCalendar 2.2.5 -->
<script src="<?php echo site_url('plugins/moment/moment.min.js'); ?>"></script>
<script src="<?php echo site_url('plugins/fullcalendar/main.js'); ?>"></script>

<!-- Tempusdominus Bootstrap 4 -->
<script src="<?php echo site_url('plugins/tempusdominus-bootstrap-4/js/tempusdominus-bootstrap-4.min.js'); ?>"></script>

<!-- Toastr -->
<script src="<?php echo site_url('plugins/toastr/toastr.min.js'); ?>"></script>

<?php if(isset($info)): ?>
<script>
	// toaster info
  $(function() {
    toastr.info('<?php echo $info; ?>')
  })
</script>
<?php endif; ?>
<?php if(isset($success)): ?>
<script>
	// toaster success
  $(function() {
    toastr.success('<?php echo $success; ?>')
  })
</script>
<?php endif; ?>
<?php if(isset($error)): ?>
<script>
	// toaster error
	$(function() {
		toastr.error('<?php echo $error; ?>')
	})
</script>
<?php endif; ?>

<script>
  $(function () {
    $("#example1").DataTable({
      "pageLength": 30,
      "responsive": true, "lengthChange": false, "autoWidth": false, "paging": false, "info": false,
      "buttons": ["copy", "csv", "excel", "pdf", "print"]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');

    $("#karyawan").DataTable({
      "pageLength": 50,
      "responsive": true, "lengthChange": false, "autoWidth": false,
      "buttons": ["copy", "csv", "excel", "pdf", "print"]
    }).buttons().container().appendTo('#karyawan_wrapper .col-md-6:eq(0)');

    $("#karyawandesc").DataTable({
      "pageLength": 50,
      "responsive": true, "lengthChange": false, "autoWidth": false,
      "buttons": ["copy", "csv", "excel", "pdf", "print"],
      "order": [[0, 'desc']]
    }).buttons().container().appendTo('#karyawandesc_wrapper .col-md-6:eq(0)');
	$("#statusdesc").DataTable({
	  "pageLength": 50,
	  "responsive": true, "lengthChange": false, "autoWidth": false,
	  "buttons": ["copy", "csv", "excel", "pdf", "print"],
	  "order": [[5, 'desc']]
	}).buttons().container().appendTo('#statusdesc_wrapper .col-md-6:eq(0)');

    $("#karyawanlist").DataTable({
      "pageLength": 50,
      "responsive": true, "lengthChange": false, "autoWidth": false,
      "buttons": ["copy", "csv", "excel", "pdf", "print"],
      "order": [[1, 'asc']]
    }).buttons().container().appendTo('#karyawanlist_wrapper .col-md-6:eq(0)');

    $("#rekap").DataTable({
      "pageLength": 50,
      "responsive": true, "lengthChange": false, "autoWidth": true,
      "buttons": ["copy", "csv", "excel", "pdf", "print"]
    }).buttons().container().appendTo('#rekap_wrapper .col-md-6:eq(0)');
  });

  function unchecked(){
    $('input:checkbox').removeAttr('checked');
  }
</script>

<?php if($page == "dashproyek"): ?>
  <script>
    $(function () {
      $("#proyek").DataTable({
        "pageLength": 50,
        "responsive": true, "lengthChange": false, "autoWidth": false,
        "buttons": ["copy", "csv", "excel", "pdf", "print"],
        "order": [[1, 'asc']]
      }).buttons().container().appendTo('#proyek_wrapper .col-md-6:eq(0)');
    });
  </script>
<?php endif; ?>

<?php if($page == "budget"): ?>
  <script>
    $(function () {
      $("#budget").DataTable({
        "pageLength": 50,
        "responsive": true, "lengthChange": false, "autoWidth": false,
        "buttons": ["copy", "csv", "excel", "pdf", "print"],
        "order": [[0, 'asc']]
      }).buttons().container().appendTo('#budget_wrapper .col-md-6:eq(0)');
    });
  </script>
<?php endif; ?>

<?php if($page == "dpb"): ?>
  <script>
    $(function () {
      $("#dpb").DataTable({
        "pageLength": 50,
        "responsive": true, "lengthChange": false, "autoWidth": false,
        "buttons": ["copy", "csv", "excel", "pdf", "print"],
        "order": [[5, 'desc']]
      }).buttons().container().appendTo('#dpb_wrapper .col-md-6:eq(0)');
    });
  </script>
<?php endif; ?>

<?php if($page == "purchase"): ?>
  <script>
    $(function () {
      $("#po").DataTable({
        "pageLength": 50,
        "responsive": true, "lengthChange": false, "autoWidth": false,
        "buttons": ["copy", "csv", "excel", "pdf", "print"]
      }).buttons().container().appendTo('#po_wrapper .col-md-6:eq(0)');
    });
  </script>
<?php endif; ?>

<?php if($page == "daftarhadir"): ?>
  <!-- Summernote -->
  <script src="<?php echo site_url('plugins/summernote/summernote-bs4.min.js'); ?>"></script>
  <script>
    $(function () {
      $("#hadir").DataTable({
        "pageLength": 25,
        "responsive": true, "lengthChange": false, "autoWidth": false,
        "buttons": ["copy", "csv", "excel", "pdf", "print"],
        "order": [[0, 'desc']]
      }).buttons().container().appendTo('#hadir_wrapper .col-md-6:eq(0)');
    });

    $(function (){
      //Timepicker
      $('#timepicker').datetimepicker({
        format:'HH:mm',
        focusOnShow: false
      });

      $('#timepicker2').datetimepicker({
        format:'HH:mm',
        focusOnShow: false
      });

      $('#timepicker3').datetimepicker({
        format:'HH:mm',
        focusOnShow: false
      });
    });

    $(function () {
      // Summernote
      $('#summernote').summernote({
        height: 250
      })

      $('#summernoterisalah').summernote({
        height: 500
      })
    })
  </script>
<?php endif; ?>

<?php if(str_contains($page, 'nav')): ?>
  <script>
    $(function () {
      $("#nav").DataTable({
        "pageLength": 50,
        "responsive": true, "lengthChange": false, "autoWidth": false,
        "buttons": ["copy", "csv", "excel", "pdf", "print"]
      }).buttons().container().appendTo('#nav_wrapper .col-md-6:eq(0)');
    });

    $(function () {
      $("#nava").DataTable({
        "pageLength": 50,
        "responsive": true, "lengthChange": false, "autoWidth": false,
        "buttons": ["copy", "csv", "excel", "pdf", "print"],
        "order": [[0, 'desc']]
      }).buttons().container().appendTo('#nav2_wrapper .col-md-6:eq(0)');
    });
  </script>
<?php endif; ?>

<?php if(str_contains($page, 'car')): ?>
  <script>
    $(function () {
      $("#car").DataTable({
        "pageLength": 50,
        "responsive": true, "lengthChange": false, "autoWidth": false,
        "buttons": ["copy", "csv", "excel", "pdf", "print"]
      }).buttons().container().appendTo('#car_wrapper .col-md-6:eq(0)');
    });
    $(function () {
      $("#carna").DataTable({
        "pageLength": 50,
        "responsive": true, "lengthChange": false, "autoWidth": false,
        "buttons": ["copy", "csv", "excel", "pdf", "print"]
      }).buttons().container().appendTo('#carna_wrapper .col-md-6:eq(0)');
    });
    $(function () {
      $("#carapp").DataTable({
        "pageLength": 50,
        "responsive": true, "lengthChange": false, "autoWidth": false,
        "buttons": ["copy", "csv", "excel", "pdf", "print"]
      }).buttons().container().appendTo('#carapp_wrapper .col-md-6:eq(0)');
    });

    $(function (){
      //Timepicker
      $('#timepicker').datetimepicker({
        format:'HH:mm',
        focusOnShow: false
      });
      $('#timepicker3').datetimepicker({
        format:'HH:mm',
        focusOnShow: false
      });
    });
  </script>
<?php endif; ?>

<script>
  $( function() {
    $('#datepicker').datepicker({ dateFormat: 'dd/mm/yy' });
    $('#datepicker2').datepicker({ dateFormat: 'dd/mm/yy' });
    $('#datepicker3').datepicker({ dateFormat: 'dd/mm/yy' });
    $('#datepicker4').datepicker({ dateFormat: 'dd/mm/yy' });
    $('#datepicker5').datepicker({ dateFormat: 'dd/mm/yy' });
    $('#datepicker6').datepicker({ dateFormat: 'dd/mm/yy' });
    $('#datepicker7').datepicker({ dateFormat: 'dd/mm/yy' });

    $('#datepickerpergi').datepicker({ dateFormat: 'yy-mm-dd' });
    $('#datepickerkembali').datepicker({ dateFormat: 'yy-mm-dd' });
  } );
</script>

<?php if($page == 'dashboard'): ?>

  <script>
  $(function () {

    /* initialize the external events
     -----------------------------------------------------------------*/
    function ini_events(ele) {
      ele.each(function () {

        // create an Event Object (https://fullcalendar.io/docs/event-object)
        // it doesn't need to have a start or end
        var eventObject = {
          title: $.trim($(this).text()) // use the element's text as the event title
        }

        // store the Event Object in the DOM element so we can get to it later
        $(this).data('eventObject', eventObject)

        // make the event draggable using jQuery UI

      })
    }

    ini_events($('#external-events div.external-event'))

    /* initialize the calendar
     -----------------------------------------------------------------*/
    //Date for the calendar events (dummy data)
    var date = new Date()
    var d    = date.getDate(),
        m    = date.getMonth(),
        y    = date.getFullYear()

    var Calendar = FullCalendar.Calendar;
    var calendarEl = document.getElementById('calendar');

    // initialize the external events
    // -----------------------------------------------------------------


    var calendar = new Calendar(calendarEl, {
      headerToolbar: {
        left  : 'prev,next today',
        center: 'title',
        right : 'dayGridMonth,timeGridWeek,timeGridDay'
      },
      initialView: 'timeGridWeek',
      themeSystem: 'bootstrap',
      //Random default events
      events: [
      <?php if(isset($birthday)): foreach($birthday as $bd): if($bd->birthday != ""): $bgc = rand(100000,999999); $tgl = explode("-",$bd->birthday); $str = date('Y')."-".$tgl[1]."-".$tgl[2]; ?>
        {
          title          : '<?php echo $bd->name; ?> birthday',
          start          : new Date('<?php echo $str; ?>'),
          backgroundColor: '#<?php echo $bgc; ?>', 
          borderColor    : '#<?php echo $bgc; ?>', 
          allDay         : true
        },
      <?php endif; endforeach; endif; ?>
      <?php if(isset($kegiatan)): foreach($kegiatan as $kg): 
        $bgc = rand(100000,999999); 
        $tgl = explode("/",$kg->tanggal);
        $bln = $tgl[1];
        if($bln[0] == 0){
          $bln = substr($bln,1);
        }
        $jam_mulai = explode(":",$kg->waktu_mulai);
        $jam_selesai = explode(":",$kg->waktu_selesai); 
        $str_mulai = date('Y')."-".$tgl[1]."-".$tgl[0]." ".$jam_mulai[0].":".$jam_mulai[1];
        $str_selesai = date('Y')."-".$tgl[1]."-".$tgl[0]." ".$jam_selesai[0].":".$jam_selesai[1];
      ?>
        {
          title          : '<?php echo "Kegiatan: ".$kg->kegiatan.", Subyek: ".$kg->subyek.", Tempat: ".$kg->tempat;; ?>',
          start          : new Date('<?php echo $str_mulai; ?>'),
          end            : new Date('<?php echo $str_selesai; ?>'),
          backgroundColor: '#<?php echo $bgc; ?>', 
          borderColor    : '#<?php echo $bgc; ?>',
          allDay         : false
        },
      <?php endforeach; endif; ?>  
      ],
      editable  : false,
      droppable : false, // this allows things to be dropped onto the calendar !!!
      
    });

    calendar.render();
    // $('#calendar').fullCalendar()

    
  })
</script>
<?php endif; ?>

</body>
</html>
