package cn.edu.jxau.jwc.util;

/**
 * @Author vector
 * @Since 1.0.0
 * @Date 2019/10/24 21:55
 */
public class PageHelper {
    //当前分页坐标
    private int lineIndex;
    //行总数
    private int lineCount;
    //分页大小
    private int pageSize;
    //分页坐标
    private int pageIndex;
    //分页数量
    private int pageCount;

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }


    public PageHelper() {
    }

    public PageHelper(int lineCount, int pageSize) {
        this.lineIndex = lineCount > 0 ? 1 : 0;
        this.lineCount = lineCount;
        this.pageSize = pageSize;
        this.pageIndex = lineCount > 0 ? 1 : 0;
        this.pageCount = this.lineCount / this.pageSize;
        if (this.lineCount % this.pageSize != 0) {
            this.pageCount++;
        }
    }

    @Override
    public String toString() {
        return "PageHelper{" +
                "lineIndex=" + lineIndex +
                ", lineCount=" + lineCount +
                ", pageSize=" + pageSize +
                ", pageCount=" + pageCount +
                '}';
    }
}
