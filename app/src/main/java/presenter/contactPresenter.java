package presenter;

import java.util.List;

import model.contactListItem;

/**
 * Created by 我是小丑逼 on 2017/1/2.
 */

public interface contactPresenter {
    void loadContacts();

    List<contactListItem> getDataList();

    void refresh();
}
