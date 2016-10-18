package cn.ucai.fulishop.bean;

/**
 * Created by Administrator on 2016/10/17.
 */

public class UserBean {
    /**
     * muserName : yulong
     * muserNick : yurnero
     * mavatarId : 78
     * mavatarPath : user_avatar
     * mavatarSuffix : null
     * mavatarType : 0
     * mavatarLastUpdateTime : 1476641144349
     */

    private String muserName;
    private String muserNick;
    private int mavatarId;
    private String mavatarPath;
    private Object mavatarSuffix;
    private int mavatarType;
    private String mavatarLastUpdateTime;
    private String pass;

    public String getMuserName() {
        return muserName;
    }

    public void setMuserName(String muserName) {
        this.muserName = muserName;
    }

    public String getMuserNick() {
        return muserNick;
    }

    public void setMuserNick(String muserNick) {
        this.muserNick = muserNick;
    }

    public int getMavatarId() {
        return mavatarId;
    }

    public void setMavatarId(int mavatarId) {
        this.mavatarId = mavatarId;
    }

    public String getMavatarPath() {
        return mavatarPath;
    }

    public void setMavatarPath(String mavatarPath) {
        this.mavatarPath = mavatarPath;
    }

    public Object getMavatarSuffix() {
        return mavatarSuffix;
    }

    public void setMavatarSuffix(Object mavatarSuffix) {
        this.mavatarSuffix = mavatarSuffix;
    }

    public int getMavatarType() {
        return mavatarType;
    }

    public void setMavatarType(int mavatarType) {
        this.mavatarType = mavatarType;
    }

    public String getMavatarLastUpdateTime() {
        return mavatarLastUpdateTime;
    }

    public void setMavatarLastUpdateTime(String mavatarLastUpdateTime) {
        this.mavatarLastUpdateTime = mavatarLastUpdateTime;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "muserName='" + muserName + '\'' +
                ", muserNick='" + muserNick + '\'' +
                ", mavatarId=" + mavatarId +
                ", mavatarPath='" + mavatarPath + '\'' +
                ", mavatarSuffix=" + mavatarSuffix +
                ", mavatarType=" + mavatarType +
                ", mavatarLastUpdateTime='" + mavatarLastUpdateTime + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
