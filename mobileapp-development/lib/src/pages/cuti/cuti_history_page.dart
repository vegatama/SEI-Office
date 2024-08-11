import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:seioffice/src/components/card.dart';
import 'package:seioffice/src/components/divider.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/service/request/cuti.dart';
import 'package:seioffice/src/service/response/cuti.dart';
import 'package:seioffice/src/service/routes.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/util.dart';

import '../../service/services/app_service.dart';
import '../../service/services/user_service.dart';
import '../../theme/theme.dart';
import 'cuti_log_filter_page.dart';

class CutiFilterData {
  final DateTime? startDate;
  final DateTime? endDate;
  final List<IzinCutiStatus> statuses;

  const CutiFilterData({
    this.startDate,
    this.endDate,
    this.statuses = IzinCutiStatus.values,
  });

  CutiFilterData copyWith({
    DateTime? startDate,
    DateTime? endDate,
    List<IzinCutiStatus>? statuses,
  }) {
    return CutiFilterData(
      startDate: startDate ?? this.startDate,
      endDate: endDate ?? this.endDate,
      statuses: statuses ?? this.statuses,
    );
  }
}

class CutiHistoryPage extends StatefulWidget {
  const CutiHistoryPage({Key? key}) : super(key: key);

  @override
  State<CutiHistoryPage> createState() => _CutiHistoryPageState();
}

class _CutiHistoryPageState extends State<CutiHistoryPage> {
  CutiFilterData filterData = const CutiFilterData();
  final List<YearData> data = [];
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
    if (filterData.statuses.isEmpty) {
      setState(() {
        reachedEnd = true;
      });
      return;
    }
    print('next request lastId: $lastId');
    final appController = getService<AppService>();
    final userController = getService<UserService>();
    final response = await appController.request(GetIzinCutiListRequest(
      empcode: userController.currentUser.employee_code,
      after: lastId,
      start: filterData.startDate,
      end: filterData.endDate,
      status: filterData.statuses,
    ));
    for (var item in response.data) {
      final year = item.startDate.year;
      final yearData = data.firstWhere(
        (element) => element.year == year,
        orElse: () {
          final newData = YearData(year: year, data: []);
          data.add(newData);
          return newData;
        },
      );
      // make sure the data is unique
      if (yearData.data.any((element) => element.id == item.id)) {
        continue;
      }
      yearData.data.add(item);
      // sort the data
      yearData.data.sort((a, b) => b.updatedAt.compareTo(a.updatedAt));
      // newLastId = max(newLastId ?? 0, item.id);
      if (lastId == null || item.id < lastId) {
        lastId = item.id;
      }
    }
    // sort the year
    data.sort((a, b) => b.year.compareTo(a.year));
    if (this.lastId == lastId) {
      reachedEnd = true;
    } else {
      this.lastId = lastId;
    }
    setState(() {
      currentRequest = null;
    });
    print('data: $data');
  }

  @override
  Widget build(BuildContext context) {
    return StandardPage(
      appBar: Header(
        title: Text('Riwayat Izin/Cuti'),
        actions: [
          IconButton(
            icon: const Icon(Icons.filter_alt),
            onPressed: () async {
              final newFilterData = await showDialog<CutiFilterData>(
                context: context,
                builder: (context) {
                  return CutiLogFilterPage(filterData: filterData);
                },
              );
              if (newFilterData != null) {
                setState(() {
                  filterData = newFilterData;
                  refresh();
                });
              }
            },
          ),
        ],
        leading: IconButton(
          onPressed: () {
            Navigator.of(context).pop();
          },
          icon: const Icon(Icons.arrow_back),
        ),
      ),
      onRefresh: () async {
        setState(() {
          refresh();
        });
      },
      padding: const EdgeInsets.only(left: 24, right: 24, bottom: 24, top: 12),
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
              if (snapshot.connectionState == ConnectionState.waiting) {
                return const SizedBox();
              }
              return const SizedBox();
            },
          );
        }
        if (index < 0 || index >= data.length) {
          return const SizedBox();
        }
        final item = data[index];
        return Container(
          margin: const EdgeInsets.only(bottom: 12),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              ContentDivider(child: Text(item.year.toString())),
              const SizedBox(height: 12),
              ...item.data.map(
                (e) => Container(
                  child: IzinCutiDataWidget(
                      data: e,
                      onRefresh: () {
                        setState(() {
                          refresh();
                        });
                      }),
                  margin: const EdgeInsets.only(bottom: 4),
                ),
              ),
            ],
          ),
        );
      },
    );
  }
}

class IzinCutiDataWidget extends StatelessWidget {
  final IzinCutiData data;
  final VoidCallback onRefresh;

  const IzinCutiDataWidget({
    required this.data,
    required this.onRefresh,
  });

  static Color getColorByStatus(BuildContext context, IzinCutiStatus status) {
    final theme = SEITheme.of(context);
    switch (status) {
      case IzinCutiStatus.APPROVED:
        return theme.colorScheme.surfaceSuccess;
      case IzinCutiStatus.REJECTED:
        return theme.colorScheme.surfaceDanger;
      case IzinCutiStatus.CANCELLED:
        return theme.colorScheme.surfaceMuted;
      case IzinCutiStatus.PENDING:
        return theme.colorScheme.surfaceWarning;
      case IzinCutiStatus.EXPIRED:
        return theme.colorScheme.surfaceMuted;
      case IzinCutiStatus.ON_GOING:
        return theme.colorScheme.surfaceFine;
      case IzinCutiStatus.DONE:
        return theme.colorScheme.surfaceMuted;
      case IzinCutiStatus.WAITING:
        return theme.colorScheme.surfaceMuted;
    }
  }

  static Color getForegroundColorByStatus(
      BuildContext context, IzinCutiStatus status) {
    final theme = SEITheme.of(context);
    switch (status) {
      case IzinCutiStatus.APPROVED:
        return theme.colorScheme.surfaceSuccessForeground;
      case IzinCutiStatus.REJECTED:
        return theme.colorScheme.surfaceDangerForeground;
      case IzinCutiStatus.CANCELLED:
        return theme.colorScheme.surfaceMutedForeground;
      case IzinCutiStatus.PENDING:
        return theme.colorScheme.surfaceWarningForeground;
      case IzinCutiStatus.EXPIRED:
        return theme.colorScheme.surfaceMutedForeground;
      case IzinCutiStatus.ON_GOING:
        return theme.colorScheme.surfaceFineForeground;
      case IzinCutiStatus.DONE:
        return theme.colorScheme.surfaceMutedForeground;
      case IzinCutiStatus.WAITING:
        return theme.colorScheme.surfaceMutedForeground;
    }
  }

  static IconData getIconByStatus(IzinCutiStatus status) {
    switch (status) {
      case IzinCutiStatus.APPROVED:
        return Icons.check_circle;
      case IzinCutiStatus.REJECTED:
        return Icons.cancel;
      case IzinCutiStatus.CANCELLED:
        return Icons.cancel;
      case IzinCutiStatus.PENDING:
        return Icons.pending;
      case IzinCutiStatus.EXPIRED:
        return Icons.cancel;
      case IzinCutiStatus.ON_GOING:
        return Icons.pending;
      case IzinCutiStatus.DONE:
        return Icons.check_circle;
      case IzinCutiStatus.WAITING:
        return Icons.pending;
    }
  }

  @override
  Widget build(BuildContext context) {
    return ActionCard(
      leading: ActionIcon(icon: getIconByStatus(data.status)),
      backgroundColor: getColorByStatus(context, data.status),
      onPressed: () {
        context.pushNamed(kPageLeaveDetail, queryParameters: {
          'id': data.id.toString(),
        }).then((value) {
          onRefresh();
        });
      },
      title: Text(
          '${data.jenisName} (${getIzinCutiStatusLocale(data.status, context)})'),
      child: data.tipe == TipePengajuan.HARIAN
          ? Text(
              '${data.startDate.format('dd MMM yyyy')} - ${data.endDate.format('dd MMM yyyy')}')
          : Text(
              '${data.startDate.format('dd MMM yyyy HH:mm')} - ${data.endDate.format('HH:mm')}'),
      trailing: Icon(
        Icons.arrow_forward,
      ),
    );
  }
}

class IzinCutiRequestDataWidget extends StatelessWidget {
  final IzinCutiRequestData data;
  final VoidCallback onRefresh;

  const IzinCutiRequestDataWidget({
    required this.data,
    required this.onRefresh,
  });

  @override
  Widget build(BuildContext context) {
    return ActionCard(
      leading:
          ActionIcon(icon: IzinCutiDataWidget.getIconByStatus(data.status)),
      onPressed: () {
        context.pushNamed(kPageLeaveReviewDetail, queryParameters: {
          'id': data.id.toString(),
        }).then((value) {
          onRefresh();
        });
      },
      backgroundColor:
          IzinCutiDataWidget.getColorByStatus(context, data.status),
      title: Text(
          '${data.jenisName} (${getIzinCutiStatusLocale(data.status, context)})'),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text('${data.employeeName} (${data.employeeJobTitle})'),
          // Text(
          //     '${data.startDate.format('dd MMM yyyy HH:mm')} - ${data.endDate.format('dd MMM yyyy HH:mm')}'),
          if (data.tipe == TipePengajuan.HARIAN)
            Text(
                '${data.startDate.format('dd MMM yyyy')} - ${data.endDate.format('dd MMM yyyy')}')
          else
            Text(
                '${data.startDate.format('dd MMM yyyy HH:mm')} - ${data.endDate.format('HH:mm')}')
        ],
      ),
      trailing: Icon(
        Icons.arrow_forward,
      ),
    );
  }
}

class YearData {
  final int year;
  final List<IzinCutiData> data;

  YearData({
    required this.year,
    required this.data,
  });

  @override
  String toString() {
    return 'YearData{year: $year, data: $data}';
  }
}
