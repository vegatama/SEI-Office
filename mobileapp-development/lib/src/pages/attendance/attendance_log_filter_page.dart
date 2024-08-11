import 'package:flutter/material.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/components/section.dart';
import 'package:seioffice/src/service/localization.dart';

import '../../components/button.dart';
import '../../components/common.dart';
import '../../theme/theme.dart';
import '../../util.dart';

class AttendanceLogFilterPage extends StatefulWidget {
  final DateTime? startDate;
  final DateTime? endDate;
  final Set<AttendanceFilterStatus> statuses;

  const AttendanceLogFilterPage({
    super.key,
    this.startDate,
    this.endDate,
    this.statuses = const {},
  });

  AttendanceLogFilterPage.fromFilterData(AttendanceFilterData data, {super.key})
      : startDate = data.startDate,
        endDate = data.endDate,
        statuses = data.statuses;

  @override
  State<AttendanceLogFilterPage> createState() =>
      _AttendanceLogFilterPageState();
}

class AttendanceFilterData {
  static const Set<AttendanceFilterStatus> allStatuses = {
    AttendanceFilterStatus.holiday,
    AttendanceFilterStatus.present,
    AttendanceFilterStatus.absent,
    AttendanceFilterStatus.permitted,
  };
  final DateTime? startDate;
  final DateTime? endDate;
  final Set<AttendanceFilterStatus> statuses;

  const AttendanceFilterData({
    this.startDate,
    this.endDate,
    this.statuses = allStatuses,
  });
}

class _AttendanceLogFilterPageState extends State<AttendanceLogFilterPage> {
  late DateTime? startDate;
  late DateTime? endDate;
  late Set<AttendanceFilterStatus> statuses;

  @override
  void initState() {
    super.initState();
    startDate = widget.startDate;
    endDate = widget.endDate;
    statuses = Set.of(widget.statuses);
  }

  @override
  Widget build(BuildContext context) {
    return StandardPage(
      appBar: Header(
        title: Text(intl.attendanceFilterTitle),
        subtitle: Text(intl.attendanceHistoryTitle),
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
              for (final status in AttendanceFilterStatus.values)
                ToggledChip(
                  child: Text(getLocaleText(status)),
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
              AttendanceFilterData(
                startDate: startDate,
                endDate: endDate,
                statuses: statuses,
              ),
            );
          },
          child: Text(intl.buttonApplyFilter),
        ),
        const SizedBox(height: 8),
        SecondaryButton(
          onPressed: () {
            Navigator.pop(context, const AttendanceFilterData());
          },
          child: Text(intl.buttonResetFilter),
        ),
      ],
    );
  }

  String getLocaleText(AttendanceFilterStatus status) {
    switch (status) {
      case AttendanceFilterStatus.holiday:
        return intl.attendanceFilterStatusHoliday;
      case AttendanceFilterStatus.present:
        return intl.attendanceFilterStatusPresent;
      case AttendanceFilterStatus.absent:
        return intl.attendanceFilterStatusAbsent;
      case AttendanceFilterStatus.permitted:
        return intl.attendanceFilterStatusPermitted;
    }
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
