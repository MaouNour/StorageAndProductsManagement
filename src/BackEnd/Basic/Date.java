package BackEnd.Basic;

import java.util.Calendar;

import BackEnd.Exceptions.PastDatesException;

public class Date {
    private int day;
    private int month;
    private int year;

    public Date(int year, int month, int day) throws PastDatesException {

        java.util.Date date = new java.util.Date(); // current date and time
        long timeInMillis = date.getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);

        int yearCurrent = calendar.get(Calendar.YEAR);
        int monthCurrent = calendar.get(Calendar.MONTH) + 1;
        int dayCurrent = calendar.get(Calendar.DAY_OF_MONTH);

        if (day <= 0 || month <= 0 || year <= 0)
            throw new PastDatesException("Do not allow you to enter zero or a negative number");

        if (year == yearCurrent)
            if (month == monthCurrent)
                if (day == dayCurrent || day > dayCurrent) {
                    this.day = day;
                    this.month = month;
                    this.year = year;
                } else
                    throw new PastDatesException("Do not allow you to enter old Date because the input day : " + day
                            + " smaller than Current " + dayCurrent);
            else if (month > monthCurrent) {
                this.day = day;
                this.month = month;
                this.year = year;
            } else
                throw new PastDatesException("Do not allow you to enter old Date because the input month : " + month
                        + " smaller than Current " + monthCurrent);
        else if (year > yearCurrent) {
            this.day = day;
            this.month = month;
            this.year = year;
        } else
            throw new PastDatesException("Do not allow you to enter old Date because the input year : " + year
                    + " smaller than Current " + yearCurrent);
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "{ " + year + " / " + month + " / " + day + " }";
    }
}
