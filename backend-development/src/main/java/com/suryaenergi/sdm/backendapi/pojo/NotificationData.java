package com.suryaenergi.sdm.backendapi.pojo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suryaenergi.sdm.backendapi.entity.Notification;
import com.suryaenergi.sdm.backendapi.notification.*;
import lombok.*;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@lombok.Data
@AllArgsConstructor
public class NotificationData {
    private static final Map<String, Class<? extends Data>> typeMap = new HashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static List<?> jsonToList(String jsonListString) {
        try {
            return objectMapper.readValue(jsonListString, List.class);
        } catch (Exception e) {
            return null;
        }
    }

    static {
        typeMap.put("AttendanceSwipeRequest", AttendanceSwipeRequest.class);
        typeMap.put("AttendanceSwipeResponseAccepted", AttendanceSwipeResponseAccepted.class);
        typeMap.put("AttendanceSwipeResponseRejected", AttendanceSwipeResponseRejected.class);
        typeMap.put("IzinCutiRequest", NotificationIzinCutiRequest.class);
        typeMap.put("IzinCutiResponseAccepted", IzinCutiResponseAccepted.class);
        typeMap.put("IzinCutiResponseRejected", IzinCutiResponseRejected.class);
        typeMap.put("VehicleRequestRejected", VehicleRequestRejected.class);
        typeMap.put("VehicleRequestAccepted", VehicleRequestAccepted.class);
        typeMap.put("VehiclePendingRequest", VehiclePendingRequest.class);
        typeMap.put("VehicleRequestReady", VehicleRequestReady.class);

    }

    private long id;
    private LocalDateTime timestamp;
    @Getter
    private Data data;
    private boolean isRead;

    public String getDataAsString() {
        try {
            return objectMapper.writeValueAsString(data.getData());
        } catch (Exception e) {
            return null;
        }
    }

    public NotificationDto toDto() {
        return new NotificationDto(id, timestamp, data.getTypeName(), data.getData(), data.getFormattedMessage(), isRead);
    }

    public static NotificationData fromEntity(Notification notification) {
        try {
            Class<? extends Data> type = typeMap.get(notification.getType());
            if (type == null) {
                return null;
            }
            Data data = type.getDeclaredConstructor().newInstance();
            data.loadData(new DataList(jsonToList(notification.getData())));
            return new NotificationData(notification.getId(), notification.getCreatedAt(), data, notification.isRead());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class DataList {
        private List<?> dataList;

        public DataList(List<?> dataList) {
            this.dataList = dataList;
        }

        public String getString(int index) {
            Object obj = dataList.get(index);
            return obj == null ? null : obj.toString();
        }

        public LocalDateTime getLocalDateTime(int index) {
            String str = getString(index);
            return str == null ? null : LocalDateTime.parse(str);
        }

        public LocalDate getLocalDate(int index) {
            String str = getString(index);
            return str == null ? null : LocalDate.parse(str);
        }

        public LocalTime getLocalTime(int index) {
            String str = getString(index);
            return str == null ? null : LocalTime.parse(str);
        }

        public long getLong(int index) {
            Object obj = dataList.get(index);
            return obj == null ? 0 : ((Number) obj).longValue();
        }

        public int getInt(int index) {
            Object obj = dataList.get(index);
            return obj == null ? 0 : ((Number) obj).intValue();
        }

        public boolean getBoolean(int index) {
            Object obj = dataList.get(index);
            return obj == Boolean.TRUE;
        }

        public double getDouble(int index) {
            Object obj = dataList.get(index);
            return obj == null ? 0 : ((Number) obj).doubleValue();
        }

        public float getFloat(int index) {
            Object obj = dataList.get(index);
            return obj == null ? 0 : ((Number) obj).floatValue();
        }

        public short getShort(int index) {
            Object obj = dataList.get(index);
            return obj == null ? 0 : ((Number) obj).shortValue();
        }

        public byte getByte(int index) {
            Object obj = dataList.get(index);
            return obj == null ? 0 : ((Number) obj).byteValue();
        }

        public char getChar(int index) {
            Object obj = dataList.get(index);
            return obj == null ? 0 : (char) obj;
        }

        public Object getObject(int index) {
            return dataList.get(index);
        }

    }

    public interface Data {
        void loadData(DataList data);
        default String getTypeName() {
            return this.getClass().getSimpleName();
        }
        List<?> getData();
        String getMessage();
        String getSubject();
        default String getFormattedMessage() {
            MessageFormat messageFormat = new MessageFormat(getMessage());
            return messageFormat.format(getData().toArray());
        }

        default NotificationData build() {
            return new NotificationData(-1, LocalDateTime.now(), this, false);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class NotificationDto {
        private long id;
        private LocalDateTime timestamp;
        private String typeName;
        private List<?> data;
        private String message;
        private boolean isRead;
    }
}
