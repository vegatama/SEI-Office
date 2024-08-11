import 'package:flutter/material.dart';
import 'package:seioffice/src/components/button.dart';
import 'package:seioffice/src/components/common.dart';
import 'package:seioffice/src/components/shimmer.dart';
import 'package:seioffice/src/pages/profile/admin_pekerjaan_page.dart';
import 'package:seioffice/src/pages/profile/data_diri_page.dart';
import 'package:seioffice/src/pages/profile/pekerja_page.dart';
import 'package:seioffice/src/pages/profile/pembayaran_page.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/request/employee.dart';
import 'package:seioffice/src/service/response/employee.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/service/services/app_service.dart';
import 'package:seioffice/src/service/services/user_service.dart';
import 'package:seioffice/src/theme/theme.dart';
import 'package:seioffice/src/util.dart';

import '../../components/page.dart';

class ProfilePage extends StatefulWidget {
  const ProfilePage({Key? key}) : super(key: key);

  @override
  State<ProfilePage> createState() => _ProfilePageState();
}

class _ProfilePageState extends State<ProfilePage> {
  late Future<EmployeeDetailResponse> _future;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    final userController = getService<UserService>();
    _future = getService<AppService>()
        .request(EmployeeProfileRequest(
            empcode: userController.currentUser.employee_code))
        .notifyError(context)
        .handleErrorMessage(context);
  }

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return FutureBuilder(
        future: _future,
        builder: (context, snapshot) {
          return Scaffold(
            backgroundColor: theme.colorScheme.secondary,
            body: SingleChildScrollView(
              padding: EdgeInsets.only(bottom: 32),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  Stack(
                    children: [
                      Positioned(
                        top: 0,
                        left: 0,
                        right: 0,
                        height: 128 + 84,
                        child: Stack(
                          children: [
                            Container(
                              color: theme.colorScheme.primary,
                            ),
                            Positioned(
                              bottom: -145,
                              left: -50,
                              width: 350,
                              height: 350,
                              child: theme.brightness == Brightness.dark
                                  ? Image.asset(
                                      'lib/assets/images/sei_logo_bg_dark'
                                      '.png')
                                  : Image.asset('lib/assets/images/sei_logo_bg'
                                      '.png'),
                            ),
                          ],
                        ),
                      ),
                      Column(
                        mainAxisSize: MainAxisSize.min,
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        children: [
                          Padding(
                            padding: const EdgeInsets.symmetric(
                                horizontal: 16, vertical: 16),
                            child: Row(
                              children: [
                                IconButton(
                                    onPressed: () {
                                      Navigator.pop(context);
                                    },
                                    icon: Icon(
                                      Icons.close,
                                      color:
                                          theme.colorScheme.primaryForeground,
                                      size: 32,
                                    )),
                                Spacer(),
                                // them
                                IconButton(
                                    onPressed: () {
                                      getService<AppService>().toggleTheme();
                                    },
                                    icon: Icon(
                                      Icons.brightness_4,
                                      size: 32,
                                      color:
                                          theme.colorScheme.primaryForeground,
                                    )),
                                IconButton(
                                    onPressed: () {
                                      getService<AppService>().toggleLanguage();
                                    },
                                    icon: Icon(
                                      Icons.language,
                                      size: 32,
                                      color:
                                          theme.colorScheme.primaryForeground,
                                    )),
                                IconButton(
                                    onPressed: () {
                                      getService<UserService>().logout();
                                    },
                                    icon: Icon(
                                      Icons.logout,
                                      size: 32,
                                      color:
                                          theme.colorScheme.primaryForeground,
                                    )),
                              ],
                            ),
                          ),
                          const SizedBox(
                            height: 64,
                          ),
                          Center(
                            child: ClipOval(
                              child: ContentPlaceholder(
                                showContent: snapshot.hasData,
                                useBox: false,
                                child: CircleAvatar(
                                  radius: 64,
                                  backgroundColor:
                                      theme.colorScheme.primaryForeground,
                                  foregroundImage: snapshot.hasData &&
                                          snapshot.requireData.avatar != null
                                      ? NetworkImage(
                                          snapshot.requireData.avatar!,
                                        )
                                      : null,
                                ),
                              ),
                            ),
                          ),
                          const SizedBox(height: 18),
                          Column(
                            children: [
                              ContentPlaceholder(
                                showContent: snapshot.hasData,
                                child: Text(
                                  snapshot.hasData
                                      ? snapshot.requireData.name
                                      : "USERNAME",
                                  style: theme.typography.xxl.copyWith(
                                      fontWeight: fontSemiBold,
                                      color: theme
                                          .colorScheme.secondaryForeground),
                                ),
                              ),
                              ContentPlaceholder(
                                showContent: snapshot.hasData,
                                child: Text(
                                  snapshot.hasData
                                      ? snapshot.requireData.job_title
                                      : "JOB TITLE",
                                  style: theme.typography.sm.copyWith(
                                      fontWeight: fontSemiBold,
                                      color: theme.colorScheme.muted),
                                ),
                              ),
                            ],
                          ),
                          const SizedBox(
                            height: 18,
                          ),
                          Padding(
                            padding:
                                const EdgeInsets.symmetric(horizontal: 32.0),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.stretch,
                              mainAxisSize: MainAxisSize.min,
                              children: [
                                Section(
                                    title: Text(intl.profileAccount),
                                    children: [
                                      SectionButton(
                                        icon: Icon(Icons.account_box),
                                        child: Text(intl.profileInformation),
                                        onPressed: () {
                                          Navigator.push(
                                            context,
                                            SEIPageRoute(
                                                builder: (context) =>
                                                    DataDiriPage()),
                                          );
                                        },
                                      ),
                                      SectionButton(
                                        icon: Icon(Icons.work),
                                        child:
                                            Text(intl.profileJobAdministration),
                                        onPressed: () {
                                          Navigator.push(
                                            context,
                                            SEIPageRoute(
                                                builder: (context) =>
                                                    AdminPekerjaanPage()),
                                          );
                                        },
                                      ),
                                      SectionButton(
                                        icon:
                                            Icon(Icons.account_balance_wallet),
                                        child: Text(intl.profilePayment),
                                        onPressed: () {
                                          Navigator.push(
                                            context,
                                            SEIPageRoute(
                                                builder: (context) =>
                                                    PembayaranPage()),
                                          );
                                        },
                                      ),
                                      SectionButton(
                                        icon: Icon(Icons.badge_sharp),
                                        child: Text(intl.profileJob),
                                        onPressed: () {
                                          Navigator.push(
                                            context,
                                            SEIPageRoute(
                                                builder: (context) =>
                                                    PekerjaPage()),
                                          );
                                        },
                                      ),
                                    ]),
                                const SizedBox(height: 24),
                                Section(title: Text('Info Lainnya'), children: [
                                  SectionButton(
                                    icon:
                                        Icon(Icons.enhanced_encryption_rounded),
                                    child: Text(intl.profilePrivacy),
                                    onPressed: () {},
                                  ),
                                  SectionButton(
                                    icon: Icon(Icons.quickreply),
                                    child: Text(intl.profileOtherInfo),
                                    onPressed: () {},
                                  ),
                                  SectionButton(
                                    icon: Icon(Icons.dataset),
                                    child: Text(intl.profileDataAttribution),
                                    onPressed: () {},
                                  ),
                                ]),
                              ],
                            ),
                          ),
                        ],
                      )
                    ],
                  )
                ],
              ),
            ),
          );
        });
  }
}
