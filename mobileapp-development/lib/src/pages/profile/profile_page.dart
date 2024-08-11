import 'package:entry/entry.dart';
import 'package:flutter/material.dart';
import 'package:seioffice/src/components/avatar.dart';
import 'package:seioffice/src/components/button.dart';
import 'package:seioffice/src/components/dialog.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/service/request/employee.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/theme/theme.dart';
import 'package:seioffice/src/util.dart';

import '../../components/animation.dart';
import '../../components/common.dart';
import '../../components/header.dart';
import '../../service/response/employee.dart';
import '../../service/services/app_service.dart';
import '../../service/services/user_service.dart';

class ProfilePage extends StatefulWidget {
  const ProfilePage({Key? key}) : super(key: key);

  @override
  State<ProfilePage> createState() => _ProfilePageState();
}

class ProfileData {
  final EmployeeDetailResponse employee;
  final EmployeeDetailResponse atasan;

  ProfileData({
    required this.employee,
    required this.atasan,
  });
}

class _ProfilePageState extends State<ProfilePage> {
  Future<ProfileData>? _future;

  Future<ProfileData> requestProfile() async {
    final appService = getService<AppService>();
    final userController = getService<UserService>();
    final employee = await appService.request(EmployeeProfileRequest(
      empcode: userController.currentUser.employee_code,
    ));
    final atasan = await appService.request(EmployeeGetByNavIdRequest(
      id: employee.atasan_user_id,
    ));
    return ProfileData(
      employee: employee,
      atasan: atasan,
    );
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    _future ??= requestProfile().notifyError(context);
  }

  Future<void> _onRefresh() async {
    setState(() {
      _future = requestProfile().notifyError(context);
    });
    await _future;
  }

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    final userController = getService<UserService>();
    return FutureBuilder(
        future: _future,
        builder: (context, snapshot) {
          return ScaffoldStandardPage(
            onRefresh: _onRefresh,
            appBar: Builder(builder: (context) {
              var iconColor = IconTheme.of(context).color;
              return Entry(
                duration: Duration(milliseconds: 500),
                delay: Duration(milliseconds: 100),
                yOffset: -200,
                opacity: 0,
                child: Header(
                  title: DefaultTextStyle.merge(
                    style: TextStyle(
                      color: DefaultTextStyle.of(context).style.color,
                    ),
                    child: Text(
                      'Profile',
                    ),
                  ),
                  leading: IconTheme(
                    data: IconThemeData(
                      color: iconColor,
                    ),
                    child: IconButton(
                      icon: Icon(Icons.arrow_back),
                      onPressed: () {
                        Navigator.of(context).pop();
                      },
                    ),
                  ),
                  actions: [
                    // switch language
                    IconTheme(
                      data: IconThemeData(
                        color: iconColor,
                      ),
                      child: IconButton(
                        icon: Icon(Icons.language),
                        onPressed: () {
                          final appService = getService<AppService>();
                          appService.toggleLanguage();
                        },
                      ),
                    ),
                    // switch theme
                    IconTheme(
                      data: IconThemeData(
                        color: iconColor,
                      ),
                      child: IconButton(
                        icon: Icon(Icons.brightness_4),
                        onPressed: () {
                          final appService = getService<AppService>();
                          appService.toggleTheme();
                        },
                      ),
                    ),
                    IconTheme(
                      data: IconThemeData(
                        color: iconColor,
                      ),
                      child: IconButton(
                        icon: Icon(Icons.help),
                        onPressed: () {
                          // TODO show help
                        },
                      ),
                    ),
                  ],
                ),
              );
            }),
            child: SingleChildScrollView(
              child: Stack(
                children: [
                  Positioned(
                    top: 0,
                    left: 0,
                    right: 0,
                    height: 64 + 72 + 24 + 150,
                    child: Stack(
                      children: [
                        Hero(
                          tag: #background,
                          createRectTween: (begin, end) {
                            return MaterialRectCenterArcTween(
                                begin: begin, end: end);
                          },
                          child: ClipRRect(
                            borderRadius: BorderRadius.zero,
                            child: Container(
                              decoration: BoxDecoration(
                                color: theme.colorScheme.primary,
                              ),
                            ),
                          ),
                        ),
                        Positioned(
                          top: -25,
                          right: -80,
                          width: 350,
                          height: 350,
                          child: Hero(
                            tag: #backgroundLogo,
                            child: AnimatedSEILogoBackground(),
                          ),
                        ),
                      ],
                    ),
                  ),
                  Padding(
                    padding: MediaQuery.of(context).padding,
                    child: Container(
                      padding: EdgeInsets.only(
                          top: 72 + 24 * 2, left: 24, right: 24, bottom: 48),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        children: [
                          Stack(
                            children: [
                              Container(
                                padding: EdgeInsets.only(top: 64),
                                width: double.infinity,
                                child: _buildCard(
                                  Container(
                                    padding: EdgeInsets.only(
                                      top: 64 + 16,
                                      left: 24,
                                      right: 24,
                                      bottom: 32,
                                    ),
                                    child: Column(
                                      children: [
                                        Text(
                                          !snapshot.hasData
                                              ? 'NAMA PEGAWAI'
                                              : snapshot
                                                  .requireData.employee.name,
                                          style: theme.typography.xxl.copyWith(
                                            color: theme.colorScheme
                                                .secondaryForeground,
                                            fontWeight: fontMedium,
                                          ),
                                        ).placeholder(snapshot),
                                        Text(
                                          snapshot.hasData
                                              ? snapshot.requireData.employee
                                                  .job_title
                                              : 'Jabatan',
                                          style: theme.typography.lg.copyWith(
                                            color:
                                                theme.colorScheme.surfaceMuted,
                                            fontWeight: fontMedium,
                                          ),
                                        ).placeholder(snapshot),
                                      ],
                                    ),
                                  ),
                                ),
                              ),
                              Positioned(
                                left: 0,
                                right: 0,
                                top: 0,
                                child: Center(
                                  child: Hero(
                                    tag: #employeeAvatar,
                                    child: EmployeeCircleAvatar(
                                      showContent: true,
                                      radius: 64,
                                      url: userController.currentUser.avatar,
                                    ),
                                  ),
                                ),
                              )
                            ],
                          ),
                          const SizedBox(height: 8),
                          _buildCard(Container(
                            padding: const EdgeInsets.all(24),
                            child: IntrinsicHeight(
                              child: Row(
                                children: [
                                  CircleAvatar(
                                    radius: 32,
                                    backgroundColor:
                                        theme.colorScheme.primaryForeground,
                                    foregroundImage: snapshot.hasData &&
                                            snapshot.requireData.atasan
                                                    .avatar !=
                                                null
                                        ? NetworkImage(
                                            snapshot.requireData.atasan.avatar!,
                                          )
                                        : null,
                                  ),
                                  const SizedBox(width: 16),
                                  Section(
                                    title: Text('Atasan',
                                        style: theme.typography.md),
                                    gap: 2,
                                    divider: (context) => const SizedBox(),
                                    children: [
                                      Text(
                                        snapshot.hasData
                                            ? snapshot.requireData.atasan.name
                                            : 'Nama Atasan',
                                        style: theme.typography.lg,
                                      ).placeholder(snapshot),
                                      Text(
                                        snapshot.hasData
                                            ? snapshot
                                                .requireData.atasan.job_title
                                            : 'Atasan',
                                        style: theme.typography.sm.copyWith(
                                          color: theme.colorScheme.surfaceMuted,
                                        ),
                                      ).placeholder(snapshot),
                                    ],
                                  )
                                ],
                              ),
                            ),
                          )),
                          const SizedBox(height: 8),
                          _buildCard(
                            Container(
                              padding: const EdgeInsets.all(24),
                              child: _buildTable([
                                _buildGridInfo(
                                    'Company Code',
                                    (data) => data.employee.company_code,
                                    snapshot),
                                _buildGridInfo(
                                    'Job Stream',
                                    (data) => data.employee.job_stream,
                                    snapshot),
                                _buildGridInfo('Grade',
                                    (data) => data.employee.grade, snapshot),
                                _buildGridInfo(
                                    'Bagian Fungsi',
                                    (data) => data.employee.bagian_fungsi,
                                    snapshot),
                                // golongan
                                _buildGridInfo('Golongan',
                                    (data) => data.employee.golongan, snapshot),
                                // direktorat
                                _buildGridInfo(
                                    'Direktorat',
                                    (data) => data.employee.direktorat,
                                    snapshot),
                                // jabatan
                                _buildGridInfo('Jabatan',
                                    (data) => data.employee.jabatan, snapshot),
                                // klasifikasi level jabatan
                                _buildGridInfo(
                                    'Klasifikasi Level Jabatan',
                                    (data) =>
                                        data.employee.klasifikasi_level_jabatan,
                                    snapshot),
                                // rumpun jabatna
                                _buildGridInfo(
                                    'Rumpun Jabatan',
                                    (data) => data.employee.rumpun_jabatan,
                                    snapshot),
                                // lokasi kerja
                                _buildGridInfo(
                                    'Lokasi Kerja',
                                    (data) => data.employee.lokasi_kerja,
                                    snapshot),
                              ]),
                            ),
                          ),
                          const SizedBox(height: 24),
                          PrimaryButton(
                            child: Text('Logout'),
                            trailing: Icon(Icons.logout),
                            color: theme.colorScheme.destructive,
                            onPressed: () {
                              showDialog(
                                context: context,
                                builder: (context) {
                                  return PopupDialog(
                                    title: Text('Logout'),
                                    content:
                                        Text('Apakah anda yakin ingin logout?'),
                                    actions: [
                                      PrimaryButton(
                                          child: Text('Ya'),
                                          onPressed: () {
                                            final userService =
                                                getService<UserService>();
                                            userService.logout();
                                          }),
                                      SecondaryButton(
                                          child: Text('Tidak'),
                                          onPressed: () {
                                            Navigator.of(context).pop();
                                          }),
                                    ],
                                  );
                                },
                              );
                            },
                          ),
                        ],
                      ),
                    ),
                  )
                ],
              ),
            ),
          );
        });
  }

  Widget _buildTable(List<Widget> children, [int columnCount = 2]) {
    List<TableRow> rows = [];
    for (int i = 0; i < children.length; i += columnCount) {
      List<Widget> row = [];
      bool isLastRow = i + columnCount >= children.length;
      for (int j = 0; j < columnCount; j++) {
        if (i + j < children.length) {
          row.add(Padding(
            padding:
                isLastRow ? EdgeInsets.zero : const EdgeInsets.only(bottom: 16),
            child: children[i + j],
          ));
        } else {
          row.add(const SizedBox());
        }
      }
      rows.add(TableRow(children: row));
    }
    return Table(
      defaultColumnWidth: FlexColumnWidth(),
      children: rows,
    );
  }

  Widget _buildGridInfo(
      String title,
      dynamic Function(ProfileData data) selector,
      AsyncSnapshot<ProfileData> snapshot) {
    final theme = SEITheme.of(context);
    return Section(
      title: Text(title,
          style: theme.typography.md.copyWith(
            color: theme.colorScheme.surfaceMuted,
          )).placeholder(snapshot),
      gap: 2,
      divider: (context) => const SizedBox(),
      children: [
        Text(
          snapshot.hasData
              ? (selector(snapshot.requireData)?.toString() ?? '')
              : 'Lorem ipsum dolor',
          style: theme.typography.lg,
        ).placeholder(snapshot),
      ],
    );
  }

  Widget _buildCard(Widget child) {
    final theme = SEITheme.of(context);
    final mediaQuery = MediaQuery.of(context);
    return Entry(
      delay: Duration(milliseconds: 100),
      duration: Duration(milliseconds: 500),
      yOffset: mediaQuery.size.height,
      opacity: 0,
      child: Card(
        elevation: 0,
        color: theme.colorScheme.card,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(25),
        ),
        child: child,
      ),
    );
  }
}
