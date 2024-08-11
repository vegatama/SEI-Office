import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:http_parser/http_parser.dart';
import 'package:open_file/open_file.dart';
import 'package:seioffice/src/components/animation.dart';
import 'package:seioffice/src/components/button.dart';
import 'package:seioffice/src/components/card.dart';
import 'package:seioffice/src/components/common.dart';
import 'package:seioffice/src/components/dialog.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/input.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/components/section.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/request/cuti.dart';
import 'package:seioffice/src/service/request/masterdata.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/transitions/reveal_page_transition.dart';

import '../../service/response/cuti.dart';
import '../../service/services/app_service.dart';
import '../../service/services/user_service.dart';
import '../../theme/theme.dart';

class CutiMasterData {
  final List<JenisIzinCutiData> jenisList;
  final JatahCutiResponse jatahCuti;

  CutiMasterData({
    required this.jenisList,
    required this.jatahCuti,
  });

  static Future<CutiMasterData> fetch(BuildContext context) async {
    final appController = context.getService<AppService>();
    final userController = context.getService<UserService>();
    final response = await appController.request(GetJenisIzinCutiListRequest(
        empCode: userController.currentUser.employee_code));
    final jatahCuti = await appController.request(
      GetJatahCutiRequest(
        empcode: userController.currentUser.employee_code,
        tahun: DateTime.now().year,
      ),
    );
    return CutiMasterData(
      jenisList: response.izinCuti,
      jatahCuti: jatahCuti,
    );
  }
}

class CutiNewPage extends StatefulWidget {
  final CutiMasterData data;

  const CutiNewPage({
    Key? key,
    required this.data,
  }) : super(key: key);

  @override
  State<CutiNewPage> createState() => _CutiNewPageState();
}

class _CutiNewPageState extends State<CutiNewPage> {
  JenisIzinCutiData? _selectedJenisCuti;
  DateTime? _startDate;
  DateTime? _endDate;
  DateTime? _anchorDate;
  final TextEditingController _keteranganController = TextEditingController();
  final List<PlatformFile> _files = [];

  final PageController _pageController = PageController();

  final FocusNode _keteranganFocus = FocusNode();

  final TimelineTween _timelineTween = TimelineTween([
    Timeline(
      duration: Duration(milliseconds: 300),
      curve: Curves.easeInCubic,
    ),
    Timeline(
      duration: Duration(milliseconds: 700),
      curve: Curves.easeOut,
      startValue: 1,
      endValue: 0,
    ),
  ]);

  @override
  void initState() {
    super.initState();
    _pageController.addListener(() {
      // focus/unfocus when entering/leaving Keterangan section
      if (_pageController.page == 2) {
        _keteranganFocus.requestFocus();
      } else {
        _keteranganFocus.unfocus();
      }
    });
  }

  DateTime get maxDate {
    int jatahCuti = widget.data.jatahCuti.jumlahHari;
    if (_startDate == null) {
      return DateTime.now().add(Duration(days: jatahCuti));
    }
    return _startDate!.add(Duration(days: jatahCuti));
  }

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    final mediaPadding = MediaQuery.paddingOf(context);
    return MediaQuery(
      data: MediaQuery.of(context).copyWith(alwaysUse24HourFormat: true),
      child: Builder(builder: (context) {
        return ProgressivePage(
          controller: _pageController,
          padding: const EdgeInsets.only(top: 48, right: 24, left: 24) +
              mediaPadding,
          children: [
            [
              Header(
                title: Text(intl.selectLeaveType),
                subtitle: Text(intl.step(1, 4)),
                actions: [
                  IconButton(
                    onPressed: () {
                      Navigator.of(context).pop();
                    },
                    icon: const Icon(Icons.close),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              CardGroupSelection.single(
                  builder: (context, item, selected) {
                    return CardGroupItem(
                      title: Text(item.namaJenis),
                      child: Text(!item.cutCuti
                          ? 'Tipe Pengajuan: ${item.pengajuan.localize(context)} \n(Bebas potongan cuti)'
                          : 'Tipe Pengajuan: ${item.pengajuan.localize(context)}'),
                      selected: selected,
                    );
                  },
                  items: widget.data.jenisList,
                  selected: _selectedJenisCuti,
                  onChanged: (item) {
                    if (item == null) {
                      _pageController.nextPage(
                          duration: const Duration(milliseconds: 300),
                          curve: Curves.easeInCubic);
                      return;
                    }
                    setState(() {
                      _selectedJenisCuti = item;
                    });
                  }),
            ],
            [
              Header(
                title: Text(intl.titleChooseDate),
                subtitle: Text(intl.step(2, 4)),
                actions: [
                  IconButton(
                    onPressed: () {
                      Navigator.of(context).pop();
                    },
                    icon: const Icon(Icons.close),
                  ),
                ],
              ),
              const SizedBox(height: 64),
              if (_selectedJenisCuti == null)
                EmptyListText(message: 'Pilih jenis cuti terlebih dahulu'),
              if (_selectedJenisCuti != null &&
                  _selectedJenisCuti!.pengajuan == TipePengajuan.HARIAN) ...[
                GestureDetector(
                  onTap: () async {
                    final date = await showDatePicker(
                      context: context,
                      initialDate: DateTime.now(),
                      firstDate:
                          DateTime.now().subtract(const Duration(days: 356)),
                      lastDate: DateTime.now().add(const Duration(days: 356)),
                    );
                    if (date != null) {
                      setState(() {
                        _startDate = date;
                      });
                      if (_startDate != null && _endDate != null) {
                        _pageController.nextPage(
                            duration: const Duration(milliseconds: 300),
                            curve: Curves.easeInCubic);
                      }
                    }
                  },
                  child: Container(
                    decoration: BoxDecoration(
                      border: Border.all(
                        color: theme.colorScheme.muted,
                        width: 2,
                      ),
                      borderRadius: BorderRadius.circular(12),
                    ),
                    padding: const EdgeInsets.all(16),
                    child: ParagraphSection(
                      title: Text(intl.chooseStartDate),
                      content: Text(_startDate == null
                          ? intl.chooseDate
                          : context.intl.formatDate(_startDate!)),
                    ),
                  ),
                ),
                const SizedBox(height: 64),
                TimelineAnimationBuilder.noChild(
                  timeline: _timelineTween,
                  builder: (context, value) {
                    return Transform.translate(
                      offset: Offset(0, 64 * (-value + 0.5)),
                      child: Icon(
                        Icons.arrow_downward,
                        size: 64,
                        color: theme.colorScheme.primary,
                      ),
                    );
                  },
                ),
                const SizedBox(height: 64),
                GestureDetector(
                  onTap: () async {
                    final date = await showDatePicker(
                      context: context,
                      initialDate: _startDate == null ||
                              _startDate!.isBefore(DateTime.now())
                          ? DateTime.now()
                          : _startDate!,
                      // firstDate: _startDate == null ||
                      //         _startDate!.isBefore(DateTime.now())
                      //     ? DateTime.now()
                      //     : _startDate!,
                      // lastDate: maxDate,
                      firstDate:
                          DateTime.now().subtract(const Duration(days: 356)),
                      lastDate: DateTime.now().add(const Duration(days: 356)),
                    );
                    if (date != null) {
                      setState(() {
                        _endDate = date;
                      });
                      if (_startDate != null && _endDate != null) {
                        _pageController.nextPage(
                            duration: const Duration(milliseconds: 300),
                            curve: Curves.easeInCubic);
                      }
                    }
                  },
                  child: Container(
                    decoration: BoxDecoration(
                      border: Border.all(
                        color: theme.colorScheme.muted,
                        width: 2,
                      ),
                      borderRadius: BorderRadius.circular(12),
                    ),
                    padding: const EdgeInsets.all(16),
                    child: ParagraphSection(
                      title: Text(intl.chooseEndDate),
                      content: Text(_endDate == null
                          ? intl.chooseDate
                          : context.intl.formatDate(_endDate!)),
                    ),
                  ),
                ),
              ],
              if (_selectedJenisCuti != null &&
                  _selectedJenisCuti!.pengajuan == TipePengajuan.MENIT) ...[
                GestureDetector(
                  onTap: () async {
                    final date = await showDatePicker(
                      context: context,
                      initialDate: DateTime.now(),
                      firstDate:
                          DateTime.now().subtract(const Duration(days: 356)),
                      lastDate: DateTime.now().add(const Duration(days: 356)),
                    );
                    if (date != null) {
                      setState(() {
                        _anchorDate = date;
                        if (_startDate != null) {
                          _startDate = DateTime(
                            date.year,
                            date.month,
                            date.day,
                            _startDate!.hour,
                            _startDate!.minute,
                          );
                        }
                        if (_endDate != null) {
                          _endDate = DateTime(
                            date.year,
                            date.month,
                            date.day,
                            _endDate!.hour,
                            _endDate!.minute,
                          );
                        }
                      });
                      if (_startDate != null && _endDate != null) {
                        _pageController.nextPage(
                            duration: const Duration(milliseconds: 300),
                            curve: Curves.easeInCubic);
                      }
                    }
                  },
                  child: Container(
                    decoration: BoxDecoration(
                      border: Border.all(
                        color: theme.colorScheme.muted,
                        width: 2,
                      ),
                      borderRadius: BorderRadius.circular(12),
                    ),
                    padding: const EdgeInsets.all(16),
                    child: ParagraphSection(
                      title: Text('Pada Tanggal'),
                      content: Text(_anchorDate == null
                          ? intl.chooseDate
                          : context.intl.formatDate(_anchorDate!)),
                    ),
                  ),
                ),
                const SizedBox(height: 8),
                GestureDetector(
                  onTap: () async {
                    final time = await showTimePicker(
                      context: context,
                      initialTime: _startDate == null
                          ? TimeOfDay.now()
                          : TimeOfDay.fromDateTime(_startDate!),
                      initialEntryMode: TimePickerEntryMode.input,
                    );
                    if (time != null) {
                      setState(() {
                        _anchorDate ??= DateTime.now();
                        _startDate = DateTime(
                          _anchorDate!.year,
                          _anchorDate!.month,
                          _anchorDate!.day,
                          time.hour,
                          time.minute,
                        );
                        // revalidate
                        if (_endDate != null) {
                          // find min max
                          if (_startDate!.isAfter(_endDate!)) {
                            _endDate = DateTime(
                              _startDate!.year,
                              _startDate!.month,
                              _startDate!.day,
                              _startDate!.hour,
                              _startDate!.minute,
                            );
                          }
                        }
                      });
                      if (_startDate != null && _endDate != null) {
                        _pageController.nextPage(
                            duration: const Duration(milliseconds: 300),
                            curve: Curves.easeInCubic);
                      }
                    }
                  },
                  child: Container(
                    decoration: BoxDecoration(
                      border: Border.all(
                        color: theme.colorScheme.muted,
                        width: 2,
                      ),
                      borderRadius: BorderRadius.circular(12),
                    ),
                    padding: const EdgeInsets.all(16),
                    child: ParagraphSection(
                      title: Text('Dari Jam'),
                      content: Text(_startDate == null
                          ? 'Pilih Waktu'
                          : context.intl.formatTimeOfDay(
                              TimeOfDay.fromDateTime(_startDate!))),
                    ),
                  ),
                ),
                const SizedBox(height: 64),
                TimelineAnimationBuilder.noChild(
                  timeline: _timelineTween,
                  builder: (context, value) {
                    return Transform.translate(
                      offset: Offset(0, 64 * (-value + 0.5)),
                      child: Icon(
                        Icons.arrow_downward,
                        size: 64,
                        color: theme.colorScheme.primary,
                      ),
                    );
                  },
                ),
                const SizedBox(height: 64),
                GestureDetector(
                  onTap: () async {
                    final time = await showTimePicker(
                      context: context,
                      initialTime: _endDate == null
                          ? TimeOfDay.now()
                          : TimeOfDay.fromDateTime(_endDate!),
                      initialEntryMode: TimePickerEntryMode.input,
                    );
                    if (time != null) {
                      setState(() {
                        _anchorDate ??= DateTime.now();
                        _endDate = DateTime(
                          _anchorDate!.year,
                          _anchorDate!.month,
                          _anchorDate!.day,
                          time.hour,
                          time.minute,
                        );
                        if (_startDate != null) {
                          // find min max
                          if (_endDate!.isBefore(_startDate!)) {
                            _startDate = DateTime(
                              _endDate!.year,
                              _endDate!.month,
                              _endDate!.day,
                              _endDate!.hour,
                              _endDate!.minute,
                            );
                          }
                        }
                      });
                      if (_startDate != null && _endDate != null) {
                        _pageController.nextPage(
                            duration: const Duration(milliseconds: 300),
                            curve: Curves.easeInCubic);
                      }
                    }
                  },
                  child: Container(
                    decoration: BoxDecoration(
                      border: Border.all(
                        color: theme.colorScheme.muted,
                        width: 2,
                      ),
                      borderRadius: BorderRadius.circular(12),
                    ),
                    padding: const EdgeInsets.all(16),
                    child: ParagraphSection(
                      title: Text('Sampai Jam'),
                      content: Text(_endDate == null
                          ? 'Pilih Waktu'
                          : context.intl.formatTimeOfDay(
                              TimeOfDay.fromDateTime(_endDate!))),
                    ),
                  ),
                ),
              ]
            ],
            [
              Header(
                title: Text(intl.remarksAndSupportingFiles),
                subtitle: Text(intl.step(3, 4)),
                actions: [
                  IconButton(
                    onPressed: () {
                      Navigator.of(context).pop();
                    },
                    icon: const Icon(Icons.close),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              StylizedTextArea(
                child: TextField(
                  focusNode: _keteranganFocus,
                  controller: _keteranganController,
                  minLines: 10,
                  maxLines: 10,
                  decoration: InputDecoration(
                    hintText: intl.leaveApplicationRemarks,
                  ),
                ),
              ),
              const SizedBox(height: 32),
              Header(
                title: Text(intl.leaveApplicationSupportingFiles),
                actions: [
                  IconButton(
                    onPressed: () async {
                      FilePickerResult? result = await FilePicker.platform
                          .pickFiles(allowMultiple: true);
                      if (result != null) {
                        setState(() {
                          _files.addAll(result.files);
                        });
                      }
                    },
                    icon: const Icon(Icons.add),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              if (_files.isEmpty)
                EmptyListText(
                    message: intl.leaveApplicationSupportingFilesDesc),
              for (var file in _files)
                Padding(
                  padding: const EdgeInsets.only(bottom: 4),
                  child: ActionCard(
                    child: Text(file.name),
                    onPressed: () async {
                      // open file
                      OpenResult result = await OpenFile.open(file.path);
                      if (result.type != ResultType.done) {
                        // error
                        debugPrint(result.message);
                      }
                    },
                    trailing: IconButton(
                      onPressed: () {
                        setState(() {
                          _files.remove(file);
                        });
                      },
                      icon: const Icon(Icons.delete),
                    ),
                  ),
                )
            ],
            [
              Header(
                title: Text(intl.leaveApplicationSummary),
                subtitle: Text(intl.step(4, 4)),
                actions: [
                  IconButton(
                    onPressed: () {
                      Navigator.of(context).pop();
                    },
                    icon: const Icon(Icons.close),
                  ),
                ],
              ),
              const SizedBox(height: 32),
              ParagraphSection(
                title: Text(intl.detailKind),
                content: Text(_selectedJenisCuti?.namaJenis ?? '-'),
              ),
              const SizedBox(height: 16),
              ParagraphSection(
                title: Text(intl.detailStartDate),
                content: Text(_startDate == null
                    ? '-'
                    : context.intl.formatDate(_startDate!)),
              ),
              const SizedBox(height: 16),
              ParagraphSection(
                title: Text(intl.detailEndDate),
                content: Text(_endDate == null
                    ? '-'
                    : context.intl.formatDate(_endDate!)),
              ),
              const SizedBox(height: 16),
              ParagraphSection(
                title: Text('Peninjau'),
                content: Text(intl.joinWithAnd(_selectedJenisCuti?.reviewers
                        ?.map((e) => '${e.empName} (${e.empJobTitle})') ??
                    ['-'])),
              ),
              const SizedBox(height: 16),
              ParagraphSection(
                title: Text(intl.leaveApplicationRemarks),
                content: ValueListenableBuilder(
                  valueListenable: _keteranganController,
                  builder: (context, value, child) {
                    return Text(_keteranganController.text.isEmpty
                        ? '-'
                        : _keteranganController.text);
                  },
                ),
              ),
              const SizedBox(height: 16),
              ParagraphSection(
                title: Text(intl.leaveApplicationSupportingFiles),
                content: Text(_files.isEmpty
                    ? intl.leaveApplicationSupportingFilesDesc
                    : intl.joinWithAnd(_files.map((e) => e.name))),
              ),
              const SizedBox(height: 72),
              Center(
                child: RippleButton(
                  onPressed: () {
                    if (_selectedJenisCuti == null) {
                      _pageController.animateToPage(0,
                          duration: const Duration(milliseconds: 300),
                          curve: Curves.easeInCubic);
                      return;
                    }
                    if (_startDate == null || _endDate == null) {
                      _pageController.animateToPage(1,
                          duration: const Duration(milliseconds: 300),
                          curve: Curves.easeInCubic);
                      return;
                    }
                    if (_keteranganController.text.isEmpty) {
                      _pageController.animateToPage(2,
                          duration: const Duration(milliseconds: 300),
                          curve: Curves.easeInCubic);
                      return;
                    }
                    showDialog(
                      context: context,
                      builder: (context) {
                        return PopupDialog(
                          title: Text('Konfirmasi Pengajuan Cuti/Izin'),
                          content: Text(
                              'Apakah Anda yakin ingin mengajukan cuti/izin? Pastikan data yang Anda masukkan sudah benar.'),
                          actions: [
                            PrimaryButton(
                              onPressed: () {
                                Navigator.of(context).pop(true);
                              },
                              child: Text('Konfirmasi'),
                            ),
                            SecondaryButton(
                              onPressed: () {
                                Navigator.of(context).pop(false);
                              },
                              child: Text('Batal'),
                            ),
                          ],
                        );
                      },
                    ).then((value) {
                      if (value == true) {
                        final appController = context.getService<AppService>();
                        final userController =
                            context.getService<UserService>();
                        showLoading(
                          context,
                          Future(() async {
                            List<IzinCutiRequestFile> files = [];
                            for (var file in _files) {
                              final bytes = await file.xFile.readAsBytes();
                              MediaType? type;
                              try {
                                type =
                                    MediaType.parse(file.xFile.mimeType ?? '');
                              } catch (e) {
                                type = null;
                              }
                              files.add(IzinCutiRequestFile(
                                name: file.name,
                                bytes: bytes,
                                contentType: type,
                              ));
                            }
                            return await appController.request(
                              IzinCutiRequest(
                                empcode:
                                    userController.currentUser.employee_code,
                                jenis: _selectedJenisCuti!.id,
                                start: _startDate!,
                                end: _endDate!,
                                reason: _keteranganController.text,
                                documents: files,
                              ),
                            );
                          }),
                        ).then((value) {
                          if (value.msg == 'SUCCESS') {
                            Navigator.of(context).pop();
                            Navigator.of(context).push(RevealPageRoute(
                              builder: (context) {
                                return ActionFeedbackPage(
                                    icon: Icon(Icons.check),
                                    title: Text('Pengajuan Cuti/Izin Berhasil'),
                                    action: [
                                      TertiaryButton(
                                        onPressed: () {
                                          Navigator.of(context).pop();
                                        },
                                        child: Text('Kembali ke Dashboard'),
                                      ),
                                    ]);
                              },
                            ));
                          } else {
                            showDialog(
                              context: context,
                              builder: (context) {
                                return PopupDialog(
                                  title: Text('Gagal Mengajukan Cuti/Izin'),
                                  content: Text(izinCutiRequestMessageToLocal(
                                      value.msg, context)),
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
                      }
                    });
                  },
                  child: Icon(Icons.send),
                ),
              ),
            ],
          ],
        );
      }),
    );
  }
}

String izinCutiRequestMessageToLocal(String msg, BuildContext context) {
  switch (msg) {
    case 'EMPLOYEE_NOT_FOUND':
      return 'Data anda tidak ditemukan';
    case 'JENIS_CUTI_NOT_FOUND':
      return 'Data jenis cuti tidak valid';
    case 'REVIEWER_NOT_FOUND':
      return 'Data peninjau tidak valid';
    case 'CUTI_OVERLAP':
      return 'Cuti yang diajukan tumpang tindih dengan cuti yang sudah ada';
    case 'JATAH_CUTI_HABIS':
      return 'Jatah cuti anda sudah habis';
    default:
      return 'Terjadi kesalahan: $msg';
  }
}
