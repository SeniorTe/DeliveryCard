package ru.netology;


import com.codeborne.selenide.Condition;
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

    private String dateForTest(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }

    @Test
    void shouldReturnInfoHappyOrder() {
        String planningDate = dateForTest(4);

        $("[data-test-id=city] input").setValue("Якутск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(planningDate);
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79998888888");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $(".notification__content")
                .shouldHave(Condition.text(("Встреча успешно забронирована на " + planningDate)), Duration.ofSeconds(15))
                .shouldBe(visible);
    }

    @Test
    void shouldReturnInfoHappyOrderBySearchCityOfList() {
        String planningDate = dateForTest(3);

        $("[data-test-id=city] input").setValue("Вла");
        $$(".menu-item__control").first().click();
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(planningDate);
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79998888888");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $(".notification__content")
                .shouldHave(Condition.text(("Встреча успешно забронирована на " + planningDate)), Duration.ofSeconds(15))
                .shouldBe(visible);
    }

    @Test
    void shouldReturnErrorByIncorrectCity() {
        $("[data-test-id=city] input").setValue("Катайск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForTest(4));
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79998888888");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldReturnErrorByIncorrectDate() {
        $("[data-test-id=city] input").setValue("Якутск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForTest(2));
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79998888888");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldReturnErrorByIncorrectName() {
        $("[data-test-id=city] input").setValue("Якутск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForTest(4));
        $("[data-test-id=name] input").setValue("John Smith");
        $("[data-test-id=phone] input").setValue("+79998888888");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldReturnErrorByIncorrectPhone() {
        $("[data-test-id=city] input").setValue("Якутск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForTest(4));
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("999888");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldReturnErrorByCheckbox() {
        $("[data-test-id=city] input").setValue("Якутск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForTest(4));
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79998888888");
        $x("//span[text()='Забронировать']").click();
        $("[data-test-id=agreement] .checkbox__text").shouldHave(cssValue("color", "rgba(255, 92, 92, 1)"));
    }

    @Test
    void shouldReturnErrorByEmptyCity() {
        $("[data-test-id=city] input").setValue("");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForTest(4));
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79998888888");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
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
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    void shouldReturnErrorByEmptyName() {
        $("[data-test-id=city] input").setValue("Якутск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForTest(4));
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue("+79998888888");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldReturnErrorByEmptyPhone() {
        $("[data-test-id=city] input").setValue("Якутск");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        $("[data-test-id=date] input").sendKeys("\b");
        $("[data-test-id=date] input").setValue(dateForTest(4));
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }
}
