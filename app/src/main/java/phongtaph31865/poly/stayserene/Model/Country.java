package phongtaph31865.poly.stayserene.Model;

public class Country {
    private String name;
    private int flag_country;

    public Country(String name, int flag_country) {
        this.name = name;
        this.flag_country = flag_country;
    }

    public int getFlag_country() {
        return flag_country;
    }

    public void setFlag_country(int flag_country) {
        this.flag_country = flag_country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
