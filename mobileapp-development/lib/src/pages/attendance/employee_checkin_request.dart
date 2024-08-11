import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:http_parser/http_parser.dart';
import 'package:seioffice/src/components/animation.dart';
import 'package:seioffice/src/components/button.dart';
import 'package:seioffice/src/components/common.dart';
import 'package:seioffice/src/components/dialog.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/input.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/service/request/attendance.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/transitions/reveal_page_transition.dart';

import '../../service/services/app_service.dart';
import '../../service/services/user_service.dart';

class EmployeeCheckinRequestPage extends StatefulWidget {
  final double distance;
  final double latitude;
  final double longitude;
  final bool isCheckOut;
  const EmployeeCheckinRequestPage({
    super.key,
    required this.distance,
    required this.latitude,
    required this.longitude,
    required this.isCheckOut,
  });

  @override
  State<EmployeeCheckinRequestPage> createState() =>
      _EmployeeCheckinRequestPageState();
}

class _EmployeeCheckinRequestPageState
    extends State<EmployeeCheckinRequestPage> {
  final TextEditingController _reasonController = TextEditingController();
  Uint8List? _photo;
  MediaType? _photoType;

  @override
  void initState() {
    super.initState();
    _reasonController.addListener(() {
      setState(() {});
    });
  }

  void append(String reason) {
    // if is not empty, add comma
    if (_reasonController.text.isNotEmpty) {
      reason = ', $reason';
    }
    _reasonController.text = _reasonController.text.trim() + reason;
  }

  String get formattedDistance {
    // distance is in meters
    if (widget.distance < 1000) {
      return '${widget.distance.toStringAsFixed(0)} M';
    } else {
      int intDistance = widget.distance ~/ 1000;
      double distanceRounded = widget.distance / 1000;
      if (intDistance == distanceRounded) {
        return '$intDistance KM';
      } else {
        return '${distanceRounded.toStringAsFixed(2)} KM';
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return StandardPage(
      appBar: Header(
        title: Text(widget.isCheckOut
            ? 'Permohonan Check Out Pegawai'
            : 'Permohonan Checkin Pegawai'),
        leading: IconButton(
          icon: Icon(Icons.arrow_back),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
      ),
      // padding: EdgeInsets.symmetric(horizontal: 24, vertical: 24),
      padding: const EdgeInsets.only(left: 24, right: 24, top: 24, bottom: 32),
      children: [
        DistanceVisualization(distance: formattedDistance),
        FieldGroup(
          label: FieldLabel(
            label: 'Alasan',
            important: true,
          ),
          child: StylizedTextFormArea(
            child: TextFormField(
              controller: _reasonController,
              minLines: 6,
              maxLines: 6,
              decoration: InputDecoration(hintText: 'Keterangan'),
            ),
          ),
        ),
        const SizedBox(height: 8),
        ChipButtonGroup(
          children: [
            ChipButton(
                child: Text('Macet'),
                onPressed: () {
                  append('Macet');
                }),
            ChipButton(
                child: Text('Kendaraan Bermasalah'),
                onPressed: () {
                  append('Kendaraan Bermasalah');
                }),
            ChipButton(
                child: Text('Kecelakaan'),
                onPressed: () {
                  append('Kecelakaan');
                }),
          ],
        ),
        SizedBox(height: 32),
        FieldGroup(
            label: FieldLabel(
              label: 'Foto Bukti',
              important: true,
            ),
            child: ConstrainedBox(
              constraints: BoxConstraints(
                minHeight: 200,
              ),
              child: PhotoUploadInput(
                image: _photo != null ? MemoryImage(_photo!) : null,
                onPhotoSelected: (value) async {
                  if (value == null) {
                    setState(() {
                      _photo = null;
                      _photoType = null;
                    });
                  } else {
                    final bytes = await value.readAsBytes();
                    setState(() {
                      _photo = bytes;
                      try {
                        _photoType = MediaType.parse(value.mimeType ?? '');
                      } catch (e) {
                        _photoType = MediaType('image', 'jpeg');
                      }
                    });
                  }
                },
              ),
            )),
        SizedBox(height: 32),
        PrimaryButton(
          onPressed: _photo == null || _reasonController.text.isEmpty
              ? null
              : () async {
                  final appController = getService<AppService>();
                  final userController = getService<UserService>();
                  final BuildContext outerContext = context;
                  showLoading(
                      context,
                      appController.request(AttendanceRequestSwipe(
                        empcode: userController.currentUser.employee_code,
                        latitude: widget.latitude,
                        longitude: widget.longitude,
                        reason: _reasonController.text,
                        photo: MultipartFile.fromBytes(
                          _photo!,
                          contentType: _photoType,
                          filename: 'photo.jpg', // DUMMY FILENAME
                          // apparently, dio doesn't like it when the filename is empty
                        ),
                      ))).then((response) {
                    if (response.msg == 'SUCCESS') {
                      Navigator.push(outerContext, RevealPageRoute(
                        builder: (context) {
                          return ActionFeedbackPage(
                            icon: AnimatedPendingIcon(),
                            title: Text(widget.isCheckOut
                                ? 'Permintaan Check Out Terkirim'
                                : 'Permintaan Check In Terkirim'),
                            action: [
                              TertiaryButton(
                                onPressed: () {
                                  Navigator.pop(context);
                                },
                                child: Text('Tutup'),
                              )
                            ],
                          );
                        },
                      )).then((value) {
                        Navigator.pop(context);
                      });
                    } else {
                      showDialog(
                        context: context,
                        builder: (context) {
                          return PopupDialog(
                              title: Text('Gagal Mengirim Permintaan'),
                              content: Text(response.msg),
                              actions: [
                                PrimaryButton(
                                  onPressed: () {
                                    Navigator.pop(context);
                                  },
                                  child: Text('Tutup'),
                                )
                              ]);
                        },
                      );
                    }
                  });
                },
          child: Text(
            'Ajukan Permintaan',
            textAlign: TextAlign.center,
          ),
        ),
      ],
    );
  }
}
