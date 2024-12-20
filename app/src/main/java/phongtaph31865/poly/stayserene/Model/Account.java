package phongtaph31865.poly.stayserene.Model;

public class Account {
    private String Uid, username, avt, email, gioiTinh, ngaySinh, password, quocTich, sdt, diaChi, _id, imgcccdtruoc, imgcccdsau, cccd, token;
    private int role;


    public Account(String uid, String username, String avt, String email, String gioiTinh, String ngaySinh, String password, String quocTich, String sdt, String diaChi, int role, String cccd, String _id, String imgcccdtruoc, String imgcccdsau) {
        Uid = uid;
        this._id = _id;
        this.username = username;
        this.avt = avt;
        this.email = email;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.password = password;
        this.quocTich = quocTich;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.role = role;
        this.cccd = cccd;
        this.imgcccdtruoc = imgcccdtruoc;
        this.imgcccdsau = imgcccdsau;
    }

    public Account() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImgcccdtruoc() {
        return imgcccdtruoc;
    }

    public void setImgcccdtruoc(String imgcccdtruoc) {
        this.imgcccdtruoc = imgcccdtruoc;
    }

    public String getImgcccdsau() {
        return imgcccdsau;
    }

    public void setImgcccdsau(String imgcccdsau) {
        this.imgcccdsau = imgcccdsau;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQuocTich() {
        return quocTich;
    }

    public void setQuocTich(String quocTich) {
        this.quocTich = quocTich;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
