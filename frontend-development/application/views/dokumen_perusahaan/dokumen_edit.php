<?php $this->load->view('header'); ?>
<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0">Edit Dokumen</h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item"><a href="<?php echo site_url('nav/dokumen'); ?>">Nav Document</a></li>
          <li class="breadcrumb-item active">Edit Dokumen</li>
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
        <div class="card card-primary">

          <div class="card-header">
            <h3 class="card-title">Data Dokumen</h3>
          </div>
          <!-- /.card-header -->
          
          <?php echo form_open('dokumen/update/'.$dataDokumen->id); ?>
          <div class="card-body">
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">No. Dokumen :</label>
              <input type="text" name="noDokumen" class="form-control col-5" value="<?php echo $dataDokumen->documentNumber;?>" required>
              <?php echo form_error('noDokumen','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Kategori :</label>              
              <select name="kategori" class="form-control col-2 js-single" required>
                <?php if($dataDokumen->category == "KEBIJAKAN"){?>
                  <option value="KEBIJAKAN" selected>KEBIJAKAN</option>
                  <?php }else{?>
                    <option value="KEBIJAKAN">KEBIJAKAN</option>
                  <?php }?>
                <?php if($dataDokumen->category == "PEDOMAN"){?>
                  <option value="PEDOMAN" selected>PEDOMAN</option>
                <?php }else{?>
                  <option value="PEDOMAN">PEDOMAN</option>
                <?php }?>
                <?php if($dataDokumen->category == "PROSEDUR"){?>
                  <option value="PROSEDUR" selected>PROSEDUR</option>
                <?php }else{?>
                  <option value="PROSEDUR">PROSEDUR</option>
                <?php }?>
                <?php if($dataDokumen->category == "INSTRUKSI_KERJA"){?>
                  <option value="INSTRUKSI_KERJA" selected>INSTRUKSI KERJA</option>
                <?php }else{?>
                  <option value="INSTRUKSI_KERJA">INSTRUKSI KERJA</option>
                <?php }?>
                <?php if($dataDokumen->category == "FORMULIR"){?>
                  <option value="FORMULIR" selected>FORMULIR</option>
                <?php }else{?>
                  <option value="FORMULIR">FORMULIR</option>
                <?php }?>
                <?php if($dataDokumen->category == "TEMPLATE"){?>
                  <option value="TEMPLATE" selected>TEMPLATE</option>
                <?php }else{?>
                  <option value="TEMPLATE">TEMPLATE</option>
                <?php }?>
              </select>
              <?php echo form_error('kategori','<div class="alert alert-warning">','</div>'); ?>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Nama Dokumen :</label>
              <input type="text" name="namaDokumen" class="form-control col-5" value="<?php echo $dataDokumen->documentName;?>" required>
              <?php echo form_error('namaDokumen','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Tanggal Pengesahan :</label>
              <div class="row px-2" >
                  <div class="col-2 px-0">
                    <div class="form-control" style="display:flex;align-items:center;justify-content:center;">
                      <i class="fa fa-calendar"></i>
                    </div>
                  </div>
                  <div class="col-8 px-0">
                    <input class="form-control datesingle" type="text" id="datesingle" name="datesingle" value="<?php echo $dataDokumen->approvalDate;?>"/>
                  </div>
                </div>
            </div>
            <div class="form-group row">
              <label class="col-sm-3 col-form-label">Revisi :</label>
              <input type="number" name="revisi" class="form-control col-2" value="<?php echo $dataDokumen->revision;?>" required>
              <?php echo form_error('revisi','<br/><div class="alert alert-warning">','</div>'); ?>
            </div>
          <div class="card-footer">
            <button type="submit" class="btn btn-primary">Simpan Data</button>
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

<script>
$(document).ready(function() {
  $('.js-single').select2({});
  $('.datesingle').daterangepicker({
      singleDatePicker: true,
      showDropdowns: true,
      minYear: parseInt(moment().format('YYYY'),10),
      maxYear: parseInt(moment().format('YYYY'),10)+1,
      locale: {
                format: 'YYYY-MM-DD'
            }
    });
});
</script>