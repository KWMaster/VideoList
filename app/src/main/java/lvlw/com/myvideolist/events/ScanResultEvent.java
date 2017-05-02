package lvlw.com.myvideolist.events;

import com.VideoInfo.entity.VideoInfo;

import java.util.List;

import lvlw.com.myvideolist.entity.QFileInfo;

/**
 * Created by Wantrer on 2017/4/23 0023.
 */

public class ScanResultEvent {
    private String addSuccessful;


    public ScanResultEvent(String addSuccessful) {
        this.addSuccessful = addSuccessful;
    }

    public ScanResultEvent() {
    }

    public String getAddSuccessful() {
        return addSuccessful;
    }

    public void setAddSuccessful(String addSuccessful) {
        this.addSuccessful = addSuccessful;
    }

}
