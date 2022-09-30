package zuev.nikita.client.resources;

import java.util.ListResourceBundle;

public class Resource_ru extends ListResourceBundle {

    private static final Object[][] contents = {
            {"key", "Ключ"},
            {"login", "Логин"},
            {"password", "Пароль"},
            {"signIn", "Войти"},
            {"signUp", "Зарегистрироваться"},
            {"authorization", "Авторизация"},
            {"languageChanging", "Смена языка"},
            {"table", "Таблица"},
            {"author", "Автор"},
            {"name", "Название"},
            {"coordinates", "Координаты"},
            {"creationDate", "Дата создания"},
            {"annualTurnover", "Годовой оборот"},
            {"address", "Адрес"},
            {"organizationType", "Тип организации"},
            {"TRUST", "Трастовая"},
            {"PUBLIC", "Публичная"},
            {"COMMERCIAL", "Коммерческая"},
            {"show", "Показать"},
            {"script", "Скрипт"},
            {"serverNotAvailable", "Сервер недоступен"},
            {"wrongLoginOrPassword", "Неверный логин или пароль"},
            {"userAlreadyExists", "Такой пользователь уже существует"},
            {"notChooseElement", "Элемент не выбран"},
            {"notYourElement", "Это не ваш элемент"},
            {"adding", "Добавление"},
            {"save", "Сохранить"},
            {"newElement", "Новый элемент"},
            {"integer", "Целое"},
            {"float", "Дробное"},
            {"insert", "Добавить"},
            {"edit", "Изменить"},
            {"remove", "Удалить"},
            {"clear", "Очистить"},
            {"removeLower", "Удалить элемент меньше данного"},
            {"removeGreaterKey", "Удалить элементы с бОльшим ключом"},
            {"filterGTPA", "Отфильтровать по адресам больше данного"},
            {"sortAsc", "Отсортировать по возрастанию"},
            {"sortDesc", "Отсортировать по убыванию"},
            {"emptyFields", "Поля ввода пустые"}



    };
    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
