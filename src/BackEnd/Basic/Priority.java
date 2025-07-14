package BackEnd.Basic;

public enum Priority {
    REGULAR,
    URGENT,
    SPECIAL;


    public static String[] getArrStringValues() {
   return new String[]{"REGULAR", "URGENT", "SPECIAL"};
    }
}
