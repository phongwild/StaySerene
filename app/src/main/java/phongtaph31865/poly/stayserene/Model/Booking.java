package phongtaph31865.poly.stayserene.Model;

public class Booking {
    private int IdPhong,IdDatPhong,IdDichVu;
    private String anhDatPhong,ghiChu,thoiGianDat,thoiGianNhan,thoiGianTra,trangThai,Uid;
    private float tongTien;

    public Booking() {
    }

    public Booking(int idPhong, int idDatPhong, int idDichVu, String anhDatPhong, String ghiChu, String thoiGianDat, String thoiGianNhan, String thoiGianTra, String trangThai, String uid, float tongTien) {
        IdPhong = idPhong;
        IdDatPhong = idDatPhong;
        IdDichVu = idDichVu;
        this.anhDatPhong = anhDatPhong;
        this.ghiChu = ghiChu;
        this.thoiGianDat = thoiGianDat;
        this.thoiGianNhan = thoiGianNhan;
        this.thoiGianTra = thoiGianTra;
        this.trangThai = trangThai;
        Uid = uid;
        this.tongTien = tongTien;
    }

    public int getIdPhong() {
        return IdPhong;
    }

    public void setIdPhong(int idPhong) {
        IdPhong = idPhong;
    }

    public int getIdDatPhong() {
        return IdDatPhong;
    }

    public void setIdDatPhong(int idDatPhong) {
        IdDatPhong = idDatPhong;
    }

    public int getIdDichVu() {
        return IdDichVu;
    }

    public void setIdDichVu(int idDichVu) {
        IdDichVu = idDichVu;
    }

    public String getAnhDatPhong() {
        return anhDatPhong;
    }

    public void setAnhDatPhong(String anhDatPhong) {
        this.anhDatPhong = anhDatPhong;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getThoiGianDat() {
        return thoiGianDat;
    }

    public void setThoiGianDat(String thoiGianDat) {
        this.thoiGianDat = thoiGianDat;
    }

    public String getThoiGianNhan() {
        return thoiGianNhan;
    }

    public void setThoiGianNhan(String thoiGianNhan) {
        this.thoiGianNhan = thoiGianNhan;
    }

    public String getThoiGianTra() {
        return thoiGianTra;
    }

    public void setThoiGianTra(String thoiGianTra) {
        this.thoiGianTra = thoiGianTra;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public float getTongTien() {
        return tongTien;
    }

    public void setTongTien(float tongTien) {
        this.tongTien = tongTien;
    }
}
