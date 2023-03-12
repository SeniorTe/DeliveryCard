package ru.netology;


import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeliveryCardTest {

    private String dateForPositiveTest() {
        LocalDate date = LocalDate.now();
        date = date.plusDays(4);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(format);
    }

    private String dateForNegativeTest() {
        LocalDate date = LocalDate.now();
        date = date.plusDays(1);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(format);
    }

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }

    @Test
    void shouldReturnInfoHappyOrder() {
        $("[data-test-id=city] input").setValue("Якутск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForPositiveTest());
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79998888888");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $x("//div[contains(@class, 'notification__title')]").should(appear, Duration.ofSeconds(15));
    }

    @Test
    void shouldReturnInfoHappyOrderBySearchCityOfList() {
        $("[data-test-id=city] input").setValue("Вла");
        $$(".menu-item__control").first().click();
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForPositiveTest());
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79998888888");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $x("//div[contains(@class, 'notification__title')]").should(appear, Duration.ofSeconds(15));
    }

    @Test
    void shouldReturnErrorByIncorrectCity() {
        $("[data-test-id=city] input").setValue("Катайск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForPositiveTest());
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79998888888");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        String expected = "Доставка в выбранный город недоступна";
        String actual = $("[data-test-id=city].input_invalid .input__sub").getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnErrorByIncorrectDate() {
        $("[data-test-id=city] input").setValue("Якутск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForNegativeTest());
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79998888888");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        String expected = "Заказ на выбранную дату невозможен";
        String actual = $("[data-test-id=date] .input_invalid .input__sub").getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnErrorByIncorrectName() {
        $("[data-test-id=city] input").setValue("Якутск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForPositiveTest());
        $("[data-test-id=name] input").setValue("John Smith");
        $("[data-test-id=phone] input").setValue("+79998888888");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = $("[data-test-id=name].input_invalid .input__sub").getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnErrorByIncorrectPhone() {
        $("[data-test-id=city] input").setValue("Якутск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForPositiveTest());
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("999888");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = $("[data-test-id=phone].input_invalid .input__sub").getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnErrorByCheckbox() {
        $("[data-test-id=city] input").setValue("Якутск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForPositiveTest());
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79998888888");
        $x("//span[text()='Забронировать']").click();
        String expected = "rgba(255, 92, 92, 1)";
        String actual = $("[data-test-id=agreement] .checkbox__text").getCssValue("color");
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnErrorByEmptyCity() {
        $("[data-test-id=city] input").setValue("");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForPositiveTest());
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79998888888");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        String expected = "Поле обязательно для заполнения";
        String actual = $("[data-test-id=city].input_invalid .input__sub").getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnErrorByEmptyDate() {
        $("[data-test-id=city] input").setValue("Якутск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79998888888");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        String expected = "Неверно введена дата";
        String actual = $("[data-test-id=date] .input_invalid .input__sub").getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnErrorByEmptyName() {
        $("[data-test-id=city] input").setValue("Якутск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForPositiveTest());
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue("+79998888888");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        String expected = "Поле обязательно для заполнения";
        String actual = $("[data-test-id=name].input_invalid .input__sub").getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnErrorByEmptyPhone() {
        $("[data-test-id=city] input").setValue("Якутск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForPositiveTest());
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        String expected = "Поле обязательно для заполнения";
        String actual = $("[data-test-id=phone].input_invalid .input__sub").getText().trim();
        assertEquals(expected, actual);
    }
}
