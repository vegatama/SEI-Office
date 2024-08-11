import 'package:flutter/material.dart';
import 'package:seioffice/src/service/localization.dart';

class EnglishSEILocalizations extends SEILocalizations {
  static const List<String> _months = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December'
  ];
  static const List<String> _days = [
    'Monday',
    'Tuesday',
    'Wednesday',
    'Thursday',
    'Friday',
    'Saturday',
    'Sunday',
  ];
  const EnglishSEILocalizations();

  @override
  String get loginDividerOr {
    return 'Or';
  }

  @override
  String get loginTitle {
    return 'Login';
  }

  @override
  String get loginChangeLanguage {
    return 'Language';
  }

  @override
  String get loginChangeTheme {
    return 'Theme';
  }

  @override
  String get loginDialogFailedMessage {
    return 'Email or password is wrong';
  }

  @override
  String get loginDialogFailedTitle {
    return 'Login Failed';
  }

  @override
  String get loginButtonGoogle {
    return 'Login with Suryaenergi';
  }

  @override
  String get loginButtonLogin {
    return 'Login';
  }

  @override
  String get loginHintPassword {
    return 'Password';
  }

  @override
  String get loginHintEmail {
    return 'Email';
  }

  @override
  String get loginSubtitle {
    return 'Login using company email';
  }

  @override
  String get buttonOk {
    return 'OK';
  }

  @override
  String get buttonCancel {
    return 'Cancel';
  }

  @override
  String get buttonYes {
    return 'Yes';
  }

  @override
  String get buttonNo {
    return 'No';
  }

  @override
  String get loading {
    return 'Loading';
  }

  @override
  String get greetingMorning {
    return 'Morning';
  }

  @override
  String get greetingAfternoon {
    return 'Afternoon';
  }

  @override
  String get greetingEvening {
    return 'Evening';
  }

  @override
  String get greetingNight {
    return 'Night';
  }

  @override
  String get errorTitle {
    return 'Error';
  }

  @override
  String get errorMessageGeneral {
    return 'Something went wrong';
  }

  @override
  String formatTime(Duration duration,
      {bool disableAbbreviation = false,
      TimeUnits? highestUnit,
      TimeUnits? lowestUnit}) {
    List<String> result = [];
    if (duration.inDays > 0 &&
        (highestUnit == null || highestUnit.index >= TimeUnits.day.index) &&
        (lowestUnit == null || lowestUnit.index <= TimeUnits.day.index)) {
      result.add(disableAbbreviation
          ? '${duration.inDays} days'
          : '${duration.inDays}d');
      duration = duration - Duration(days: duration.inDays);
    }
    if (duration.inHours > 0 &&
        (highestUnit == null || highestUnit.index >= TimeUnits.hour.index) &&
        (lowestUnit == null || lowestUnit.index <= TimeUnits.hour.index)) {
      result.add(disableAbbreviation
          ? '${duration.inHours} hours'
          : '${duration.inHours}h');
      duration = duration - Duration(hours: duration.inHours);
    }
    if (duration.inMinutes > 0 &&
        (highestUnit == null || highestUnit.index >= TimeUnits.minute.index) &&
        (lowestUnit == null || lowestUnit.index <= TimeUnits.minute.index)) {
      result.add(disableAbbreviation
          ? '${duration.inMinutes} minutes'
          : '${duration.inMinutes}m');
      duration = duration - Duration(minutes: duration.inMinutes);
    }
    if (duration.inSeconds > 0 &&
        (highestUnit == null || highestUnit.index >= TimeUnits.second.index) &&
        (lowestUnit == null || lowestUnit.index <= TimeUnits.second.index)) {
      result.add(disableAbbreviation
          ? '${duration.inSeconds} seconds'
          : '${duration.inSeconds}s');
      duration = duration - Duration(seconds: duration.inSeconds);
    }
    if (result.isEmpty) {
      switch (lowestUnit ?? TimeUnits.second) {
        case TimeUnits.second:
          result.add(disableAbbreviation ? '0 seconds' : '0s');
          break;
        case TimeUnits.minute:
          result.add(disableAbbreviation ? '0 minutes' : '0m');
          break;
        case TimeUnits.hour:
          result.add(disableAbbreviation ? '0 hours' : '0h');
          break;
        case TimeUnits.day:
          result.add(disableAbbreviation ? '0 days' : '0d');
          break;
      }
    }
    return result.join(' ');
  }

  @override
  String formatTimes(int times) {
    return '$times times';
  }

  @override
  String get emptyAttendanceHistory {
    return 'No attendance history';
  }

  @override
  String get profileDataAttribution {
    return 'Data Attribution';
  }

  @override
  String get profilePrivacy {
    return 'Privacy Policy';
  }

  @override
  String get profileOtherInfo {
    return 'Other Info';
  }

  @override
  String get profileJob {
    return 'Job';
  }

  @override
  String get profilePayment {
    return 'Payment';
  }

  @override
  String get profileJobAdministration {
    return 'Job Administration';
  }

  @override
  String get profileInformation {
    return 'Profile Info';
  }

  @override
  String get profileAccount {
    return 'Account';
  }

  @override
  String get serviceTitle {
    return 'Service';
  }

  @override
  String get serviceAttendance {
    return 'Attendance';
  }

  @override
  String get serviceEvents {
    return 'Events';
  }

  @override
  String get serviceCalendar {
    return 'Calendar';
  }

  @override
  String get serviceDocuments {
    return 'Documents';
  }

  @override
  String get serviceVehicles {
    return 'Vehicles';
  }

  @override
  String get serviceProjects {
    return 'Projects';
  }

  @override
  String get serviceAttendanceDescription {
    return 'Manage your attendance';
  }

  @override
  String get serviceEventsDescription {
    return 'Manage list of events';
  }

  @override
  String get serviceCalendarDescription {
    return 'Manage your calendar';
  }

  @override
  String get serviceDocumentsDescription {
    return 'Manage your documents';
  }

  @override
  String get serviceVehiclesDescription {
    return 'Manage your vehicle request';
  }

  @override
  String get serviceProjectsDescription {
    return 'Monitor your projects';
  }

  @override
  String get dashboardLateMinutes {
    return 'Late minutes this month';
  }

  @override
  String get dashboardWorkHourDeficiency {
    return 'Work hour deficiency this month';
  }

  @override
  String get dashboardForgotCheckTime {
    return 'Forgot to check time this month';
  }

  @override
  String get attendanceHistoryTitle {
    return 'Attendance History';
  }

  @override
  String get attendanceTitle {
    return 'Attendance';
  }

  @override
  String formatDate(DateTime date, {bool time = false}) {
    if (time) {
      return '${_days[date.weekday - 1]}, ${date.day} ${_months[date.month - 1]} ${date.year} ${date.hour}:${date.minute.toString().padLeft(2, '0')}';
    }
    return '${_days[date.weekday - 1]}, ${date.day} ${_months[date.month - 1]} ${date.year}';
  }

  @override
  String get attendanceCheckInTime {
    return 'Check In Time';
  }

  @override
  String get attendanceCheckOutTime {
    return 'Check Out Time';
  }

  @override
  String get attendanceLateTime {
    return 'Late Time';
  }

  @override
  String get attendanceReason {
    return 'Reason';
  }

  @override
  String get attendanceStatus {
    return 'Status';
  }

  @override
  String get attendanceAttachedPhoto {
    return 'Attached Photo';
  }

  @override
  String get attendanceFilterTitle {
    return 'Filter';
  }

  @override
  String get attendanceFilterFrom {
    return 'From Date';
  }

  @override
  String get attendanceFilterTo {
    return 'To Date';
  }

  @override
  String get attendanceFilterStatus {
    return 'Status';
  }

  @override
  String get buttonApplyFilter {
    return 'Apply Filter';
  }

  @override
  String get buttonResetFilter {
    return 'Reset Filter';
  }

  @override
  String get attendanceCheckIn {
    return 'Check In';
  }

  @override
  String get attendanceCheckOut {
    return 'Check Out';
  }

  @override
  String get attendanceFilterStatusHoliday {
    return 'Holiday';
  }

  @override
  String get attendanceFilterStatusPresent {
    return 'Present';
  }

  @override
  String get attendanceFilterStatusPermitted {
    return 'Permitted';
  }

  @override
  String get attendanceFilterStatusAbsent {
    return 'Absent';
  }

  @override
  String get seeMore {
    return 'See More';
  }

  @override
  String get attendanceStatusUnknown {
    return 'HAS NOT CHECKED IN';
  }

  @override
  String get attendanceStatusPresent {
    return 'PRESENT';
  }

  @override
  String get attendanceStatusAbsent {
    return 'ABSENT';
  }

  @override
  String get attendanceStatusPermitted {
    return 'PERMITTED';
  }

  @override
  String get attendanceStatusHoliday {
    return 'HOLIDAY';
  }

  @override
  String get attendanceStatusLate {
    return 'LATE';
  }

  @override
  String getWeekday(int day) {
    return _days[day - 1];
  }

  @override
  String getMonth(int month) {
    return _months[month - 1];
  }

  @override
  String get thisMonth {
    return 'This Month';
  }

  @override
  String get today {
    return 'Today';
  }

  @override
  String lateBy(int minutes) {
    Duration duration = Duration(minutes: minutes);
    return 'Late by ${formatTime(duration, disableAbbreviation: true)}';
  }

  @override
  String get swipeToCheckIn {
    return 'Swipe to Check In';
  }

  @override
  String get swipeToCheckOut {
    return 'Swipe to Check Out';
  }

  @override
  String get checkInSuccessful {
    return 'Check In Successful';
  }

  @override
  String get checkOutSuccessful {
    return 'Check Out Successful';
  }

  @override
  String get checkInFailed {
    return 'Check In Failed';
  }

  @override
  String get checkOutFailed {
    return 'Check Out Failed';
  }

  @override
  String get attendanceStatusPending {
    return 'PENDING';
  }

  @override
  String get attendanceStatusRejected {
    return 'REJECTED';
  }

  @override
  String get attendanceOutOfRangeTitle {
    return 'You are out of range';
  }

  @override
  String get attendanceCheckOutOutOfRangeMessage {
    return 'Your Check In process will need approval';
  }

  @override
  String get leavesTitle {
    return 'Permission and leave';
  }

  @override
  String get recapPermit {
    return 'Recap Permits and Leave';
  }

  @override
  String formatTimeOfDay(TimeOfDay time) {
    return '${time.hour.toString().padLeft(2, '0')}:${time.minute.toString().padLeft(2, '0')}';
  }

  @override
  String get remainPermission {
    return 'Accumulated Time Off';
  }

  @override
  String get remainMinute {
    return 'Minute';
  }

  @override
  String get remainDaysOff {
    return 'Remaining Days Off';
  }

  @override
  String get remainDay {
    return 'Day';
  }

  @override
  String get activeLeave {
    return 'Active Leave';
  }

  @override
  String get remainingLeave {
    return 'Remaining';
  }

  @override
  String get applyPermissionLeave {
    return 'Apply Permission and Leave';
  }

  @override
  String get permitAndLeaveHistory {
    return 'Permit And leave History';
  }

  @override
  String get leaveAndPermission {
    return 'Leave and permits';
  }

  @override
  String get leaveAndPermissionDescription {
    return 'Manage your leave and permits';
  }

  @override
  String get leaveAndPermissionHistory {
    return 'Leave';
  }

  @override
  String get leaveAndPermissionFail {
    return 'Leave (Not Accepted)';
  }

  @override
  String get seeAll {
    return 'See All';
  }

  @override
  String get employmentAdministration {
    return 'Employment Administration';
  }

  @override
  String get companyCode {
    return 'Company Code';
  }

  @override
  String get jobTitle {
    return 'Job Title';
  }

  @override
  String get grade {
    return 'Grade';
  }

  @override
  String get group {
    return 'Group';
  }

  @override
  String get part {
    return 'Part/Function';
  }

  @override
  String get department {
    return 'Department';
  }

  @override
  String get directorate {
    return 'Directorate';
  }

  @override
  String get jobClassificationLevel {
    return 'Position Level Classification';
  }

  @override
  String get jobStream {
    return 'Job Type';
  }

  @override
  String get position {
    return 'Position';
  }

  @override
  String get workLocation {
    return 'Job Location';
  }

  @override
  String get workingPositionFamily {
    return 'Position Family';
  }

  @override
  String get recapAttendanceDashboard {
    return 'Attendance Recap';
  }

  @override
  String get attendance {
    return 'Attendance';
  }

  @override
  String get minuteAttendance {
    return 'Minute';
  }

  @override
  String get totallyLate {
    return 'Total Late';
  }

  @override
  String get attendanceHistory {
    return 'Attendance History';
  }

  @override
  String get totalForgotCheckOut {
    return 'Total Forgot Check Out';
  }

  @override
  String get totalShortWorkHour {
    return 'Total Short Work Hour';
  }

  @override
  String get times {
    return 'Times';
  }

  @override
  String get entryTime {
    return 'Entry Time';
  }

  @override
  String get exitTime {
    return 'Exit Time ';
  }

  @override
  String get approved {
    return 'Approved';
  }

  @override
  String get rejected {
    return 'Rejected';
  }

  @override
  String get pending {
    return 'Pending';
  }

  @override
  String get leaveHistoryAndPermits {
    return 'Leave History and Permits';
  }

  @override
  String get detailKind {
    return 'Kind of Leave';
  }

  @override
  String get detailLeaveHistoryType {
    return 'Leave Type';
  }

  @override
  String get detailStartDate {
    return 'Detail Start Date';
  }

  @override
  String get detailEndDate {
    return 'Detail End Date';
  }

  @override
  String get detailTimelineText1 {
    return 'Request Sent';
  }

  @override
  String get leaveTimelineBegins {
    return 'Time off begins';
  }

  @override
  String get leaveTimelineEnds {
    return 'Time off ends';
  }

  @override
  String approveBy(String name) {
    return 'Approved by $name';
  }

  @override
  String pendingBy(String name) {
    return 'Waiting for $name approval';
  }

  @override
  String rejectBy(String name) {
    return 'Rejected by $name';
  }

  @override
  String get detailTabs {
    return 'Detail';
  }

  @override
  String get timeLineTabs {
    return 'Time Line';
  }

  @override
  String get documentTabs {
    return 'Document';
  }

  @override
  String get cancelButton {
    return 'Cancel Request';
  }

  @override
  String get applicationLeaveNew {
    return 'Application for permits and leave';
  }

  @override
  String get applicationListNew {
    return 'Leave';
  }

  @override
  String get applicationListReject {
    return 'Leave (Rejected)';
  }

  @override
  String get requestLeaveAndPermits {
    return 'Request for permits and leave';
  }

  @override
  String get approvedButton {
    return 'Approve Request';
  }

  @override
  String get rejectedButton {
    return 'Reject Request';
  }

  @override
  String get name {
    return 'Name';
  }

  @override
  String step(int step, int total) {
    return 'Step $step of $total';
  }

  @override
  String get selectLeaveType {
    return 'Select Time Off Type';
  }

  @override
  String get titleChooseDate {
    return 'Select a date';
  }

  @override
  String get chooseStartDate {
    return 'Start Date';
  }

  @override
  String get chooseEndDate {
    return 'End Date';
  }

  @override
  String get chooseDate {
    return 'Select a date';
  }

  @override
  String get leaveApplicationReviewer {
    return 'Leave Application Reviewer';
  }

  @override
  String get leaveApplicationReviewerDesc {
    return 'Must be filled in';
  }

  @override
  String get leaveApplicationReviewerDesc2 {
    return 'Optional';
  }

  @override
  String get remarksAndSupportingFiles {
    return 'Remarks and Supporting Files';
  }

  @override
  String get leaveApplicationRemarks {
    return 'Information';
  }

  @override
  String get leaveApplicationSupportingFiles {
    return 'Supporting Files';
  }

  @override
  String get leaveApplicationSupportingFilesDesc {
    return 'No files uploaded';
  }

  @override
  String get leaveApplicationSummary {
    return 'Leave/Permit Application Summary';
  }

  @override
  String joinWithAnd(Iterable<String> strings) {
    if (strings.isEmpty) {
      return '';
    }
    if (strings.length == 1) {
      return strings.first;
    }
    if (strings.length == 2) {
      return '${strings.first} and ${strings.last}';
    }
    return '${strings.take(strings.length - 1).join(', ')}, and ${strings.last}';
  }

  @override
  String? localizeErrorMessage(String errorMessage) {
    return null;
  }
}
