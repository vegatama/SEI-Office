import 'package:flutter/material.dart';
import 'package:seioffice/src/service/services/app_service.dart';
import 'package:seioffice/src/service/services/user_service.dart';
import 'package:seioffice/src/service/request/employee.dart';
import 'package:seioffice/src/service/response/employee.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/theme/theme.dart';
import 'package:seioffice/src/util.dart';

class PekerjaPage extends StatefulWidget {
  const PekerjaPage({Key? key}) : super(key: key);

  @override
  State<PekerjaPage> createState() => _PekerjaPageState();
}

class _PekerjaPageState extends State<PekerjaPage> {
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
                            'Pekerja',
                            style: theme.typography.xxl.copyWith(
                                fontWeight: fontSemiBold,
                                color: theme.colorScheme.secondaryForeground),
                          ),
                          Text(
                            employeeData.job_title, // Menggunakan data dari API
                            style: theme.typography.sm.copyWith(
                                fontWeight: fontSemiBold,
                                color: theme.colorScheme.muted),
                          ),
                        ],
                      ),
                    ),
                    SizedBox(height: 16),
                    buildTextFormFieldWithLabel(
                        'Status', employeeData.status_kerja),
                    SizedBox(height: 16),
                    buildTextFormFieldWithLabel(
                        'Tanggal Mulai Kerja', employeeData.mulai_kerja),
                    SizedBox(height: 16),
                    buildTextFormFieldWithLabel(
                        'Tanggal Akhir Kerja', employeeData.akhir_kerja),
                    // SizedBox(height: 16),
                    // // buildTextFormFieldWithLabel('Tanggal MMP', employeeData.mmpDate),
                    SizedBox(height: 16),
                    // buildTextFormFieldWithLabel(
                    //     'Tanggal Pensiun', employeeData.th),
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
            initialValue:
                hintText, // Menggunakan data dari API sebagai nilai awal
            readOnly: true, // Membuat field hanya bisa dibaca
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
