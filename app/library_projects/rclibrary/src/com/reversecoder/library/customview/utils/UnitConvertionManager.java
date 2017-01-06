package com.reversecoder.library.customview.utils;

import java.text.DecimalFormat;

/**
 * Created by Rashed on 4/22/2016.
 */
public class UnitConvertionManager {

    public enum Units {
        ANGSTORM(1e-10),
        ASTRONOMICAL_UNIT(1495978714.64e2),
        BARLEYCORN(8.46e-3),
        BOHR(5.3e-11),
        CABLE_LENGTH(185.2),
        CENTIMETRE(0.01),
        CHAIN(20.11684),
        CUBIT(0.5),
        ELL(1.143),
        FATHOM(1.8288),
        FERMI(1e-15),
        FINGER(0.022225),
        FOOT(0.304799735),
        FRENCH(0.0003),
        FURLONG(201.168),
        HAND(0.1016),
        INCH(0.0254),
        KILOMETRE(1000),
        LEAGUE(4828.032),
        LIGHT_YEAR(9.4607304725808e15),
        LIGHT_DAY(2.59020683712e13),
        LIGHT_HOUR(1.0792528488e12),
        LIGHT_MINUTE(1.798754748e10),
        LIGHT_SECOND(299792458),
        LINE(0.002116),
        LINK_GUNTERS_SURVEYORS(0.201168),
        LINK_RAMSDENS_ENGINEERS(0.3048),
        METRE(1),
        MICKEY(1.27e-4),
        MICRON(1e-6),
        MIL(2.54e-5),
        MILE(1609.344),
        MILLIMETRE(1e-3),
        NAIL(0.05715),
        NANOMETER(1e-9),
        NAUTICAL_LEAGUE(5556),
        NAUTICAL_MILE(1853),
        PACE(0.762),
        PALM(0.0762),
        PARSEC(30.857e15),
        POINT(0.000351450),
        QUARTER(0.2286),
        ROD(5.0292),
        ROPE(6.096),
        SPAN(0.2286),
        SPAT(1e12),
        STICK(0.0508),
        PICOMETRE(1e-12),
        X_UNIT(1.002e-13),
        YARD(0.9144);

        Units(double value) {
            this.length = value;
        }

        public double length;

        public double getLength() {
            return length;
        }

        public void setLength(double length) {
            this.length = length;
        }

        @Override
        public String toString() {
            return "Units{" +
                    "length=" + length +
                    '}';
        }
    }

    public static double convertLength(double length, Units from, Units to) {
        if ((from == to) || (length <= 0)) {
            return length;
        } else {
            double from_length__in_meter = from.length;
            double to_length_in_meter = to.length;

            double metre_len = (1 / to_length_in_meter);

            double d = length * from_length__in_meter * metre_len;

            if (d < 10e-5 || d > 10e5) {
                DecimalFormat decimalFormat = new DecimalFormat("0.00E00");
                String s = decimalFormat.format(d);
                d = Double.parseDouble(s);
                return d;
            } else {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String s = decimalFormat.format(d);
                d = Double.parseDouble(s);
                return d;
            }
        }
    }
}
