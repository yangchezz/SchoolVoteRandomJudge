package cn.edu.jxau.jwc.pojo.model;

/**
 * @Author Vector
 * @Date 2019/12/12
 * @Desc ...
 * @Since 1.0.0
 */
public class Choose {
    private Integer chooseIndex;
    private Long chooseId;
    private Integer chooseType;

    public int getChooseIndex() {
        return chooseIndex;
    }

    public void setChooseIndex(int chooseIndex) {
        this.chooseIndex = chooseIndex;
    }

    public Long getChooseId() {
        return chooseId;
    }

    public void setChooseId(Long chooseId) {
        this.chooseId = chooseId;
    }

    public int getChooseType() {
        return chooseType;
    }

    public void setChooseType(int chooseType) {
        this.chooseType = chooseType;
    }

    @Override
    public String toString() {
        return "Choose{" +
                "chooseIndex=" + chooseIndex +
                ", chooseId=" + chooseId +
                ", chooseType=" + chooseType +
                '}';
    }
}
