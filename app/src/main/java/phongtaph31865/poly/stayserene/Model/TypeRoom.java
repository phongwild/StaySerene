package phongtaph31865.poly.stayserene.Model;

public class TypeRoom {
    private String _id, IdKhachSan, tenLoaiPhong, moTaLoaiPhong, anhLoaiPhong, tienNghi;
    private int soLuongPhong;
    private float dienTich;
    private double giaLoaiPhong;

    public TypeRoom() {
    }

    public TypeRoom(String _id, String idKhachSan, String tenLoaiPhong, String moTaLoaiPhong, String anhLoaiPhong, String tienNghi, int soLuongPhong, float dienTich, double giaLoaiPhong) {
        this._id = _id;
        IdKhachSan = idKhachSan;
        this.tenLoaiPhong = tenLoaiPhong;
        this.moTaLoaiPhong = moTaLoaiPhong;
        this.anhLoaiPhong = anhLoaiPhong;
        this.tienNghi = tienNghi;
        this.soLuongPhong = soLuongPhong;
        this.dienTich = dienTich;
        this.giaLoaiPhong = giaLoaiPhong;
    }

    public double getGiaLoaiPhong() {
        return giaLoaiPhong;
    }

    public void setGiaLoaiPhong(double giaLoaiPhong) {
        this.giaLoaiPhong = giaLoaiPhong;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIdKhachSan() {
        return IdKhachSan;
    }

    public void setIdKhachSan(String idKhachSan) {
        IdKhachSan = idKhachSan;
    }

    public String getTenLoaiPhong() {
        return tenLoaiPhong;
    }

    public void setTenLoaiPhong(String tenLoaiPhong) {
        this.tenLoaiPhong = tenLoaiPhong;
    }

    public String getMoTaLoaiPhong() {
        return moTaLoaiPhong;
    }

    public void setMoTaLoaiPhong(String moTaLoaiPhong) {
        this.moTaLoaiPhong = moTaLoaiPhong;
    }

    public String getAnhLoaiPhong() {
        return anhLoaiPhong;
    }

    public void setAnhLoaiPhong(String anhLoaiPhong) {
        this.anhLoaiPhong = anhLoaiPhong;
    }

    public String getTienNghi() {
        return tienNghi;
    }

    public void setTienNghi(String tienNghi) {
        this.tienNghi = tienNghi;
    }

    public int getSoLuongPhong() {
        return soLuongPhong;
    }

    public void setSoLuongPhong(int soLuongPhong) {
        this.soLuongPhong = soLuongPhong;
    }

    public float getDienTich() {
        return dienTich;
    }

    public void setDienTich(float dienTich) {
        this.dienTich = dienTich;
    }
}
