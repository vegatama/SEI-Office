<?php
require_once './dist/dompdf/autoload.inc.php'; 
use Dompdf\Dompdf;
use Dompdf\Options;

$html = '<html>
<head>
    <title>Daftar Hadir Kegiatan '.$hadir->subyek.'</title>
    <style>
        .header {
            font-family: "Arial";
            font-size: 14pt;
            font-weight: bold;
            text-decoration: underline;
            text-align: center;
        }
        .subheader {
            font-family: "Calibri", sans-serif;
            font-size: 10pt;
            font-weight: normal;
            text-align: left;
        }
        .table2{
            border-collapse: collapse;
            font-family: "Arial";
            font-size: 12pt;
            text-align: center; 
        }
        .table2 th{
            font-weight: bold;
            padding-bottom: 10px;
            padding-top: 10px;
            background-color: #D9D9D9;
        }
        .table2 td{
            padding-bottom: 5px;
            padding-top: 5px;
            padding-right: 5px;
            paading-left: 5px;
        }
        #footer-atas {
            position: fixed; 
            bottom: -35px; 
            left: 0px; 
            right: 0px;
            height: 50px; 
            font-family: "Calibri", sans-serif;
            font-size: 8pt;

            /** Extra personal styles **/
            text-align: center;
            line-height: 35px;
        }
        #footer-bawah {
            position: fixed; 
            bottom: -60px; 
            left: 0px; 
            right: 0px;
            height: 60px; 
            font-family: "Calibri", sans-serif;
            font-size: 8pt;

            /** Extra personal styles **/
            #background-color: #43B049;
            color: black;
            text-align: center;
            line-height: 35px;
        }
        .ttd{
            font-family: "Arial";
            font-size: 8pt;
            text-align: center; 
        }
    </style>
</head>
<body>
<table border="0" width="100%">
<tr>
    <td align="left"><img src="https://office.suryaenergi.com/img/logo-len.png" /></td>
    <td>&nbsp;</td>
    <td align="right"><img src="https://office.suryaenergi.com/img/logo-sei.png" /></td>
</tr>
<tr>
    <td colspan="3">&nbsp;</td>
<tr>
<tr>
    <td colspan="3" class="header">RISALAH RAPAT</td>
<tr>
<tr>
    <td colspan="3">&nbsp;</td>
<tr>
<tr class="header2">
    <td width="25%">Kegiatan</td>
    <td width="5%">:</td>
    <td>'.$hadir->kegiatan.'</td>
</tr>
<tr class="header2">
    <td width="25%">Subyek</td>
    <td width="5%">:</td>
    <td>'.$hadir->subyek.'</td>
</tr>
<tr class="header2">
    <td width="25%">Tanggal</td>
    <td width="5%">:</td>
    <td>'.$hadir->tanggal.'</td>
</tr>
<tr class="header2">
    <td width="25%">Trainer / Pimpinan Rapat</td>
    <td width="5%">:</td>
    <td>'.$hadir->pimpinan.'</td>
</tr>
</table>
<p>&nbsp;</p>
<table border="1" width="100%" cellspacing="0" cellpadding="0">
<tr><td width="100%" style="padding: 5px;">
'.$hadir->risalah.'
</td></tr>
</table>
<p>&nbsp;</p>
<table border="0" width="100%">
<tr>
    <td align="center" width="50%">Notulis</td>
    <td align="center" width="50%">Trainer / Pimpinan Rapat</td>
</tr>
<tr>
    <td colspan="2">&nbsp;</td>
</tr>
<tr>
    <td colspan="2">&nbsp;</td>
</tr>
<tr>
    <td colspan="2">&nbsp;</td>
</tr>
<tr>
    <td colspan="2">&nbsp;</td>
</tr>
<tr>
    <td align="center" width="50%">('.$hadir->notulis->name.')</td>
    <td align="center" width="50%">('.$hadir->pimpinan.')</td>
</tr>
</table>
<div id="footer-atas">Certified : ISO 9001:2005 - ISO 14001:2015 - ISO 45001:2018 | Soekarno Hatta 439, Bandung, West Java, Indonesia</div>
<div id="footer-bawah">Phone: +62 22 73518273 | Fax: +62 22 73518273 | Email: info@suryaenergi.com | Website: www.suryaenergi.com
</div>
</body>
</html>';

//echo $html;
$options = new Options();
$options->set('isRemoteEnabled', true);
$dompdf = new Dompdf($options);
$dompdf->loadHtml($html); 
 
// (Optional) Setup the paper size and orientation 
$dompdf->setPaper('A4', 'portrait'); 
 
// Render the HTML as PDF 
$dompdf->render(); 
 
// Output the generated PDF to Browser 
$dompdf->stream('daftar-hadir.pdf',array('Attachment' => 0));
?>