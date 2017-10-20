package model;

/**
 * Created by 我是小丑逼 on 2017/1/2.
 */

public class contactListItem {
    public String firstLetter;//首字符
    public String url;//图片的url
    public String contact;//联系人的名字
    public boolean showFirstLetter = true;
    public String groupId;
    public boolean isGroupManager;

    public String getFirstLetter(){
        return String.valueOf(contact.charAt(0)).toUpperCase();
    }
}
