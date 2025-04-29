package types;


public enum SearchCriterionUser {
    ON_TRIP("В пути"),
    NO_ON_TRIP("Не в пути"),
    LOGIN("Логин"),
    FIRSTNAME("Имя"),
    MIDDLENAME("Отчество"),
    LASTNAME("Фамилия"),
    CONFIRMED("Подтвержден"),
    BLOCKED("Заблокирован"),
    WAIT("В ожидание");



    private final String displayName;

    SearchCriterionUser(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
