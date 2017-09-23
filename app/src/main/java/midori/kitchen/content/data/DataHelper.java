package midori.kitchen.content.data;

import java.util.ArrayList;

import midori.kitchen.content.model.HistoryModel;
import midori.kitchen.content.model.MenuModel;
import midori.kitchen.content.model.SportModel;

/**
 * Created by M. Asrof Bayhaqqi on 3/11/2017.
 */

public class DataHelper {

    public static ArrayList<MenuModel> getDataMenu() {
        ArrayList<MenuModel> menuItems = new ArrayList<>();
        String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
                "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. " +
                "\n\n" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
                "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

        menuItems.add(new MenuModel("1", "Proll Tape", description, "https://instagram.fcgk10-1.fna.fbcdn.net/t51.2885-15/e35/14073174_1101393899897405_1348272808_n.jpg", 5000, "Senin, 13 Maret 2017"));
        menuItems.add(new MenuModel("2", "Pecel Bu Iin", description, "https://b.zmtcdn.com/data/pictures/4/7415434/33fb37733195c98b8106f2e9790712f1_featured_v2.jpg", 7000, "Selasa, 14 Maret 2017"));
        menuItems.add(new MenuModel("3", "Soto Bu Sri", description, "http://blog.travelio.com/wp-content/uploads/2015/03/Soto-Lamongan-Jawa-Timur-Indonesia.jpg", 10000, "Senin, 14 Maret 2017"));
        menuItems.add(new MenuModel("4", "Bosingke", description, "https://scontent.cdninstagram.com/t51.2885-15/s640x640/sh0.08/e35/14240914_296350614053986_729584486_n.jpg", 3500, "Salasa, 15 Maret 2017"));
        menuItems.add(new MenuModel("5", "Bukan Tahu Biasa", description, "https://scontent-sin6-1.cdninstagram.com/t51.2885-15/e35/16585290_1157947320997776_167456290925182976_n.jpg", 4000, "Salasa, 15 Maret 2017"));
        menuItems.add(new MenuModel("6", "Kare Ayam Bu Sri", description, "https://ichaawe.files.wordpress.com/2008/12/824163193_cbd0c58940.jpg", 12000, "Rabu, 16 Maret 2017"));
        menuItems.add(new MenuModel("7", "Rawon Sapi Bu Sri", description, "http://tips-cara.info/wp-content/uploads/2014/05/resep-cara-membuat-rawon-daging-sapi-surabaya-enak-700x400.jpeg", 15000, "Kamis, 17 Maret 2017"));
        menuItems.add(new MenuModel("8", "Mie Ayam Bu Iin", description, "https://resepnona.com/wp-content/uploads/2015/01/Resep-Mie-Ayam.jpg", 8000, "Jumat, 18 Maret 2017"));
        menuItems.add(new MenuModel("9", "Roti Bluder", description, "http://www.maksindo.com/wp-content/uploads/2016/01/roti-bluder-maksindo.jpg", 5000, "Sabtu, 19 Maret 2017"));
        menuItems.add(new MenuModel("10", "Lemper Budi Jaya", description, "http://resepcaramasak.com/wp-content/uploads/2015/09/Cara-Membuat-Kue-Basah-Lemper-Ayam-Enak.jpg", 2500, "Minggu, 20 Maret 2017"));
        return menuItems;
    }

    public static ArrayList<HistoryModel> getDataHistory() {
        ArrayList<HistoryModel> historyItems = new ArrayList<>();
        historyItems.add(new HistoryModel("1", "Proll Tape", 5000, "Senin, 13 Maret 2017", "Delivered"));
        historyItems.add(new HistoryModel("2", "Pecel Bu Iin", 7000, "Selasa, 14 Maret 2017", "Delivered"));
        historyItems.add(new HistoryModel("3", "Soto Bu Sri", 10000, "Senin, 14 Maret 2017", "Canceled"));
        historyItems.add(new HistoryModel("4", "Bosingke", 3500, "Salasa, 15 Maret 2017", "Cooking"));
        historyItems.add(new HistoryModel("5", "Bukan Tahu Biasa", 4000, "Salasa, 15 Maret 2017", "Cooking"));
        historyItems.add(new HistoryModel("6", "Kare Ayam Bu Sri", 12000, "Rabu, 16 Maret 2017", "Delivered"));
        historyItems.add(new HistoryModel("7", "Rawon Sapi Bu Sri", 15000, "Kamis, 17 Maret 2017", "Delivered"));
        historyItems.add(new HistoryModel("8", "Mie Ayam Bu Iin", 8000, "Jumat, 18 Maret 2017", "Canceled"));
        historyItems.add(new HistoryModel("9", "Roti Bluder", 5000, "Sabtu, 19 Maret 2017", "Cooking"));
        historyItems.add(new HistoryModel("10", "Lemper Budi Jaya", 2500, "Minggu, 20 Maret 2017", "Cooking"));
        return historyItems;
    }
    public static ArrayList<SportModel> getDataSport() {
        ArrayList<SportModel> sportModels = new ArrayList<>();
        sportModels.add(new SportModel("1", "Badminton Araya", 255000, "23", "Jl Araya 1 A","https://d3u4pi4hof4b65.cloudfront.net/uploads/photograph/image/2611/regular_Badminton_Hall_HOME_PAGE.jpg"));
        sportModels.add(new SportModel("1", "Futsal Araya", 600000, "23", "Jl Araya 1 B","http://www.thefa.com/-/media/images/thefaportal/pillars/sgp/article-and-news-620x349/futsal-sports-hall-620x349.ashx?w=320&h=180&hash=A2A0462DBCEEC2383D242E3436D024EFC93F1C46"));
        sportModels.add(new SportModel("1", "Gym Araya", 500000, "23", "Jl Araya 1 C","http://4.bp.blogspot.com/-tfUvrfNcCsw/UfEcnW90bqI/AAAAAAAABSQ/MsocDYb0DRA/s1600/alamat-fitnes-malang.jpg"));
        sportModels.add(new SportModel("1", "Gym Kelapa Gading", 479000, "23", "Jl Kelapa Gading 2","http://resepcaramasak.com/wp-content/uploads/2015/09/Cara-Membuat-Kue-Basah-Lemper-Ayam-Enak.jpg"));
        sportModels.add(new SportModel("1", "Gym Safa", 480000, "23", "Jl Sawojajar 2 ","http://storanfluids.com/images/gal5.jpg"));
        sportModels.add(new SportModel("1", "Gym Celeb", 600000, "23", "Jl Soekarno Hatta","http://www3.hilton.com/resources/media/hi/BHXPHHN/en_US/img/shared/full_page_image_gallery/main/HL_fitnesscenter02_6_675x359_FitToBoxSmallDimension_Center.jpg"));

        return sportModels;
    }
}
