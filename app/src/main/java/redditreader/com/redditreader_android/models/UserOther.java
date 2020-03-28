package redditreader.com.redditreader_android.models;

public class UserOther {
    private String _username = "Username";
    private String _profileURL = "https://www.redditstatic.com/avatars/avatar_default_03_FFB000.png";

    public UserOther(String _username, String _profileURL) {
        this._username = _username;
        this._profileURL = _profileURL;
    }

    public String get_username() {
        return _username;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public String get_profileURL() {
        return _profileURL;
    }

    public void set_profileURL(String _profileURL) {
        this._profileURL = _profileURL;
    }
}
