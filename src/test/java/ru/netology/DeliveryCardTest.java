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

public class DeliveryCardTest {

    private String dateForTest() {
        LocalDate date = LocalDate.now();
        date = date.plusDays(4);
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
        $("[data-test-id=date] input").setValue(dateForTest());
        $("[data-test-id=name] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79998888888");
        $("[data-test-id=agreement] span").click();
        $x("//span[text()='Забронировать']").click();
        $x("//div[contains(@class, 'notification__title')]").should(appear, Duration.ofSeconds(15));
    }
}
