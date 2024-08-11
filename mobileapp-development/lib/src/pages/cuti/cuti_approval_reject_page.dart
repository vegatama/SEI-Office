import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:seioffice/src/components/button.dart';
import 'package:seioffice/src/components/dialog.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/input.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/components/section.dart';

class CutiApprovalRejectPage extends StatefulWidget {
  const CutiApprovalRejectPage({Key? key}) : super(key: key);

  @override
  State<CutiApprovalRejectPage> createState() => _CutiApprovalRejectPageState();
}

class _CutiApprovalRejectPageState extends State<CutiApprovalRejectPage> {
  final TextEditingController _reasonController = TextEditingController();
  @override
  Widget build(BuildContext context) {
    return StandardPage(
      appBar: Header(
        title: Text('Tolak Cuti'),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            context.pop();
          },
        ),
      ),
      padding: const EdgeInsets.only(bottom: 0, left: 24, right: 24, top: 12),
      children: [
        const SizedBox(height: 16),
        ParagraphSection(
          title: Text('Alasan Penolakan'),
          gap: 8,
          content: StylizedTextArea(
            child: TextField(
              maxLines: 5,
              controller: _reasonController,
              decoration: const InputDecoration(
                hintText: 'Masukkan alasan penolakan',
              ),
            ),
          ),
        ),
        const SizedBox(height: 16),
        ValueListenableBuilder(
          valueListenable: _reasonController,
          builder: (context, value, child) {
            return PrimaryButton(
                child: Text('Tolak Cuti'),
                onPressed: _reasonController.text.isNotEmpty
                    ? () {
                        // context.pop(_reasonController.text);
                        showDialog(
                          context: context,
                          builder: (context) {
                            return PopupDialog(
                              title: Text('Konfirmasi'),
                              content: Text(
                                  'Apakah anda yakin ingin menolak cuti ini?'),
                              actions: [
                                PrimaryButton(
                                  child: Text('Ya'),
                                  onPressed: () {
                                    context.pop(true);
                                  },
                                ),
                                SecondaryButton(
                                  child: Text('Batal'),
                                  onPressed: () {
                                    context.pop(false);
                                  },
                                ),
                              ],
                            );
                          },
                        ).then((value) {
                          if (value == true) {
                            context.pop(_reasonController.text);
                          }
                        });
                      }
                    : null);
          },
        )
      ],
    );
  }
}
