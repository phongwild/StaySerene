package phongtaph31865.poly.stayserene.Model;

public class ChangePassRequest {
    private String email;
    private String oldPassword;
    private String newPassword;

    public ChangePassRequest(String email, String oldPassword, String newPassword) {
        this.email = email;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
