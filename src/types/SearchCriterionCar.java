package types;

public enum SearchCriterionCar {
    AVAILABLE("Доступна"),
    NO_AVAILABLE("Недоступна"),
    MODEL("Модель"),
    FUEL("Уровень топлива");

    private final String displayName;

    SearchCriterionCar(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
