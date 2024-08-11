import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:seioffice/src/components/card.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/request/cuti.dart';
import 'package:seioffice/src/service/routes.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/util.dart';

import '../../components/header.dart';
import '../../service/services/app_service.dart';
import '../../service/services/user_service.dart';
import '../../service/response/cuti.dart';
import '../../theme/theme.dart';

class JatahCutiFilterData {
  final DateTime? startDate;
  final DateTime? endDate;

  const JatahCutiFilterData({
    this.startDate,
    this.endDate,
  });
}

class CutiQuotaHistoryPage extends StatefulWidget {
  final int year;

  const CutiQuotaHistoryPage({
    super.key,
    required this.year,
  });

  @override
  State<CutiQuotaHistoryPage> createState() => _CutiQuotaHistoryPageState();
}

class _CutiQuotaHistoryPageState extends State<CutiQuotaHistoryPage> {
  JatahCutiFilterData filter = const JatahCutiFilterData();

  final List<JatahCutiData> data = [];

  int? lastId;
  Future<void>? currentRequest;
  bool reachedEnd = false;

  void refresh() {
    reachedEnd = false;
    currentRequest = null;
    lastId = null;
    data.clear();
  }

  Future<void> createNextRequest(int? lastId) async {
    final appController = getService<AppService>();
    final userController = getService<UserService>();
    final response = await appController.request(GetJatahCutiListRequest(
        year: widget.year,
        empcode: userController.currentUser.employee_code,
        after: lastId));
    for (var item in response.data) {
      data.add(item);
      if (lastId == null || item.id! < lastId) {
        lastId = item.id;
      }
    }
    if (this.lastId == lastId) {
      reachedEnd = true;
    } else {
      this.lastId = lastId;
    }
    setState(() {
      currentRequest = null;
    });
  }

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return StandardPage(
      appBar: Header(
        title: Text('Jatah Cuti Tahun ${widget.year}'),
        leading: IconButton(
          onPressed: () {
            Navigator.pop(context);
          },
          icon: const Icon(Icons.arrow_back),
        ),
      ),
      padding: const EdgeInsets.all(24),
      onRefresh: () async {
        setState(() {
          refresh();
        });
        await currentRequest;
      },
      itemCount: data.length + (reachedEnd ? 0 : 1),
      builder: (context, index) {
        if (index == data.length) {
          if (reachedEnd) {
            return const SizedBox();
          }
          currentRequest ??= createNextRequest(lastId);
          return FutureBuilder(
            future: currentRequest,
            builder: (context, snapshot) {
              return snapshot.connectionState == ConnectionState.waiting
                  ? const Center(child: CircularProgressIndicator())
                  : const SizedBox();
            },
          );
        }
        final item = data[index];
        return Container(
          margin: const EdgeInsets.only(bottom: 4),
          child: ActionCard(
            leading: item.jumlahCuti < 0
                ? ActionIcon(icon: Icons.arrow_drop_down)
                : ActionIcon(icon: Icons.arrow_drop_up),
            child: Text(KeteranganJatahCuti.parse(item.keterangan ?? '')
                    ?.localize(context) ??
                item.keterangan ??
                '-'),
            description: item.updatedDate == null
                ? null
                : Text(intl.formatDate(item.updatedDate!)),
            onPressed: () {
              if (item.referrer != null) {
                context.pushNamed(kPageLeaveDetail, queryParameters: {
                  'id': item.referrer!,
                });
              }
            },
            // title: Text(item.referrerName ??
            //     (item.jumlahCuti < 0
            //         ? 'Pengurangan Jatah'
            //         : 'Penambahan Jatah')
            //         ),
            title: item.referrerName != null ? Text(item.referrerName!) : null,
            // title: Text(item.id.toString()),
            backgroundColor:
                item.jumlahCuti < 0 ? theme.colorScheme.surfaceDanger : null,
            trailing: Container(
              padding: EdgeInsets.only(right: 8),
              child: Text(
                item.jumlahCuti < 0
                    ? '-${item.jumlahCuti.abs()}'
                    : '+${item.jumlahCuti}',
                style: theme.typography.lg.copyWith(
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
          ),
        );
      },
    );
  }
}
