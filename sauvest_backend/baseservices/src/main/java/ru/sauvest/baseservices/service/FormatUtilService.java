package ru.sauvest.baseservices.service;

public final class FormatUtilService {

    private FormatUtilService() {}

    public static String getNonNullExceptionMessage(Exception e) {
        if (e.getMessage() == null) {
            return String.format("Класс исключения: %s. Первая строка трейса: %s.", e.getClass().getName(), e.getStackTrace()[0].toString());
        }
        return e.getMessage();
    }
}
