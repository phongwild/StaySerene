package phongtaph31865.poly.stayserene.Model;

public class Order_Room {
    private String _id, IdPhong, Uid, IdDichVu, orderTime, timeGet, timeCheckout, note, img;
    private float total;
    private int status;

    public Order_Room(String _id, String idPhong, String uid, String idDichVu, String orderTime, String timeGet, String timeCheckout, String note, String img, float total, int status) {
        this._id = _id;
        IdPhong = idPhong;
        Uid = uid;
        IdDichVu = idDichVu;
        this.orderTime = orderTime;
        this.timeGet = timeGet;
        this.timeCheckout = timeCheckout;
        this.note = note;
        this.img = img;
        this.total = total;
        this.status = status;
    }

    public Order_Room() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIdPhong() {
        return IdPhong;
    }

    public void setIdPhong(String idPhong) {
        IdPhong = idPhong;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getIdDichVu() {
        return IdDichVu;
    }

    public void setIdDichVu(String idDichVu) {
        IdDichVu = idDichVu;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getTimeGet() {
        return timeGet;
    }

    public void setTimeGet(String timeGet) {
        this.timeGet = timeGet;
    }

    public String getTimeCheckout() {
        return timeCheckout;
    }

    public void setTimeCheckout(String timeCheckout) {
        this.timeCheckout = timeCheckout;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
