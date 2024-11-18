package phongtaph31865.poly.stayserene.Model;

public class Service {
    private String _id,tenDichVu,motaDichVu,anhDichVu;
    private int giaDichVu;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTenDichVu() {
        return tenDichVu;
    }

    public void setTenDichVu(String tenDichVu) {
        this.tenDichVu = tenDichVu;
    }

    public String getMotaDichVu() {
        return motaDichVu;
    }

    public void setMotaDichVu(String motaDichVu) {
        this.motaDichVu = motaDichVu;
    }

    public String getAnhDichVu() {
        return anhDichVu;
    }

    public void setAnhDichVu(String anhDichVu) {
        this.anhDichVu = anhDichVu;
    }

    public int getGiaDichVu() {
        return giaDichVu;
    }

    public void setGiaDichVu(int giaDichVu) {
        this.giaDichVu = giaDichVu;
    }

    public Service() {
    }

    public Service(String _id, String tenDichVu, String motaDichVu, String anhDichVu, int giaDichVu) {
        this._id = _id;
        this.tenDichVu = tenDichVu;
        this.motaDichVu = motaDichVu;
        this.anhDichVu = anhDichVu;
        this.giaDichVu = giaDichVu;
    }
}
