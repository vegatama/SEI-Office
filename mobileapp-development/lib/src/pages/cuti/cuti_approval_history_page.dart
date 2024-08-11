import 'package:flutter/material.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/pages/cuti/cuti_approval_log_filter_page.dart';
import 'package:seioffice/src/service/request/cuti.dart';
import 'package:seioffice/src/service/response/cuti.dart';
import 'package:seioffice/src/service/service.dart';

import '../../service/services/app_service.dart';
import '../../service/services/user_service.dart';
import 'cuti_history_page.dart';

class CutiApprovalHistoryPage extends StatefulWidget {
  const CutiApprovalHistoryPage({Key? key}) : super(key: key);

  @override
  State<CutiApprovalHistoryPage> createState() =>
      _CutiApprovalHistoryPageState();
}

class _CutiApprovalHistoryPageState extends State<CutiApprovalHistoryPage> {
  CutiFilterData filterData = const CutiFilterData();
  final List<IzinCutiRequestData> data = [];
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
    final appController = getService<AppService>();
    final userController = getService<UserService>();
    final response = await appController.request(GetReviewedIzinCutiListRequest(
      empcode: userController.currentUser.employee_code,
      after: lastId,
      start: filterData.startDate,
      end: filterData.endDate,
      status: filterData.statuses,
    ));
    for (var item in response.data) {
      data.add(item);
      if (lastId == null || item.id < lastId) {
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
    return StandardPage(
      appBar: Header(
        title: Text('Menunggu Persetujuan'),
        actions: [
          IconButton(
            icon: const Icon(Icons.filter_alt),
            onPressed: () async {
              final newFilterData = await showDialog<CutiFilterData>(
                context: context,
                builder: (context) {
                  return CutiApprovalLogFilterPage(filterData: filterData);
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
          margin: const EdgeInsets.only(bottom: 4),
          child: IzinCutiRequestDataWidget(
            data: item,
            onRefresh: () {
              setState(() {
                refresh();
              });
            },
          ),
        );
      },
    );
  }
}
