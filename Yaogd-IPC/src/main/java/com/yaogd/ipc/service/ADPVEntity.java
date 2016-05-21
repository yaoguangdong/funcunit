package com.yaogd.ipc.service;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 广告统计的pv实体
 */
public class ADPVEntity implements Parcelable {

    /**广告统计类型：1可见 */
    public static final int TYPE_VISIABLE = 1;
    /**广告统计类型：0曝光 */
    public static final int TYPE_EXPOSURE = 0;

    /**数据库自增主键*/
    public int _id;

	/**广告位可见和广告素材曝光的开始时间，ms*/
    public long beginTime;

    /**广告位可见和广告素材曝光的结束时间，ms*/
    public long endTime;

    /**广告位是否可见*/
    public boolean isVisable;
    
    /**pvid广告曝光/可见的唯一id*/
    public String pvid;
    
    /**广告是否有素材*/
    public boolean isHaveContent;
    
    /**素材是否加载完成*/
    public boolean isContentLoaded;

    /**广告统计类型：0曝光；1可见（如果是异步曝光的，此值由isHaveContent和isContentLoaded共同决定）*/
    public int type;

    /**广告图片类型：gif/jpg，目前只用于开屏广告*/
    public String imgType;

    public ADPVEntity(){
    }

	public ADPVEntity(String pvid, boolean isHaveContent){
		this.pvid = pvid;
		this.isHaveContent = isHaveContent;
        this.imgType = "";
	}

    public ADPVEntity(String pvid, int type, long beginTime, long endTime, String imgType){
        this.pvid = pvid;
        this.type = type;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.imgType = imgType == null ? "" : imgType;
    }

    protected ADPVEntity(Parcel in) {
        _id = in.readInt();
        beginTime = in.readLong();
        endTime = in.readLong();
        isVisable = in.readByte() != 0;
        pvid = in.readString();
        isHaveContent = in.readByte() != 0;
        isContentLoaded = in.readByte() != 0;
        type = in.readInt();
        imgType = in.readString();
    }

    public static final Creator<ADPVEntity> CREATOR = new Creator<ADPVEntity>() {
        @Override
        public ADPVEntity createFromParcel(Parcel in) {
            return new ADPVEntity(in);
        }

        @Override
        public ADPVEntity[] newArray(int size) {
            return new ADPVEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeLong(beginTime);
        dest.writeLong(endTime);
        dest.writeByte((byte) (isVisable ? 1 : 0));
        dest.writeString(pvid);
        dest.writeByte((byte) (isHaveContent ? 1 : 0));
        dest.writeByte((byte) (isContentLoaded ? 1 : 0));
        dest.writeInt(type);
        dest.writeString(imgType);
    }
}