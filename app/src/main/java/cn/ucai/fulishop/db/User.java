package cn.ucai.fulishop.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by Administrator on 2016/10/17.
 */

@Entity
public class User {
    /**
     * muserName : yulong
     * muserNick : yurnero
     * mavatarId : 78
     * mavatarPath : user_avatar
     * mavatarSuffix : null
     * mavatarType : 0
     * mavatarLastUpdateTime : 1476641144349
     */
    @Id
    private Long id;
    @Property(nameInDb = "USER_NAME")
    @Unique
    private String muserName;
    @Property(nameInDb = "USER_NICK")
    private String muserNick;
    @Property(nameInDb = "USER_AVATAR_ID")
    private int mavatarId;
    @Property(nameInDb = "USER_AVATAR_PATH")
    private String mavatarPath;
    @Property(nameInDb = "USER_AVATAR_SUFFIX")
    private String mavatarSuffix;
    @Property(nameInDb = "USER_AVATAR_TYPE")
    private int mavatarType;
    @Property(nameInDb = "USER_AVATAR_TIME")
    private String mavatarLastUpdateTime;

    @Generated(hash = 12253437)
    public User(Long id, String muserName, String muserNick, int mavatarId, String mavatarPath, String mavatarSuffix, int mavatarType, String mavatarLastUpdateTime) {
        this.id = id;
        this.muserName = muserName;
        this.muserNick = muserNick;
        this.mavatarId = mavatarId;
        this.mavatarPath = mavatarPath;
        this.mavatarSuffix = mavatarSuffix;
        this.mavatarType = mavatarType;
        this.mavatarLastUpdateTime = mavatarLastUpdateTime;
    }

    public User(String muserName, String muserNick, int mavatarId, String mavatarPath, String mavatarSuffix, int mavatarType, String mavatarLastUpdateTime) {
        this.muserName = muserName;
        this.muserNick = muserNick;
        this.mavatarId = mavatarId;
        this.mavatarPath = mavatarPath;
        this.mavatarSuffix = mavatarSuffix;
        this.mavatarType = mavatarType;
        this.mavatarLastUpdateTime = mavatarLastUpdateTime;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getMavatarSuffix() {
        return mavatarSuffix;
    }

    public void setMavatarSuffix(String mavatarSuffix) {
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", muserName='" + muserName + '\'' +
                ", muserNick='" + muserNick + '\'' +
                ", mavatarId=" + mavatarId +
                ", mavatarPath='" + mavatarPath + '\'' +
                ", mavatarSuffix='" + mavatarSuffix + '\'' +
                ", mavatarType=" + mavatarType +
                ", mavatarLastUpdateTime='" + mavatarLastUpdateTime + '\'' +
                '}';
    }
}
