package dsproject;

public class Service {

    private int meal;
    private int wifi;
    private int seatComfort;
    private int entertainment;

    public Service(int mealChoice, int wifiChoice, int seatChoice, int entertainmentChoice) {

        switch (mealChoice) {
            case 1: meal = 20; break;
            case 2: meal = 0; break;
            default: meal = 0;
        }

        switch (wifiChoice) {
            case 1: wifi = 10; break;
            case 2: wifi = 0; break;
            default: wifi = 0;
        }

        switch (seatChoice) {
            case 1: seatComfort = 20; break;
            case 2: seatComfort = 0; break;
            default: seatComfort = 0;
        }

        switch (entertainmentChoice) {
            case 1: entertainment = 30; break;
            case 2: entertainment = 0; break;
            default: entertainment = 0;
        }
    }

    // Sum of all selected services (per ticket)
    public int getTotalServicesPrice() {
        return meal + wifi + seatComfort + entertainment;
    }
}