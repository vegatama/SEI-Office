import 'package:flutter/material.dart';
import 'package:seioffice/src/components/card.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/components/section.dart';
import 'package:seioffice/src/components/steps.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/request/cuti.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/util.dart';
import 'package:url_launcher/url_launcher_string.dart';

import '../../components/button.dart';
import '../../components/dialog.dart';
import '../../components/header.dart';
import '../../service/services/app_service.dart';
import '../../service/services/user_service.dart';
import '../../service/response/cuti.dart';
import '../../theme/theme.dart';
import 'cuti_approval_reject_page.dart';
import 'cuti_detail_page.dart';
import 'cuti_log_filter_page.dart';

class CutiApprovalDetailPage extends StatefulWidget {
  final int id;

  const CutiApprovalDetailPage({required this.id});

  @override
  State<CutiApprovalDetailPage> createState() => _CutiApprovalDetailPageState();
}

class _CutiApprovalDetailPageState extends State<CutiApprovalDetailPage> {
  late Future<IzinCutiDetailResponse> _future;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    final appController = context.getService<AppService>();
    // final userController = context.getService<UserController>();
    _future = appController
        .request(GetIzinCutiDetail(izinCutiId: widget.id))
        .onError((error, stackTrace) {
      debugPrint('Error: $error');
      debugPrint('Stacktrace: $stackTrace');
      throw error!;
    });
  }

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    final userController = context.getService<UserService>();
    return FutureBuilder(
        future: _future,
        key: ValueKey(_future.hashCode),
        builder: (context, snapshot) {
          return TabbedPage(
            appBar: Header(
              subtitle: Text('Menunggu Persetujuan'),
              title: Text('Cuti Tahunan').placeholder(
                  snapshot, () => Text(snapshot.requireData.employeeName)),
              leading: IconButton(
                onPressed: () {
                  Navigator.of(context).pop();
                },
                icon: const Icon(Icons.arrow_back),
              ),
            ),
            padding:
                const EdgeInsets.only(bottom: 0, left: 24, right: 24, top: 12),
            tabs: [
              Tab(text: intl.detailTabs),
              Tab(text: intl.timeLineTabs),
              Tab(text: intl.documentTabs),
            ],
            onRefresh: () async {
              setState(() {
                _future = context
                    .getService<AppService>()
                    .request(GetIzinCutiDetail(izinCutiId: widget.id));
              });
              await _future;
            },
            children: [
              getDetailPage(context, snapshot),
              getTimelinePage(context, snapshot),
              getBerkasPage(context, snapshot),
            ],
          );
        });
  }

  bool canApproveOrReject(String empCode, IzinCutiDetailResponse data) {
    List<IzinCutiApprovalData> approvals = data.approvals;
    // bool previouslyApproved = false;
    // int index = 0;
    // for (var approval in approvals) {
    //   if (approval.reviewerEmployeeCode == empCode) {
    //     return previouslyApproved || index == 0;
    //   } else if (approval.status == IzinCutiStatus.APPROVED) {
    //     previouslyApproved = true;
    //   }
    //   index++;
    // }
    for (int i = 0; i < approvals.length; i++) {
      if (approvals[i].reviewerEmployeeCode == empCode) {
        return i == 0 || approvals[i - 1].status == IzinCutiStatus.APPROVED;
      }
    }
    return false;
  }

  List<Widget> getDetailPage(
      BuildContext context, AsyncSnapshot<IzinCutiDetailResponse> snapshot) {
    final appController = context.getService<AppService>();
    final userController = context.getService<UserService>();
    final theme = SEITheme.of(context);
    IzinCutiStatus selfStatus = IzinCutiStatus.PENDING;
    String? selfReason;
    if (snapshot.hasData) {
      var approvalData = snapshot.requireData.approvals
          .where((element) =>
              element.reviewerEmployeeCode ==
              userController.currentUser.employee_code)
          .firstOrNull;
      selfStatus = approvalData?.status ?? IzinCutiStatus.PENDING;
      selfReason = approvalData?.reason;
    }
    return [
      const SizedBox(height: 16),
      ParagraphSection(
        title: Text(intl.detailKind).placeholder(snapshot),
        content: Text('Lorem Ipsum dolor').placeholder(
            snapshot, () => Text(snapshot.requireData.jenis.namaJenis)),
      ),
      const SizedBox(height: 16),
      ParagraphSection(
        title: Text(intl.detailLeaveHistoryType).placeholder(snapshot),
        content: Text('Lorem Ipsum').placeholder(snapshot,
            () => Text(snapshot.requireData.jenis.pengajuan.localize(context))),
      ),
      if (snapshot.hasData &&
          snapshot.requireData.jenis.pengajuan == TipePengajuan.HARIAN)
        const SizedBox(height: 16),
      if (snapshot.hasData &&
          snapshot.requireData.jenis.pengajuan == TipePengajuan.HARIAN)
        ParagraphSection(
          title: Text('Jatah Cuti Terpakai'),
          content: Text(snapshot.requireData.jenis.cutCuti
              ? snapshot.requireData.endDate
                      .difference(snapshot.requireData.startDate)
                      .inDays
                      .toString() +
                  ' hari'
              : 'Bebas potongan cuti'),
        ),
      const SizedBox(height: 16),
      ParagraphSection(
        title: Text('Status').placeholder(snapshot),
        content: Text('Lorem Ipsum dolor').placeholder(
            snapshot,
            () => Text(
                getIzinCutiStatusLocale(snapshot.requireData.status, context))),
      ),
      const SizedBox(height: 16),
      ParagraphSection(
        title: Text(intl.detailStartDate).placeholder(snapshot),
        content: Text('Senin, 21 Februari 2024').placeholder(
            snapshot,
            () => Text(intl.formatDate(snapshot.requireData.startDate,
                time: snapshot.requireData.jenis.pengajuan ==
                    TipePengajuan.MENIT))),
      ),
      const SizedBox(height: 16),
      ParagraphSection(
        title: Text(intl.detailEndDate).placeholder(snapshot),
        content: Text('Senin, 21 Februari 2024').placeholder(
            snapshot,
            () => Text(intl.formatDate(snapshot.requireData.endDate,
                time: snapshot.requireData.jenis.pengajuan ==
                    TipePengajuan.MENIT))),
      ),
      const SizedBox(height: 16),
      ParagraphSection(
        title: Text('Keterangan').placeholder(snapshot),
        content: Text('Lorem ipsum dolor sit amet consectetur')
            .placeholder(snapshot, () => Text(snapshot.requireData.reason)),
      ),
      const SizedBox(height: 48),
      if (snapshot.hasData &&
          (snapshot.requireData.status == IzinCutiStatus.PENDING) &&
          snapshot.requireData.startDate.isAfter(DateTime.now()) &&
          selfStatus == IzinCutiStatus.PENDING &&
          canApproveOrReject(
              userController.currentUser.employee_code, snapshot.requireData))
        PrimaryButton(
          onPressed: () {
            // prompt approve dialog
            showDialog(
              context: context,
              builder: (context) {
                return PopupDialog(
                  title: Text('Setujui Izin/Cuti'),
                  content:
                      Text('Apakah anda yakin ingin menyetujui izin/cuti ini?'),
                  actions: [
                    PrimaryButton(
                      onPressed: () async {
                        Navigator.of(context).pop(true);
                      },
                      child: Text('Ya'),
                    ),
                    SecondaryButton(
                      onPressed: () {
                        Navigator.of(context).pop();
                      },
                      child: Text('Tidak'),
                    ),
                  ],
                );
              },
            ).then((value) {
              if (value == true) {
                showLoading(
                        context,
                        appController.request(ApproveCutiRequest(
                            izinCutiId: widget.id,
                            reviewerEmpCode:
                                userController.currentUser.employee_code)))
                    .then((value) {
                  if (mounted) {
                    if (value.msg != 'SUCCESS') {
                      showDialog(
                        context: context,
                        builder: (context) {
                          return PopupDialog(
                            title: Text('Error'),
                            content: Text(value.msg),
                            actions: [
                              PrimaryButton(
                                onPressed: () {
                                  Navigator.of(context).pop();
                                },
                                child: Text('Tutup'),
                              ),
                            ],
                          );
                        },
                      );
                    } else {
                      setState(() {
                        _future = appController
                            .request(GetIzinCutiDetail(izinCutiId: widget.id));
                      });
                    }
                    // setState(() {
                    //   _future = appController
                    //       .request(GetIzinCutiDetail(izinCutiId: widget.id));
                    // });
                  }
                });
              }
            });
          },
          child: Text('Setujui Izin/Cuti'),
        ),
      // text to show if the cuti is already approved by the user
      if (snapshot.hasData &&
          snapshot.requireData.status == IzinCutiStatus.APPROVED &&
          snapshot.requireData.startDate.isAfter(DateTime.now()) &&
          selfStatus == IzinCutiStatus.APPROVED)
        const SizedBox(height: 8),
      if (snapshot.hasData && selfStatus == IzinCutiStatus.APPROVED)
        Text(
          'Izin/Cuti ini sudah disetujui oleh anda',
          style: theme.typography.sm.copyWith(
            color: theme.colorScheme.surfaceMuted,
          ),
          textAlign: TextAlign.center,
        ),
      if (snapshot.hasData && selfStatus == IzinCutiStatus.REJECTED)
        Text(
          'Izin/Cuti ini sudah ditolak oleh anda: \n${selfReason ?? '-'}',
          style: theme.typography.sm.copyWith(
            color: theme.colorScheme.surfaceMuted,
          ),
          textAlign: TextAlign.center,
        ),
      if (snapshot.hasData && selfStatus == IzinCutiStatus.APPROVED)
        const SizedBox(height: 8),
      if (snapshot.hasData &&
          (snapshot.requireData.status == IzinCutiStatus.PENDING ||
              snapshot.requireData.status == IzinCutiStatus.APPROVED) &&
          snapshot.requireData.startDate.isAfter(DateTime.now()) &&
          (selfStatus == IzinCutiStatus.PENDING ||
              selfStatus == IzinCutiStatus.APPROVED))
        const SizedBox(height: 8),
      if (snapshot.hasData &&
          (snapshot.requireData.status == IzinCutiStatus.PENDING) &&
          snapshot.requireData.startDate.isAfter(DateTime.now()) &&
          (selfStatus == IzinCutiStatus.PENDING ||
              selfStatus == IzinCutiStatus.APPROVED) &&
          canApproveOrReject(
              userController.currentUser.employee_code, snapshot.requireData))
        SecondaryButton(
          child: Text('Tolak Izin/Cuti'),
          onPressed: () {
            Navigator.of(context).push(SEIPageRoute(
              builder: (context) {
                return CutiApprovalRejectPage();
              },
            )).then((value) {
              if (value is String) {
                showLoading(
                  context,
                  appController.request(
                    RejectCutiRequest(
                      izinCutiId: widget.id,
                      reviewerEmpCode: userController.currentUser.employee_code,
                      reason: value,
                    ),
                  ),
                ).then((value) {
                  if (mounted) {
                    if (value.msg != 'SUCCESS') {
                      showDialog(
                        context: context,
                        builder: (context) {
                          return PopupDialog(
                            title: Text('Error'),
                            content: Text(value.msg!),
                            actions: [
                              PrimaryButton(
                                onPressed: () {
                                  Navigator.of(context).pop();
                                },
                                child: Text('Tutup'),
                              ),
                            ],
                          );
                        },
                      );
                    } else {
                      setState(() {
                        _future = appController
                            .request(GetIzinCutiDetail(izinCutiId: widget.id));
                      });
                    }
                  }
                });
              }
            });
          },
        ),
    ];
  }

  List<Widget> getTimelinePage(
      BuildContext context, AsyncSnapshot<IzinCutiDetailResponse> snapshot) {
    if (!snapshot.hasData) {
      return [
        Container(
          height: 200,
          child: Center(
            child: CircularProgressIndicator(),
          ),
        ),
      ];
    }
    IzinCutiTimelineList timeline =
        IzinCutiTimelineList.fromResponse(snapshot.requireData, context);
    return [
      Steps(
        lastFailed: timeline.currentStepFailed,
        reverse: true,
        currentStep: timeline.currentStep,
        children: [
          for (var t in timeline.data)
            ParagraphSection(
              title: Text(intl.formatDate(t.date, time: true)),
              content: Text(t.title),
              description: t.subtitle != null ? Text(t.subtitle!) : null,
            ),
        ],
      ),
      if (snapshot.hasError)
        ParagraphSection(
          title: Text('Error'),
          content: Text(snapshot.error.toString()),
        ),
      if (snapshot.connectionState == ConnectionState.waiting)
        ParagraphSection(
          title: Text('Loading'),
          content: Text('Please wait...'),
        ),
    ];
  }

  List<Widget> getBerkasPage(
      BuildContext context, AsyncSnapshot<IzinCutiDetailResponse> snapshot) {
    final theme = SEITheme.of(context);
    return [
      if (!snapshot.hasData)
        Container(
          height: 200,
          child: Center(
            child: CircularProgressIndicator(),
          ),
        ),
      if (snapshot.hasData && snapshot.requireData.files.isEmpty)
        Container(
          height: 200,
          child: Center(
            child: Text('Tidak ada berkas terkait',
                style: theme.typography.sm.copyWith(
                  color: theme.colorScheme.surfaceMuted,
                )),
          ),
        ),
      if (snapshot.hasData)
        for (var berkas in snapshot.requireData.files)
          Container(
            margin: const EdgeInsets.only(bottom: 4),
            child: ActionCard(
              onPressed: () {
                showLoading(context, launchUrlString(berkas.fileDownloadUri))
                    .then((value) {
                  if (value == false) {
                    showDialog(
                      context: context,
                      builder: (context) {
                        return PopupDialog(
                          title: Text('Error'),
                          content: Text('Gagal membuka berkas'),
                          actions: [
                            PrimaryButton(
                              onPressed: () {
                                Navigator.of(context).pop();
                              },
                              child: Text('Tutup'),
                            ),
                          ],
                        );
                      },
                    );
                  }
                });
              },
              child: Text(berkas.fileName),
              leading: ActionIcon(
                padding: EdgeInsets.all(16),
                icon: Icons.file_copy,
              ),
              trailing: Icon(Icons.open_in_new),
            ),
          )
    ];
  }
}
