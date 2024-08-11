import 'package:flutter/material.dart';
import 'package:seioffice/src/components/common.dart';
import 'package:seioffice/src/components/input.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/components/shimmer.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/request/employee.dart';
import 'package:seioffice/src/service/response/employee.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/service/services/app_service.dart';
import 'package:seioffice/src/service/services/user_service.dart';
import 'package:seioffice/src/theme/theme.dart';
import 'package:seioffice/src/util.dart';

import '../../components/header.dart';

class AdminPekerjaanPage extends StatefulWidget {
  const AdminPekerjaanPage({Key? key}) : super(key: key);

  @override
  State<AdminPekerjaanPage> createState() => _AdminPekerjaanPageState();
}

class _AdminPekerjaanPageState extends State<AdminPekerjaanPage> {
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
    return FutureBuilder(
        future: _future,
        builder: (context, snapshot) {
          return StandardPage(
            appBar: Header(
              title: Text(intl.employmentAdministration),
              leading: IconButton(
                icon: Icon(Icons.arrow_back),
                onPressed: () {
                  Navigator.of(context).pop();
                },
              ),
            ),
            padding: EdgeInsets.all(24),
            children: [
              buildTextFormFieldWithLabel(intl.companyCode,
                  (response) => response.company_code, snapshot),
              SizedBox(height: 16),
              buildTextFormFieldWithLabel(
                  intl.jobTitle, (response) => response.job_title, snapshot),
              SizedBox(height: 16),
              buildTextFormFieldWithLabel(
                  intl.grade, (response) => response.grade, snapshot),
              SizedBox(height: 16),
              buildTextFormFieldWithLabel(
                  intl.group, (response) => response.golongan, snapshot),
              SizedBox(height: 16),
              buildTextFormFieldWithLabel(
                  intl.part, (response) => response.bagian_fungsi, snapshot),
              SizedBox(height: 16),
              buildTextFormFieldWithLabel(
                  intl.department, (response) => response.direktorat, snapshot),
              SizedBox(height: 16),
              buildTextFormFieldWithLabel(intl.directorate,
                  (response) => response.direktorat, snapshot),
              SizedBox(height: 16),
              buildTextFormFieldWithLabel(
                  intl.position, (response) => response.jabatan, snapshot),
              SizedBox(height: 16),
              buildTextFormFieldWithLabel(intl.jobClassificationLevel,
                  (response) => response.klasifikasi_level_jabatan, snapshot),
              SizedBox(height: 16),
              buildTextFormFieldWithLabel(
                  intl.jobStream, (response) => response.job_stream, snapshot),
              SizedBox(height: 16),
              // buildTextFormFieldWithLabel(
              //     'Klasifikasi Job', employeeData.jobStream),
              // SizedBox(height: 16),
              buildTextFormFieldWithLabel(intl.workingPositionFamily,
                  (response) => response.rumpun_jabatan, snapshot),
              SizedBox(height: 16),
              buildTextFormFieldWithLabel(intl.workLocation,
                  (response) => response.lokasi_kerja, snapshot),
            ],
          );
        });
  }

  Widget buildTextFormFieldWithLabel(
      String labelText,
      Function(EmployeeDetailResponse response) hintText,
      AsyncSnapshot<EmployeeDetailResponse> snapshot) {
    return Section(
      title: Text(labelText),
      children: [
        ContentPlaceholder(
          showContent: snapshot.hasData,
          child: StylizedTextFormField(
            child: TextFormField(
              initialValue: snapshot.hasData
                  ? hintText(snapshot.requireData)?.toString()
                  : 'LOADING',
              readOnly: true,
            ),
          ),
        ),
      ],
    );
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
          ContentPlaceholder(
            showContent: snapshot.hasData,
            child: StylizedTextFormField(
              child: TextFormField(
                initialValue: snapshot.hasData
                    ? hintText(snapshot.requireData)?.toString()
                    : 'LOADING',
                readOnly: true,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
