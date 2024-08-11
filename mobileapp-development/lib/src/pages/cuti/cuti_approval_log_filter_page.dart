import 'package:flutter/material.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/components/section.dart';
import 'package:seioffice/src/pages/cuti/cuti_history_page.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/response/cuti.dart';

import '../../components/button.dart';
import '../../components/common.dart';
import '../../theme/theme.dart';
import 'cuti_log_filter_page.dart';

class CutiApprovalLogFilterPage extends StatefulWidget {
  final CutiFilterData filterData;

  const CutiApprovalLogFilterPage({
    super.key,
    required this.filterData,
  });

  @override
  State<CutiApprovalLogFilterPage> createState() =>
      _CutiApprovalLogFilterPageState();
}

class _CutiApprovalLogFilterPageState extends State<CutiApprovalLogFilterPage> {
  late DateTime? startDate;
  late DateTime? endDate;
  late Set<IzinCutiStatus> statuses;

  @override
  void initState() {
    super.initState();
    startDate = widget.filterData.startDate;
    endDate = widget.filterData.endDate;
    statuses = Set.of(widget.filterData.statuses);
  }

  @override
  Widget build(BuildContext context) {
    return StandardPage(
      appBar: Header(
        title: Text(intl.attendanceFilterTitle),
        subtitle: Text('Menunggu Persetujuan'),
        leading: IconButton(
          onPressed: () {
            Navigator.pop(context);
          },
          icon: const Icon(Icons.arrow_back),
        ),
      ),
      // padding: const EdgeInsets.all(24),
      padding: const EdgeInsets.only(left: 24, right: 24, top: 24),
      children: [
        dateField(intl.attendanceFilterFrom, startDate, (date) {
          setState(() {
            startDate = date;
          });
        }),
        const SizedBox(height: 32),
        dateField(intl.attendanceFilterTo, endDate, (date) {
          setState(() {
            endDate = date;
          });
        }),
        const SizedBox(height: 32),
        ParagraphSection(
          title: Text(intl.attendanceFilterStatus),
          gap: 12,
          content: Wrap(
            spacing: 4,
            runSpacing: 4,
            children: [
              for (final status in [
                IzinCutiStatus.PENDING,
                IzinCutiStatus.APPROVED,
                IzinCutiStatus.REJECTED,
                IzinCutiStatus.CANCELLED,
              ])
                ToggledChip(
                  child: Text(getIzinCutiStatusLocale(
                    status,
                    context,
                  )),
                  selected: statuses.contains(status),
                  onChanged: (selected) {
                    setState(() {
                      if (selected) {
                        statuses.add(status);
                      } else {
                        statuses.remove(status);
                      }
                    });
                  },
                )
            ],
          ),
        ),
        const SizedBox(height: 48),
        PrimaryButton(
          onPressed: () {
            Navigator.pop(
              context,
              CutiFilterData(
                startDate: startDate,
                endDate: endDate,
                statuses: statuses.toList(),
              ),
            );
          },
          child: Text(intl.buttonApplyFilter),
        ),
        const SizedBox(height: 8),
        SecondaryButton(
          onPressed: () {
            Navigator.pop(context, const CutiFilterData());
          },
          child: Text(intl.buttonResetFilter),
        ),
      ],
    );
  }

  Widget dateField(
      String label, DateTime? value, ValueChanged<DateTime> onChanged) {
    return GestureDetector(
      behavior: HitTestBehavior.translucent,
      onTap: () async {
        final date = await showDatePicker(
          context: context,
          initialDate: value ?? DateTime.now(),
          firstDate: DateTime(2020),
          lastDate: DateTime(2100),
        );
        if (date != null) {
          onChanged(date);
        }
      },
      child: Row(
        children: [
          Expanded(
            child: ParagraphSection(
              title: Text(label),
              content: Text(
                value == null ? '-' : intl.formatDate(value),
              ),
            ),
          ),
          Icon(Icons.edit,
              size: 32,
              color: SEITheme.of(context).colorScheme.secondaryForeground),
        ],
      ),
    );
  }
}
