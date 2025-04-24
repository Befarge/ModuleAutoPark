package support;

public class UserValidator {

    // Проверка возраста
    public static boolean isValidAge(int age) {
        return age >= 18;
    }

    // Проверка на пустые или null значения
    public static boolean areFieldsNotEmpty(String... fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // Проверка номера телефона: начинается с 89 и содержит ровно 11 цифр
    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("^89\\d{9}$");
    }
}
