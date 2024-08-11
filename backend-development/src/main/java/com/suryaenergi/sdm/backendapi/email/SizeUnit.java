package com.suryaenergi.sdm.backendapi.email;

public class SizeUnit {
    public static SizeUnit px(double size) {
        return new SizeUnit(size, UnitType.PX);
    }

    public static SizeUnit pt(double size) {
        return new SizeUnit(size, UnitType.PT);
    }

    public static SizeUnit em(double size) {
        return new SizeUnit(size, UnitType.EM);
    }

    public static SizeUnit percent(double size) {
        return new SizeUnit(size, UnitType.PERCENT);
    }
    private final double size;
    private final UnitType unitType;

    public SizeUnit(double size, UnitType unitType) {
        this.size = size;
        this.unitType = unitType;
    }

    public double getSize() {
        return size;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void build(StringBuilder builder) {
        builder.append(size).append(unitType.unit);
    }

    public enum UnitType {
        PX("px"),
        PT("pt"),
        EM("em"),
        PERCENT("%");

        private final String unit;

        UnitType(String unit) {
            this.unit = unit;
        }
    }
}
