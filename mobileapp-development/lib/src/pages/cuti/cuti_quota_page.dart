import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:seioffice/src/components/card.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/service/routes.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/util.dart';

import '../../service/services/app_service.dart';
import '../../service/services/user_service.dart';
import '../../service/request/cuti.dart';
import '../../service/response/cuti.dart';
import '../../theme/theme.dart';

class CutiQuotaPage extends StatefulWidget {
  const CutiQuotaPage({Key? key}) : super(key: key);

  @override
  State<CutiQuotaPage> createState() => _CutiQuotaPageState();
}

class _CutiQuotaPageState extends State<CutiQuotaPage> {
  late Future<List<JatahCutiData>> _future;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    _refresh();
  }

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return FutureBuilder(
      future: _future,
      builder: (context, snapshot) {
        return StandardPage(
          appBar: Header(
            title: const Text('Jatah Cuti'),
            leading: IconButton(
              onPressed: () {
                Navigator.pop(context);
              },
              icon: const Icon(Icons.arrow_back),
            ),
          ),
          padding: const EdgeInsets.only(left: 24, right: 24, top: 24),
          onRefresh: () async {
            setState(() {
              _refresh();
            });
            await _future;
          },
          children: [
            if (snapshot.hasData)
              for (var item in snapshot.requireData)
                Container(
                  margin: const EdgeInsets.only(bottom: 4),
                  child: GestureDetector(
                    onTap: () {
                      context
                          .pushNamed(kPageLeaveQuotaHistory, queryParameters: {
                        'year': item.tahun.toString(),
                      }).then((_) {
                        _refresh();
                      });
                      Feedback.forTap(context);
                    },
                    child: AlertCard(
                      content: Text(item.tahun.toString()),
                      icon: Icon(Icons.calendar_month),
                      color: theme.colorScheme[item.tahun],
                      foregroundColor:
                          theme.colorScheme[item.tahun].withContrast(0.8),
                      title: Text('Jatah Cuti Tahun',
                          style: TextStyle(
                            color:
                                theme.colorScheme[item.tahun].withContrast(0.6),
                          )),
                      details: Text(
                        'Sisa Cuti: ${item.jumlahCuti}',
                        style: TextStyle(
                          color:
                              theme.colorScheme[item.tahun].withContrast(0.4),
                        ),
                      ),
                    ),
                  ),
                ),
            if (!snapshot.hasData)
              for (int i = 0; i < 5; i++)
                Container(
                  child: Container(
                    height: 100,
                  ).placeholder(snapshot),
                  margin: const EdgeInsets.only(bottom: 4),
                ),
          ],
        );
      },
    );
  }

  void _refresh() {
    _future = getService<AppService>()
        .request(GetJatahCutiGroupRequest(
      empcode: getService<UserService>().currentUser.employee_code,
    ))
        .then((response) {
      response.data.sort((a, b) => b.tahun.compareTo(a.tahun));
      return response.data;
    });
  }
}
