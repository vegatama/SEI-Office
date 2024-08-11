<?php
require('./dist/fpdf/fpdf.php');

$pdf = new FPDF();
$pdf->AddPage();
$pdf->Image('./img/logo-len.png',10,6,30);
$pdf->Image('./img/logo-sei.png',170,6,30);
$pdf->SetFont('Arial','BU',14);
$pdf->Cell(0,40,'DAFTAR HADIR',0,1,'C');
$pdf->AddFont('Calibri','','calibri.php');
$pdf->SetFont('Calibri','',10);
$pdf->SetY(50);
$pdf->Cell(1,0,'Kegiatan');
$pdf->Cell(50);
$pdf->Cell(1,0,':');
$pdf->Cell(5);
$pdf->Cell(5,0,$hadir->kegiatan);
$pdf->SetY(55);
$pdf->Cell(1,0,'Subyek');
$pdf->Cell(50);
$pdf->Cell(1,0,':');
$pdf->Cell(5);
$pdf->Cell(5,0,$hadir->subyek);
$pdf->SetY(60);
$pdf->Cell(1,0,'Tanggal');
$pdf->Cell(50);
$pdf->Cell(1,0,':');
$pdf->Cell(5);
$pdf->Cell(5,0,$hadir->tanggal);
$pdf->SetY(65);
$pdf->Cell(1,0,'Trainer / Pimpinan Rapat');
$pdf->Cell(50);
$pdf->Cell(1,0,':');
$pdf->Cell(5);
$pdf->Cell(5,0,$hadir->pimpinan);

//tabel
$pdf->SetY(75);
// Column widths
$w = array(10, 40, 40, 55, 45);
// Header
$pdf->SetFont('Arial','B',10);
$header = array('No','Nama','Departemen / Bagian','Email / Phone','Tanda Tangan');
for($i=0;$i<count($header);$i++)
    $pdf->Cell($w[$i],7,$header[$i],1,0,'C');
$pdf->Ln();
// Data
$pdf->SetFont('Arial','',10);
$i=1;
if(count($hadir->data_peserta) > 0){
    foreach($hadir->data_peserta as $dt){
        $arr = array($i,$dt->nama,$dt->bagian,$dt->email_phone,'digitally signed: '.$dt->tanda_tangan);
        $data[] = $arr;
        $i++;
    }
    //print_r($data);
    foreach($data as $row)
    {
        $pdf->Cell($w[0],6,$row[0],'LR',0,'C');
        $pdf->Cell($w[1],6,$row[1],'LR',0,'C');
        $pdf->Cell($w[2],6,$row[2],'LR',0,'C');
        $pdf->Cell($w[3],6,$row[3],'LR',0,'C');
        $pdf->Cell($w[4],6,$row[4],'LR',0,'C');
        $pdf->Ln();
    }
    // Closing line
    $pdf->Cell(array_sum($w),0,'','T');
}

$pdf->Output();

?>