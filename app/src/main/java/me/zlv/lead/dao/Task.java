package me.zlv.lead.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table TASK.
 */
public class Task {

    private Long id;
    /** Not-null value. */
    private String title;
    private int priority;
    private int status;
    /** Not-null value. */
    private java.util.Date createTime;
    private java.util.Date excuteTime;

    // KEEP FIELDS - put your custom fields here
    public static final int STATUS_ADD = 1;
    public static final int STATUS_FINISHED = 2;

    public static final int PRIORITY_IMPORTANT_URGENT = 0x11;
    public static final int PRIORITY_IMPORTANT = 0x01;
    public static final int PRIORITY_URGENT = 0x10;
    public static final int PRIORITY_REST = 0x00;
    // KEEP FIELDS END

    public Task() {
    }

    public Task(Long id) {
        this.id = id;
    }

    public Task(Long id, String title, int priority, int status, java.util.Date createTime, java.util.Date excuteTime) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = status;
        this.createTime = createTime;
        this.excuteTime = excuteTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getTitle() {
        return title;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /** Not-null value. */
    public java.util.Date getCreateTime() {
        return createTime;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public java.util.Date getExcuteTime() {
        return excuteTime;
    }

    public void setExcuteTime(java.util.Date excuteTime) {
        this.excuteTime = excuteTime;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
