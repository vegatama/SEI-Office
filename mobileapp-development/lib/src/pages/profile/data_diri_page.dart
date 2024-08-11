import 'package:flutter/material.dart';
import 'package:seioffice/src/service/services/app_service.dart';
import 'package:seioffice/src/service/services/user_service.dart';
import 'package:seioffice/src/service/request/employee.dart';
import 'package:seioffice/src/service/response/employee.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/theme/theme.dart';
import 'package:seioffice/src/util.dart';

class DataDiriPage extends StatefulWidget {
  const DataDiriPage({Key? key}) : super(key: key);

  @override
  State<DataDiriPage> createState() => _DataDiriPageState();
}

class _DataDiriPageState extends State<DataDiriPage> {
  late Future<EmployeeDetailResponse> _future;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    final userController = getService<UserService>();
    _future = getService<AppService>()
        .request(EmployeeProfileRequest(
            empcode: userController.currentUser.employee_code))
        .notifyError(context);
  }

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return Scaffold(
      extendBodyBehindAppBar: true,
      body: FutureBuilder<EmployeeDetailResponse>(
        future: _future,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(
              child: CircularProgressIndicator(),
            );
          } else if (snapshot.hasError) {
            return Center(
              child: Text('Error: ${snapshot.error}'),
            );
          } else {
            final employeeData = snapshot.data!;
            return SingleChildScrollView(
              padding: EdgeInsets.only(bottom: 32),
              child: Padding(
                padding: const EdgeInsets.fromLTRB(16, 16, 16, 16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 4.0),
                      child: IconButton(
                        icon: Icon(Icons.arrow_back),
                        onPressed: () {
                          Navigator.of(context).pop();
                        },
                      ),
                    ),
                    Align(
                      alignment: Alignment.center,
                      child: Column(
                        children: [
                          Text(
                            'Data Diri',
                            style: theme.typography.xxl.copyWith(
                                fontWeight: fontSemiBold,
                                color: theme.colorScheme.secondaryForeground),
                          ),
                          Text(
                            employeeData.job_title,
                            style: theme.typography.sm.copyWith(
                                fontWeight: fontSemiBold,
                                color: theme.colorScheme.muted),
                          ),
                        ],
                      ),
                    ),
                    SizedBox(height: 16),
                    buildTextFormFieldWithLabel('NIK', employeeData.nik),
                    SizedBox(height: 16),
                    buildTextFormFieldWithLabel(
                        'FIRST NAME', employeeData.first_name),
                    SizedBox(height: 16),
                    buildTextFormFieldWithLabel(
                        'LAST NAME', employeeData.middle_name),
                    SizedBox(height: 16),
                    buildTextFormFieldWithLabel('GENDER', employeeData.gender),
                    // SizedBox(height: 16),
                    // buildTextFormFieldWithLabel('AGAMA', employeeData.),
                    // SizedBox(height: 16),
                    buildTextFormFieldWithLabel(
                        'Tempat Lahir', employeeData.place_birthdate),
                    SizedBox(height: 16),
                    buildTextFormFieldWithLabel(
                        'Tanggal Lahir', employeeData.birthday),
                    // SizedBox(height: 16),
                    // buildTextFormFieldWithLabel('Umur', employeeData.umur),
                    SizedBox(height: 16),
                    buildTextFormFieldWithLabel(
                        'Alamat Rumah', employeeData.address),
                    SizedBox(height: 16),
                    buildTextFormFieldWithLabel('Kota', employeeData.city),
                    SizedBox(height: 16),
                    buildTextFormFieldWithLabel(
                        'Kode Pos', employeeData.post_code),
                    SizedBox(height: 16),
                    buildTextFormFieldWithLabel(
                        'Nomor Telp', employeeData.mobile_phone_no),
                    SizedBox(height: 16),
                    buildTextFormFieldWithLabel('Email', employeeData.email),
                    SizedBox(height: 16),
                    buildTextFormFieldWithLabel(
                        'Golongan Darah', employeeData.gol_darah),
                    SizedBox(height: 16),
                    buildTextFormFieldWithLabel(
                        'Status Pernikahan', employeeData.status_pernikahan),
                    SizedBox(height: 16),
                    buildTextFormFieldWithLabel('NPWP', employeeData.npwp),
                  ],
                ),
              ),
            );
          }
        },
      ),
    );
  }

  Widget buildTextFormFieldWithLabel(String labelText, String hintText) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            labelText,
            style: TextStyle(
              fontWeight: FontWeight.bold,
            ),
          ),
          SizedBox(height: 8),
          TextFormField(
            initialValue: hintText, // Gunakan data dari API sebagai nilai awal
            readOnly: true, // Buat field hanya bisa dibaca
            decoration: InputDecoration(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(8.0),
              ),
              contentPadding: EdgeInsets.symmetric(horizontal: 12, vertical: 8),
            ),
          ),
        ],
      ),
    );
  }
}
