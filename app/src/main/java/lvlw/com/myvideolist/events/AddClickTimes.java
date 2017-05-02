package lvlw.com.myvideolist.events;

/**
 * Created by Wantrer on 2017/5/2 0002.
 */

public class AddClickTimes {
    public AddClickTimes(int times) {
        this.times = times;
    }

    public int getTimes() {
        return times;
    }

    private int times;
}
