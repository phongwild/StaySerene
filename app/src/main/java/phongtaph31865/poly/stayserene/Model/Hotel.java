package phongtaph31865.poly.stayserene.Model;

public class Hotel {
    private String IdHotel, anhKhachSan, diaChi, emailKhachSan, moTaKhachSan, sdtKhachSan, tenKhachSan;
    private int status;
    private float danhGia;

    public Hotel(String idHotel, String anhKhachSan, String diaChi, String emailKhachSan, String moTaKhachSan, String sdtKhachSan, String tenKhachSan, int status, float danhGia) {
        IdHotel = idHotel;
        this.anhKhachSan = anhKhachSan;
        this.diaChi = diaChi;
        this.emailKhachSan = emailKhachSan;
        this.moTaKhachSan = moTaKhachSan;
        this.sdtKhachSan = sdtKhachSan;
        this.tenKhachSan = tenKhachSan;
        this.status = status;
        this.danhGia = danhGia;
    }

    public Hotel() {
    }

    public String getIdHotel() {
        return IdHotel;
    }

    public void setIdHotel(String idHotel) {
        IdHotel = idHotel;
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

    public String getSdtKhachSan() {
        return sdtKhachSan;
    }

    public void setSdtKhachSan(String sdtKhachSan) {
        this.sdtKhachSan = sdtKhachSan;
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
