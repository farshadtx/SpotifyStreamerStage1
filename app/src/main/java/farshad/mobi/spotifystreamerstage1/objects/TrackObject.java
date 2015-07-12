package farshad.mobi.spotifystreamerstage1.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Farshad on 7/12/15.
 */
public class TrackObject implements Parcelable {
    public String Id;
    public String Name;
    public String AlbumName;
    public String AlbumThumbImage;
    public String PreviewURL;

    public TrackObject(String _id, String _name, String _albumName, String _thumbImage, String _previewURL) {
        this.Id = _id;
        this.Name = _name;
        this.AlbumName = _albumName;
        this.AlbumThumbImage = _thumbImage;
        this.PreviewURL = _previewURL;
    }

    private TrackObject(Parcel in) {
        Id = in.readString();
        Name = in.readString();
        AlbumName = in.readString();
        AlbumThumbImage = in.readString();
        PreviewURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(Name);
        dest.writeString(AlbumName);
        dest.writeString(AlbumThumbImage);
        dest.writeString(PreviewURL);
    }

    public final Parcelable.Creator<TrackObject> CREATOR = new Parcelable.Creator<TrackObject>(){

        @Override
        public TrackObject createFromParcel(Parcel source) {
            return new TrackObject(source);
        }

        @Override
        public TrackObject[] newArray(int i) {
            return new TrackObject[i];
        }
    };
}
