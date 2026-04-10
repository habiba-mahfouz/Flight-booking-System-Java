
package dsproject;

public class Rate {

        private final double WebRate;
        private final double ServiceRate;

        public Rate(double WebRateEntered, double ServiceRateEntered) {
                this.WebRate = WebRateEntered;
                this.ServiceRate = ServiceRateEntered;      
        }

       
        public double getRate() {
            return (WebRate + ServiceRate)/2;
        }
        public boolean isValid() {
            return getRate() >= 0 && getRate() <= 5;
        }
    }
