package com.aepl.sam.actions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CalendarActions {
	private WebDriver driver;
	private WebDriverWait wait;

	// Constructor
	public CalendarActions(WebDriver driver, WebDriverWait wait) {
		if (driver == null) {
			throw new IllegalArgumentException("WebDriver instance cannot be null");
		}
		this.driver = driver;
		this.wait = wait;
	}

	public void selectDate(By calendarLocator, String targetDate) {
		if (calendarLocator == null || targetDate == null || targetDate.isEmpty()) {
			throw new IllegalArgumentException("calendarLocator and targetDate cannot be null or empty");
		}

		try {
			WebElement calendarElement = wait.until(ExpectedConditions.elementToBeClickable(calendarLocator));
			calendarElement.click();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			LocalDate targetLocalDate = LocalDate.parse(targetDate, formatter);
			String targetDay = String.valueOf(targetLocalDate.getDayOfMonth());
			String targetMonth = targetLocalDate.getMonth().name().substring(0, 3).toLowerCase();
			String targetYear = String.valueOf(targetLocalDate.getYear());

			System.out.println(
					"Target date details: Day=" + targetDay + ", Month=" + targetMonth + ", Year=" + targetYear);

			// Open year selection dropdown
			WebElement dropdown = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//span[@aria-hidden='true' and contains(text(), 'MAR 2025')]")));
			dropdown.click();

			// Select year
			WebElement yearElement = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='" + targetYear + "']")));
			yearElement.click();

			// Select month
			WebElement monthElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"//span[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '"
							+ targetMonth + "')]")));
			monthElement.click();

			// Select day
			WebElement dayElement = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), '" + targetDay
							+ "') and contains(@class, 'mat-calendar-body-cell-content')]")));
			dayElement.click();
		} catch (Exception e) {
			throw new RuntimeException("Failed to select date: " + targetDate, e);
		}
	}
}
