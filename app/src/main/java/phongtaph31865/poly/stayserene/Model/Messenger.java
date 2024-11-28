package phongtaph31865.poly.stayserene.Model;

public class Messenger {
    private String _id;
    private String IdKhachSan;
    private String Uid;
    private String thoiGianGui;
    private String noiDungGui;
    private String vaiTro;
    private int trangThaiKh;
    private int trangThaiNv;
    private String userTokenFCM;

    // Constructor
    public Messenger(String _id, String idKhachSan, String uid, String thoiGianGui,
                     String noiDungGui, String vaiTro, int trangThaiKh, int trangThaiNv, String userTokenFCM) {
        this._id = _id;
        this.IdKhachSan = idKhachSan;
        this.Uid = uid;
        this.thoiGianGui = thoiGianGui;
        this.noiDungGui = noiDungGui;
        this.vaiTro = vaiTro;
        this.trangThaiKh = trangThaiKh;
        this.trangThaiNv = trangThaiNv;
        this.userTokenFCM = userTokenFCM;
    }

    public Messenger() {
    }

    // Getters and setters

    public String getUserTokenFCM() {
        return userTokenFCM;
    }

    public void setUserTokenFCM(String userTokenFCM) {
        this.userTokenFCM = userTokenFCM;
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
        this.IdKhachSan = idKhachSan;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        this.Uid = uid;
    }

    public String getThoiGianGui() {
        return thoiGianGui;
    }

    public void setThoiGianGui(String thoiGianGui) {
        this.thoiGianGui = thoiGianGui;
    }

    public String getNoiDungGui() {
        return noiDungGui;
    }

    public void setNoiDungGui(String noiDungGui) {
        this.noiDungGui = noiDungGui;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public int getTrangThaiKh() {
        return trangThaiKh;
    }

    public void setTrangThaiKh(int trangThaiKh) {
        this.trangThaiKh = trangThaiKh;
    }

    public int getTrangThaiNv() {
        return trangThaiNv;
    }

    public void setTrangThaiNv(int trangThaiNv) {
        this.trangThaiNv = trangThaiNv;
    }
}