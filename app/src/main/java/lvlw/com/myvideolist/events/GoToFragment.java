package lvlw.com.myvideolist.events;

/**
 * Created by Wantrer on 2017/4/23 0023.
 */

public class GoToFragment {
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public GoToFragment(Integer index) {
        this.index = index;
    }

    Integer index;
}
