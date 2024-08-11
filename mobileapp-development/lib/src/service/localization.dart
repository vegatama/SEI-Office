import 'dart:async';

import 'package:flutter/material.dart';
import 'package:seioffice/src/components/data.dart';
import 'package:seioffice/src/locale/en_us.dart';

import '../locale/id_id.dart';

typedef MessageBuilder = String Function(BuildContext context);

abstract class SEILocalizations {
  static const SEILocalizationsDelegate delegate = SEILocalizationsDelegate();
  const SEILocalizations();
  String get loginTitle;
  String get loginSubtitle;
  String get loginHintEmail;
  String get loginHintPassword;
  String get loginButtonLogin;
  String get loginDividerOr;
  String get loginButtonGoogle;
  String get loginDialogFailedTitle;
  String get loginDialogFailedMessage;
  String get loginChangeTheme;
  String get loginChangeLanguage;
  String get buttonOk;
  String get buttonCancel;
  String get buttonYes;
  String get buttonNo;
  String get loading;
  String get greetingMorning;
  String get greetingAfternoon;
  String get greetingEvening;
  String get greetingNight;
  String get errorTitle;
  String get errorMessageGeneral;
  String formatTime(Duration duration,
      {bool disableAbbreviation = false,
      TimeUnits? highestUnit,
      TimeUnits? lowestUnit});
  String formatTimes(int times);
  String get emptyAttendanceHistory;
  String get serviceTitle;
  String get serviceAttendance;
  String get serviceEvents;
  String get serviceCalendar;
  String get serviceDocuments;
  String get serviceVehicles;
  String get serviceProjects;
  String get serviceAttendanceDescription;
  String get serviceEventsDescription;
  String get serviceCalendarDescription;
  String get serviceDocumentsDescription;
  String get serviceVehiclesDescription;
  String get serviceProjectsDescription;
  String get dashboardLateMinutes;
  String get dashboardWorkHourDeficiency;
  String get dashboardForgotCheckTime;
  String get attendanceHistoryTitle;
  String get attendanceTitle;
  String formatDate(DateTime date, {bool time = false});
  String formatTimeOfDay(TimeOfDay time);
  String get attendanceCheckInTime;
  String get attendanceCheckOutTime;
  String get attendanceLateTime;
  String get attendanceReason;
  String get attendanceStatus;
  String get attendanceAttachedPhoto;
  String get attendanceFilterTitle;
  String get attendanceFilterFrom;
  String get attendanceFilterTo;
  String get attendanceFilterStatus;
  String get buttonApplyFilter;
  String get buttonResetFilter;
  String get attendanceCheckIn;
  String get attendanceCheckOut;
  String get attendanceFilterStatusHoliday;
  String get attendanceFilterStatusPresent;
  String get attendanceFilterStatusPermitted;
  String get attendanceFilterStatusAbsent;
  String get seeMore;
  String get attendanceStatusUnknown;
  String get attendanceStatusPresent;
  String get attendanceStatusAbsent;
  String get attendanceStatusPermitted;
  String get attendanceStatusHoliday;
  String get attendanceStatusLate;
  String getWeekday(int day);
  String getMonth(int month);
  String get thisMonth;
  String get today;
  String lateBy(int minutes);
  String get swipeToCheckIn;
  String get swipeToCheckOut;
  String get checkInSuccessful;
  String get checkOutSuccessful;
  String get checkInFailed;
  String get checkOutFailed;
  String get attendanceStatusPending;
  String get attendanceStatusRejected;
  String get attendanceOutOfRangeTitle;
  String get attendanceCheckOutOutOfRangeMessage;
  String get profileAccount;
  String get profileInformation;
  String get profileJobAdministration;
  String get profilePayment;
  String get profileJob;
  String get profileOtherInfo;
  String get profilePrivacy;
  String get profileDataAttribution;
  String get leavesTitle;
  String get recapPermit;
  String get remainPermission;
  String get remainMinute;
  String get remainDaysOff;
  String get remainDay;
  String get activeLeave;
  String get remainingLeave;
  String get applyPermissionLeave;
  String get permitAndLeaveHistory;
  String get leaveAndPermission;
  String get leaveAndPermissionDescription;
  String get leaveAndPermissionHistory;
  String get leaveAndPermissionFail;
  String get seeAll;
  String get employmentAdministration;
  String get companyCode;
  String get jobTitle;
  String get grade;
  String get group;
  String get part;
  String get department;
  String get directorate;
  String get position;
  String get jobClassificationLevel;
  String get jobStream;
  String get workingPositionFamily;
  String get workLocation;
  String get attendance;
  String get recapAttendanceDashboard;
  String get totallyLate;
  String get minuteAttendance;
  String get totalShortWorkHour;
  String get totalForgotCheckOut;
  String get attendanceHistory;
  String get times;
  String get entryTime;
  String get exitTime;
  String get approved;
  String get rejected;
  String get pending;
  String get leaveHistoryAndPermits;
  String get detailKind;
  String get detailLeaveHistoryType;
  String get detailStartDate;
  String get detailEndDate;
  String get detailTimelineText1;
  String get leaveTimelineBegins;
  String get leaveTimelineEnds;
  String get detailTabs;
  String get timeLineTabs;
  String get documentTabs;
  String get cancelButton;
  String get applicationLeaveNew;
  String get applicationListNew;
  String get applicationListReject;
  String approveBy(String name);
  String pendingBy(String name);
  String rejectBy(String name);
  String get requestLeaveAndPermits;
  String get approvedButton;
  String get rejectedButton;
  String get name;
  String get selectLeaveType;
  String step(int step, int total);
  String get titleChooseDate;
  String get chooseStartDate;
  String get chooseEndDate;
  String get chooseDate;
  String get leaveApplicationReviewer;
  String get leaveApplicationReviewerDesc;
  String get leaveApplicationReviewerDesc2;
  String get remarksAndSupportingFiles;
  String get leaveApplicationRemarks;
  String get leaveApplicationSupportingFiles;
  String get leaveApplicationSupportingFilesDesc;
  String get leaveApplicationSummary;
  String joinWithAnd(Iterable<String> strings);
  String? localizeErrorMessage(String errorMessage);
}

enum TimeUnits {
  second,
  minute,
  hour,
  day,
}

class SEILocalizationsDelegate extends LocalizationsDelegate<SEILocalizations> {
  final Map<String, SEILocalizations> _localizations = const {
    'id-ID': DefaultSEILocalizations(),
    'en-US': EnglishSEILocalizations(),
  };

  const SEILocalizationsDelegate();

  @override
  bool isSupported(Locale locale) {
    return _localizations.containsKey(locale.toLanguageTag());
  }

  @override
  bool shouldReload(LocalizationsDelegate<SEILocalizations> old) {
    return false;
  }

  @override
  Future<SEILocalizations> load(Locale locale) async {
    return _localizations[locale.toLanguageTag()] ?? _localizations['id-ID']!;
  }
}

extension SEILocalizationsExtension on BuildContext {
  SEILocalizations get intl {
    SEILocalizations? customData = Data.maybeOf(this);
    if (customData != null) {
      return customData;
    }
    return Localizations.of<SEILocalizations>(this, SEILocalizations)!;
  }
}

extension SEIStatelessLocalizationsExtension on StatelessWidget {
  SEILocalizations intl(BuildContext context) {
    SEILocalizations? customData = Data.maybeOf(context);
    if (customData != null) {
      return customData;
    }
    return Localizations.of<SEILocalizations>(context, SEILocalizations)!;
  }
}

extension SEIStateLocalizationsExtension on State {
  SEILocalizations get intl {
    SEILocalizations? customData = Data.maybeOf(context);
    if (customData != null) {
      return customData;
    }
    return Localizations.of<SEILocalizations>(context, SEILocalizations)!;
  }
}
