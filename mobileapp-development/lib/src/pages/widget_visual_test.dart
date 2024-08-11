import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:seioffice/src/components/animation.dart';
import 'package:seioffice/src/components/button.dart';
import 'package:seioffice/src/components/common.dart';
import 'package:seioffice/src/components/dialog.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/image.dart';
import 'package:seioffice/src/components/input.dart';
import 'package:seioffice/src/components/map/google_map.dart';
import 'package:seioffice/src/components/section.dart';
import 'package:seioffice/src/components/steps.dart';
import 'package:seioffice/src/components/swiper.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/theme/theme.dart';
import 'package:seioffice/src/transitions/reveal_page_transition.dart';
import 'package:seioffice/src/util.dart';

import '../components/card.dart';
import '../components/divider.dart';
import '../components/page.dart';
import '../service/services/app_service.dart';

class WidgetVisualTest extends StatefulWidget {
  @override
  State<WidgetVisualTest> createState() => _WidgetVisualTestState();
}

class _WidgetVisualTestState extends State<WidgetVisualTest> {
  Uint8List? photo;
  bool selected = false;
  bool selected1 = false;
  bool selected2 = false;
  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return StandardPage(
      appBar: Header(
        title: Text('Widget Visual Test'),
        // actions: IconButton(onPressed: () {}, icon: Icon(Icons.arrow_back)),
        actions: [
          IconButton(
            onPressed: () {
              Navigator.push(context, SEIPageRoute(
                builder: (context) {
                  return WidgetVisualTest2();
                },
              ));
            },
            icon: Icon(Icons.open_in_new),
          ),
          IconButton(
            onPressed: () {
              final appController = getService<AppService>();
              appController.toggleTheme();
            },
            icon: Icon(theme.brightness == Brightness.dark
                ? Icons.dark_mode
                : Icons.light_mode),
          ),
        ],
      ),
      onRefresh: () async {
        await Future.delayed(const Duration(seconds: 1));
      },
      padding: const EdgeInsets.all(24),
      children: [
        _named(
            'Primary Button',
            PrimaryButton(
              child: Text('Primary Button'),
              onPressed: () {
                showDialog(
                  context: context,
                  builder: (context) {
                    return PopupDialog(
                      title: Text('Gagal'),
                      content: Text('Email atau password anda salah!'),
                      actions: [
                        SecondaryButton(
                          child: Text('Batal'),
                          onPressed: () {
                            Navigator.pop(context);
                          },
                        ),
                        PrimaryButton(
                          child: Text('Coba Lagi'),
                          onPressed: () {
                            Navigator.pop(context);
                          },
                        ),
                      ],
                    );
                  },
                );
              },
            )),
        _gap(),
        _named(
            'Primary Button with Leading and Trailing',
            PrimaryButton(
              leading: Icon(Icons.person),
              trailing: Icon(Icons.arrow_forward),
              onPressed: () {},
              child: Text('Primary Button with Leading and Trailing'),
            )),
        _gap(),
        _named(
            'Secondary Button',
            SecondaryButton(
              child: Text('Secondary Button'),
              onPressed: () {},
            )),
        _gap(),
        _named(
            'Secondary Button with Leading and Trailing',
            SecondaryButton(
              leading: Icon(Icons.person),
              trailing: Icon(Icons.arrow_forward),
              onPressed: () {},
              child: Text('Secondary Button with Leading and Trailing'),
            )),
        _gap(),
        // disabled button
        _named(
            'Disabled Primary Button',
            PrimaryButton(
              child: Text('Disabled Primary Button'),
            )),
        _gap(),
        _named(
            'Disabled Primary Button with Leading and Trailing',
            PrimaryButton(
              leading: Icon(Icons.person),
              trailing: Icon(Icons.arrow_forward),
              child: Text('Disabled Primary Button with Leading and Trailing'),
            )),
        _gap(),
        _named(
            'Disabled Secondary Button',
            SecondaryButton(
              child: Text('Disabled Secondary Button'),
            )),
        _gap(),
        _named(
            'Disabled Secondary Button with Leading and Trailing',
            SecondaryButton(
              leading: Icon(Icons.person),
              trailing: Icon(Icons.arrow_forward),
              child:
                  Text('Disabled Secondary Button with Leading and Trailing'),
            )),
        _gap(),
        _named(
            'ContentDivider',
            SizedBox(
                width: 200, child: ContentDivider(child: Text('Divider')))),
        _gap(),
        _named(
            'PrimaryIconButton',
            PrimaryIconButton(
              icon: Icon(Icons.event_available),
              onPressed: () {},
              color: theme.colorScheme[1],
              label: Text('Kehadiran'),
            )),
        // disabled
        _gap(),
        _named(
            'Disabled PrimaryIconButton',
            PrimaryIconButton(
              icon: Icon(Icons.event_available),
              color: theme.colorScheme[1],
              label: Text('Kehadiran'),
            )),
        _gap(),
        _named(
          'CardButton',
          CardButton(
            leading: Icon(Icons.event_available),
            trailing: Icon(Icons.arrow_forward),
            title: Text('Kehadiran'),
            subtitle: Text('Isi dan kelola kehadiran anda'),
            color: theme.colorScheme[1],
            onPressed: () {},
          ),
        ),
        _gap(),
        _named(
          'Disabled CardButton',
          CardButton(
            leading: Icon(Icons.event_available),
            title: Text('Kehadiran'),
            subtitle: Text('Isi dan kelola kehadiran anda'),
            color: theme.colorScheme[1],
          ),
        ),
        _gap(),
        _named(
          'Header',
          Header(
            title: Text('Senin, 14 Februari 2024'),
            subtitle: Text('Riwayat Kehadiran'),
            leading: IconButton(onPressed: () {}, icon: Icon(Icons.arrow_back)),
            actions: [
              IconButton(onPressed: () {}, icon: Icon(Icons.search)),
              IconButton(onPressed: () {}, icon: Icon(Icons.filter_alt)),
            ],
          ),
        ),
        _gap(),
        _named(
            'AlertCard',
            SizedBox(
              width: 300,
              child: AlertCard(
                title: Text('Alert Card'),
                content: Text('This is an alert card'),
                details: Text('Details'),
                icon: const Icon(Icons.event_busy),
                color: theme.colorScheme.surfaceFine,
                foregroundColor: theme.colorScheme.surfaceFineForeground,
              ),
            )),
        _gap(),
        _named(
            'ParagraphSection',
            ParagraphSection(
              title: Text('Paragraph Section'),
              content: Text('This is a paragraph section'),
            )),
        _gap(),
        _named(
            'Steps',
            Steps(
              children: [
                ParagraphSection(
                    title: Text('Check In'), content: Text('09:00')),
                ParagraphSection(title: Text('Break'), content: Text('12:00')),
                ParagraphSection(
                    title: Text('Check Out'), content: Text('17:00')),
              ],
            )),
        _gap(),
        _named(
          'Section',
          Section(
            title: Text('Akun'),
            children: [
              SectionButton(
                icon: Icon(Icons.account_box),
                child: Text('Data Diri'),
                onPressed: () {},
              ),
              SectionButton(
                icon: Icon(Icons.work),
                child: Text('Administrasi Pekerjaan'),
                onPressed: () {},
              ),
              // pembayaran
              SectionButton(
                icon: Icon(Icons.payment),
                child: Text('Pembayaran'),
                onPressed: () {},
              ),
            ],
          ),
        ),
        _gap(),
        _named(
          'LabeledIcon (Leading)',
          LabeledIcon(
            icon: Icon(Icons.verified),
            leading: Text('Disetujui'),
          ),
        ),
        _gap(),
        _named(
          'LabeledIcon (Trailing)',
          LabeledIcon(
            icon: Icon(Icons.verified),
            trailing: Text('Disetujui'),
          ),
        ),
        _gap(),
        _named(
          'NotificationCard',
          NotificationCard(
            icon: Icon(Icons.verified),
            child: Text('Fauzan Azmi membutuhkan konfirmasi kehadiran'),
            trailing: IconButton(
              onPressed: () {},
              icon: Icon(Icons.chevron_right),
            ),
          ),
        ),
        _gap(),
        // notificationcard with shadow
        _named(
          'NotificationCard with Shadow',
          NotificationCard(
            icon: Icon(Icons.verified),
            child: Text('Fauzan Azmi membutuhkan konfirmasi kehadiran'),
            trailing: IconButton(
              onPressed: () {},
              icon: Icon(Icons.chevron_right),
            ),
            dropShadow: true,
          ),
        ),
        _gap(),
        _named(
          'SummaryCard',
          SummaryCard(
            icon: Icon(Icons.history),
            title: Text('11 Menit'),
            content: Text('Total keterlambatan bulan ini'),
          ),
        ),
        _gap(),
        _named(
          'AnimatedPendingIcon',
          PrimaryButton(
            child: Text('Click Here'),
            onPressed: () {
              Navigator.push(
                context,
                RevealPageRoute(
                  alignment: Alignment.bottomCenter,
                  builder: (context) {
                    return ActionFeedbackPage(
                      icon: AnimatedPendingIcon(),
                      backgroundColor: theme.colorScheme.warning,
                      title: Text('Menunggu Check In'),
                      subtitle: Text('Check In anda sedang dalam persetujuan'),
                      action: [
                        TertiaryButton(
                          child: Text('Selesai'),
                          onPressed: () {
                            Navigator.pop(context);
                          },
                        ),
                      ],
                    );
                  },
                ),
              );
            },
          ),
        ),
        _gap(),
        _named(
            'DistanceVisualization',
            SizedBox(
                width: 300, child: DistanceVisualization(distance: '9KM'))),
        _gap(),
        _named(
          'ToggledChip',
          Row(
            children: [
              ToggledChip(
                child: Text('Hadir'),
                selected: selected,
                onChanged: (value) {
                  setState(() {
                    selected = value;
                  });
                },
              ),
              ToggledChip(
                child: Text('Izin'),
                selected: selected1,
                onChanged: (value) {
                  setState(() {
                    selected1 = value;
                  });
                },
              ),
              ToggledChip(
                child: Text('Tidak Hadir'),
                selected: selected2,
                onChanged: (value) {
                  setState(() {
                    selected2 = value;
                  });
                },
              )
            ].joinSeparator(const SizedBox(width: 8)).toList(),
          ),
        ),
        _gap(),
        _named(
          'PhotoUploadInput',
          PhotoUploadInput(
            image: photo == null ? null : MemoryImage(photo!),
            onPhotoSelected: (value) async {
              if (value == null) {
                setState(() {
                  photo = null;
                });
                return;
              }
              var imageBytes = await value.readAsBytes();
              setState(() {
                photo = imageBytes;
              });
            },
          ),
        ),
        _gap(),
        _named(
            'PhotoPreview',
            PrimaryButton(
              child: Text('Click Here'),
              onPressed: photo == null
                  ? null
                  : () {
                      showDialog(
                        context: context,
                        builder: (context) {
                          return PhotoPreview(
                            child: Image.memory(photo!),
                            onSave: () {
                              debugPrint('Save');
                            },
                          );
                        },
                      );
                    },
            )),
        _gap(),
        _named(
            'StylizedTextField',
            StylizedTextField(
                child: TextField(
              decoration: InputDecoration(
                prefixIcon: Icon(Icons.lock_outline),
                suffixIcon: Icon(Icons.visibility),
                hintText: 'Password',
              ),
            ))),
        _gap(),
        _named(
            'StylizedTextArea',
            StylizedTextArea(
                child: TextField(
              minLines: 3,
              maxLines: 3,
              decoration: InputDecoration(
                hintText: 'Isikan alasan anda',
              ),
            ))),
        _gap(),
        _named(
          'ChipButtonGroup',
          ChipButtonGroup(
            children: [
              ChipButton(
                child: Text('Macet'),
                onPressed: () {},
              ),
              ChipButton(
                child: Text('Hujan'),
                onPressed: () {},
              ),
              ChipButton(
                child: Text('Kecelakaan'),
                onPressed: () {},
              ),
              ChipButton(
                child: Text('Urusan Lain'),
              ),
              ChipButton(
                child: Text('Projek'),
              ),
            ],
          ),
        ),
        _gap(),
        _named(
          'FieldGroup & FieldLabel',
          FieldGroup(
            label: FieldLabel(
              label: 'Alasan',
              important: true,
            ),
            child: StylizedTextFormArea(
              child: TextFormField(
                minLines: 5,
                maxLines: 5,
              ),
            ),
          ),
        ),
      ],
    );
  }

  Widget _gap() {
    return const SizedBox(height: 16);
  }

  Widget _named(String name, Widget widget) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        Text(name),
        const SizedBox(height: 8),
        _wrap(widget),
      ],
    );
  }

  Widget _wrap(Widget widget) {
    return Center(
      child: widget,
    );
  }
}

class WidgetVisualTest2 extends StatefulWidget {
  @override
  State<WidgetVisualTest2> createState() => _WidgetVisualTest2State();
}

class _WidgetVisualTest2State extends State<WidgetVisualTest2> {
  final LocalMapController _mapController = LocalMapController();
  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return StackedPage(
      appBar: Header(
        title: Text('Widget Visual Test'),
        leading: IconButton(
            onPressed: () {
              Navigator.pop(context);
            },
            icon: Icon(Icons.arrow_back)),
        actions: [
          // toggle theme
          IconButton(
            onPressed: () {
              final appController = getService<AppService>();
              appController.toggleTheme();
            },
            icon: Icon(theme.brightness == Brightness.dark
                ? Icons.dark_mode
                : Icons.light_mode),
          ),
        ],
      ),
      flexibleSpace: NotificationCard(
        child: Text('Anda Berada Diluar Jangkauan'),
        subtitle: Text('Proses Check In anda akan membutuhkan persetujuan'),
        icon: Icon(Icons.error_outline),
        backgroundColor: theme.colorScheme.surfaceNotificationDestructive,
        dropShadow: true,
      ),
      body: StackedBottomSheet(
        body: LocalMap(
          controller: _mapController,
          centerLatitude: -6.94760,
          centerLongitude: 107.60090,
          radius: 150,
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: () {
            _mapController.center();
          },
          child: Icon(Icons.my_location),
        ),
        bottomSheet: Container(
            padding: const EdgeInsets.symmetric(horizontal: 12),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                Text(
                  'Senin, 14 Februari 2024',
                  textAlign: TextAlign.center,
                  style: theme.typography.md.copyWith(
                    fontWeight: fontMedium,
                  ),
                ),
                const SizedBox(height: 24),
                Steps(
                  children: [
                    ParagraphSection(
                        title: Text('Check In'), content: Text('09:00')),
                    ParagraphSection(
                        title: Text('Break'), content: Text('12:00')),
                    ParagraphSection(
                        title: Text('Check Out'), content: Text('17:00')),
                  ],
                ),
                const SizedBox(height: 24),
                Swiper(
                  onSwiped: (swiped) {
                    debugPrint('Swiped: $swiped');
                    if (swiped) {
                      Future<void> a() async {
                        await Future.delayed(const Duration(seconds: 2));
                        print('done');
                        Navigator.push(
                          context,
                          RevealPageRoute(
                            alignment: Alignment.bottomCenter,
                            builder: (context) {
                              return ActionFeedbackPage(
                                icon: Icon(Icons.verified),
                                title: Text('Berhasil Check In'),
                                action: [
                                  TertiaryButton(
                                    child: Text('Selesai'),
                                    onPressed: () {
                                      Navigator.pop(context);
                                    },
                                  ),
                                ],
                              );
                            },
                          ),
                        );
                      }

                      showLoading(context, a());
                    }
                  },
                ),
              ],
            )),
      ),
    );
  }
}
