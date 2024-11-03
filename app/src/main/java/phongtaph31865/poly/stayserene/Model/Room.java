package phongtaph31865.poly.stayserene.Model;

public class Room {
    private String _id, IdLoaiPhong, anhPhong, moTaPhong;
    private int giaPhong, soTang, tinhTrangPhong, soPhong ;

    public Room() {
    }

    public Room(String _id, String idLoaiPhong, String anhPhong, String moTaPhong, int soPhong, int giaPhong, int soTang, int tinhTrangPhong) {
        this._id = _id;
        IdLoaiPhong = idLoaiPhong;
        this.anhPhong = anhPhong;
        this.moTaPhong = moTaPhong;
        this.soPhong = soPhong;
        this.giaPhong = giaPhong;
        this.soTang = soTang;
        this.tinhTrangPhong = tinhTrangPhong;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIdLoaiPhong() {
        return IdLoaiPhong;
    }

    public void setIdLoaiPhong(String idLoaiPhong) {
        IdLoaiPhong = idLoaiPhong;
    }

    public String getAnhPhong() {
        return anhPhong;
    }

    public void setAnhPhong(String anhPhong) {
        this.anhPhong = anhPhong;
    }

    public String getMoTaPhong() {
        return moTaPhong;
    }

    public void setMoTaPhong(String moTaPhong) {
        this.moTaPhong = moTaPhong;
    }

    public int getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(int soPhong) {
        this.soPhong = soPhong;
    }

    public int getGiaPhong() {
        return giaPhong;
    }

    public void setGiaPhong(int giaPhong) {
        this.giaPhong = giaPhong;
    }

    public int getSoTang() {
        return soTang;
    }

    public void setSoTang(int soTang) {
        this.soTang = soTang;
    }

    public int getTinhTrangPhong() {
        return tinhTrangPhong;
    }

    public void setTinhTrangPhong(int tinhTrangPhong) {
        this.tinhTrangPhong = tinhTrangPhong;
    }
}
