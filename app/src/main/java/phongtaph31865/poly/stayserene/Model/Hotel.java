package phongtaph31865.poly.stayserene.Model;

public class Hotel {
    private String _id, anhKhachSan, diaChi, emailKhachSan, moTaKhachSan, sdt, tenKhachSan;
    private int status;
    private float danhGia;

    public Hotel(String _id, String anhKhachSan, String diaChi, String emailKhachSan, String moTaKhachSan, String sdt, String tenKhachSan, int status, float danhGia) {
        this._id = _id;
        this.anhKhachSan = anhKhachSan;
        this.diaChi = diaChi;
        this.emailKhachSan = emailKhachSan;
        this.moTaKhachSan = moTaKhachSan;
        this.sdt = sdt;
        this.tenKhachSan = tenKhachSan;
        this.status = status;
        this.danhGia = danhGia;
    }

    public Hotel() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAnhKhachSan() {
        return anhKhachSan;
    }

    public void setAnhKhachSan(String anhKhachSan) {
        this.anhKhachSan = anhKhachSan;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getEmailKhachSan() {
        return emailKhachSan;
    }

    public void setEmailKhachSan(String emailKhachSan) {
        this.emailKhachSan = emailKhachSan;
    }

    public String getMoTaKhachSan() {
        return moTaKhachSan;
    }

    public void setMoTaKhachSan(String moTaKhachSan) {
        this.moTaKhachSan = moTaKhachSan;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getTenKhachSan() {
        return tenKhachSan;
    }

    public void setTenKhachSan(String tenKhachSan) {
        this.tenKhachSan = tenKhachSan;
    }

    public float getDanhGia() {
        return danhGia;
    }

    public void setDanhGia(float danhGia) {
        this.danhGia = danhGia;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
