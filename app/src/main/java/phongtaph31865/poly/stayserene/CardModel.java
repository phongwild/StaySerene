package phongtaph31865.poly.stayserene;

public class CardModel {

        private String billId;
        private String hotelId;
        private String dateTime;
        private String totalAmount;
        private String paymentMethod;
        private String status;

        public CardModel(String billId, String hotelId, String dateTime, String totalAmount, String paymentMethod, String status) {
            this.billId = billId;
            this.hotelId = hotelId;
            this.dateTime = dateTime;
            this.totalAmount = totalAmount;
            this.paymentMethod = paymentMethod;
            this.status = status;
        }

        public String getBillId() {
            return billId;
        }

        public String getHotelId() {
            return hotelId;
        }

        public String getDateTime() {
            return dateTime;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public String getStatus() {
            return status;
        }
    }


