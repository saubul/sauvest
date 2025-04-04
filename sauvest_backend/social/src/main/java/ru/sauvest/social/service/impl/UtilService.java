package ru.sauvest.social.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import ru.sauvest.social.dto.CandleDto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


public final class UtilService {

    private UtilService() {
    }

    public static String getUsernameFromJwt(String authorizationHeader) {
        String accessToken = authorizationHeader.substring("Bearer ".length());
        DecodedJWT decodedJWT = JWT.decode(accessToken);
        return decodedJWT.getClaim("username").asString();
    }

    public static Date convertStringToDate(String date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);

        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static LocalDate convertCandleDateToLocalDate(CandleDto candleDTO) {
        String candleDTODate = candleDTO.getDate().substring(0, candleDTO.getDate().length() - 5).replace("T", " ");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            return dateFormat.parse(candleDTODate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (ParseException e) {
            return null;
        }
    }

    public static CandleDto getCandleOnDate(List<CandleDto> candles, Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);
        List<CandleDto> candleDtos = candles.stream().filter(candle -> {
            String candleDTODate = candle.getDate().substring(0, candle.getDate().length() - 14);
            return candleDTODate.equals(dateString);
        }).toList();
        return !candleDtos.isEmpty() ? candleDtos.get(0) : null;
    }

    public static Date parse(String dateString, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

}
