import 'package:flutter/material.dart';
import 'package:seioffice/src/components/card.dart';
import 'package:seioffice/src/components/dialog.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/components/section.dart';
import 'package:seioffice/src/components/steps.dart';
import 'package:seioffice/src/pages/cuti/cuti_log_filter_page.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/request/cuti.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/util.dart';
import 'package:url_launcher/url_launcher_string.dart';

import '../../components/button.dart';
import '../../components/header.dart';
import '../../service/services/app_service.dart';
import '../../service/services/user_service.dart';
import '../../service/response/cuti.dart';
import '../../theme/theme.dart';

class CutiDetailPage extends StatefulWidget {
  final int id;

  const CutiDetailPage({required this.id});

  @override
  State<CutiDetailPage> createState() => _CutiDetailPageState();
}

class IzinCutiTimelineList {
  final int currentStep;
  final bool currentStepFailed;
  final List<IzinCutiTimelineData> data;

  IzinCutiTimelineList({
    required this.currentStep,
    required this.currentStepFailed,
    required this.data,
  });

  static IzinCutiTimelineList fromResponse(
      IzinCutiDetailResponse response, BuildContext context) {
    List<IzinCutiTimelineData> data =
        IzinCutiTimelineData.fromResponse(response, context);
    int? currentFailedStep;
    for (int i = 0; i < data.length; i++) {
      if (data[i].isRejected) {
        currentFailedStep = data.length - i;
        break;
      }
    }
    int currentStep = 1;
    DateTime now = DateTime.now();
    for (int i = data.length - 1; i >= 0; i--) {
      if (data[i].date.isBefore(now) &&
          data[i].isActive &&
          !data[i].isRejected) {
        currentStep = data.length - i;
      } else {
        break;
      }
    }
    return IzinCutiTimelineList(
      currentStep: currentFailedStep ?? currentStep,
      currentStepFailed: currentFailedStep != null,
      data: data,
    );
  }
}

class IzinCutiTimelineData {
  final DateTime date;
  final bool isRejected;
  final bool isActive;
  final String title;
  final String? subtitle;

  IzinCutiTimelineData({
    required this.date,
    required this.isRejected,
    required this.isActive,
    required this.title,
    this.subtitle,
  });

  static List<IzinCutiTimelineData> fromResponse(
      IzinCutiDetailResponse response, BuildContext context) {
    List<IzinCutiTimelineData> data = [];
    data.add(
      IzinCutiTimelineData(
        date: response.createdAt,
        isRejected: false,
        title: 'Permintaan Diajukan',
        isActive: true,
      ),
    );
    bool previouslyRejected = false;
    bool previouslyAccepted = true;
    for (var approval in response.approvals) {
      if (approval.status == IzinCutiStatus.REJECTED) {
        data.add(
          IzinCutiTimelineData(
            date: approval.createdAt,
            isRejected: true,
            title:
                'Ditolak ${approval.reviewerName} (${approval.reviewerJobTitle})',
            subtitle: approval.reason,
            isActive: previouslyAccepted,
          ),
        );
        previouslyRejected = true;
        previouslyAccepted = false;
      } else if (approval.status == IzinCutiStatus.APPROVED) {
        data.add(
          IzinCutiTimelineData(
            date: approval.createdAt,
            isRejected: false || previouslyRejected,
            title:
                'Disetujui ${approval.reviewerName} (${approval.reviewerJobTitle})',
            isActive: previouslyAccepted,
          ),
        );
        previouslyAccepted = true;
      } else {
        data.add(
          IzinCutiTimelineData(
            date: approval.createdAt,
            isRejected: false || previouslyRejected,
            title:
                'Menunggu persetujuan ${approval.reviewerName} (${approval.reviewerJobTitle})',
            isActive: previouslyAccepted,
          ),
        );
        previouslyAccepted = false;
      }
    }
    data.add(
      IzinCutiTimelineData(
        date: response.startDate,
        isRejected: false,
        title: 'Izin/Cuti Dimulai',
        isActive: previouslyAccepted,
      ),
    );
    data.add(
      IzinCutiTimelineData(
        date: response.endDate,
        isRejected: false,
        title: 'Izin/Cuti Berakhir',
        isActive: previouslyAccepted,
      ),
    );
    if (response.status == IzinCutiStatus.CANCELLED) {
      data.add(
        IzinCutiTimelineData(
          date: response.updatedAt,
          isRejected: true,
          title: 'Permintaan Dibatalkan',
          isActive: true,
        ),
      );
    }
    // reverse
    // data.sort((a, b) => b.date.compareTo(a.date));
    return data.reversed.toList();
  }
}

class _CutiDetailPageState extends State<CutiDetailPage> {
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
    return FutureBuilder(
        future: _future,
        key: ValueKey(_future.hashCode),
        builder: (context, snapshot) {
          return TabbedPage(
            appBar: Header(
              subtitle: Text(intl.leaveHistoryAndPermits),
              title: Text('Cuti Tahunan').placeholder(
                  snapshot, () => Text(snapshot.requireData.jenis.namaJenis)),
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

  List<Widget> getDetailPage(
      BuildContext context, AsyncSnapshot<IzinCutiDetailResponse> snapshot) {
    final appController = context.getService<AppService>();
    final userController = context.getService<UserService>();
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
      const SizedBox(height: 16),
      ParagraphSection(
        title: Text('Status').placeholder(snapshot),
        content: Text('Lorem Ipsum dolor').placeholder(
            snapshot,
            () => Text(
                getIzinCutiStatusLocale(snapshot.requireData.status, context))),
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
          (snapshot.requireData.status == IzinCutiStatus.PENDING ||
              snapshot.requireData.status == IzinCutiStatus.EXPIRED))
        PrimaryButton(
          onPressed: () {
            showDialog(
              context: context,
              builder: (context) {
                return PopupDialog(
                  title: Text('Batalkan Cuti'),
                  content:
                      Text('Apakah anda yakin ingin membatalkan cuti ini?'),
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
                        appController.request(CancelCutiRequest(
                            izinCutiId: widget.id,
                            empCode: userController.currentUser.employee_code)))
                    .then((value) {
                  if (mounted) {
                    setState(() {
                      _future = appController
                          .request(GetIzinCutiDetail(izinCutiId: widget.id));
                    });
                  }
                });
              }
            });
          },
          child: Text(intl.cancelButton),
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
                  color: theme.colorScheme.muted,
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
