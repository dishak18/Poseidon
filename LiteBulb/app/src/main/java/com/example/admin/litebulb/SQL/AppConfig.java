package com.example.admin.litebulb.SQL;

public class AppConfig {
    //studio.litebulb.in
    public static String IP_ADDRESS="http://litebulb.in/studio/";
    public static String IP_ADDRESS_TEMP="http://192.168.1.5/";

    public static String URL_LOGIN = IP_ADDRESS+"android_login_api/login.php";
    public static String URL_REGISTER = IP_ADDRESS+"android_login_api/register.php";
    public static String URL_PHOTOS = IP_ADDRESS+"uploads";
    public static String URL_USER_FEATURED = IP_ADDRESS_TEMP+"android_login_api/featured_authors.php";
    public static String URL_ITEM = IP_ADDRESS_TEMP+"android_login_api/items.php";
    public static String URL_CATEGORIES = IP_ADDRESS+"android_login_api/categories.php";
    public static String URL = IP_ADDRESS+"android_login_api/index.php";
    public static String URL_ITEMS_TO_CATEGORY = IP_ADDRESS+"android_login_api/items_to_category.php";
    public static String URL_SYSTEM = IP_ADDRESS_TEMP+"android_login_api/system.php";
    public static String URL_BADGES = IP_ADDRESS_TEMP+"android_login_api/badges.php";
    public static String URL_TAGS = IP_ADDRESS_TEMP+"android_login_api/tags.php";
    public static String URL_ITEMS_TAGS = IP_ADDRESS_TEMP+"android_login_api/items_tags.php";
}

