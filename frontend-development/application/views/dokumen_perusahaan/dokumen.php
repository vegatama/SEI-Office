<?php $this->load->view('header'); ?>

<div class="content-wrapper">
<!-- Content Header (Page header) -->
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-9">
        <h1 class="m-0">Dokumen Perusahaan</h1>
      </div><!-- /.col -->
      <div class="col-sm-3">
        <ol class="breadcrumb float-sm-right">
          <li class="breadcrumb-item"><a href="<?php echo site_url('dashboard'); ?>">Home</a></li>
          <li class="breadcrumb-item">Nav Document</li>
          <li class="breadcrumb-item active">Dokumen</li>
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
        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title"><i class="fas fa-file-invoice"></i> Dokumen Perusahaan</h3>
            <a class="btn btn-secondary btn-sm float-sm-right" href="<?php echo site_url('dokumen/tambah'); ?>">
              <i class="fas fa-plus"></i>&nbsp; Tambah Dokumen
            </a>
          </div>
          <div class="card-body">
          <?php echo form_open('dokumen/dokumen'); ?>
          <div class="form-group">
              <label>Kategori</label>
              <div class="row">
              <div class="col-md-4">
              <select class="form-control select2" name="kategori" style="width: 90%;">
              <option value="ALL" <?php if('ALL' == $ktgr) echo "selected"; ?>>Semua Kategori</option>
              <option value="KEBIJAKAN" <?php if('KEBIJAKAN' == $ktgr) echo "selected"; ?>>KEBIJAKAN</option>
              <option value="PEDOMAN" <?php if('PEDOMAN' == $ktgr) echo "selected"; ?>>PEDOMAN</option>
              <option value="PROSEDUR" <?php if('PROSEDUR' == $ktgr) echo "selected"; ?>>PROSEDUR</option>
              <option value="INSTRUKSI_KERJA" <?php if('INSTRUKSI_KERJA' == $ktgr) echo "selected"; ?>>INSTRUKSI KERJA</option>
              <option value="FORMULIR" <?php if('FORMULIR' == $ktgr) echo "selected"; ?>>FORMULIR</option>
              <option value="TEMPLATE" <?php if('TEMPLATE' == $ktgr) echo "selected"; ?>>TEMPLATE</option>
              </select>
              </div>
              <div class="col-md-8">
              <button class="btn btn-primary" type="submit" name="tampil">Tampilkan Data</button>
              </div>
              </div>
            </div>
            <?php echo form_close(); ?><br/>

            <table id="nav" class="table table-bordered table-striped">
              <thead>
              <tr>
                <th>No. Dokumen</th>
                <th>Kategori</th>
                <th width="35%">Nama</th>
                <th width="5%">Tanggal Pengesahan</th>
                <th width="5%">Revisi</th>
                <th width="30%">&nbsp;</th>
              </tr>
              </thead>
              <tbody>
              <?php 
                if(isset($documents)):
                foreach($documents as $dt) : 
              ?>
              <tr>
                <td><?php echo $dt->documentNumber; ?></td>
                <td><?php echo $dt->category; ?></td>
                <td><?php echo $dt->documentName; ?></td>
                <td><?php echo $dt->approvalDate; ?></td>
                <td><?php echo $dt->revision; ?></td>
                <td class="col-3 text-center">
                  <a class="btn btn-success btn-sm" href="<?php echo $dt->fileUrl; ?>">
                    <i class="fas fa-download"></i> Download
                  </a>
                  <a class="btn btn-primary btn-sm" href="<?php echo site_url('dokumen/view/'.$dt->id); ?>" target="_blank">
                    <i class="fas fa-archive"></i> View
                  </a>
                  <?php if($user_empCode === $dt->uploaderEmployeeCode):?>
                  <a class="btn btn-warning btn-sm" href="<?php echo site_url('dokumen/edit/'.$dt->id); ?>">
                    <i class="fas fa-wrench"></i> Update
                  </a>
                  <a class="btn btn-danger btn-sm" href="<?php echo site_url('dokumen/delete/'.$dt->id); ?>">
                    <i class="fas fa-ban"></i> Delete
                  </a>
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
  </div>
  <!-- /.content -->
  </section>
</div>
<!-- /.content-wrapper -->

<?php $this->load->view('footer'); ?>
