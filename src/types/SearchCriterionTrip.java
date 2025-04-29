package types;

public enum SearchCriterionTrip {
    DRIVER("По номеру водителя"),
    CAR("Номер машины");

    private final String displayName;

    SearchCriterionTrip(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}