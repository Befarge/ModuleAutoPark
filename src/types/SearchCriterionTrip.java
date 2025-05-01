package types;

public enum SearchCriterionTrip {
    DRIVER("По номеру водителя"),
    CAR("Номер машины"),
    END("Закончена"),
    NO_END("Не закончена");

    private final String displayName;

    SearchCriterionTrip(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}