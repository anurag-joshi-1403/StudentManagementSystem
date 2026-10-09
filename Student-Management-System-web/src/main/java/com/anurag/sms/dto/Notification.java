package com.anurag.sms.dto;

/**
 * One item in the navbar's notification panel (F1). Built on the fly from
 * live data by NotificationService, so there is no table to keep in step:
 * paying a fee or taking attendance changes the next page's list.
 *
 * @param icon   Bootstrap Icons name, e.g. "bi-cash-stack"
 * @param tone   colour class in notification.css: red, orange or blue
 * @param title  the headline, e.g. "1 overdue fee"
 * @param detail the line under it
 * @param link   where clicking the item goes
 */
public record Notification(String icon, String tone, String title, String detail, String link) {
}
